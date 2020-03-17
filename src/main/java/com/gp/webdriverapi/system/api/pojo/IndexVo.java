package com.gp.webdriverapi.system.api.pojo;

import com.gp.webdriverapi.system.service.file.pojo.WdFile;
import com.gp.webdriverapi.system.service.folder.WdFolder;

import java.util.List;

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
