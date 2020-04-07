package com.gp.webdriverapi.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.common.annotation.Crypto;
import com.gp.webdriverapi.common.pojo.IndexVo;
import com.gp.webdriverapi.system.service.FileService;
import com.gp.webdriverapi.common.pojo.WdFile;
import com.gp.webdriverapi.system.service.FolderService;
import com.gp.webdriverapi.common.pojo.WdFolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页控制器
 * 获取首页信息
 *
 * @author vent
 * @date 2020/03/15
 */
@Api(tags = "首页数据获取接口")
@RestController
@Crypto(decrypt = false)
public class SystemController extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired
    private FolderService folderService;

    @GetMapping({"/info/{folderId}", "/info"})
    @ApiOperation(value = "获取当前文件夹的所有文件夹和文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "folderId", value = "文件夹id", dataType = "Integer", example = "0")
    })
    public R getFileAndFolder(@PathVariable(required = false) Integer folderId) {
        if (folderId == null) {
            folderId = 0;
        }
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
        return success(listVo);
    }


}
