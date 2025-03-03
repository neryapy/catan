
import java.util.HashMap;

public class Road {
    private int v1;
    private int v2;
    private int indexHexagon;
    public Road(int indexHexagon, int v1, int v2) {
        this.v1 = v1;
        this.v2 = v2;
        this.indexHexagon=indexHexagon;}
    public int getIndexHexagon() {return indexHexagon;}

    public int getv1() {
        return v1;
    }
    public int getv2() {
        return v2;
    }
}
