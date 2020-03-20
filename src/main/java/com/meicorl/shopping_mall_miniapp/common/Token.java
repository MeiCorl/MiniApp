package com.meicorl.shopping_mall_miniapp.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {
    private String openid;
    private String session_key;
}
