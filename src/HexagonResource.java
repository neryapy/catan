import java.awt.*;
import java.util.Objects;

public class HexagonResource implements Comparable<HexagonResource> {
    private int x, y; // Center of the hexagon
    private String resourceType;
    public int number;
    private int size;
    private int index;

    public HexagonResource(int x, int y, String resourceType, int number, int size, int index) {
        this.x = x;
        this.y = y;
        this.resourceType = resourceType;
        this.number = number;
        this.size = size;
        this.index = index;
    }

    public int getSize() {return size;}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    public String getResourceType() {
        return resourceType;
    }

    public int getNumber() {
        return number;
    }

    public Polygon getHexagonShape() {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            xPoints[i] = (int) (x + size * Math.cos(Math.toRadians(60 * i + 30)));
            yPoints[i] = (int) (y + size * Math.sin(Math.toRadians(60 * i + 30)));
        }
        return new Polygon(xPoints, yPoints, 6);
    }

    public Point getVertex(int vertexIndex) {
        int vertexX = (int) (x + size * Math.cos(Math.toRadians(60 * vertexIndex + 30)));
        int vertexY = (int) (y + size * Math.sin(Math.toRadians(60 * vertexIndex + 30)));
        return new Point(vertexX, vertexY);
    }
    public void draw(Graphics2D g2d) {
        Polygon hexagon = getHexagonShape();
        switch (resourceType) {
            case "Wood":
                g2d.setColor(new Color(161, 102, 47));
                break;
            case "Brick":
                g2d.setColor(new Color(227, 118, 25));
                break;
            case "Wheat":
                g2d.setColor(new Color(255, 215, 0));
                break;
            case "Sheep":
                g2d.setColor(new Color(105, 150, 0));
                break;
            case "Ore":
                g2d.setColor(new Color(156, 141, 118));
                break;
            case "Desert":
                g2d.setColor(new Color(210, 180, 140));
                break;
            default:
                g2d.setColor(Color.BLACK);
                break;
        }

        // Draw the hexagon
        g2d.fill(hexagon);
        g2d.setColor(Color.BLACK);
        g2d.draw(hexagon);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics metrics = g2d.getFontMetrics();

        // Center text horizontally and vertically within the hexagon
        String resourceText = resourceType;
        int resourceTextWidth = metrics.stringWidth(resourceText);
        int resourceTextHeight = metrics.getAscent();
        g2d.setColor(Color.WHITE);
        g2d.drawString(resourceText, x - resourceTextWidth / 2, y - size / 2 + resourceTextHeight / 2);

        String numberText = String.valueOf(number);
        int numberTextWidth = metrics.stringWidth(numberText);
        int numberTextHeight = metrics.getAscent();
        g2d.drawString(numberText, x - numberTextWidth / 2, y + size / 2 - numberTextHeight / 2);
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
