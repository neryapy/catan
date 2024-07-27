import java.awt.*;
import java.util.List;

public class Village {
    private int locationRes;
    private int locationVertex;
    public Village(int location_res, int location_vertex){
        this.locationRes=locationRes;
        this.locationVertex=locationVertex;
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
        int villageSize = 10;  // Size of the village

        // Draw a filled circle to represent the village
        g2d.fillOval(vertex1.x - villageSize / 2, vertex1.y - villageSize / 2, villageSize, villageSize);

        // Optionally, draw a border around the village
        g2d.setColor(Color.BLACK);
        g2d.drawOval(vertex1.x - villageSize / 2, vertex1.y - villageSize / 2, villageSize, villageSize);
    }
}
