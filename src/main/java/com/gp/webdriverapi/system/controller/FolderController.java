package com.gp.webdriverapi.system.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.common.annotation.Crypto;
import com.gp.webdriverapi.common.pojo.WdFolder;
import com.gp.webdriverapi.system.service.FolderDetailService;
import com.gp.webdriverapi.system.service.FolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 文件夹控制器
 *
 * @author Devonte
 * @date 2020/03/15
 */
@Crypto(decrypt = false)
@RestController
@Api(tags = "文件夹操作接口")
public class FolderController extends BaseController {

    @Autowired
    private FolderService folderService;
    @Autowired
    private FolderDetailService folderDetailService;

    @PutMapping("/folder")
    @ApiOperation(value = "更新文件夹信息")
    @ApiImplicitParam(name = "wdFolder", value = "更新后的文件夹信息")
    public R update(WdFolder wdFolder) {
        boolean updated = folderService.update(wdFolder,
                new UpdateWrapper<WdFolder>()
                        .eq("id", wdFolder.getId()));
        if (updated) {
            return success(folderDetailService.getAllInfoByFolderId(wdFolder.getParentId()));
        }
        return failed(UPDATE_FAILED);
    }

    @PostMapping("/folder")
    @ApiOperation(value = "创建一个文件夹")
    public R create(@RequestBody WdFolder wdFolder) {
        wdFolder.setLastUpdateTime(new Date(System.currentTimeMillis()));
        boolean saved = folderService.save(wdFolder);
        if (saved) {
            return success(folderDetailService.getAllInfoByFolderId(wdFolder.getParentId()));
        }
        return failed(CREATE_FAILED);
    }

    @DeleteMapping("/folder/delete")
    @ApiOperation(value = "完全删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的文件夹的id的集合")
    })
    public R delete(Integer[] ids) {
        List<Integer> list = Arrays.asList(ids);
        boolean deleted = folderService.removeByIds(list);
        return successOrFail(deleted, null, DELETE_FAILED);
    }

    @DeleteMapping("/folder")
    @ApiOperation(value = "删除一个或多个文件夹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的文件夹的id的集合")
    })
    public R logicalDelete(Integer[] ids) {
        List<WdFolder> wdFolders = folderService.listByIds(Arrays.asList(ids));
        wdFolders.forEach(wdFolder -> wdFolder.setLogicalDel(1));
        WdFolder folder = wdFolders.get(0);
        int parentId = folder.getParentId();
        folderService.updateBatchById(wdFolders);
        return success(folderDetailService.getAllInfoByFolderId(parentId));
    }


}
