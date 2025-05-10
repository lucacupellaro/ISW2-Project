package utils;

public class Methods {

    private String name;
    private int LOC;
    private int LOC_touched;
    private int NR;
    private int NFix;
    private int NAuth;
    private int LOC_added;
    private int MAX_LOC_added;
    private int AVG_LOC_added;

    // Costruttore completo
    public Methods(String name, int LOC, int LOC_touched, int NR, int NFix,
                int NAuth, int LOC_added, int MAX_LOC_added, int AVG_LOC_added) {
        this.name = name;
        this.LOC = LOC;
        this.LOC_touched = LOC_touched;
        this.NR = NR;
        this.NFix = NFix;
        this.NAuth = NAuth;
        this.LOC_added = LOC_added;
        this.MAX_LOC_added = MAX_LOC_added;
        this.AVG_LOC_added = AVG_LOC_added;
    }

    // Costruttore iniziale
    public Methods(String name, int LOC) {
        this.name = name;
        this.LOC = LOC;
    }

    // Getter e setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLOC() {
        return LOC;
    }

    public void setLOC(int LOC) {
        this.LOC = LOC;
    }

    public int getLOC_touched() {
        return LOC_touched;
    }

    public void setLOC_touched(int LOC_touched) {
        this.LOC_touched = LOC_touched;
    }

    public int getNR() {
        return NR;
    }

    public void setNR(int NR) {
        this.NR = NR;
    }

    public int getNFix() {
        return NFix;
    }

    public void setNFix(int NFix) {
        this.NFix = NFix;
    }

    public int getNAuth() {
        return NAuth;
    }

    public void setNAuth(int NAuth) {
        this.NAuth = NAuth;
    }

    public int getLOC_added() {
        return LOC_added;
    }

    public void setLOC_added(int LOC_added) {
        this.LOC_added = LOC_added;
    }

    public int getMAX_LOC_added() {
        return MAX_LOC_added;
    }

    public void setMAX_LOC_added(int MAX_LOC_added) {
        this.MAX_LOC_added = MAX_LOC_added;
    }

    public int getAVG_LOC_added() {
        return AVG_LOC_added;
    }

    public void setAVG_LOC_added(int AVG_LOC_added) {
        this.AVG_LOC_added = AVG_LOC_added;
    }
}