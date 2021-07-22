package pers.noxcode.autotrans.trans.factory;

import pers.noxcode.autotrans.trans.api.Translator;
import pers.noxcode.autotrans.trans.baidu.BaiduTranslator;

/**
 * 用于创建Translator的实例对象
 * @author van人之雄
 * @version 1.0
 * @date 2021.07.22
 */

public class TranslatorFactory {
    /**
     * 获取一个Translator实现类的实例
     * @return 实例对象
     */
    public static Translator newTranslator() {
        //TODO
        return new BaiduTranslator();
    }

}
