package com.opera.interceptors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opera.common.utils.HttpUtils2;
import org.apache.commons.io.Charsets;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 订阅号没有网页获取用户基本信息的能力 服务号必须通过微信认证
 */
public class WechatPageInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestURI = request.getRequestURI();

        // 如果是微信回调页面，则进入 handler 去获取数据，存到 session，然后送回公众号网页首页
        // 这里检查一下看要不要打日志，实际可以不配在拦截范围里
        if (requestURI.startsWith("/get_code")) { // get_code?code=%s&state=%s
            return true;
        }

        // 非回调页面，判断是否已记录用户信息
        Long tokenExpireTime = (Long)request.getSession().getAttribute("token_expire_time");
        if (tokenExpireTime == null) {
            // 应用授权作用域
            // snsapi_base，不弹出授权页面，直接跳转，只能获取用户openid。response_type 需在 state 后
            // snsapi_userinfo，弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息。 response_type 需在 state 前
            // 在需要的时候再获取 snsapi_userinfo，更新或插入到 openId 相关表里

            String addr = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid=wx3132c952fe582974" +
                    "&redirect_uri=%s" +
                    "&response_type=code" +
                    "&scope=snsapi_base" +
                    "&state=#wechat_redirect";
            String callbackUrl = URLEncoder.encode("https://387d6837c0e3.ngrok.io/get_code", Charsets.UTF_8);

            response.sendRedirect(String.format(addr, callbackUrl));
            return false;
        }

        // 非回调页面，判断 access_token 是否需要主动刷新
        if (tokenExpireTime > System.currentTimeMillis()) {
            String refreshToken = (String) request.getSession().getAttribute("refresh_token");
            String addr = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                    "appid=wx3132c952fe582974" +
                    "&grant_type=refresh_token" +
                    "&refresh_token=%s";

            String res = HttpUtils2.get(String.format(addr, refreshToken));

            JSONObject jsonObject = JSON.parseObject(res);
            String accessToken = jsonObject.getString("access_token");

            // 存储新的 access_token 及 过期时间
            request.getSession().setAttribute("access_token", accessToken);
            request.getSession().setAttribute("token_expire_time", System.currentTimeMillis() + 3600 * 1000);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
