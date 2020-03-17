package com.gp.webdriverapi.system.service.folder;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.webdriverapi.system.service.file.pojo.WdFile;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Service
public class FolderService extends ServiceImpl<FolderMapper, WdFolder> {

    public boolean restore(Collection<? extends Serializable> collections) {
        List<WdFolder> wdFolders = this.listByIds(collections);
        wdFolders.forEach(wdFolder -> wdFolder.setLogicDel(0));
        return this.updateBatchById(wdFolders);
    }

}
