import java.io.Serializable;

public class Village implements Serializable {
    private static final long serialVersionUID = 1L;
    public int index;
    public int vertex;
    public int owner = -1;
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
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Village other = (Village) obj;
    return this.index == other.index &&
           this.vertex == other.vertex &&
           this.owner == other.owner;
}
}
