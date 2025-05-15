import java.io.Serializable;
public class City  implements Serializable {
    private static final long serialVersionUID = 1L;
    public int index;
    public int vertex;
    public int owner = -1;
    public City(int index, int vertex, int owner) {
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
        System.out.println("City at Resource index: " + index + ", vertex: " + vertex);
    }
}
