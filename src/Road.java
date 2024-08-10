import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;

public class Road {
    private int index;
    private int locationVertex1;
    private int locationVertex2;

    public Road(int index, int locationVertex1, int locationVertex2) {
        this.index = index;
        this.locationVertex1 = locationVertex1;
        this.locationVertex2 = locationVertex2;
    }

    public void printVertex() {
        System.out.println("Road between vertex: " + locationVertex1 + " and vertex: " + locationVertex2 + " on hexagon index: " + index);
    }

    public void draw(Graphics2D g2d, List<HexagonResource> hexagons) {
        HexagonResource hex = hexagons.get(index);
        Point vertex1 = hex.getVertex(locationVertex1);
        Point vertex2 = hex.getVertex(locationVertex2);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(vertex1.x, vertex1.y, vertex2.x, vertex2.y);
    }


    public int getIndex() {
        return index;
    }
}
