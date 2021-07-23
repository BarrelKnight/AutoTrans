package pers.noxcode.autotrans.trans.api;
import pers.noxcode.autotrans.trans.baidu.BaiduTranslator;

/**
 * 翻译器的接口
 * @author van人之雄
 * @version 1.0
 * @date 2021.07.21
 */

@SuppressWarnings("unused")
public interface Translator {
    /**
     * 标准的翻译方法
     * @param src 要翻译的文本内容， 为保证翻译的准确性要求在6000个字节以内
     * @param from 文本的原语言的代码，具体请参照项目的语种对照表
     * @param to 目标语言的代码，同上
     * @return 翻译后的文本，翻译失败返回null
     */
    String translate(String src, String from, String to);

    /**
     * 自动识别原语种并翻译为中文，需要原语种支持自动语种识别, 具体请参照项目的语种对照表
     * @param src 要翻译的文本内容， 为保证翻译的准确性要求在6000个字节以内
     * @return 翻译后的文本，翻译失败返回null
     */
    String translate(String src);

    /**
     * 自动识别原语种，并翻译为目标语种
     * @param src 要翻译的文本内容， 为保证翻译的准确性要求在6000个字节以内
     * @param to 目标语言的代码，具体请参照项目的语种对照表
     * @return 翻译后的文本，翻译失败返回null
     */
    String translate(String src, String to);

    /**
     * 获取一个翻译器的实例对象由{@link BaiduTranslator} 实现
     * @see BaiduTranslator
     * @return 实例对象
     */
    static Translator newBaiduTranslator(){
        return new BaiduTranslator();
    }
}
