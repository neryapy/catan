import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HexagonResource implements Comparable<HexagonResource> {
    private String resourceType;
    private int number;
    private int index;
    public List<vertex> vertices;
    private List<lineHexagon> lines;
    private boolean hasRobber=false;

    public HexagonResource(String resourceType, int number, int index) {
        this.resourceType = resourceType;
        this.number = number;
        this.index = index;
        this.vertices = new ArrayList<>();
        initializeVertices();
        this.lines= new ArrayList<>();
        initializelines();
    }
    public boolean isHasRobber() {return hasRobber;}

    public void setHasRobber(boolean hasRobber) {this.hasRobber = hasRobber;}

    private void initializelines() {
        for (int i = 0; i < 6; i++) { // Hexagon has 6 vertices
            lineHexagon l = new lineHexagon(i);
            lines.add(l);
        }
    }
    private void initializeVertices() {
        for (int i = 0; i < 6; i++) { // Hexagon has 6 vertices
            vertex v = new vertex(i);
            vertices.add(v);
        }
    }
    public int getIndex() {
        return index;
    }

    public List<lineHexagon> getLines() {
        return lines;
    }
    public void setNumber(int number){
        this.number=number;
    }
    public String getResourceType() {
        return resourceType;
    }
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    public int getNumber() {
        return number;
    }

    public List<vertex> getVertices() {
        return vertices;
    }

    public vertex getVertex(int vertexIndex) {
        return vertices.get(vertexIndex);
    }

    @Override
    public int compareTo(HexagonResource other) {
        return Integer.compare(this.index, other.index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HexagonResource that = (HexagonResource) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
