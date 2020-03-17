package com.gp.webdriverapi.system.api;

import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.system.service.file.FileService;
import com.gp.webdriverapi.system.service.file.FileTransmitter;
import com.gp.webdriverapi.system.service.file.pojo.WdFile;
import com.gp.webdriverapi.system.service.file.pojo.WdUploadFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
@Api(tags = "文件操作接口")
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileTransmitter fileTransmitter;
    @Autowired
    private IndexController indexController;

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
        String originalName = wdUploadFile.getOriginalName();
        logger.info("开始上传: %s", originalName);

        int total = wdUploadFile.getTotal();
        int done = wdUploadFile.getCurrent();
        fileTransmitter.upload("./upload/", wdUploadFile);
        System.out.println(wdUploadFile);
        // 当前为最后一个文件
        if (done == total - 1 || total == 1) {
            String realPath = fileTransmitter.merge(wdUploadFile);
            //  文件合并成功
            if (realPath != null) {
                WdFile fileInfo = new WdFile();
                BeanUtils.copyProperties(wdUploadFile, fileInfo);
                fileInfo.setRealPath(realPath);
                boolean saved = fileService.save(fileInfo);
                if (saved) {
                    logger.info("%s 上传成功!", originalName);
                    return indexController
                            .getFileAndFolder(wdUploadFile.getFolderId())
                            .setCode(201)
                            .setMsg("文件上传成功");
                }
            }

            logger.info("%s 上传失败!", originalName);
            return failed("文件信息保存出错！");
        }
        return ok();
    }

    @GetMapping("/download/{fileId}")
    @ApiOperation(value = "文件下载接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件id")
    })
    public void download(@PathVariable String fileId, HttpServletResponse response) {
        // get file info from database
        WdFile wdFile = fileService.getById(fileId);
        // use output stream to output file
        String fileName = wdFile.getOriginalName();
        logger.info("开始下载 %s", fileName);

        fileTransmitter.download(response, wdFile);
        logger.info("%s 下载完成", fileName);
    }

    @DeleteMapping("/file/delete")
    @ApiOperation(value = "完全删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件id")
    })
    public R delete(List<String> ids) {
        List<WdFile> wdFiles = fileService.listByIds(ids);
        wdFiles.forEach(wdFile -> fileTransmitter.deleteFile(wdFile));
        boolean deleted = fileService.removeByIds(ids);
        if (deleted) {
            return ok();
        }
        return failed(DELETE_FAILED);
    }

    @DeleteMapping("/file")
    @ApiOperation(value = "文件逻辑删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "需要删除的文件id数组")
    })
    public R logicDelete(String[] ids) {
        List<WdFile> wdFiles = fileService.listByIds(Arrays.asList(ids));
        wdFiles.forEach(wdFile -> wdFile.setLogicalDel(1));
        if (wdFiles.size() > 0) {
            int folderId = wdFiles.get(0).getFolderId();
            boolean delete = fileService.updateBatchById(wdFiles);
            if (delete) {
                return indexController.getFileAndFolder(folderId);
            }
        }
        return failed(DELETE_FAILED);
    }


}
