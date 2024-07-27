import java.awt.*;

public class HexagonResource {
    private int x, y;
    private String resourceType;
    public int number;
    private int size;

    public HexagonResource(int x, int y, String resourceType, int number, int size) {
        this.x = x;
        this.y = y;
        this.resourceType = resourceType;
        this.number = number;
        this.size = size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "HexagonResource{" +
                "x=" + x +
                ", y=" + y +
                ", resourceType='" + resourceType + '\'' +
                ", number=" + number +
                '}';
    }

    public void draw(Graphics2D g2d) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        for (int i = 0; i < 6; i++) {
            xPoints[i] = (int) (x + size * Math.cos(Math.toRadians(60 * i + 30))); // Rotate 30 degrees
            yPoints[i] = (int) (y + size * Math.sin(Math.toRadians(60 * i + 30)));
        }

        Polygon hexagon = new Polygon(xPoints, yPoints, 6);

        // Set the color based on the resource type
        switch (resourceType) {
            case "Wood":
                g2d.setColor(new Color(161, 102, 47)); // Green
                break;
            case "Brick":
                g2d.setColor(new Color(227, 118, 25)); // Red
                break;
            case "Wheat":
                g2d.setColor(new Color(255, 215, 0)); // Yellow
                break;
            case "Sheep":
                g2d.setColor(new Color(105, 150, 0)); // Light green
                break;
            case "Ore":
                g2d.setColor(new Color(156, 141, 118)); // Gray
                break;
            case "Desert":
                g2d.setColor(new Color(210, 180, 140)); // Tan
                break;
            default:
                g2d.setColor(Color.BLACK); // Default color
                break;
        }

        g2d.fill(hexagon);

        // Draw the outline of the hexagon
        g2d.setColor(Color.BLACK);
        g2d.draw(hexagon);

        // Set the font for drawing text
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics metrics = g2d.getFontMetrics();

        // Draw the resource name
        String resourceText = resourceType;
        int resourceTextWidth = metrics.stringWidth(resourceText);
        g2d.setColor(Color.WHITE);
        g2d.drawString(resourceText, x - resourceTextWidth / 2, y - 5);

        // Draw the number
        String numberText = String.valueOf(number);
        int numberTextWidth = metrics.stringWidth(numberText);
        int numberTextHeight = metrics.getAscent();
        g2d.drawString(numberText, x - numberTextWidth / 2, y + numberTextHeight / 2);
    }
    public Point getVertex(int vertexIndex) {
        int vertexX = (int) (x + size * Math.cos(Math.toRadians(60 * vertexIndex + 45)));
        int vertexY = (int) (y + size * Math.sin(Math.toRadians(60 * vertexIndex + 45)));
        return new Point(vertexX, vertexY);
    }
}
