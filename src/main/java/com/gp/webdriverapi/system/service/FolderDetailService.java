package com.gp.webdriverapi.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gp.webdriverapi.common.pojo.IndexVo;
import com.gp.webdriverapi.common.pojo.WdFile;
import com.gp.webdriverapi.common.pojo.WdFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Devonte
 * @date 2020/4/20
 */
@Service
public class FolderDetailService {

    @Autowired
    private FolderService folderService;
    @Autowired
    private FileService fileService;

    public IndexVo getAllInfoByFolderId(int folderId) {

        // 获取文件夹
        List<WdFolder> folders = folderService.list(new QueryWrapper<WdFolder>()
                .eq("parent_id", folderId)
                .eq("logical_del", 0));
        // 获取文件
        List<WdFile> files = fileService.list(new QueryWrapper<WdFile>()
                .eq("folder_id", folderId)
                .eq("logical_del", 0));

        IndexVo listVo = new IndexVo();
        listVo.setFiles(files);
        listVo.setFolders(folders);
        return listVo;
    }


}
