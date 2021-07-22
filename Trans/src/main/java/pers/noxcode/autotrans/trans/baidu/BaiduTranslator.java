package pers.noxcode.autotrans.trans.baidu;

import org.apache.commons.codec.digest.DigestUtils;
import pers.noxcode.autotrans.trans.api.Translator;
import pers.noxcode.autotrans.trans.consts.LogManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;


/**
 * 百度翻译
 * @author van人之雄
 * @version 1.0
 * @date 2021.07.22
 */

public class BaiduTranslator implements Translator {
    /**
     * 百度翻译id， https://api.fanyi.baidu.com/doc/21查看
     */
    private static String APP_ID;
    /**
     * 百度翻译的密钥 https://api.fanyi.baidu.com/doc/21查看
     */
    private static String SECURITY_KEY;
    /**
     * 百度翻译的主机
     */
    private static String TRANS_API_HOST;

    /**
     * 连接超时时长
     */
    private static String connectionTimeout;

    /**
     * 用于访问百度的服务器
     */
    private HttpClient httpClient;


    //从配置文件中读取，初始化app_id和密钥
    static {
        InputStream inputStream = BaiduTranslator.class.getClassLoader().getResourceAsStream("Translator.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            LogManager.LOGGER.error("配置文件：Translator.properties丢失，请检查配置文件");
            //TODO
            e.printStackTrace();
        }
        //非空检测
        try {
            APP_ID = Optional.of(properties.getProperty("baidu_app_id")).get();
            SECURITY_KEY = Optional.of(properties.getProperty("baidu_security_key")).get();
            TRANS_API_HOST = Optional.of(properties.getProperty("baidu_trans_host")).get();
            connectionTimeout = Optional.of(properties.getProperty("baidu_connection_timeout")).get();
        } catch (NullPointerException e) {
            LogManager.LOGGER.fatal("配置文件异常，请检查配置文件的内容是否为空");
            //TODO
            e.printStackTrace();
        }
    }

    /**
     * 初始化httpClient
     * 配置ssl证书
     */
    public BaiduTranslator() {
        SSLContext sslcontext;
        try {
            sslcontext = SSLContext.getInstance("TLS");
            //ssl初始化的参数，不做任何处理
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {}
                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {}
                @Override
                public X509Certificate[] getAcceptedIssuers() {return null;}
            };

            sslcontext.init(null, new TrustManager[]{x509TrustManager}, null);
        } catch (NoSuchAlgorithmException e) {
            LogManager.LOGGER.error("未知错误，获取SSLContext失败，请重试");
            e.printStackTrace();
            return;
        } catch (KeyManagementException e) {
            LogManager.LOGGER.error("未知错误，ssl初始化失败，请重试");
            e.printStackTrace();
            return;
        }
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(Long.parseLong(connectionTimeout)))
                .sslContext(sslcontext)
                .build();
    }

    /**
     * 标准的翻译方法
     * @param src  要翻译的文本内容， 为保证翻译的准确性要求在6000个字节以内
     * @param from 文本的原语言的代码，具体请参照项目的语种对照表 或访问{https://api.fanyi.baidu.com/doc/21}
     * @param to   目标语言的代码，同上
     * @return 翻译后的文本，翻译失败返回null
     */
    @Override
    public String translate(String src, String from, String to) {
        //构造请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUrlWithQueryString(src, from, to)))
                .GET()
                .build();
        try {
            //发送请求
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 自动识别原语种并翻译为中文，需要原语种支持自动语种识别, 具体请参照项目的语种对照表 或访问{https://api.fanyi.baidu.com/doc/21}
     * @param src 要翻译的文本内容， 为保证翻译的准确性要求在6000个字节以内
     * @return 翻译后的文本，翻译失败返回null
     */
    @Override
    public String translate(String src) {
        return translate(src, "auto", "zh");
    }

    /**
     * 自动识别原语种，并翻译为目标语种
     *
     * @param src 要翻译的文本内容， 为保证翻译的准确性要求在6000个字节以内
     * @param to  目标语言的代码，具体请参照项目的语种对照表 或访问{https://api.fanyi.baidu.com/doc/21}
     * @return 翻译后的文本，翻译失败返回null
     */
    @Override
    public String translate(String src, String to) {
        return translate(src, "auto", to);
    }


    /**
     * 通过参数，安装百度翻译的协议构建url
     * @param query 要翻译的文本内容， 为保证翻译的准确性要求在6000个字节以内
     * @param from 文本的原语言的代码，具体请参照项目的语种对照表 或访问{https://api.fanyi.baidu.com/doc/21}
     * @param to 目标语言的代码，具体请参照项目的语种对照表 或访问{https://api.fanyi.baidu.com/doc/21}
     * @return 构建完成的url
     */
    private String getUrlWithQueryString(String query, String from, String to) {

        StringBuilder url = new StringBuilder(TRANS_API_HOST).append('?');
        url.append('&').append("q").append('=').append(urlEncode(query));
        url.append('&').append("from").append('=').append(urlEncode(from));
        url.append('&').append("to").append('=').append(urlEncode(to));
        url.append('&').append("appid").append('=').append(urlEncode(APP_ID));
        String salt = String.valueOf(~System.currentTimeMillis());
        url.append('&').append("salt").append('=').append(urlEncode(salt));
        url.append('&').append("sign").append('=').append(urlEncode(DigestUtils.md5Hex(APP_ID + query + salt + SECURITY_KEY)));
        return url.toString();
    }

    /**
     * 为了使代码跟美观，提取的公共代码
     * 对字符串进行URL编码
     * @param src 源字符串
     * @return 编码的结果，以字符串形式
     */
    private String urlEncode(String src) {
        return URLEncoder.encode(src, StandardCharsets.UTF_8);
    }
}
