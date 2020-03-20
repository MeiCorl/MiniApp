package com.meicorl.shopping_mall_miniapp.components;

import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HttpClient {
    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private OkHttpClient okHttpClient;

    @Autowired
    public HttpClient(OkHttpClient  okHttpClient) {
        this.okHttpClient= okHttpClient;
    }

    /**
     * get
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public String get(String url, Map<String, String> queries) {
        String responseBody = null;
        StringBuilder sb = new StringBuilder(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        Request request = new Request.Builder()
                .url(sb.toString())
                .build();
        logger.info("Http Request: " + request);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseBody = response.body().string();
                logger.info("Http Response: " + responseBody);
            }
            else {
                logger.error("Http Response: " + response.toString());
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return responseBody;
    }

    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public String post(String url, Map<String, String> params) {
        String responseBody = null;
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        logger.info("Http Request: " + request);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseBody = response.body().string();
                logger.info("Http Response: " + responseBody);
            }
            else {
                logger.error("Http Response: " + response.toString());
            }
        } catch (Exception e) {
            logger.error("okhttp3 post error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return responseBody;
    }

    /**
     * get
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public String getForHeader(String url, Map<String, String> queries) {
        String responseBody = null;
        StringBuilder sb = new StringBuilder(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        Request request = new Request.Builder()
                .addHeader("key", "value")
                .url(sb.toString())
                .build();
        logger.info("Http Request: " + request);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseBody = response.body().string();
                logger.info("Http Response: " + responseBody);
            }
            else {
                logger.error("Http Response: " + response.toString());
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return responseBody;
    }

    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public String postJsonParams(String url, String jsonParams) {
        String responseBody = null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        logger.info("Http Request: " + request);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseBody = response.body().string();
                logger.info("Http Response: " + responseBody);
            }
            else {
                logger.error("Http Response: " + response.toString());
            }
        } catch (Exception e) {
            logger.error("okhttp3 post error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return responseBody;
    }

    /**
     * Post请求发送xml数据....
     * 参数一：请求Url
     * 参数二：请求的xmlString
     * 参数三：请求回调
     */
    public String postXmlParams(String url, String xml) {
        String responseBody = null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        logger.info("Http Request: " + request);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseBody = response.body().string();
                logger.info("Http Response: " + responseBody);
            }
            else {
                logger.error("Http Response: " + response.toString());
            }
        } catch (Exception e) {
            logger.error("okhttp3 post error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return responseBody;
    }
}
