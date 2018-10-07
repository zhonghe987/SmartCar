package com.log;

/**
 * 日志文件输出模式
 */
public enum GenerateMode {

    /**
     * 按每个小时生成日志文
     */
    ByEveryHour,
    /**
     * 按每天生成日志文
     */
    ByEveryDay,
    /**
     * 按每个周生成日志文件
     */
    ByEveryWeek,
    /**
     * 按每个月生成日志文件
     */
    ByEveryMonth,
    /**
     * 按每季度生成日志文件
     */
    ByEverySeason ,
    /**
     * 按每年生成日志文
     */
    ByEveryYear,
}
