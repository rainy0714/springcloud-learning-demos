package org.apache.myfaces.blank.http;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private static int CONN_TIMEOUT = 20 * 1000;// 连接超时时间
    private static int SOCKET_TIMEOUT = 20 * 1000;// read timeout

    //-------------------------------------------------
    public final static String doPost(String url, Map<String, String> paramsMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        if (MapUtils.isEmpty(paramsMap) == false) {
            for (String key : paramsMap.keySet())
                nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }

        try {
            HttpPost httpPost = new HttpPost(url);
            if (CollectionUtils.isEmpty(nvps) == false)
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "GBK"));// nameAndValuePairs, parameters
            // Create a custom response handler
            ResponseHandler<String> responseHandler = getResponseHandler();
            String resp = httpClient.execute(httpPost, responseHandler);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected static ResponseHandler<String> getResponseHandler() {
        return new ResponseHandler<String>() {
            @Override
            public String handleResponse(
                    final HttpResponse response) throws IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };
    }

    protected static ResponseHandler<InputStream> getResponseHandlerResInputStream() {
        return new ResponseHandler<InputStream>() {
            @Override
            public InputStream handleResponse(
                    final HttpResponse response) throws IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? entity.getContent() : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };
    }


    public final static String doGet(String url) {
        return doGet(url, null);
    }

    public final static String doGetWithHeaders(String url, Map<String, String> headersMap, Map<String, Object> paramsMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuilder appendArgs = new StringBuilder();
        if (MapUtils.isEmpty(paramsMap) == false) {
            int startPrefix = 0;
            for (String key : paramsMap.keySet()) {
                if(startPrefix == 0){
                    appendArgs.append("?" + key + "=" + paramsMap.get(key));
                }else{
                    appendArgs.append("&" + key + "=" + paramsMap.get(key));
                }
                startPrefix = 1;
            }
            url += appendArgs.toString();
        }
        try {
            HttpGet httpGet = new HttpGet(url);

            if (MapUtils.isNotEmpty(headersMap)) {
                for (String headerName : headersMap.keySet()) {
                    httpGet.setHeader(headerName, headersMap.get(headerName));
                }
            }

            // Create a custom response handler
            ResponseHandler<String> responseHandler = getResponseHandler();
            String resp = httpClient.execute(httpGet, responseHandler);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public final static String doGet(String url, Map<String, String> paramsMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuilder appendArgs = new StringBuilder();
        if (MapUtils.isEmpty(paramsMap) == false) {
            for (String key : paramsMap.keySet())
                appendArgs.append("&" + key + "=" + paramsMap.get(key));
            url += appendArgs.toString();
        }
        try {
            HttpGet httpGet = new HttpGet(url);

            // Create a custom response handler
            ResponseHandler<String> responseHandler = getResponseHandler();
            String resp = httpClient.execute(httpGet, responseHandler);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * post
     *
     * @param url
     * @return
     */
    public final static InputStream doGetResInputStream(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            HttpGet httpGet = new HttpGet(url);
            // Create a custom response handler
            HttpResponse httpResponse = httpClient.execute(httpGet);
            InputStream input = httpResponse.getEntity().getContent();
            //避免httpClient close之后，流无法读取
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            InputStream resp = new ByteArrayInputStream(baos.toByteArray());
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                IOUtils.closeQuietly(baos);
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * post
     *
     * @param url
     * @param data
     * @return
     */
    public final static String doPost(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            if (StringUtils.isNotEmpty(data))
                httpPost.setEntity(EntityBuilder.create().setText(data).setContentEncoding("UTF-8").build());
            // Create a custom response handler
            ResponseHandler<String> responseHandler = getResponseHandler();
            String resp = httpClient.execute(httpPost, responseHandler);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 添加ssl相关的密钥
     *
     * @param url
     * @param data
     * @param mch_id
     * @param sslFilePath
     * @param cryType
     * @return
     * @throws Exception
     */
    public final static String doPost(String url, String data, String mch_id, String sslFilePath, String cryType) throws Exception {
//        String mch_id = "1238463502"; //商户号即密钥
        KeyStore keyStore = KeyStore.getInstance(cryType);
//        FileInputStream instream = new FileInputStream(new File("D:/apiclient_cert.p12"));
        FileInputStream instream = new FileInputStream(new File(sslFilePath));

        try {

            keyStore.load(instream, mch_id.toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mch_id.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        try {
            HttpPost httpPost = new HttpPost(url);
            if (StringUtils.isNotEmpty(data))
                httpPost.setEntity(EntityBuilder.create().setText(data).setContentEncoding("UTF-8").build());
            // Create a custom response handler
            ResponseHandler<String> responseHandler = getResponseHandler();
            String resp = httpclient.execute(httpPost, responseHandler);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * post
     *
     * @param url
     * @param data
     * @return
     */
    public final static InputStream doPostResInputStream(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            if (StringUtils.isNotEmpty(data))
                httpPost.setEntity(EntityBuilder.create().setText(data).setContentEncoding("UTF-8").build());
            // Create a custom response handler
            ResponseHandler<InputStream> responseHandler = getResponseHandlerResInputStream();
            InputStream resp = httpClient.execute(httpPost, responseHandler);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * post
     *
     * @param url
     * @param headersMap
     * @param paramsMap
     * @return
     */
    public final static String doPostWithHeaders(String url, Map<String, String> headersMap, Map<String, String> paramsMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (MapUtils.isNotEmpty(paramsMap)) {
            for (String key : paramsMap.keySet())
                nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }

        try {
            HttpPost httpPost = new HttpPost(url);

            if (MapUtils.isNotEmpty(headersMap)) {
                for (String headerName : headersMap.keySet()) {
                    httpPost.setHeader(headerName, headersMap.get(headerName));
                }
            }

            if (CollectionUtils.isNotEmpty(nvps)) {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));// nameAndValuePairs, parameters
            }

            // Create a custom response handler
            ResponseHandler<String> responseHandler = getResponseHandler();
            String resp = httpClient.execute(httpPost, responseHandler);
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String httpClientUploadFile(String url, Map<String, ContentBody> partyMap, Map<String, String> headersMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        //每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
        String boundary = "--------------4585696313564699";
        try {
            HttpPost httpPost = new HttpPost(url);
            //设置请求头
            httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);

            //HttpEntity builder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //字符编码
            builder.setCharset(Charset.forName("UTF-8"));
            //模拟浏览器
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //boundary
            builder.setBoundary(boundary);

            if (MapUtils.isNotEmpty(partyMap)) {
                for (String name : partyMap.keySet()) {
                    builder.addPart(name, partyMap.get(name));
                }
            }
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);

            if (MapUtils.isNotEmpty(headersMap)) {
                for (String headerName : headersMap.keySet()) {
                    httpPost.setHeader(headerName, headersMap.get(headerName));
                }
            }

            //响应
            ResponseHandler<String> responseHandler = getResponseHandler();
            result = httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println("result" + result);
        return result;
    }

    public static String doPostWithHeadersAndJson(String url, Map<String, String> headersMap, String json) {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            if (MapUtils.isNotEmpty(headersMap)) {
                for (String headerName : headersMap.keySet()) {
                    httpPost.setHeader(headerName, headersMap.get(headerName));
                }
            }
            httpPost.setEntity(requestEntity);

            ResponseHandler<String> responseHandler = getResponseHandler();
            result = httpClient.execute(httpPost, responseHandler);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return result;
    }

}
