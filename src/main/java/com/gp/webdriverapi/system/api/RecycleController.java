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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Api(tags = "回收站接口")
@RestController
public class RecycleController extends BaseController {

    @Autowired
    private FolderService folderService;
    @Autowired
    private FileService fileService;

    @GetMapping("/recycle")
    @ApiOperation(value = "获取回收站的信息")
    public R recycleBin() {
        WdFolder folder = new WdFolder();
        folder.setLogicDel(1);
        List<WdFolder> foldersInRecycle = folderService.list(new QueryWrapper<>(folder));

        WdFile file = new WdFile();
        file.setLogicalDel(1);
        List<WdFile> filesInRecycle = fileService.list(new QueryWrapper<>(file));

        IndexVo vo = new IndexVo();
        vo.setFiles(filesInRecycle);
        vo.setFolders(foldersInRecycle);
        return ok(vo);
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
            fileService.removeByIds(Arrays.asList(sId));
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
            folderService.restore(Arrays.asList(iId));
        }
        if (sId.length > 0) {
            fileService.restore(Arrays.asList(sId));
        }
        return recycleBin();
    }

}
