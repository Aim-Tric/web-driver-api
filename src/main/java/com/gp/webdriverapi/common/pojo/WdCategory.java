package com.gp.webdriverapi.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class WdCategory {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;

}
