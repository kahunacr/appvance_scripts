/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package appvance;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Daniel
 */
public class JenkinsPlugin {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        String url = args[0];
        String file = args[1];
        String username = args[2];
        String password = args[3];

        AppvanceRestClient arc = new AppvanceRestClient(url);
        if (arc.logIn(username, password)) {
            System.out.println("Log In succeed. Starting scenario.");
            arc.startScenario(file);

            while (true) {
                if (!arc.getStatus(file, 0).getString("status").equals("Loading")) {
                    break;
                }
                System.out.println("Scenario Loading...");
                Thread.sleep(10000);
            }

            JSONObject obj = null;
            boolean isRunning = true;

            System.out.println("Scenario running...");
            while (isRunning) {
                obj = arc.getStatus(file, 0);
                try {
                    isRunning = !obj.getBoolean("complete");
                    Thread.sleep(5000);
                } catch (JSONException e) {
                    break;
                }
            }

            System.out.println("Output:\n" + obj.getString("output"));
            System.out.println("Reports URL: " + arc.getAnalizeUrl(file));
            JSONObject results = arc.getStatus(file, 0);
            int success = results.getInt("success");
            int failures = results.getInt("failures");

            if (success == 0) {
                throw new Exception("No successful transactions.");
            }
            if (failures > success) {
                throw new Exception("More failures than success transactions.");
            }
            if (failures > 0) {
                System.err.println(failures + " failures trasactions during the test.");
            }
        } else {
            throw new Exception("Invalid Log In");
        }
    }
}
