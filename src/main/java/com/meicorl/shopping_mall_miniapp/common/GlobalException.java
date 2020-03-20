package com.meicorl.shopping_mall_miniapp.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description: 通用异常
 *
 * @author ：meicorl
 * @date ：2019/8/24 23:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GlobalException(String msg) {
        super(msg);
    }
}
