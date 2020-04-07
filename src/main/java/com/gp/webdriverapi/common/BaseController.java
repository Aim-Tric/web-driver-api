package com.gp.webdriverapi.common;

import com.baomidou.mybatisplus.extension.api.R;
import com.gp.webdriverapi.common.utils.LogUtils;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 控制器基类
 *
 * @author vent
 * @date 2020/03/15
 */
@RequestMapping("/api/v1")
public class BaseController {

    protected static String CREATE_FAILED = "创建失败！";
    protected static String UPDATE_FAILED = "更新失败！";
    protected static String DELETE_FAILED = "删除失败！";
    protected static Logger logger = LogUtils.getPlatformLogger();

    protected R success() {
        return R.ok(null);
    }

    protected R successOrFail(boolean isSuccess, Object data, String failureMsg) {
        if(isSuccess) {
            return success(data);
        }
        return failed(failureMsg);
    }

    protected R success(Object data) {
        return R.ok(data);
    }

    protected R failed(String msg) {
        return R.failed(msg);
    }
}
