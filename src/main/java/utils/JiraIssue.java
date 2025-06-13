package utils;

import java.util.List;

// Modello dati per una issue Jira
public class JiraIssue {
    public String key;
    public String summary;
    public String reporter;
    public String status;
    public String created;
    public List<String> fixVersions;

    public JiraIssue(String key, String summary, String reporter, String status, String created, List<String> fixVersions) {
        this.key = key;
        this.summary = summary;
        this.reporter = reporter;
        this.status = status;
        this.created = created;
        this.fixVersions = fixVersions;
    }

    public String returnKey(){
        return key;
    }

    public String summary(){
        return summary;
    }

    public String reporter(){
        return reporter;
    }

    public String status(){
        return status;
    }

    public String created(){
        return created;
    }

    public List<String> fixVersions(){
        return fixVersions;
    }

    public void setKey(){
        this.key = key;
    }

    public void setSummary(){
        this.summary = summary;
    }

    public void setReporter(){
        this.reporter = reporter;
    }

    public void setStatus(){
        this.status = status;

    }

    public void setCreated(){
        this.created = created;
    }

    public void setFixVersions(){
        this.fixVersions = fixVersions;
    }
}
