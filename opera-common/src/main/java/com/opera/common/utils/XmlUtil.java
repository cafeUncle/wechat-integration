package com.opera.common.utils;

import com.opera.common.models.XmlMessageRequest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XmlUtil {

    public static void main(String[] args) throws JAXBException {
        String xmlContent = "<xml>\n" +
                "  <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "  <CreateTime>1348831860</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA[this is a test]]></Content>\n" +
                "  <MsgId>1234567890123456</MsgId>\n" +
                "</xml>";

        XmlMessageRequest xmlMessageRequest = new XmlMessageRequest();
        xmlMessageRequest.setToUserName("ROSS");
        xmlMessageRequest.setFromUserName("GZ");

        JAXBContext context = JAXBContext.newInstance(XmlMessageRequest.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(xmlMessageRequest, System.out);
    }
}
