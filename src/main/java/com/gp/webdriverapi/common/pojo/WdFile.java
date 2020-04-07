package com.gp.webdriverapi.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Data
public class WdFile {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String newName;
    private String originalName;
    private String size;
    private String md5;
    private String fileSuffix;
    private String realPath;
    private Date lastUpdateTime;
    private int folderId;
    private Integer category;
    private Integer logicalDel = 0;
}
