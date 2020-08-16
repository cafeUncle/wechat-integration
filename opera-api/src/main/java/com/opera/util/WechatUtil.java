package com.opera.util;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

public class WechatUtil {

    private static final String GET_AUTH_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?" +
            "grant_type=client_credential&appid=wx788be908e58775d6&secret=e5b014acb2da9d2f6bf3ee639f656abf";

    private static final String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    private static final String CLEAR_LIMIT = "https://api.weixin.qq.com/cgi-bin/clear_quota?access_token=%s";

    private static String getAuthToken() throws IOException {
        String res = HttpUtil.get(GET_AUTH_TOKEN);
        return JSON.parseObject(res).getString("access_token");
    }

    private static void clearApiLimit() throws IOException {
        // 正式账号 token
        String authToken = "36_fDz3CanCzWKTFNKzcnB08e988zsMsbgfOeF4y9ei4eABi_3Nf_D_CZBxrOq2ywvhVMJ4HnoFoYGE66ZINShaEzah7oh3MG6EWsBmwvwUR8CN38zkxSEYO82IO4fPKD_IIQkCt-vmHVMDVXwWRCYiAGAXIE";
        String url = String.format(CLEAR_LIMIT, authToken);
        // 待清零的测试账号 appid
        HttpUtil.post(url, "{ \"appid\":\"wx788be908e58775d6\" }");
    }

    private static void createMenu() throws IOException {
        String authToken = getAuthToken();
        String url = String.format(CREATE_MENU, authToken);
        HttpUtil.post(url, "");
    }

    public static void main(String[] args) {
        try {
            clearApiLimit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
