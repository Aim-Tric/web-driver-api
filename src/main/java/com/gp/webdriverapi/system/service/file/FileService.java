package com.gp.webdriverapi.system.service.file;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.webdriverapi.system.service.file.pojo.WdFile;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class FileService extends ServiceImpl<FileMapper, WdFile> {

    public boolean restore(Collection<? extends Serializable> collections) {
        List<WdFile> wdFiles = this.listByIds(collections);
        wdFiles.forEach(wdFile -> wdFile.setLogicalDel(0));
        return this.updateBatchById(wdFiles);
    }

}
