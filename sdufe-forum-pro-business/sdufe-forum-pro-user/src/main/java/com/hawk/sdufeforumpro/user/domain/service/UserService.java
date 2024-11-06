package com.hawk.sdufeforumpro.user.domain.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.template.QuickConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hawk.sdufeforumpro.api.user.constant.UserConstant;
import com.hawk.sdufeforumpro.api.user.constant.UserOperateTypeEnum;
import com.hawk.sdufeforumpro.api.user.request.UserModifyRequest;
import com.hawk.sdufeforumpro.api.user.response.UserOperatorResponse;
import com.hawk.sdufeforumpro.api.user.response.data.InviteRankInfo;
import com.hawk.sdufeforumpro.base.exception.BizException;
import com.hawk.sdufeforumpro.base.exception.RepoErrorCode;
import com.hawk.sdufeforumpro.base.response.PageResponse;
import com.hawk.sdufeforumpro.lock.DistributeLock;
import com.hawk.sdufeforumpro.user.domain.entity.User;
import com.hawk.sdufeforumpro.user.infrastructure.exception.UserErrorCode;
import com.hawk.sdufeforumpro.user.infrastructure.exception.UserException;
import com.hawk.sdufeforumpro.user.infrastructure.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hawk.sdufeforumpro.user.infrastructure.exception.UserErrorCode.*;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements InitializingBean {

    private static final String DEFAULT_NICK_NAME_PREFIX = "微享生活用户_";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserOperateStreamService userOperateStreamService;

    @Autowired
    private RedissonClient redissonClient;

    private RBloomFilter<String> nickNameBloomFilter;

    private RBloomFilter<String> inviteCodeBloomFilter;

    private RScoredSortedSet<String> inviteRank;

    @Autowired
    private CacheManager cacheManager;

    private Cache<String, User> idUserCache;

    @PostConstruct
    public void init() {
        QuickConfig idQc = QuickConfig.newBuilder(":user:cache:id:")
                .cacheType(CacheType.BOTH)
                .expire(Duration.ofHours(2))
                .syncLocal(true)
                .build();
        idUserCache = cacheManager.getOrCreateCache(idQc);
    }

    /**
     * 注册
     *
     * @param telephone
     * @param inviteCode
     * @return
     */
    @DistributeLock(keyExpression = "#telephone", scene = "USER_REGISTER")
    public UserOperatorResponse register(String telephone, String inviteCode) {
        String defaultNickName;
        String randomString;
        do {
            randomString = RandomUtil.randomString(6).toUpperCase();
            //前缀 + 6位随机数 + 手机号后四位
            defaultNickName = DEFAULT_NICK_NAME_PREFIX + randomString + telephone;
        } while (nickNameExist(defaultNickName) || inviteCodeExist(randomString));

        String inviterId = null;
        if (StringUtils.isNotBlank(inviteCode)) {
            User inviter = userMapper.findByInviteCode(inviteCode);
            if (inviter != null) {
                inviterId = inviter.getId().toString();
            }
        }

        User user = register(telephone, defaultNickName, telephone, randomString, inviterId);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());

        addNickName(defaultNickName);
        addInviteCode(randomString);
        updateInviteRank(inviterId);
        updateUserCache(user.getId().toString(), user);

        //加入流水
        long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.REGISTER);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }

    private User register(String telephone, String nickName, String password, String inviteCode, String inviterId) {
        if (userMapper.findByTelephone(telephone) != null) {
            throw new UserException(DUPLICATE_TELEPHONE_NUMBER);
        }

        User user = new User();
        user.register(telephone, nickName, password, inviteCode, inviterId);
        return save(user) ? user : null;
    }

    /**
     * 通过手机号查询用户信息
     *
     * @param telephone
     * @return
     */
    public User findByTelephone(String telephone) {
        return userMapper.findByTelephone(telephone);
    }

    /**
     * 通过手机号和密码查询用户信息
     *
     * @param telephone
     * @param password
     * @return
     */
    public User findByTelephoneAndPass(String telephone, String password) {
        return userMapper.findByTelephoneAndPass(telephone, DigestUtil.md5Hex(password));
    }

    /**
     * 通过用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    @Cached(name = ":user:cache:id", cacheType = CacheType.BOTH, key = "#userId", cacheNullValue = true)
    @CacheRefresh(refresh = 60, timeUnit = TimeUnit.MINUTES)
    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    public PageResponse<User> pageQueryByState(String keyWord, String state, int currentPage, int pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("state", state);

        if (keyWord != null) {
            wrapper.like("telephone", keyWord);
        }
        wrapper.orderBy(true, true, "gmt_create");

        Page<User> userPage = this.page(page, wrapper);

        return PageResponse.of(userPage.getRecords(), (int) userPage.getTotal(), pageSize, currentPage);
    }

    @CacheInvalidate(name = ":user:cache:id:", key = "#userModifyRequest.userId")
    public UserOperatorResponse modify(UserModifyRequest userModifyRequest) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();;
        User user = userMapper.findById(userModifyRequest.getUserId());
        Assert.notNull(user, () -> new UserException(USER_NOT_EXIST));
        Assert.isTrue(user.canModifyInfo(), () -> new UserException(USER_STATUS_CANT_OPERATE));

        if (StringUtils.isNotBlank(userModifyRequest.getNickName()) && nickNameExist(userModifyRequest.getNickName())) {
            throw new UserException(NICK_NAME_EXIST);
        }
        BeanUtils.copyProperties(userModifyRequest, user);

        if (StringUtils.isNotBlank(userModifyRequest.getPassword())) {
            user.setPasswordHash(DigestUtil.md5Hex(userModifyRequest.getPassword()));
        }

        if (updateById(user)) {
            //加入流水
            long streamResult = userOperateStreamService.insertStream(user, UserOperateTypeEnum.MODIFY);
            Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
            addNickName(userModifyRequest.getNickName());
            userOperatorResponse.setSuccess(true);

            return userOperatorResponse;
        }

        userOperatorResponse.setSuccess(false);
        userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getCode());
        userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getMessage());

        return userOperatorResponse;
    }

    public Integer getInviteRank(String userId) {
        Integer rank = inviteRank.revRank(userId);
        if (rank != null) {
            return rank + 1;
        }
        return null;
    }

    public PageResponse<User> getUsersByInviterId(String inviterId, Integer currentPage, Integer pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("nick_name", "gmt_create");
        wrapper.eq("inviter_id", inviterId);

        wrapper.orderBy(true, false, "gmt_create");

        Page<User> userPage = this.page(page, wrapper);
        return PageResponse.of(userPage.getRecords(), (int) userPage.getTotal(), pageSize, currentPage);
    }

    public List<InviteRankInfo> getTopN(Integer topN) {
        Collection<ScoredEntry<String>> rankInfos = inviteRank.entryRangeReversed(0, topN - 1);

        List<InviteRankInfo> inviteRankInfos = new ArrayList<>();

        if (rankInfos != null) {
            for (ScoredEntry<String> rankInfo : rankInfos) {
                InviteRankInfo inviteRankInfo = new InviteRankInfo();
                String userId = rankInfo.getValue();
                if (StringUtils.isNotBlank(userId)) {
                    User user = findById(Long.valueOf(userId));
                    if (user != null) {
                        inviteRankInfo.setNickName(user.getNickName());
                        inviteRankInfo.setInviteCode(user.getInviteCode());
                        inviteRankInfo.setInviteScore(rankInfo.getScore().intValue());
                        inviteRankInfos.add(inviteRankInfo);
                    }
                }
            }
        }

        return inviteRankInfos;
    }

    public void follow(String followId, String followedId) {
        RScoredSortedSet<String> followSet = redissonClient.getScoredSortedSet(UserConstant.FOLLOW_KEY_PREFIX + followId);
        RScoredSortedSet<String> followedSet = redissonClient.getScoredSortedSet(UserConstant.FOLLOWED_KEY_PREFIX + followedId);

        followSet.add((double) new Date().getTime() / 1000, followedId);
        followedSet.add((double) new Date().getTime() / 1000, followId);
    }

    public void unFollow(String followId, String followedId) {
        RScoredSortedSet<String> followSet = redissonClient.getScoredSortedSet(UserConstant.FOLLOW_KEY_PREFIX + followId);
        RScoredSortedSet<String> followedSet = redissonClient.getScoredSortedSet(UserConstant.FOLLOWED_KEY_PREFIX + followedId);

        followSet.remove(followedId);
        followedSet.remove(followId);
    }

    public PageResponse<User> getFollowUser(String userId, Integer currentPage, Integer pageSize) {
        RScoredSortedSet<String> followSet = redissonClient.getScoredSortedSet(UserConstant.FOLLOW_KEY_PREFIX + userId);
        Collection<String> followUserIdList = followSet.valueRangeReversed(0, followSet.size());

        List<User> userList = followUserIdList.stream().map(id -> findById(Long.valueOf(id))).toList();
        return PageResponse.of(userList, userList.size(), pageSize, currentPage);
    }

    public PageResponse<User> getFollowedUser(String userId, Integer currentPage, Integer pageSize) {
        RScoredSortedSet<String> followedSet = redissonClient.getScoredSortedSet(UserConstant.FOLLOWED_KEY_PREFIX + userId);
        Collection<String> followedUserIdList = followedSet.valueRangeReversed(0, followedSet.size());

        List<User> userList = followedUserIdList.stream().map(id -> findById(Long.valueOf(id))).toList();
        return PageResponse.of(userList, userList.size(), pageSize, currentPage);
    }

    public boolean nickNameExist(String nickName) {
        //如果布隆过滤器中存在，再进行数据库二次判断
        if (this.nickNameBloomFilter != null && this.nickNameBloomFilter.contains(nickName)) {
            return userMapper.findByNickname(nickName) != null;
        }

        return false;
    }

    public boolean inviteCodeExist(String inviteCode) {
        //如果布隆过滤器中存在，再进行数据库二次判断
        if (this.inviteCodeBloomFilter != null && this.inviteCodeBloomFilter.contains(inviteCode)) {
            return userMapper.findByInviteCode(inviteCode) != null;
        }

        return false;
    }

    private boolean addNickName(String nickName) {
        return this.nickNameBloomFilter != null && this.nickNameBloomFilter.add(nickName);
    }

    private boolean addInviteCode(String inviteCode) {
        return this.inviteCodeBloomFilter != null && this.inviteCodeBloomFilter.add(inviteCode);
    }

    private void updateInviteRank(String inviterId) {
        if (inviterId == null) {
            return;
        }
        RLock rLock = redissonClient.getLock(inviterId);
        rLock.lock();
        try {
            Double score = inviteRank.getScore(inviterId);
            if (score == null) {
                score = 0.0;
            }
            inviteRank.add(score + 100.0, inviterId);
        } finally {
            rLock.unlock();
        }
    }

    private void updateUserCache(String userId, User user) {
        idUserCache.put(userId, user);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.nickNameBloomFilter = redissonClient.getBloomFilter("nickName");
        if (nickNameBloomFilter != null && !nickNameBloomFilter.isExists()) {
            this.nickNameBloomFilter.tryInit(100000L, 0.01);
        }

        this.inviteCodeBloomFilter = redissonClient.getBloomFilter("inviteCode");
        if (inviteCodeBloomFilter != null && !inviteCodeBloomFilter.isExists()) {
            this.inviteCodeBloomFilter.tryInit(100000L, 0.01);
        }

        this.inviteRank = redissonClient.getScoredSortedSet("inviteRank");
    }
}
