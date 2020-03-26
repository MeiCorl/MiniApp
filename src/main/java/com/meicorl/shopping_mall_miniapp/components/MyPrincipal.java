package com.meicorl.shopping_mall_miniapp.components;

import com.meicorl.shopping_mall_miniapp.common.Token;

import java.security.Principal;

public interface MyPrincipal extends Principal {
    public Token getToken();
}
