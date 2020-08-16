package com.opera.common.utils;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;

public class WechatUtil {

    private static final String GET_AUTH_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?" +
            "grant_type=client_credential&appid=wx788be908e58775d6&secret=e5b014acb2da9d2f6bf3ee639f656abf";

    private static final String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    private static final String CLEAR_LIMIT = "https://api.weixin.qq.com/cgi-bin/clear_quota?access_token=%s";

    // 创建临时素材，返回 type，create_at，media_id
    private static final String CREATE_TMP_MEDIA = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";

    // 创建永久图文素材，返回 media_id
    private static final String CREATE_ALBUM_MEDIA = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=%s";

    // 上传图文消息内的图片，获取URL。 图文消息的具体内容中，微信后台将过滤外部的图片链接，所以所有图片必须用这个接口上传创建
    private static final String CREATE_IMAGE = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=%s";

    // 创建永久其它素材，image video voice thumb，返回 media_id，新增图片素材时还会返回 url
    private static final String CREATE_OTHER_MEDIA = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=%s&type=%s";

    private static final String QUERY_MEDIA = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=%s";

    private static final String QUERY_MEDIA_COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=%s";

    private static final String CREATE_KF_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=%s";

    private static final String GET_KF_ACCOUNTS = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=%s";

    private static final String SEND_KF_MSG = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";

    private static final String CREATE_POI = "http://api.weixin.qq.com/cgi-bin/poi/addpoi?access_token=%s";

    public static String AUTH_TOKEN = null;

    private static String getAuthToken() throws IOException {
        if (AUTH_TOKEN == null) {
            String res = HttpUtil.get(GET_AUTH_TOKEN);
            return JSON.parseObject(res).getString("access_token");
        }
        return AUTH_TOKEN;
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

    public static String createMedia(String type, File file) throws IOException {
        return HttpUtils2.postFile(String.format(CREATE_TMP_MEDIA, getAuthToken(), type), file, "media");
    }

    public static String queryMedia(String type, int offset, int count) throws IOException {
        String body = "{\n" +
                "    \"type\":\"%s\",\n" +
                "    \"offset\":%d,\n" +
                "    \"count\":%d\n" +
                "}";
        return HttpUtils2.post(String.format(QUERY_MEDIA, getAuthToken()), String.format(body, type, offset, count));
    }

    public static String queryMediaCount() throws IOException {
        return HttpUtils2.get(String.format(QUERY_MEDIA_COUNT, getAuthToken()));
    }

    public static String createKfAccount() throws IOException {
        // 测试号不要添加客服就可以发送客服消息。测试号调用创建、查询时，会返回 65400 错误
        String data = "{\n" +
                "     \"kf_account\" : \"yang@gh_82281c31c5a4\",\n" +
                "     \"nickname\" : \"客服1号\",\n" +
                "     \"password\" : \"passwd\"\n" +
                "}";
        return HttpUtils2.post(String.format(CREATE_KF_ACCOUNT, getAuthToken()), data);
    }

    public static String getKfAccounts() throws IOException {
        return HttpUtils2.get(String.format(GET_KF_ACCOUNTS, getAuthToken()));
    }

    public static String createImage(File file) throws IOException {
        // sample: http://mmbiz.qpic.cn/mmbiz_png/TyjHe2tZsaouGWWVpTjTV8NqHPfufWWwcGn7tLCOevx7biah11rjKkVn73R7WKUDXIheu5cJZfUQq8L8kqg2gOw/0
        return HttpUtils2.postFile(String.format(CREATE_IMAGE, getAuthToken()), file, "media");
    }

    public static String sendKfMsg() throws IOException {
        String msg = "{\n" +
                "    \"touser\":\"oqdZ86qa77lIq12Wc2SSj1ZKcJvA\",\n" +
                "    \"msgtype\":\"text\",\n" +
                "    \"text\":\n" +
                "    {\n" +
                "         \"content\":\"Hello World\"\n" +
                "    }\n" +
                "}";
        return HttpUtils2.post(String.format(SEND_KF_MSG, getAuthToken()), msg);
    }

    public static String createPoi() throws IOException {
        String body = "{\"business\":{\n" +
                "\"base_info\":{\n" +
                "\"sid\":\"33788392\",\n" +
                "\"business_name\":\"创意美食\",\n" +
                "\"branch_name\":\"望京店\",\n" +
                "\"province\":\"这个省份\",\n" +
                "\"city\":\"这个城市\",\n" +
                "\"district\":\"这个地区\",\n" +
                "\"address\":\"这个地址\",\n" +
                "\"telephone\":\"010-12312312\",\n" +
                "\"categories\":[\"美食,小吃快餐\"],\n" +
                "\"offset_type\":3,\n" +
                "\"longitude\":116.39039,\n" +
                "\"latitude\":40.00376,\n" +
                "\"photo_list\":[{\"photo_url\":\"http://mmbiz.qpic.cn/mmbiz_png/TyjHe2tZsaouGWWVpTjTV8NqHPfufWWwcGn7tLCOevx7biah11rjKkVn73R7WKUDXIheu5cJZfUQq8L8kqg2gOw/0\"}],\n" +
                "\"recommend\":\"麦辣鸡腿堡套餐，麦乐鸡，全家桶\",\n" +
                "\"special\":\"免费wifi，外卖服务\",\n" +
                "\"introduction\":\"全球大型跨国连锁餐厅，1940 年创立于美国，在世界上大约拥有3 万间分店。\n" +
                "主要售卖汉堡包，以及薯条、炸鸡、汽水、冰品、沙拉、 水果等快餐食品\",\n" +
                "\"open_time\":\"8:00-20:00\",\n" +
                "\"avg_price\":35\n" +
                "}\n" +
                "}\n" +
                "}";
        return HttpUtils2.post(String.format(CREATE_POI, getAuthToken()), body);
    }

    public static void main(String[] args) {
        try {
            System.out.println(createPoi());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
