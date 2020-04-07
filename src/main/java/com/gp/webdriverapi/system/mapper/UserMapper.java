package com.gp.webdriverapi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.webdriverapi.common.pojo.WdUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<WdUser> {
}
