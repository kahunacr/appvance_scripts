/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appvance;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author appvance
 */
public class DBConfiguration {

    public int abandonedTimeout = 60;
    public boolean autoCommit = true;
    public String database = "apc";
    public String driver = "com.mysql.jdbc.Driver";
    public String engine = "MySQL";
    public boolean logAvandoned = true;
    public int maxActive = 100;
    public int maxIdle = 30;
    public int maxWait = 1000;
    public String password = "apc";
    public boolean removeAvandone = true;
    public String url = "jdbc:mysql://localhost:3306/apc";
    public String username = "apc";

    /**
     * 
     * @return
     * @throws JSONException 
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("abandonedTimeout", abandonedTimeout);
        obj.put("autoCommit", autoCommit);
        obj.put("database", database);
        obj.put("driver", driver);
        obj.put("engine", engine);
        obj.put("logAvandoned", logAvandoned);
        obj.put("maxActive", maxActive);
        obj.put("maxIdle", maxIdle);
        obj.put("maxWait", maxWait);
        obj.put("password", password);
        obj.put("removeAvandoned", removeAvandone);
        obj.put("url", url);
        obj.put("username", username);

        return obj;
    }
    /*abandonedTimeout: "60"
     autoCommit: true
     database: "apc"
     driver: "com.mysql.jdbc.Driver"
     engine: "MySQL"
     logAvandoned: true
     maxActive: "100"
     maxIdle: "30"
     maxWait: "1000"
     password: "apc"
     removeAvandoned: true
     url: "jdbc:mysql://mysqlsmoketest.cj7ftkrgeamp.us-east-1.rds.amazonaws.com:3306/apc"
     username: "apc" */
}
