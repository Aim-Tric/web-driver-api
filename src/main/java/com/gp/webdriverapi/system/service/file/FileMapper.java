package com.gp.webdriverapi.system.service.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.webdriverapi.system.service.file.pojo.WdFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<WdFile> {
}
