package com.gp.webdriverapi.system.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.system.service.file.FileService;
import com.gp.webdriverapi.system.service.folder.FolderService;
import com.gp.webdriverapi.system.service.folder.WdFolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "文件夹操作接口")
public class FolderController extends BaseController {

    @Autowired
    private FolderService folderService;
    @Autowired
    private IndexController indexController;

    @PutMapping("/folder")
    @ApiOperation(value = "更新文件夹信息")
    @ApiImplicitParam(name = "wdFolder", value = "更新后的文件夹信息")
    public R update(WdFolder wdFolder) {
        boolean updated = folderService.update(new QueryWrapper<>(wdFolder));
        if (updated) {
            return R.ok(null);
        }
        return R.failed(UPDATE_FAILED);
    }

    @PostMapping("/folder")
    @ApiOperation(value = "创建一个文件夹")
    public R create(WdFolder wdFolder) {
        wdFolder.setLastUpdateTime(new Date(System.currentTimeMillis()));
        boolean saved = folderService.save(wdFolder);
        if (saved) {
            return indexController.getFileAndFolder(wdFolder.getParentId());
        }
        return R.failed(CREATE_FAILED);
    }

    @DeleteMapping("/folder")
    @ApiOperation(value = "删除一个或多个文件夹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的文件夹的id的集合")
    })
    public R delete(Integer[] ids) {
        List<WdFolder> wdFolders = folderService.listByIds(Arrays.asList(ids));
        wdFolders.forEach(wdFolder -> wdFolder.setLogicDel(1));
        WdFolder folder = wdFolders.get(0);
        int parentId = folder.getParentId();
        folderService.updateBatchById(wdFolders);
        return indexController.getFileAndFolder(parentId);
    }


}
