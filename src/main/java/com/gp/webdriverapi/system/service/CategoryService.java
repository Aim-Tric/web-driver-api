package com.gp.webdriverapi.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.webdriverapi.system.mapper.CategoryMapper;
import com.gp.webdriverapi.common.pojo.WdCategory;
import org.springframework.stereotype.Service;

/**
 * 分类服务类
 *
 * @author  Devonte
 * @date 2020/03/15
 */
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, WdCategory> {
}
