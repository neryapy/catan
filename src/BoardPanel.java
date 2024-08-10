import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {
    private static final int HEX_SIZE = 50; // Size of the hexagon
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 600;
    private static final int HEX_WIDTH = (int) (HEX_SIZE * 2);
    private static final int HEX_HEIGHT = (int) (Math.sqrt(3) * HEX_SIZE);
    private static final int HEX_VERTICAL_SPACING = (int) (HEX_HEIGHT * 0.75); // vertical spacing between hexes
    private static final int HEX_HORIZONTAL_SPACING = HEX_WIDTH - HEX_SIZE; // horizontal spacing between hexes
    private BufferedImage hexImage;
    private Map<Integer, Point> hexLocations;

    public BoardPanel() {
        try {
            hexImage = ImageIO.read(new File("C:/prog/workspace java/catan/src/hex.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        hexLocations = new HashMap<>();
        generateHexLocations();
    }

    private void generateHexLocations() {
        int xOffset = BOARD_WIDTH / 2 - HEX_WIDTH;
        int yOffset = BOARD_HEIGHT / 2 - HEX_HEIGHT;

        // Generate hex positions for the board
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (Math.abs(row - 2) + Math.abs(col - 2) < 3) {
                    int x = xOffset + col * HEX_HORIZONTAL_SPACING;
                    int y = yOffset + row * HEX_VERTICAL_SPACING;
                    hexLocations.put(row * 5 + col, new Point(x, y));
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Map.Entry<Integer, Point> entry : hexLocations.entrySet()) {
            Point location = entry.getValue();
            drawHex(g2d, location.x, location.y);
        }
    }

    private void drawHex(Graphics2D g2d, int x, int y) {
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(x, y);
        g2d.drawImage(hexImage, 0, 0, HEX_WIDTH, HEX_HEIGHT, this);
        g2d.setTransform(originalTransform);
    }
}
