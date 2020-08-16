package com.opera.common.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    public static void main(String[] args) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "*/*");
        map.put("Pragma", "no-cache");
        map.put("Connection", "keep-alive");
        map.put("Cache-Control", "no-cache");
        map.put("Accept-Language", "zh-CN,zh;q=0.9");
        // map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");

        String url = "http://www.baidu.com/";
        String html = get(url, map);
        System.out.println(html);
    }

    /**
     * Send a get request
     *
     * @param url
     * @return response
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * Send a get request
     *
     * @param url     Url as string
     * @param headers Optional map with headers
     * @return response   Response as string
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        return fetch("GET", url, null, headers);
    }

    /**
     * Send a post request
     *
     * @param url     Url as string
     * @param body    Request body as string
     * @param headers Optional map with headers
     * @return response   Response as string
     * @throws IOException
     */
    public static String post(String url, String body, Map<String, String> headers) throws IOException {
        return fetch("POST", url, body, headers);
    }

    /**
     * Send a post request
     *
     * @param url  Url as string
     * @param body Request body as string
     * @return response   Response as string
     * @throws IOException
     */
    public static String post(String url, String body) throws IOException {
        return post(url, body, null);
    }

    public static String postFile(String url, InputStream inputStream, String fileName, long size) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String BOUNDARY = "----------" + System.currentTimeMillis();
        headers.put("content-type", "multipart/form-data; boundary=" + BOUNDARY);
        headers.put("charset", "UTF-8");

        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线 这里说明下，这两个横杠是http协议要求的，用来分隔提交的参数用的，不懂的可以看看http 协议头
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"type\" \r\n\r\n"); //这里是参数名，参数名和值之间要用两次
        sb.append("jpg"+"\r\n"); //参数的值

        //这块是上传video是必须的参数，你们可以在这里根据文件类型做if/else 判断
//        sb.append("--"); // 必须多两道线
//        sb.append(BOUNDARY);
//        sb.append("\r\n");
//        sb.append("Content-Disposition: form-data;name=\"description\" \r\n\r\n");
//        sb.append(j.toString()+"\r\n");

        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        //这里是media参数相关的信息，这里是否能分开下我没有试，感兴趣的可以试试
        sb.append("filename=\"" + fileName + "\";filelength=\"" + size + "\" \r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        return post(url, sb.toString() + streamToString(inputStream) + "\r\n--" + BOUNDARY + "--\r\n", headers);
}


    /**
     * Post a form with parameters
     *
     * @param url    Url as string
     * @param params map with parameters/values
     * @return response   Response as string
     * @throws IOException
     */
    public static String postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, params, null);
    }

    /**
     * Post a form with parameters
     *
     * @param url     Url as string
     * @param params  Map with parameters/values
     * @param headers Optional map with headers
     * @return response   Response as string
     * @throws IOException
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        // set content type
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        // parse parameters
        String body = "";
        if (params != null) {
            boolean first = true;
            for (String param : params.keySet()) {
                if (first) {
                    first = false;
                } else {
                    body += "&";
                }
                String value = params.get(param);
                body += URLEncoder.encode(param, "UTF-8") + "=";
                body += URLEncoder.encode(value, "UTF-8");
            }
        }

        return post(url, body, headers);
    }

    /**
     * Send a put request
     *
     * @param url     Url as string
     * @param body    Request body as string
     * @param headers Optional map with headers
     * @return response   Response as string
     * @throws IOException
     */
    public static String put(String url, String body, Map<String, String> headers) throws IOException {
        return fetch("PUT", url, body, headers);
    }

    /**
     * Send a put request
     *
     * @param url Url as string
     * @return response   Response as string
     * @throws IOException
     */
    public static String put(String url, String body) throws IOException {
        return put(url, body, null);
    }

    /**
     * Send a delete request
     *
     * @param url     Url as string
     * @param headers Optional map with headers
     * @return response   Response as string
     * @throws IOException
     */
    public static String delete(String url, Map<String, String> headers) throws IOException {
        return fetch("DELETE", url, null, headers);
    }

    /**
     * Send a delete request
     *
     * @param url Url as string
     * @return response   Response as string
     * @throws IOException
     */
    public static String delete(String url) throws IOException {
        return delete(url, null);
    }

    /**
     * Append query parameters to given url
     *
     * @param url    Url as string
     * @param params Map with query parameters
     * @return url   Url with query parameters appended
     * @throws IOException
     */
    public static String appendQueryParams(String url, Map<String, String> params) throws IOException {
        String fullUrl = url;
        if (params != null) {
            boolean first = (fullUrl.indexOf('?') == -1);
            for (String param : params.keySet()) {
                if (first) {
                    fullUrl += '?';
                    first = false;
                } else {
                    fullUrl += '&';
                }
                String value = params.get(param);
                fullUrl += URLEncoder.encode(param, "UTF-8") + '=' + URLEncoder.encode(value, "UTF-8");
            }
        }
        return fullUrl;
    }

    /**
     * Retrieve the query parameters from given url
     *
     * @param url Url containing query parameters
     * @return params     Map with query parameters
     * @throws IOException
     */
    public static Map<String, String> getQueryParams(String url) throws IOException {
        Map<String, String> params = new HashMap<>();

        int start = url.indexOf('?');
        while (start != -1) {
            // read parameter name
            int equals = url.indexOf('=', start);
            String param = "";
            if (equals != -1) {
                param = url.substring(start + 1, equals);
            } else {
                param = url.substring(start + 1);
            }

            // read parameter value
            String value = "";
            if (equals != -1) {
                start = url.indexOf('&', equals);
                if (start != -1) {
                    value = url.substring(equals + 1, start);
                } else {
                    value = url.substring(equals + 1);
                }
            }

            params.put(URLDecoder.decode(param, "UTF-8"), URLDecoder.decode(value, "UTF-8"));
        }

        return params;
    }

    /**
     * Returns the url without query parameters
     *
     * @param url Url containing query parameters
     * @return url        Url without query parameters
     * @throws IOException
     */
    public static String removeQueryParams(String url) throws IOException {
        int q = url.indexOf('?');
        if (q != -1) {
            return url.substring(0, q);
        } else {
            return url;
        }
    }

    /**
     * Send a request
     *
     * @param method  HTTP method, for example "GET" or "POST"
     * @param url     Url as string
     * @param body    Request body as string
     * @param headers Optional map with headers
     * @return response   Response as string
     * @throws IOException
     */
    public static String fetch(String method, String url, String body, Map<String, String> headers) throws IOException {
        // connection
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setConnectTimeout(100000);
        conn.setReadTimeout(300000);

        // method
        if (method != null) {
            conn.setRequestMethod(method);
        }

        // headers
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.addRequestProperty(key, headers.get(key));
            }
        }

        // body
        if (body != null) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();
        }

        // response
        InputStream is = conn.getInputStream();
        String response = streamToString(is);
        is.close();

        // handle redirects
        if (conn.getResponseCode() == 301) {
            String location = conn.getHeaderField("Location");
            return fetch(method, location, body, headers);
        }

        return response;
    }

    /**
     * Read an input stream into a string
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            out.append(line);
        }

        // byte[] b = new byte[4096];
        // for (int n; (n = in.read(b)) != -1; ) {
        //     out.append(new String(b, 0, n));
        // }

        return out.toString();
    }
}
