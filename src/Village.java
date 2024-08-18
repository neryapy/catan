import java.awt.*;
import java.util.List;

public class Village {
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
    public Point getCoordinates(List<HexagonResource> hexagons) {
        HexagonResource hex = hexagons.get(index);
        return hex.getVertex(vertex);
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
        if(owner==1){g2d.setColor(Color.RED);}
        else if(owner==2){g2d.setColor(Color.BLUE);}
        else if(owner==3){g2d.setColor(Color.green);}
        else if(owner==4){g2d.setColor(Color.gray);}
        int villageSize = 10;
        g2d.fillOval(vertex1.x - villageSize / 2, vertex1.y - villageSize / 2, villageSize, villageSize);
        System.out.println(index+" "+vertex+" "+(vertex1.x - villageSize / 2)+" "+(vertex1.y - villageSize / 2));
        System.out.println(owner);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(vertex1.x - villageSize / 2, vertex1.y - villageSize / 2, villageSize, villageSize);
    }
}
