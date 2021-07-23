package pers.noxcode.autotrans.trans.api;

/**
 * 异常处理的实现类,我们可以通过修改{@code situationHandle}来改变本项目的异常处理机制
 *
 * @author van人之雄
 * @version 1.0
 * @date 2021.07.23
 * @see SituationHandle
 */

public class ExceptionManager {
    /**
     * 默认的异常处理
     */
    private static SituationHandle situationHandle = System.out::println;

    /**
     * 获取异常处理器
     *
     * @return 异常处理器
     */
    public static SituationHandle getSituationHandle() {
        return situationHandle;
    }

    /**
     * 修改异常处理器
     *
     * @param situationHandle 新的异常处理器
     */
    @SuppressWarnings("unused")
    public static void setSituationHandle(SituationHandle situationHandle) {
        if (situationHandle != null) {
            ExceptionManager.situationHandle = situationHandle;
        }
    }
}
