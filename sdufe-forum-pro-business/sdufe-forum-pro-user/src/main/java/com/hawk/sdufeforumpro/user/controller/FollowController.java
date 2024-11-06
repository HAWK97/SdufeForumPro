package com.hawk.sdufeforumpro.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.hawk.sdufeforumforumpro.web.vo.MultiResult;
import com.hawk.sdufeforumforumpro.web.vo.Result;
import com.hawk.sdufeforumpro.api.user.response.data.UserInfo;
import com.hawk.sdufeforumpro.base.response.PageResponse;
import com.hawk.sdufeforumpro.user.domain.entity.User;
import com.hawk.sdufeforumpro.user.domain.entity.convertor.UserConvertor;
import com.hawk.sdufeforumpro.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user/follow")
public class FollowController {

    @Autowired
    private UserService userService;

    @GetMapping("/doFollow")
    public Result<Boolean> follow(Long followedId) {
        String followId = (String) StpUtil.getLoginId();
        userService.follow(followId, followedId.toString());
        return Result.success(true);
    }

    @GetMapping("/undoFollow")
    public Result<Boolean> unFollow(Long followedId) {
        String followId = (String) StpUtil.getLoginId();
        userService.unFollow(followId, followedId.toString());
        return Result.success(true);
    }

    @GetMapping("/getFollowUser")
    public MultiResult<UserInfo> getFollowUser(Integer currentPage, Integer pageSize) {
        String followId = (String) StpUtil.getLoginId();
        PageResponse<User> pageResponse = userService.getFollowUser(followId, currentPage, pageSize);
        return MultiResult.successMulti(UserConvertor.INSTANCE.mapToVo(pageResponse.getDatas()), pageResponse.getTotal(), currentPage, pageSize);
    }

    @GetMapping("/getFollowedUser")
    public MultiResult<UserInfo> getFollowedUser(Integer currentPage, Integer pageSize) {
        String followedId = (String) StpUtil.getLoginId();
        PageResponse<User> pageResponse = userService.getFollowedUser(followedId, currentPage, pageSize);
        return MultiResult.successMulti(UserConvertor.INSTANCE.mapToVo(pageResponse.getDatas()), pageResponse.getTotal(), currentPage, pageSize);
    }
}
