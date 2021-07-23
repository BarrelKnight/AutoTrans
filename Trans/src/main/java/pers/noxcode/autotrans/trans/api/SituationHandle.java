package pers.noxcode.autotrans.trans.api;

/**
 * 内部发生的异常会调用这个进行处理，外部只需要修改{@code SITUATION_HANDLE}就行了
 * @author van人之雄
 * @version 1.0
 * @date 2021.07.23
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface SituationHandle {
    /**
     * 默认处理，什么都不做
     */
    SituationHandle SITUATION_HANDLE = (info, e) -> {};

    /**
     * 处理异常
     * @param info 异常的信息
     * @param e 异常
     */
    void situationHandle(String info, Exception e);
}
