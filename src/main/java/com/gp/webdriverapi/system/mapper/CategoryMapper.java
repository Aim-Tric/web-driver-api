package com.gp.webdriverapi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.webdriverapi.common.pojo.WdCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<WdCategory> {
}
