public class Village{
    public int index;
    public int vertex;
    public int owner = 0;
    public Village(int index, int vertex, int owner) {
        this.index = index;
        this.vertex = vertex;
        this.owner=owner;
    }
    public int getOwner() {
        return owner;
    }
    public int getIndex() {
        return index;
    }
    public int getVertex() {
        return vertex;
    }
    public void printVertex() {
        System.out.println("Village at index: " + index + ", vertex: " + vertex);
    }
}
