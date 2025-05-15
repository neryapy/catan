import java.io.Serializable;
public class Road  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int v1;
    private int v2;
    private int indexHexagon;
    private int owner=-1; // -1 means no owner, 0 means player 0, 1 means player 1, etc.
    public Road(int indexHexagon, int v1, int v2, int owner) {
        this.owner = owner;
        this.v1 = v1;
        this.v2 = v2;
        this.indexHexagon=indexHexagon;}
    public int getIndexHexagon() {return indexHexagon;}
    public int getOwner() {
        return owner;
    }

    public int getv1() {
        return v1;
    }
    public int getv2() {
        return v2;
    }
}
