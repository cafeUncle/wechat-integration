package com.opera.common.models.response;

public class TextResponse {

    private static final String TEMPLATE = "<xml>\n" +
            "  <ToUserName>%s</ToUserName>\n" +
            "  <FromUserName>%s</FromUserName>\n" +
            "  <CreateTime>%d</CreateTime>\n" +
            "  <MsgType>%s</MsgType>\n" +
            "  <Content>%s</Content>\n" +
            "</xml>";

    public static String build(String toUserName, String fromUserName, Long createTime, String msgType, String content) {
        return String.format(TEMPLATE, toUserName, fromUserName, createTime, msgType, content);
    }
}
