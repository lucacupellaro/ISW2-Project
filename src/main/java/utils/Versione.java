package utils;

import java.util.List;

public class Versione {

    private String versione;
    private List<Files> file;

    public Versione(String versione, List<Files> file) {
        this.versione = versione;
        this.file = file;
    }

    public String getVersione() {
        return versione;
    }

    public List<Files> getFile() {
        return file;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }
    public void setFile(List<Files> file) {
        this.file = file;
    }
}
