package utils;

import java.util.List;

public class Files {

    private String fileName;
    private List<Methods> methods; // Opzionale
    private int LOC;
    private int NR;
    private int NFix;

    public Files(String fileName, List<Methods> methods, int LOC, int NR, int NFix) {
        this.fileName = fileName;
        this.methods = methods;
        this.LOC = LOC;
        this.NR = NR;
        this.NFix = NFix;
    }

    public Files(String name) {
    }

    public String getFileName() {
        return fileName;
    }

    public List<Methods> getMethods() {
        return methods;
    }

    public int getLOC() {
        return LOC;
    }

    public int getNR() {
        return NR;
    }

    public int getNFix() {
        return NFix;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMethods(List<Methods> methods) {
        this.methods = methods;
    }

    public void setLOC(int LOC) {
        this.LOC = LOC;
    }

    public void setNR(int NR) {
        this.NR = NR;
    }

    public void setNFix(int NFix) {
        this.NFix = NFix;
    }
}