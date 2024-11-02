package com.hawk.sdufeforumpro.notice.infrastructure;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hawk.sdufeforumpro.notice.domain.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
