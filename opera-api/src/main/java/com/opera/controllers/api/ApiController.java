package com.opera.controllers.api;

import com.opera.cron.AuthTokenFetcher;
import com.opera.common.models.XmlMessageRequest;
import com.opera.common.models.response.TextResponse;
import com.opera.common.utils.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.opera.common.utils.CyptUtil;
import com.opera.common.utils.NameUtil;

import java.io.IOException;
import java.util.Arrays;

@RestController
public class ApiController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static final String token = "sdiug81ijdasjk123lhsa";

    @Autowired
    AuthTokenFetcher authTokenFetcher;

    @GetMapping("/o")
    public String hi() {
        logger.error("hi~");
        return authTokenFetcher.getAuthToken();
    }

    @GetMapping("/token")
    public String checkSignature(@RequestParam String signature, @RequestParam String timestamp,
                                 @RequestParam String nonce, @RequestParam String echostr) {
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);

        String[] params = {timestamp, nonce, token};
        Arrays.sort(params);

        String joinResult = String.join("", Arrays.asList(params));

        if (signature.equals(CyptUtil.getSha1(joinResult.getBytes()))) {
            return echostr;
        } else {
            return "error";
        }
    }

    /**
     * 关于重试的消息排重，有msgid的消息推荐使用msgid排重。事件类型消息推荐使用FromUserName + CreateTime 排重。
     *
     * 假如服务器无法保证在五秒内处理并回复，必须做出下述回复，这样微信服务器才不会对此作任何处理，
     * 并且不会发起重试（这种情况下，可以使用客服消息接口进行异步回复），否则，将出现严重的错误提示。详见下面说明：
     * 1、直接回复success（推荐方式） 2、直接回复空串（指字节长度为0的空字符串，而不是XML结构体中content字段的内容为空）
     *
     * 一旦遇到以下情况，微信都会在公众号会话中，向用户下发系统提示“该公众号暂时无法提供服务，请稍后再试”：
     * 1、开发者在5秒内未回复任何内容 2、开发者回复了异常数据，比如JSON数据等
     *
     * 微信的请求头是 text/xml，不是application/json 也不是 application/xml，配错的话会返回 415 错误
     * @param o 请求消息
     */
    @PostMapping(value = "/token", consumes="text/xml", produces = "text/xml")
    public String action(@RequestBody XmlMessageRequest o) {
        System.out.println(o);
        if ("text".equals(o.getMsgType())) {
            String content = o.getContent();
            String responseContent = null;
            if (content.contains("起名")) {
                String gender = content.substring(content.length() - 1);
                String key = content.substring(content.length() - 2, content.length() -1);
                responseContent = NameUtil.getName(key, gender);
                logger.info("一个起名的请求");
            } else {
                responseContent = "回复 - " + o.getContent();
            }

            return TextResponse.build(o.getFromUserName(), o.getToUserName(), o.getCreateTime(), "text", responseContent);
        }
        return "success";
    }

    /**
     * 分页查询素材列表
     * @param type
     * @param offset
     * @param count
     * @return
     */
    @GetMapping(value = "/medias/{type:[a-z]+}/{offset:\\d+}/{count:\\d+}")
    public String medias(@PathVariable String type, @PathVariable int offset, @PathVariable int count) {
        try {
            return WechatUtil.queryMedia(type, offset, count);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
