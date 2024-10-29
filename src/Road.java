public class Road {
    private int indexLine;
    private int indexHexagon;
    public Road(int indexHexagon, int indexLine) {
        this.indexLine = indexLine;
        this.indexHexagon=indexHexagon;}
    public int getIndexHexagon() {return indexHexagon;}
    public int getIndexLine() {
        return indexLine;
    }
}
