package utils;

import java.util.List;

// Modello dati per una issue Jira
public class JiraIssue {
    public final String key;
    public final String summary;
    public final String reporter;
    public final String status;
    public final String created;
    public final List<String> fixVersions;

    public JiraIssue(String key, String summary, String reporter, String status, String created, List<String> fixVersions) {
        this.key = key;
        this.summary = summary;
        this.reporter = reporter;
        this.status = status;
        this.created = created;
        this.fixVersions = fixVersions;
    }
}
