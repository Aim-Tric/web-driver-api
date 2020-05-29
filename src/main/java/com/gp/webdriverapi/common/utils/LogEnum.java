package com.gp.webdriverapi.common.utils;

/**
 * 本地日志枚举
 *
 * @author Devonte
 * @date 2020/03/15
 */
public enum LogEnum {

    /**
     * 业务型日志
     */
    BUSINESS("business"),
    /**
     * 平台日志
     */
    PLATFORM("platform"),
    /**
     * 数据库日志
     */
    DB("db"),
    /**
     * 异常日志
     */
    EXCEPTION("exception"),

    ;


    private String category;


    LogEnum(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}