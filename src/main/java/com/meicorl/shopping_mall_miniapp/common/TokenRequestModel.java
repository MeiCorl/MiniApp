package com.meicorl.shopping_mall_miniapp.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取token请求实体")
public class TokenRequestModel {
    @ApiModelProperty(value = "微信登录票据", required = true)
    private String code;

    @ApiModelProperty(value = "用户昵称", required = true)
    private String nickName;

    @ApiModelProperty(value = "用户头像地址", required = true)
    private String headImg;

    @ApiModelProperty(value = "用户性别")
    String sex;

    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;
}