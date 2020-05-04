package com.gp.webdriverapi.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.common.annotation.Crypto;
import com.gp.webdriverapi.common.pojo.WdFile;
import com.gp.webdriverapi.common.pojo.WdUploadFile;
import com.gp.webdriverapi.common.utils.CryptoUtils;
import com.gp.webdriverapi.system.service.FileService;
import com.gp.webdriverapi.system.service.FileTransmitter;
import com.gp.webdriverapi.system.service.FolderDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 处理文件相关操作的控制器
 *
 * @author vent
 * @date 2020/03/15
 */
@Crypto(decrypt = false)
@RestController
@Api(tags = "文件操作接口")
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileTransmitter fileTransmitter;
    @Autowired
    private FolderDetailService folderDetailService;

    @GetMapping("/filter/{categoryId}")
    @ApiOperation(value = "按分类过滤文件")
    @ApiImplicitParam(name = "id", value = "分类id")
    public R filterByCategory(@PathVariable String categoryId) {
        String decrypt = decryptParam(categoryId);
        List<WdFile> category = fileService.list(new QueryWrapper<WdFile>()
                .eq("category", Integer.parseInt(decrypt)));
        return success(category);
    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件的标识"),
            @ApiImplicitParam(name = "newName", value = "文件的新名称"),
            @ApiImplicitParam(name = "originalName", value = "文件的原名称"),
            @ApiImplicitParam(name = "size", value = "文件的大小"),
            @ApiImplicitParam(name = "folderId", value = "上传的逻辑目录id"),
            @ApiImplicitParam(name = "category", value = "分类的id"),
            @ApiImplicitParam(name = "total", value = "文件分片的总数"),
            @ApiImplicitParam(name = "current", value = "当前上传的是第几个文件分片"),
            @ApiImplicitParam(name = "md5", value = "文件的md5"),
            @ApiImplicitParam(name = "lastUpdateTime", value = "上传文件的时间"),
            @ApiImplicitParam(name = "fileSuffix", value = "文件的后缀"),
            @ApiImplicitParam(name = "files", value = "上传的文件"),
    })
    public R upload(WdUploadFile wdUploadFile) {
        Integer category = Integer.valueOf(CryptoUtils.decrypt(wdUploadFile.getCategory().getBytes()));
        Integer folderId = Integer.valueOf(CryptoUtils.decrypt(wdUploadFile.getFolderId().getBytes()));

        WdFile fileInfo = new WdFile();
        BeanUtils.copyProperties(wdUploadFile, fileInfo);
        fileInfo.setCategory(category);
        fileInfo.setFolderId(folderId);

        String originalName = wdUploadFile.getOriginalName();
        logger.info("开始上传: %s", originalName);

        int total = wdUploadFile.getTotal();
        int done = wdUploadFile.getCurrent();
        fileTransmitter.upload("./upload/", wdUploadFile);
        // 当前为最后一个文件
        if (done == total - 1 || total == 1) {
            String realPath = fileTransmitter.merge(wdUploadFile);
            //  文件合并成功
            if (realPath != null) {

                fileInfo.setRealPath(realPath);
                boolean saved = fileService.save(fileInfo);
                if (saved) {
                    logger.info("%s 上传成功!", originalName);
                    return success(folderDetailService
                            .getAllInfoByFolderId(folderId))
                            .setCode(201)
                            .setMsg("文件上传成功");
                }
            }

            logger.info("%s 上传失败!", originalName);
            return failed("文件信息保存出错！");
        }
        return success();
    }

    @GetMapping("/download/{fileId}")
    @ApiOperation(value = "文件下载接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件id")
    })
    public void download(@PathVariable String fileId, HttpServletRequest request, HttpServletResponse response) {
        WdFile wdFile = fileService.getById(decryptParam(fileId));
        fileTransmitter.download(request, response, wdFile);
    }

    @PutMapping("/file")
    @ApiOperation(value = "更新文件信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id"),
            @ApiImplicitParam(name = "originalName", value = "文件的新名称")
    })
    public R update(WdFile wdFile) {
        boolean updated = fileService.update(new UpdateWrapper<WdFile>()
                .eq("id", wdFile.getId())
                .set("original_name", wdFile.getOriginalName()));
        if (updated) {
            return success(folderDetailService.getAllInfoByFolderId(wdFile.getFolderId()));
        }
        return failed(UPDATE_FAILED);
    }

    @DeleteMapping("/file/delete")
    @ApiOperation(value = "完全删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件id")
    })
    public R delete(String[] ids) {
        List<String> list = Arrays.asList(ids);
        List<WdFile> wdFiles = fileService.listByIds(list);
        wdFiles.forEach(wdFile -> fileTransmitter.deleteFile(wdFile));
        boolean deleted = fileService.removeByIds(list);
        if (deleted) {
            return success();
        }
        return failed(DELETE_FAILED);
    }

    @DeleteMapping("/file")
    @ApiOperation(value = "文件逻辑删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的文件id数组")
    })
    public R logicDelete(String[] ids) {
        if (ids.length > 0) {
            fileService.update(new UpdateWrapper<WdFile>()
                    .in("id", ids)
                    .set("logical_del", 1));
            WdFile file = fileService.getById(ids[0]);
            return success(folderDetailService.getAllInfoByFolderId(file.getFolderId()));
        }
        return failed(DELETE_FAILED);
    }


}
