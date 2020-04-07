package com.gp.webdriverapi.common.pojo;

import com.gp.webdriverapi.common.pojo.WdFile;
import com.gp.webdriverapi.common.pojo.WdFolder;

import java.util.List;

/**
 * 首页信息的数据
 *
 * @author vent
 * @date 2020/03/15
 */
public class IndexVo {

    private List<WdFile> files;
    private List<WdFolder> folders;

    public List<WdFile> getFiles() {
        return files;
    }

    public void setFiles(List<WdFile> files) {
        this.files = files;
    }

    public List<WdFolder> getFolders() {
        return folders;
    }

    public void setFolders(List<WdFolder> folders) {
        this.folders = folders;
    }
}
