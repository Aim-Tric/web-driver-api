package com.gp.webdriverapi.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.common.annotation.Crypto;
import com.gp.webdriverapi.common.pojo.IndexVo;
import com.gp.webdriverapi.common.pojo.WdFile;
import com.gp.webdriverapi.common.pojo.WdFolder;
import com.gp.webdriverapi.system.service.FileService;
import com.gp.webdriverapi.system.service.FolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 回收站控制器
 *
 * @author vent
 * @date 2020/03/15
 */
@Api(tags = "回收站接口")
@RestController
@Crypto(decrypt = false)
public class RecycleController extends BaseController {

    @Autowired
    private FolderService folderService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileController fileController;

    @GetMapping("/recycle")
    @ApiOperation(value = "获取回收站的信息")
    public R recycleBin() {
        List<WdFolder> foldersInRecycle = folderService.list(new QueryWrapper<WdFolder>()
                .eq("logical_del", 1));

        List<WdFile> filesInRecycle = fileService.list(new QueryWrapper<WdFile>()
                .eq("logical_del", 1));

        IndexVo vo = new IndexVo();
        vo.setFiles(filesInRecycle);
        vo.setFolders(foldersInRecycle);
        return success(vo);
    }


    @DeleteMapping("/recycle")
    @ApiOperation(value = "批量删除回收站的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sId", value = "文件的id集合"),
            @ApiImplicitParam(name = "iId", value = "文件夹的id集合")
    })
    public R delete(String[] sId, Integer[] iId) {
        if (iId.length > 0) {
            folderService.removeByIds(Arrays.asList(iId));
        }
        if (sId.length > 0) {
            fileController.delete(sId);
        }
        return recycleBin();
    }

    @PostMapping("/recycle")
    @ApiOperation(value = "还原回收站的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sId", value = "文件的id集合"),
            @ApiImplicitParam(name = "iId", value = "文件夹的id集合")
    })
    public R reset(String[] sId, Integer[] iId) {
        if (iId.length > 0) {
            folderService.update(new UpdateWrapper<WdFolder>()
                    .in("id", iId)
                    .set("logical_del", 0));
        }
        if (sId.length > 0) {
            fileService.update(new UpdateWrapper<WdFile>()
                    .in("id", sId)
                    .set("logical_del", 0));
        }
        return recycleBin();
    }

}
