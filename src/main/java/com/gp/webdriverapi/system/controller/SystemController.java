package com.gp.webdriverapi.system.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.common.annotation.Crypto;
import com.gp.webdriverapi.system.service.FolderDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页控制器
 * 获取首页信息
 *
 * @author Devonte
 * @date 2020/03/15
 */
@Api(tags = "首页数据获取接口")
@RestController
@Crypto(decrypt = false)
public class SystemController extends BaseController {

    @Autowired
    private FolderDetailService folderDetailService;

    @GetMapping({"/info/{folderId}", "/info"})
    @ApiOperation(value = "获取当前文件夹的所有文件夹和文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "folderId", value = "文件夹id", dataType = "Integer", example = "0")
    })
    public R getFileAndFolder(@PathVariable(required = false) String folderId) {
        int id = 0;
        if (folderId != null) {
            String decrypt = decryptParam(folderId);
            id = Integer.parseInt(decrypt);
        }
        return success(folderDetailService.getAllInfoByFolderId(id));
    }

}
