package com.gp.webdriverapi.system.service.folder;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FolderMapper extends BaseMapper<WdFolder> {

    WdFolder fetchFolderInfo(Integer folderId);

}
