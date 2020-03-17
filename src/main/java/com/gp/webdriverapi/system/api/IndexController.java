package com.gp.webdriverapi.system.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.system.api.pojo.IndexVo;
import com.gp.webdriverapi.system.service.file.FileService;
import com.gp.webdriverapi.system.service.file.pojo.WdFile;
import com.gp.webdriverapi.system.service.folder.FolderService;
import com.gp.webdriverapi.system.service.folder.WdFolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "首页数据获取接口")
@RestController
public class IndexController extends BaseController {

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
        WdFolder folder = new WdFolder();
        folder.setParentId(folderId);
        List<WdFolder> folders = folderService.list(new QueryWrapper<>(folder));
        // 获取文件
        WdFile wdFile = new WdFile();
        wdFile.setFolderId(folderId);
        List<WdFile> files = fileService.list(new QueryWrapper<>(wdFile));

        IndexVo listVo = new IndexVo();
        listVo.setFiles(files);
        listVo.setFolders(folders);
        return ok(listVo);
    }


}
