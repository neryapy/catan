import java.awt.*;
import java.util.List;

public class Road {
    private int locationRes;
    private int locationVertex1;
    private int locationVertex2;

    public Road(int locationRes, int locationVertex1, int locationVertex2) {
        this.locationRes = locationRes;
        this.locationVertex1 = locationVertex1;
        this.locationVertex2 = locationVertex2;
    }

    public void draw(Graphics2D g2d, List<HexagonResource> hexagons) {
        HexagonResource hex = hexagons.get(locationRes);
        Point vertex1 = hex.getVertex(locationVertex1);
        Point vertex2 = hex.getVertex(locationVertex2);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(vertex1.x, vertex1.y-4, vertex2.x+4, vertex2.y);
    }
}
