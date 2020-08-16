package com.opera.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/")
    public String home() {
        System.out.println("income home");
        return "home";
    }
}
