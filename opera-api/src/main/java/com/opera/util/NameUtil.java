package com.opera.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class NameUtil {

    private static final String URL = "http://www.guabu.com/api/qiming/shici/?key=%s&sex=%d&num=1";

    public static String getName(String key, String gender) {
        int sex = "女".equals(gender) ? 0 : 1;
        try {
            String result = HttpUtil.get(String.format(URL, key, sex));
            JSONObject jsonObject = JSON.parseObject(result);
            JSONArray data = jsonObject.getJSONArray("data");
            if (!data.isEmpty()) {
                return data.getJSONObject(0).getString("name");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "起名失败";
    }
}
