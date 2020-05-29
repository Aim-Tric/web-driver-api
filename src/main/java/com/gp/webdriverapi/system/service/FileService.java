package com.gp.webdriverapi.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.webdriverapi.common.pojo.WdFile;
import com.gp.webdriverapi.system.mapper.FileMapper;
import org.springframework.stereotype.Service;

/**
 * 文件服务
 *
 * @author Devonte
 * @date 2020/03/15
 */
@Service
public class FileService extends ServiceImpl<FileMapper, WdFile> {

}
