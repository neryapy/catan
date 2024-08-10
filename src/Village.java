import java.awt.*;
import java.util.List;

public class Village {
    public int index;
    public int vertex;

    public Village(int index, int vertex) {
        this.index = index;
        this.vertex = vertex;
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
    public void draw(Graphics2D g2d, List<HexagonResource> hexagons) {
        HexagonResource hex = hexagons.get(index);
        Point vertex1 = hex.getVertex(vertex);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.RED);
        int villageSize = 10;
        g2d.fillOval(vertex1.x - villageSize / 2, vertex1.y - villageSize / 2, villageSize, villageSize);
        System.out.println(index+" "+vertex+" "+(vertex1.x - villageSize / 2)+" "+(vertex1.y - villageSize / 2));
        g2d.setColor(Color.BLACK);
        g2d.drawOval(vertex1.x - villageSize / 2, vertex1.y - villageSize / 2, villageSize, villageSize);
    }
}
