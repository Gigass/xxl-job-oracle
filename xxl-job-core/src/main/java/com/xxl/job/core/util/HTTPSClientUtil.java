package com.xxl.job.core.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
public class HTTPSClientUtil {
    /**
     * 接口调用(post请求) 数据处理
     *
     * @param :url
     *            请求路径 例如：http://127.0.0.1:8080/test/test
     * @param :param
     *            请求参数 例如：{ "userName":"Lily", "password":"123456" }
     * @return 响应数据 例如：{ "resultId":"1" "resultMsg":"操作成功" }
     */
    public static String insureResponsePost(String param) {
        //将json字符串转化成json对象
        JSONObject jo = JSON.parseObject(param);
        String url=jo.containsKey("url")?jo.getString("url"):"";
        param=jo.containsKey("param")?jo.getString("param"):"";
        String header=jo.containsKey("header")?jo.getString("header"):"";
        PrintWriter out = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = "";
        HttpURLConnection conn = null;
        StringBuffer strBuffer = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod( "POST");
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(300000);
            conn.setRequestProperty("Charset", "UTF-8");
           // 传输数据为json，如果为其他格式可以进行修改
            conn.setRequestProperty( "Content-Type", "application/json");
            conn.setRequestProperty( "Content-Encoding", "utf-8");
            if(!StringUtil.isNullOrEmpty(header)){
                JSONObject jsonObject=JSON.parseObject(header);
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    conn.setRequestProperty( entry.getKey(),entry.getValue().toString());
                }

            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput( true);
            conn.setDoInput( true);
            conn.setUseCaches( false);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            is = conn.getInputStream();
            br = new BufferedReader( new InputStreamReader(is));
            String line = null;
            while ((line=br.readLine())!= null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            System. out.println( "发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (br != null) {
                    br.close();
                }
                if (conn!= null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}