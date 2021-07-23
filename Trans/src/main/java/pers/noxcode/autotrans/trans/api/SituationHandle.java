package pers.noxcode.autotrans.trans.api;

/**
 * 内部发生的异常会调用这个进行处理，外部只需要修改{@code SITUATION_HANDLE}就行了
 *
 * @author van人之雄
 * @version 1.0
 * @date 2021.07.23
 */
@FunctionalInterface
public interface SituationHandle {
    /**
     * 处理异常
     *
     * @param info 异常的信息
     */
    void situationHandle(String info);
}
