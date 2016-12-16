/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appvance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import org.apache.appvance434.http.HttpEntity;
import org.apache.appvance434.http.HttpResponse;
import org.apache.appvance434.http.NameValuePair;
import org.apache.appvance434.http.client.CookieStore;
import org.apache.appvance434.http.client.HttpClient;
import org.apache.appvance434.http.client.entity.UrlEncodedFormEntity;
import org.apache.appvance434.http.client.methods.HttpGet;
import org.apache.appvance434.http.client.methods.HttpPost;
import org.apache.appvance434.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.appvance434.http.conn.ssl.SSLContextBuilder;
import org.apache.appvance434.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.appvance434.http.entity.StringEntity;
import org.apache.appvance434.http.entity.mime.MultipartEntityBuilder;
import org.apache.appvance434.http.entity.mime.content.ByteArrayBody;
import org.apache.appvance434.http.entity.mime.content.FileBody;
import org.apache.appvance434.http.impl.client.BasicCookieStore;
import org.apache.appvance434.http.impl.client.HttpClients;
import org.apache.appvance434.http.message.BasicNameValuePair;
import org.apache.appvance434.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Luis
 */
public class AppvanceRestClient {

    private final String baseUrl;
    private final String baseUrlForAppvanceServices;
    private final HttpClient httpClient;

    public AppvanceRestClient(String theUrl) throws Exception {
        URL url = new URL(theUrl);
        String protocol = url.getProtocol();
        String server = url.getHost();
        int port = url.getPort();
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        baseUrl = protocol + "://" + server + ":" + port + "/AppvanceServer/rest/";
        baseUrlForAppvanceServices = protocol + "://" + server + ":" + port + "/AppvanceServices/rest/";
        CookieStore cookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore).build();

    }

    /**
     *
     * @param iterationId
     * @param listOfResources
     * @return
     */
    public boolean addResourcesEntries(int iterationId, JSONArray listOfResources) {
        String syncServerURL = baseUrlForAppvanceServices + "resources/addResources?iterationId=" + iterationId;
        HttpPost post = new HttpPost(syncServerURL);
        try {
            StringEntity requiredParameters = new StringEntity(listOfResources.toString());

            post.setHeader("Content-type", "application/json");
            post.setEntity(requiredParameters);
            HttpResponse response = httpClient.execute(post);
            JSONObject resp = new JSONObject(getBodyString(response.getEntity()));

            return resp.getBoolean("sucess");
        } catch (Exception ex) {
            System.out.println("Error when AppvanceRestClient was trying to execute a call to:" + syncServerURL);
            System.out.println("Reason:" + ex.getMessage());
            return false;
        } finally {
            post.releaseConnection();

        }
    }

    public boolean requestLock(int iterationId) throws IOException, JSONException, Exception {
        String syncServerURL = baseUrlForAppvanceServices + "services/requestLock?iterationId=" + iterationId;
        HttpGet httpGet = new HttpGet(syncServerURL);
        try {
            HttpResponse response1 = httpClient.execute(httpGet);
            JSONObject percentiles = new JSONObject(getBodyString(response1.getEntity()));
            return percentiles.getBoolean("lock");
        } finally {
            httpGet.releaseConnection();
        }
    }

    public boolean releaseLock(int iterationId) throws IOException, JSONException, Exception {
        String syncServerURL = baseUrlForAppvanceServices + "services/releaseLock?iterationId=" + iterationId;
        HttpGet httpGet = new HttpGet(syncServerURL);

        try {
            HttpResponse response1 = httpClient.execute(httpGet);
            JSONObject percentiles = new JSONObject(getBodyString(response1.getEntity()));
            return percentiles.getBoolean("released");
        } finally {
            httpGet.releaseConnection();
        }
    }

    public boolean requestStrictLock(int iterationId) throws IOException, JSONException, Exception {
        String syncServerURL = baseUrlForAppvanceServices + "services/requestStrictLock?iterationId=" + iterationId;
        HttpGet httpGet = new HttpGet(syncServerURL);
        try {
            HttpResponse response1 = httpClient.execute(httpGet);
            JSONObject percentiles = new JSONObject(getBodyString(response1.getEntity()));
            return percentiles.getBoolean("lock");
        } finally {
            httpGet.releaseConnection();
        }
    }

    public boolean releaseStrictLock(int iterationId) throws IOException, JSONException, Exception {
        String syncServerURL = baseUrlForAppvanceServices + "services/releaseStrictLock?iterationId=" + iterationId;
        HttpGet httpGet = new HttpGet(syncServerURL);
        try {
            HttpResponse response1 = httpClient.execute(httpGet);
            JSONObject percentiles = new JSONObject(getBodyString(response1.getEntity()));
            return percentiles.getBoolean("released");
        } finally {
            httpGet.releaseConnection();
        }
    }

    public JSONObject getPercentile(int iterationId, int resourceId) throws IOException, JSONException, Exception {
        String syncServerURL = baseUrlForAppvanceServices + "resources/getPercentile?iterationId=" + iterationId + "&resourceId=" + resourceId;
        HttpGet httpGet = new HttpGet(syncServerURL);

        try {
            HttpResponse response1 = httpClient.execute(httpGet);
            JSONObject percentiles = new JSONObject(getBodyString(response1.getEntity()));
            return percentiles;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public JSONObject deletePercentile(int iterationId, int resourceId) throws IOException, JSONException, Exception {
        String syncServerURL = baseUrlForAppvanceServices + "resources/deleteResource?iterationId=" + iterationId + "&resourceId=" + resourceId;
        HttpGet httpGet = new HttpGet(syncServerURL);
        try {
            HttpResponse response1 = httpClient.execute(httpGet);
            JSONObject percentiles = new JSONObject(getBodyString(response1.getEntity()));
            return percentiles;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public JSONObject deletePercentile(int iterationId) throws IOException, JSONException, Exception {
        String syncServerURL = baseUrlForAppvanceServices + "resources/deleteResources?iterationId=" + iterationId;
        HttpGet httpGet = new HttpGet(syncServerURL);
        try {
            HttpResponse response1 = httpClient.execute(httpGet);
            JSONObject percentiles = new JSONObject(getBodyString(response1.getEntity()));
            return percentiles;
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Returns body as string
     *
     * @param entity1
     * @return
     * @throws Exception
     */
    private String getBodyString(HttpEntity entity1) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = entity1.getContent()) {
            byte[] data = new byte[1000];
            int read;
            while ((read = is.read(data)) >= 0) {
                sb.append(new String(data, 0, read));
            }
            is.close();
        }
        return sb.toString();
    }

    public boolean logIn(String username, String password) throws Exception {
        HttpPost httppost = new HttpPost(baseUrl + "admin/loggin");
        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("username", username));
        postParameters.add(new BasicNameValuePair("password", password));

        httppost.setEntity(new UrlEncodedFormEntity(postParameters));
        HttpResponse response1 = httpClient.execute(httppost);
        
        try {
            JSONObject login = new JSONObject(getBodyString(response1.getEntity()));
            return login.getBoolean("logged");
        } finally {
            httppost.releaseConnection();
        }
    }

    public boolean changePassword(String password) throws Exception {
        HttpPost httppost = new HttpPost(baseUrl + "admin/changePassword");
        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("password", password));

        httppost.setEntity(new UrlEncodedFormEntity(postParameters));
        HttpResponse response1 = httpClient.execute(httppost);
        try {
            JSONObject login = new JSONObject(getBodyString(response1.getEntity()));
            return login.getBoolean("updated");
        } finally {
            httppost.releaseConnection();
        }
    }

    public int readContent(InputStream is) throws Exception {
        byte[] data = new byte[1024];
        int read = 0;
        int all = 0;
        while ((read = is.read(data)) > 0) {
            all += read;

            System.out.println(new String(data, 0, read));
        }
        return all;
    }

    /**
     * Logs
     * inhttp://localhost:8080/AppvanceServer/rest/reports/getTx?config=default&txId=56602
     *
     * @param request
     * @return
     * @throws Exception
     */
    public int getRequest(int request) throws Exception {
        HttpGet httpGet = new HttpGet(baseUrl + "reports/getRequest?config=default&requestId=" + request);
        HttpResponse response = httpClient.execute(httpGet);
        try {
            int size = readContent(response.getEntity().getContent());
            return size;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public int getTX(int tx) throws Exception {
        HttpGet httpGet = new HttpGet(baseUrl + "reports/getTx?config=default&txId=" + tx);
        HttpResponse response = httpClient.execute(httpGet);
        try {
            int size = readContent(response.getEntity().getContent());
            return size;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public String reboot() throws Exception {
        HttpGet httpGet = new HttpGet(baseUrl + "apps/reboot");
        HttpResponse response1 = httpClient.execute(httpGet);
        try {
            return getBodyString(response1.getEntity());
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Creates or updates a file
     *
     * @param file
     * @param keepBrowserTypes
     * @param keepLocalProperties
     *
     * @return
     * @throws Exception
     */
    public String updateAPC(String file, boolean keepBrowserTypes, boolean keepLocalProperties) throws Exception {

        String keepBrowserTypes_ = "" + keepBrowserTypes;
        String keepLocalProperties_ = "" + keepLocalProperties;

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        System.out.println("Url for upgrading");
        System.out.println(baseUrl + "updater/updateAPC?keepBrowserTypes=" + keepBrowserTypes_ + "&keepLocalProperties=" + keepLocalProperties);

        HttpPost httpPost = new HttpPost(baseUrl + "updater/updateAPC?keepBrowserTypes=" + keepBrowserTypes_ + "&keepLocalProperties=" + keepLocalProperties_);

        builder.addPart("data", new FileBody(new File(file)));
        httpPost.setEntity(builder.build());
        HttpResponse response1 = httpClient.execute(httpPost);
        try {
            return getBodyString(response1.getEntity());
        } finally {
            httpPost.releaseConnection();
        }

    }

    /**
     * Starts a scenario
     *
     * @param file
     * @return
     * @throws Exception
     */
    public JSONObject getFunctionalTree(String file) throws Exception {
        HttpGet httpGet = new HttpGet(baseUrl + "execution/getFunctionalTree?file=" + file);
        HttpResponse response1 = httpClient.execute(httpGet);
        try {
            JSONObject tree = new JSONArray(getBodyString(response1.getEntity())).getJSONObject(0);
            return tree;
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Starts a scenario
     *
     * @param file
     * @return
     * @throws Exception
     */
    public boolean startScenario(String file) throws Exception {
        HttpGet httpGet = new HttpGet(baseUrl + "execution/startExecution?file=" + file);
        HttpResponse response1 = httpClient.execute(httpGet);
        try {
            JSONObject status = new JSONObject(getBodyString(response1.getEntity()));
            return "running".equals(status.getString("status"));
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Gets the status
     *
     * @param file
     * @param maxTransactions
     * @return
     * @throws Exception
     */
    public JSONObject getStatus(String file, int maxTransactions) throws Exception {
        HttpGet httpGet = new HttpGet(baseUrl
                + "execution/getStatus?realtimeTXCount=" + maxTransactions + "&file=" + file);
        HttpResponse response1 = httpClient.execute(httpGet);
        try {
            JSONObject obj = new JSONObject(getBodyString(response1.getEntity()));
            return obj;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public String getAnalizeUrl(String file) throws Exception {
        String url = baseUrl.replace("/AppvanceServer/rest/", "");
        url = url + "/Appvance/analyze.html?config=" + getStatus(file, 0).getLong("config") + "&exeId=" + getStatus(file, 0).getLong("exeId");
        //String url = protocol + "://" + server + ":" + port + "/Appvance/analyze.html?config=";
        return url;
    }

    public String getLicense() throws IOException {
        String url = baseUrl + "license/getLicense";

        System.out.println("URL: " + url);
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        return result;
    }

    public String downloadAPC(String urlFile, String pathToSave) throws IOException, Exception {
        HttpGet httpget = new HttpGet(urlFile);
        HttpResponse response = httpClient.execute(httpget);
        HttpEntity entity = response.getEntity();

        InputStream inputStream = entity.getContent();
        String filePath = pathToSave;

        FileOutputStream fos = new FileOutputStream(new File(filePath));
        /*
         byte[] readBytes = new byte[1000];
         int read;
        
         while ( ( read = inputStream.read( readBytes ) ) >= 0 ) {
         System.out.println( "writting.." );
         fos.write( readBytes, 0, read);
         }*/

        int inByte;

        while ((inByte = inputStream.read()) != -1) {
            fos.write(inByte);
        }
        inputStream.close();
        fos.close();
        return response.toString();
    }

    public String setLicense(String fileName) throws IOException, Exception {
        //http://localhost:8080/AppvanceServer/rest/license/setLicense?file=unlimited.lic
        String responseString = "";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        InputStream inputStream = AppvanceRestClient.class.getResourceAsStream(fileName);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] readBytes = new byte[1000];
        int read;

        while ((read = inputStream.read(readBytes)) >= 0) {
            bytes.write(readBytes, 0, read);
        }

        builder.addPart("data", new ByteArrayBody(bytes.toByteArray(), "data"));

        String url = baseUrl + "license/setLicense";

        System.out.println("URL: " + url);
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(builder.build());

        HttpResponse response = httpClient.execute(httpPost);

        responseString = response.toString();
        System.out.println(responseString);
        String result = EntityUtils.toString(response.getEntity());
        return result;
    }

    public void deleteExecutionRecords(long scenarioExecutionId) throws JSONException, Exception {

        HttpGet httpGet = new HttpGet(baseUrl + "reports/deleteScenarioExecution?config=default&scenario=" + scenarioExecutionId);
        HttpResponse response = httpClient.execute(httpGet);
        try {
            //JSONObject obj = new JSONObject());
            System.out.println(getBodyString(response.getEntity()));
            //return obj;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public String getFutionalUniqueData(String data) throws Exception {

        HttpGet httpGet = new HttpGet(baseUrlForAppvanceServices + "unique/verifyUniqueData?data=" + data);
        HttpResponse response = httpClient.execute(httpGet);
        String res;
        try {
            res = getBodyString(response.getEntity());
            // System.out.println("Information!!!"+data);
            return res;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public void verifyFuntionalUniqueData() throws Exception {
        HttpGet httpGet = new HttpGet(baseUrlForAppvanceServices + "unique/getUniqueData");
        HttpResponse response = httpClient.execute(httpGet);
        String res;

        try {
            res = getBodyString(response.getEntity());
            // System.out.println("VERIFY INFORMATION!! "+res);
        } finally {
            httpGet.releaseConnection();
        }
    }

    public String reportFunctionalUserTermination(Long executionId) throws Exception {
        HttpGet httpGet = new HttpGet(baseUrlForAppvanceServices + "concurrency/reportFunctionalUserTermination?executionId=" + executionId);
        HttpResponse response = httpClient.execute(httpGet);
        String res;
        try {
            res = getBodyString(response.getEntity());
            //System.out.println("* reportTermination REST:"+res);
            return res;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public Boolean allowToStartFunctionalUser(Long executionId, Integer maxUsers) throws Exception {
        HttpGet httpGet = new HttpGet(baseUrlForAppvanceServices + "concurrency/allowToStartFunctionalUser?executionId=" + executionId + "&maxUsers=" + maxUsers);
        HttpResponse response = httpClient.execute(httpGet);
        String res;
        try {
            res = getBodyString(response.getEntity());
            return !("false".equals(res));
        } finally {
            httpGet.releaseConnection();
        }
    }

    public String addRepository(String jsonData) throws UnsupportedEncodingException, IOException, Exception {
        String url = baseUrl + "preferences/saveRepository";
        System.out.println("URL: " + url);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json");
        StringEntity params = new StringEntity(jsonData);
        httpPost.setHeader("dataType", "text");
        httpPost.setEntity(params);
        HttpResponse response = httpClient.execute(httpPost);
        return getBodyString(response.getEntity());
    }

    public boolean fileExists(String path) throws Exception {
        String url = baseUrl + "file/getFile?dir=" + path;
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = httpClient.execute(httpPost);
        String status= getBodyString(response.getEntity());
        return !status.contains("File Not Found");
    }

    /**
     * This methods are added for Cisco Testing
     */
    public JSONArray getRepository(String name) throws Exception {
        String resource = "preferences/getRepository?name=" + name;
        HttpGet httpGet = new HttpGet(baseUrl + resource);
        System.out.println("get repo name start()");
        try {

            HttpResponse response1 = httpClient.execute(httpGet);
            String temp = getBodyString(response1.getEntity());
            System.out.println(temp);
            JSONArray arr = new JSONArray(temp);
            return arr;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public JSONArray getRepositoryNames() throws Exception {
        String resource = "preferences/getRepositoryNames";
        HttpGet httpGet = new HttpGet(baseUrl + resource);
        System.out.println("get repo name start()");
        try {

            HttpResponse response1 = httpClient.execute(httpGet);
            String temp = getBodyString(response1.getEntity());
            System.out.println(temp);
            JSONArray arr = new JSONArray(temp);
            return arr;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public JSONArray getReport(String exeid, String configid) throws Exception {
        String resource = "reports/getFunctionalReport?exeId=" + exeid + "&config=" + configid;
        HttpGet httpGet = new HttpGet(baseUrl + resource);
        HttpResponse response1 = httpClient.execute(httpGet);
        try {
            String temp = getBodyString(response1.getEntity());

            System.out.println(temp);
            JSONArray arr = new JSONArray(temp);

//            System.out.println("MY SIZE from GeTREPORT: " + arr.length());
            return arr;
        } finally {
            httpGet.releaseConnection();
        }
    }

    public String deleteRepository(String repositoryName) throws Exception {
        System.out.println("Deleting Repository "+repositoryName);
        HttpPost post = new HttpPost(baseUrl + "preferences/deleteRepository?name="+repositoryName);
        post.setHeader("Content-type", "text/html");
        HttpResponse response1 = httpClient.execute(post);
        try {
            System.out.print(response1.getStatusLine().getStatusCode());
            return getBodyString(response1.getEntity());
        } finally {
            post.releaseConnection();
        }
    }

    public byte[] getFile(String file) throws Exception {

        HttpGet httpGet = new HttpGet(baseUrl
                + "file/getFile?dir=" + file);
        HttpResponse response1 = httpClient.execute(httpGet);
        try {
            return getBody(response1.getEntity());
        } finally {
            httpGet.releaseConnection();
        }
    }

    private byte[] getBody(HttpEntity entity1) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        InputStream is = entity1.getContent();
        byte[] data = new byte[1000];
        int read;
        while ((read = is.read(data)) >= 0) {
            bytes.write(data, 0, read);
        }

        EntityUtils.consume(entity1);
        return bytes.toByteArray();
    }

    public String udpateFile(String path, String fileName, byte[] data) throws Exception {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        HttpPost httpPost = new HttpPost(baseUrl
                + "file/updateFile?path=" + path);
        builder.addPart("data", new ByteArrayBody(data, "data"));
        httpPost.setEntity(builder.build());
        System.out.println("Posting Data to APC");
        HttpResponse response1 = httpClient.execute(httpPost);
        try {
            return getBodyString(response1.getEntity());
        } finally {
            httpPost.releaseConnection();
        }
    }
    
    public JSONObject updateDBCredentials(DBConfiguration credentails)throws Exception {
        HttpPost httpPost = new HttpPost(baseUrl
                + "preferences/saveResultsRepository");
        httpPost.setHeader("Content-type", "application/json");
        StringEntity requiredParameters = new StringEntity(credentails.toJSON().toString());
        httpPost.setEntity(requiredParameters);
        HttpResponse response1 = httpClient.execute(httpPost);
        try {
            String result =getBodyString(response1.getEntity());
            System.out.println(result);
            return new JSONObject(result);
        } finally {
            httpPost.releaseConnection();
        }
    }

    public String generateSaveContent(String fileName, String content) {

        String boundary = "/";

        String header = "--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"itemfile\"; " + "filename=\"" + fileName + "\"\r\n" + "Content-Type: application/octet-stream" + "\r\n" + "--" + boundary + "\r\n";

        String fullContent = header + "\r\n" + content + "\r\n--" + boundary + "--";
        System.out.println("Generating headers: " + header);
        return fullContent;
    }

    /**
     * End of the methods.
     */
    public static void main(String args[]) throws Exception {
        AppvanceRestClient arc = new AppvanceRestClient("http://ec2-54-166-28-31.compute-1.amazonaws.com:8080");
        System.out.println(arc.logIn("appvance", "Njyh162m@nop23dH"));
        System.out.println(arc.reboot());
    }
}
