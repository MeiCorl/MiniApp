package com.meicorl.shopping_mall_miniapp.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meicorl.shopping_mall_miniapp.common.GlobalException;
import com.meicorl.shopping_mall_miniapp.components.HttpClient;
import com.meicorl.shopping_mall_miniapp.mybatis.pojo.User;
import com.meicorl.shopping_mall_miniapp.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WechatService {
    private final static String wxUserInfoUrl = "https://api.weixin.qq.com/sns/jscode2session";
    private final static String wxTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";

    // auth 用于保存微信后台接口access_token及过期时间
    private ConcurrentHashMap<String, String> auth = new ConcurrentHashMap<>();

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    HttpClient httpClient;

    @Autowired
    UserService userService;

    @Value("${wechat.miniapp.appid}")
    private String appid;

    @Value("${wechat.miniapp.secret}")
    private String secret;

    /**
     * 从微信后台获取小程序用户信息，并生成自定义登录token
     * @param code 小程序通过wx.login()获取到的code
     * @param nickName 用户昵称
     * @param headImg 用户头像地址
     * @param sex 用户性别
     * @param phoneNumber 用户手机号
     * @return 成功返回自定义token
     */
    public JSONObject getWxUserInfo(String code, String nickName, String headImg, String sex, String phoneNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");

        String res = httpClient.get(wxUserInfoUrl, params);
        if(res == null)
            throw new GlobalException("获取微信登录凭证校验异常网络异常!");

        JSONObject jsonRes = JSON.parseObject(res);
        if(jsonRes.getIntValue("errcode") != 0)
            throw new GlobalException("获取微信登录凭证校验异常: " + jsonRes.getString("errmsg"));

        // 判断当前用户是否已在系统中
        User user;
        String openId = jsonRes.getString("openid");
        if(!stringRedisTemplate.opsForSet().isMember("miniapp_user_openids", openId)) {
            // 保存新用户信息
            user = new User(openId, phoneNumber, nickName, sex, headImg, new Date());
            userService.saveUser(user);

            stringRedisTemplate.opsForSet().add("miniapp_user_openids", openId);
            log.info("新用户保存成功: " + JSON.toJSONString(user));
        }

        // 根据微信返回信息生成自定义token和refreshToken
        jsonRes.remove("errcode");
        jsonRes.remove("errmsg");
        jsonRes.remove("unionid");
        String token = TokenUtil.generateToken(jsonRes);

        JSONObject result = new JSONObject();
        result.put("token", token);
        return result;
    }

    /**
     * 获取微信后台接口调用凭证
     */
    private String getWxAccessToken() {
        String access_token = this.auth.get("access_token");
        String expire_time = this.auth.get("expire_time");
        if( access_token == null || Long.parseLong(expire_time) * 1000 < System.currentTimeMillis())
            return refreshWxAccessToken();
        return access_token;
    }

    /**
     * 刷新微信access_token并返回
     */
    private String refreshWxAccessToken() {
        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("grant_type", "client_credential");

        String res = httpClient.get(wxTokenUrl, params);
        if(res == null)
            throw new GlobalException("获取微信token异常网络异常!");

        JSONObject jsonRes = JSON.parseObject(res);
        if(jsonRes.getIntValue("errcode") != 0)
            throw new GlobalException("获取微信token异常: " + jsonRes.getString("errmsg"));

        // 计算改token过期时间, 为了及时刷新token, 提前1分钟刷新token
        long expire_time = System.currentTimeMillis() / 1000 + jsonRes.getLongValue("expires_in") - 60;
        this.auth.put("access_token", jsonRes.getString("access_token"));
        this.auth.put("expire_time", String.valueOf(expire_time));
        log.info("Wechat access_token refreshed, next expire time: " + expire_time);

        return this.auth.get("access_token");
    }
}
