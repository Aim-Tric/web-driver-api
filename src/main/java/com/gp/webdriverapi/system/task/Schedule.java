package com.gp.webdriverapi.system.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gp.webdriverapi.system.service.FileService;
import com.gp.webdriverapi.common.pojo.WdFile;
import com.gp.webdriverapi.system.service.FolderService;
import com.gp.webdriverapi.common.pojo.WdFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * 定时清理回收站
 *
 * @author vent
 * @date 2020/03/20
 */
@Component
public class Schedule {

    private final static int AMOUNT = -30;

    @Autowired
    private FileService fileService;
    @Autowired
    private FolderService folderService;

    @Scheduled(cron = "0 0 0 * * *")
    public void clearRecycleBin() {
        // 判断是否超过30天，删除超过30天的文件
        List<String> sId = new ArrayList<>();
        List<Integer> iId = new ArrayList<>();

        List<WdFolder> foldersInRecycle = folderService.list(new QueryWrapper<WdFolder>()
                .eq("logical_del", 1));

        List<WdFile> filesInRecycle = fileService.list(new QueryWrapper<WdFile>()
                .eq("logical_del", 1));

        filesInRecycle.forEach(wdFile -> {
            if (compareTime(wdFile.getLastUpdateTime())) {
                // do delete
                deleteFile(wdFile.getRealPath());
                sId.add(wdFile.getId());
            }
        });
        foldersInRecycle.forEach(wdFolder -> {
            if(compareTime(wdFolder.getLastUpdateTime())) {
                iId.add(wdFolder.getId());
            }
        });
        if (iId.size() > 0) {
            folderService.removeByIds(iId);
        }
        if (sId.size() > 0) {
            fileService.removeByIds(sId);
        }
        System.out.println("删除任务完成！当前时间为：" + new Date());
    }

    private boolean compareTime(Date lastUpdateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, AMOUNT);
        return lastUpdateTime.compareTo(calendar.getTime()) >= 0;
    }

    private void deleteFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
    }

}
