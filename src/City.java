import java.awt.*;
import java.util.List;

public class City {
    private int locationRes;
    private int locationVertex;

    public City(int locationRes, int locationVertex) {
        this.locationRes = locationRes;
        this.locationVertex = locationVertex;
    }
    public void printVertex() {
        System.out.println("City at resource index: " + locationRes + ", vertex: " + locationVertex);
    }
    public void draw(Graphics2D g2d, List<HexagonResource> hexagons) {
        // Retrieve the hexagon and the vertex
        HexagonResource hex = hexagons.get(locationRes);
        Point vertex1 = hex.getVertex(locationVertex);

        // Draw the hexagon and other elements if needed
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));

        // Draw the village
        g2d.setColor(Color.RED);  // Color of the village
        int citySize = 15;  // Size of the village

        // Draw a filled circle to represent the village
        g2d.fillRect(vertex1.x - citySize / 2, vertex1.y - citySize / 2, citySize, citySize);

        // Optionally, draw a border around the village
        g2d.setColor(Color.BLACK);
        g2d.drawRect(vertex1.x - citySize / 2, vertex1.y - citySize / 2, citySize, citySize);
    }
}
