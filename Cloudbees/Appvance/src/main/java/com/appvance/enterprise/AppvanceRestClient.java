/*
Appvance Community Project: Cloudbees Jenkins Plug-in

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published
by the Free  Software Foundation; either version 2 of the License,
or (at your option) any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details

PushToTest TestMaker comes with a copy of the GNU General Public
License in the docs/license.html file if you cannot find the license, then write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

Please direct questions regarding this license agreement to
Appvance at 1735 Technology Drive, Suite 820
San Jose CA 95110-1384 USA

(c) 2013 Frank Cohen. All rights reserved.
*/
package com.appvance.enterprise;

import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 *
 * @author Luis
 */
public class AppvanceRestClient {

    private String baseUrl;
    private DefaultHttpClient httpclient;
    private String file;
    private String output;
    private int currentPos;
    private String config;
    private long exeId;
    private String originalURL;
    private int success;
    private int failures;

    public AppvanceRestClient(String theUrl) {
        originalURL = theUrl;
        baseUrl = "http://" + theUrl + "/TestMakerServer/rest/";
        httpclient = new DefaultHttpClient();
        output = "";
        currentPos = 0;
    }

    public String getBody(HttpEntity entity1) throws Exception {
        InputStream is = entity1.getContent();
        StringBuilder sb = new StringBuilder();
        byte[] data = new byte[1000];
        int read;
        while ((read = is.read(data)) >= 0) {
            sb.append(new String(data, 0, read));
        }
        is.close();
        EntityUtils.consume(entity1);
        return sb.toString();
    }

    //http://localhost:8080/TestMakerServer/rest/execution/startExecution?file=D%3A%2FNetworkTest%2Fhtmlunit2.7.scenario&_=1372899787303
    public void startScenario(String theFile) throws Exception {
        file = theFile;
        output = "";
        currentPos = 0;
        HttpGet httpGet = new HttpGet(baseUrl + "execution/startExecution?file=" + file);
        HttpResponse response1 = httpclient.execute(httpGet);
        try {
            System.out.println("Executing " + file);
            getBody(response1.getEntity());
        } finally {
            httpGet.releaseConnection();
        }
    }

    //http://localhost:8080/TestMakerServer/rest/execution/getStatus?file=D%3A%2FNetworkTest%2Fhtmlunit2.7.scenario&realtimeTXCount=5&_=1372900754967
    public boolean isRunning() throws Exception {
        HttpGet httpGet = new HttpGet(baseUrl + "execution/getStatus?realtimeTXCount=0&file=" + file);
        HttpResponse response1 = httpclient.execute(httpGet);
        try {
            JSONObject obj = new JSONObject(getBody(response1.getEntity()));
            output = obj.getString("output");
            try {
                config = obj.getString("config");
            } catch (Exception e) {
            }
            try {
                exeId = obj.getLong("exeId");
            } catch (Exception e) {
            }
            try {
                success = obj.getInt("success");
            } catch (Exception e) {
            }
            try {
                failures = obj.getInt("failures");
            } catch (Exception e) {
            }
            return !obj.getBoolean("complete");

            // do something useful with the response body
            // and ensure it is fully consumed
        } finally {
            httpGet.releaseConnection();
        }
    }

    public String getReportURL(){
        return "http://" +originalURL+"/Appvance/analyze.html?config="+config+"&exeId="+exeId;
    }

    public String getNextOutput() {
        if (currentPos >= output.length()) {
            return "";
        }
        String s = output.substring(currentPos);
        currentPos = output.length();
        return s;
    }

    public int getSuccess(){
        return success;
    }

     public int getFailures(){
        return failures;
    }

}
