package utils;

import java.util.List;

public class Versione {

    private int id;
    private String versione;
    private List<Files> file;

    public Versione(String versione, List<Files> file,int i) {
        this.versione = versione;
        this.file = file;
        this.id = i;
    }

    public Versione(String versione,int i) {
        this.versione = versione;
        this.id = i;
    }

    public int getid() {
        return id;
    }

    public String getVersione() {
        return versione;
    }

    public List<Files> getFile() {
        return file;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setVersione(String versione) {
        this.versione = versione;
    }
    public void setFile(List<Files> file) {
        this.file = file;
    }
}
