package com.gp.webdriverapi.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@TableName("wd_folder")
@Data
public class WdFolder {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private int parentId;
    private String name;
    private Date lastUpdateTime;
    private int logicalDel = 0;
}
