package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.JiraIssue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JiraIssueFetcher {

    private static final String API_URL =
            "https://issues.apache.org/jira/rest/api/2/search?jql=project=ZOOKEEPER&maxResults=50&fields=key,summary,reporter,status,created,fixVersions";

    /**
     * Recupera una lista di issue da Jira in modalità anonima.
     * @return lista di JiraIssue
     * @throws Exception in caso di errore HTTP o parsing
     */
    public List<JiraIssue> fetchIssues() throws Exception {
        List<JiraIssue> issuesList = new ArrayList<>();

        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Errore HTTP: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        JSONObject response = new JSONObject(content.toString());
        JSONArray issues = response.getJSONArray("issues");

        for (int i = 0; i < issues.length(); i++) {
            JSONObject issue = issues.getJSONObject(i);
            JSONObject fields = issue.getJSONObject("fields");

            String key = issue.getString("key");
            String summary = fields.getString("summary");
            String reporter = fields.isNull("reporter") ? "N/A" : fields.getJSONObject("reporter").getString("displayName");
            String status = fields.getJSONObject("status").getString("name");
            String created = fields.getString("created");

            List<String> fixVersions = new ArrayList<>();
            JSONArray fixVersionsArray = fields.getJSONArray("fixVersions");
            for (int j = 0; j < fixVersionsArray.length(); j++) {
                fixVersions.add(fixVersionsArray.getJSONObject(j).getString("name"));
            }

            issuesList.add(new JiraIssue(key, summary, reporter, status, created, fixVersions));
        }

        return issuesList;
    }

    /**
     * Stampa le informazioni principali delle issue
     * @param issues lista di JiraIssue
     */
    public void printIssues(List<JiraIssue> issues) {
        for (JiraIssue issue : issues) {
            System.out.println("Key: " + issue.key);
            System.out.println("Summary: " + issue.summary);
            System.out.println("Reporter: " + issue.reporter);
            System.out.println("Status: " + issue.status);
            System.out.println("Created: " + issue.created);
            System.out.println("Fix Versions: " + issue.fixVersions);
            System.out.println("----");
        }
    }


}
