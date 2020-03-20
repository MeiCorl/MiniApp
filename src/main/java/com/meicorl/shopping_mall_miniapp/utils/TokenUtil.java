package com.meicorl.shopping_mall_miniapp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meicorl.shopping_mall_miniapp.common.Token;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Random;

public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * 根据用户身份生成token
     * @return token
     */
    public static String generateToken(JSONObject object) {
        return CommonUtil.parseByte2HexStr(encodeString(object.toJSONString()).getBytes());
    }

    /**
     * 根据token信息验证用户身份及有效性
     * @return token有效返回用户true, 否则返回false
     */
    public static boolean checkToken(String tokenStr, Token token) {
        try {
            String decodedToken = encodeString(new String(Objects.requireNonNull(CommonUtil.parseHexStr2Byte(tokenStr))));
            JSONObject jsonToken = JSONObject.parseObject(decodedToken);
            token.setOpenid(jsonToken.getString("openid"));
            token.setSession_key(jsonToken.getString("session_key"));
            return !StringUtils.isEmpty(token.getOpenid()) && !StringUtils.isEmpty(token.getSession_key());
        }catch (Exception e) {
            // token 无法解析
            logger.error("token解析失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 生成指定位随机字符串
     * @return 随机字符串
     */
    private static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder(length);
        for(int i = 0; i< length; i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 自定义加密字符串
     * @param str 待加密字符串
     * @return 加密字符串
     */
    private static String encodeString(String str) {
        String password = "S9LYUl73fquXIRCosFHyVKY9HUKAlT7O";
        char[] pwd = password.toCharArray();
        int pwdLen = pwd.length;

        char[] strArray = str.toCharArray();
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = (char)(strArray[i] ^ pwd[i % pwdLen]);
        }
        return new String(strArray);
    }

    public static void main(String[] args) {
        JSONObject jo = new JSONObject();
        jo.put("openid", "caomei");
        jo.put("session_key", "test");

        String token = generateToken(jo);
        System.out.println(token);

        Token t = new Token();
        System.out.println(checkToken(token, t));
        System.out.println(JSON.toJSONString(t));
    }
}
