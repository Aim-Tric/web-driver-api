package com.gp.webdriverapi.system.service.folder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gp.webdriverapi.system.service.file.pojo.WdFile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@TableName("wd_folder")
@Data
@Setter
@Getter
@ToString
public class WdFolder {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private int parentId;
    private String name;
    private Date lastUpdateTime;
    private int logicDel = 0;

    @TableField(exist = false)
    List<WdFolder> folders;
    @TableField(exist = false)
    List<WdFile> files;
}
