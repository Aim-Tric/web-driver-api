package com.gp.webdriverapi.system.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.BaseController;
import com.gp.webdriverapi.common.annotation.Crypto;
import com.gp.webdriverapi.system.service.CategoryService;
import com.gp.webdriverapi.common.pojo.WdCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 *
 * @author Devonte
 * @date 2020/03/15
 */
@RestController
@Crypto(decrypt = false)
@Api(tags = "分类操作接口")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    @ApiOperation(value = "获得当前用户的分类数据")
    public R list() {
        List<WdCategory> categories = categoryService.list();
        return success(categories);
    }

    @PostMapping("/category")
    @ApiOperation(value = "创建分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "分类名称"),
            @ApiImplicitParam(name = "description", value = "分类描述"),
    })
    public R create(WdCategory category) {
        boolean saved = categoryService.save(category);
        return successOrFail(saved,
                categoryService.list(),
                CREATE_FAILED);
    }

    @DeleteMapping("/category")
    @ApiOperation(value = "删除分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "需要删除的分类id"),
    })
    public R delete(String id) {
        boolean removed = categoryService.removeById(id);
        return successOrFail(removed,
                categoryService.list(),
                DELETE_FAILED);
    }

    @PutMapping("/category")
    @ApiOperation(value = "更新分类信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "分类名称"),
            @ApiImplicitParam(name = "description", value = "分类描述"),
    })
    public R update(WdCategory category) {
        boolean updated = categoryService.update(new UpdateWrapper<>(category)
                .eq("id", category.getId()));
        return successOrFail(updated,
                categoryService.list(),
                UPDATE_FAILED);
    }

}
