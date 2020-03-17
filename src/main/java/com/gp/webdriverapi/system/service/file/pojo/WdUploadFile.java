package com.gp.webdriverapi.system.service.file.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@ToString
public class WdUploadFile implements Serializable {

    private String id;
    private String newName;
    private String originalName;
    private String size;
    private Integer folderId;
    private Integer category;
    private int total;
    private int current;
    private String md5;
    private Date lastUpdateTime;
    private String fileSuffix;
    private List<MultipartFile> files;

}
