package pers.noxcode.autotrans.trans.api;

import org.apache.logging.log4j.Logger;

/**
 * 日志管理的类，可以通过这个类{@code LOGGER}修改日志记录器
 *
 * @author van人之雄
 * @version 1.0
 * @date 2021.07.22
 */
public class LogManager {
    /**
     * 默认的日志记录器
     */
    private static Logger logger = org.apache.logging.log4j.LogManager.getLogger(LogManager.class);

    /**
     * 获取日志记录器
     *
     * @return 日志记录器
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * 修改日志记录器
     *
     * @param logger 新的日志记录器
     */
    @SuppressWarnings("unused")
    public static void setLogger(Logger logger) {
        LogManager.logger = logger;
    }
}
