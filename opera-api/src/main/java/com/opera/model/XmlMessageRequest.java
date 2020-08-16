package com.opera.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name ="xml")
public class XmlMessageRequest implements Serializable {
    /**
     * 开发者微信号
     */
    @XmlElement
    private String ToUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @XmlElement
    private String FromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @XmlElement
    private Long CreateTime;
    /**
     * 消息类型
     *
     * 普通消息：text / image / voice / video / shortvideo / location / link 文本消息 图片消息 语音消息 视频消息 小视频消息 地理位置消息 链接消息
     *
     * 事件推送：event 事件子类型用 EVENT 字段区分
     */
    @XmlElement
    private String MsgType;


    /**
     * 普通消息的消息id，64位整型。 事件消息没有该字段。
     */
    @XmlElement
    private Long MsgId;

    /**
     * 文本消息内容
     */
    @XmlElement
    private String Content;

    /**
     * 图片消息的图片链接（由系统生成）
     */
    @XmlElement
    private String PicUrl;

    /**
     * 图片消息 / 语音消息 / 视频(和小视频)消息 的媒体id，可以调用获取临时素材接口拉取数据。
     */
    @XmlElement
    private String MediaId;

    /**
     * 语音格式，如amr，speex等
     */
    @XmlElement
    private String Format;
    /**
     * 开通语音识别后，语音识别结果，UTF8编码
     */
    @XmlElement
    private String Recognition;

    /**
     * 视频(和小视频)消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XmlElement
    private String ThumbMediaId;

    /**
     * 地理位置纬度
     */
    @XmlElement
    private String Location_X;
    /**
     * 地理位置经度
     */
    @XmlElement
    private String Location_Y;
    /**
     * 地图缩放大小
     */
    @XmlElement
    private String Scale;
    /**
     * 地理位置信息
     */
    @XmlElement
    private String Label;

    /**
     * 链接消息的标题
     */
    @XmlElement
    private String Title;
    /**
     * 链接消息的描述
     */
    @XmlElement
    private String Description;
    /**
     * 链接消息的链接
     */
    @XmlElement
    private String Url;



    /**
     * 事件类型
     *
     * 关注/取消关注事件 subscribe / unsubscribe 仅 ToUserName FromUserName CreateTime MsgType Event 字段
     *
     * 扫描带参数二维码事件  用户未关注时，进行关注后的事件推送 subscribe 用户已关注时的事件推送 SCAN
     * （如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。如果用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者。）
     *
     * 上报地理位置事件 LOCATION
     * （用户同意上报地理位置后，每次进入公众号会话时，都会在进入时上报地理位置，或在进入会话后每5秒上报一次地理位置，公众号可以在公众平台网站中修改以上设置。上报地理位置时，微信会将上报地理位置事件推送到开发者填写的URL。）
     *
     * 自定义菜单事件 CLICK VIEW location_select 等
     */
    @XmlElement
    private String Event;

    // 二维码事件 或 自定义菜单事件 相关
    /**
     * 未关注扫二维码 事件KEY值，qrscene_为前缀，后面为二维码的参数值
     * 已关注扫二维码 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
     * 自定义菜单事件 事件KEY值，CLICK 时与自定义菜单接口中KEY值对应，VIEW 时为设置的跳转URL，location_select 为null
     */
    @XmlElement
    private String EventKey;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    @XmlElement
    private String Ticket;

    // 上报地理位置事件 相关
    /**
     * 地理位置纬度
     */
    @XmlElement
    private String Latitude;
    /**
     * 地理位置经度
     */
    @XmlElement
    private String Longitude;
    /**
     * 地理位置精度
     */
    @XmlElement
    private String Precision;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public Long getMsgId() {
        return MsgId;
    }

    public void setMsgId(Long msgId) {
        MsgId = msgId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public String getRecognition() {
        return Recognition;
    }

    public void setRecognition(String recognition) {
        Recognition = recognition;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }

    public String getLocation_X() {
        return Location_X;
    }

    public void setLocation_X(String location_X) {
        Location_X = location_X;
    }

    public String getLocation_Y() {
        return Location_Y;
    }

    public void setLocation_Y(String location_Y) {
        Location_Y = location_Y;
    }

    public String getScale() {
        return Scale;
    }

    public void setScale(String scale) {
        Scale = scale;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPrecision() {
        return Precision;
    }

    public void setPrecision(String precision) {
        Precision = precision;
    }

    @Override
    public String toString() {
        return "XmlMessageRequest{" +
                "ToUserName='" + ToUserName + '\'' +
                ", FromUserName='" + FromUserName + '\'' +
                ", CreateTime=" + CreateTime +
                ", MsgType='" + MsgType + '\'' +
                ", MsgId=" + MsgId +
                ", Content='" + Content + '\'' +
                ", PicUrl='" + PicUrl + '\'' +
                ", MediaId='" + MediaId + '\'' +
                ", Format='" + Format + '\'' +
                ", Recognition='" + Recognition + '\'' +
                ", ThumbMediaId='" + ThumbMediaId + '\'' +
                ", Location_X='" + Location_X + '\'' +
                ", Location_Y='" + Location_Y + '\'' +
                ", Scale='" + Scale + '\'' +
                ", Label='" + Label + '\'' +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Url='" + Url + '\'' +
                ", Event='" + Event + '\'' +
                ", EventKey='" + EventKey + '\'' +
                ", Ticket='" + Ticket + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Precision='" + Precision + '\'' +
                '}';
    }
}
