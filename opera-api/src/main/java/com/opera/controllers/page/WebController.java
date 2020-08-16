package com.opera.controllers.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opera.common.utils.HttpUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * https://blog.csdn.net/xueba8/article/details/85253743
 * 在 view resolver 配置异常的情况下，return view 与 mapping 同名，则返回 circle 异常，不同名则返回 404 异常。
 * 上述这篇文章有说明，同时也描述了下为什么 jsp 只需要 internalViewResolver，而不需要额外配置其它。（但不推荐他的改法
 * 所以排除静态资源即可，思路如下：
 * 1. 如 jsp 一般也在 web.xml 配一个 servlet-mapping servlet-name 复用 default url-pattern *.html
 * 2. <mvc:resources location="/html/" mapping="/**.html" />
 */
@Controller
public class WebController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/")
    public String home() {
        logger.info("income home");
        return "home";
    }

    @RequestMapping("/page/welcome")
    public String firstPage() {
        return "welcome";
    }

    @RequestMapping("/get_code")
    public void getCode(@RequestParam String code, @RequestParam String state,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取各种数据，存到 session，然后送回公众号网页首页

        System.out.println(code);
        System.out.println(state);

        // 用 code 换取 web accessToken
        String addr = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=wx788be908e58775d6" +
                "&secret=e5b014acb2da9d2f6bf3ee639f656abf" +
                "&code=%s&grant_type=authorization_code";

        String res = HttpUtils2.get(String.format(addr, code));
        JSONObject jsonObject = JSON.parseObject(res);
        System.out.println(jsonObject);

        String accessToken = jsonObject.getString("access_token");
        String refreshToken = jsonObject.getString("refresh_token");
        String openId = jsonObject.getString("openid");

        request.getSession().setAttribute("access_token", accessToken);
        request.getSession().setAttribute("refresh_token", refreshToken);
        request.getSession().setAttribute("openId", openId);
        // access_token 两小时过期，这里设置一个一小时的过期时间，用来主动刷新 access_token
        request.getSession().setAttribute("token_expire_time", System.currentTimeMillis() + 3600 * 1000);

        response.sendRedirect("/page/welcome");
    }
}
