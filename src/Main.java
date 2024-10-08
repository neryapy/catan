import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.util.List;

public class Main extends Board{
    private static Map<String, Integer> devCardList = new HashMap<>();
    private static Random random = new Random();
    public int playerPlayActually;
    JPanel mainPanel = new JPanel();
    public void main(String[] args) {
        JFrame frame = new JFrame();
        Board board = new Board();
        frame.add(board);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        satrtGame();
    }
    public void satrtGame(){
        Player p1 = new Player(1, new ArrayList<Village>(1), 0, false, 0, 0, 0, 0, 0);
        Player p2 = new Player(2, new ArrayList<Village>(1), 0, false, 0, 0, 0, 0, 0);
        Player p3 = new Player(3, new ArrayList<Village>(1), 0, false, 0, 0, 0, 0, 0);
        Player p4 = new Player(4, new ArrayList<Village>(1), 0, false, 0, 0, 0, 0, 0);
        addVillageWithOwner(1);

    }
    private void addVillageWithOwner(int owner){
        mode=1;
        ownerAbstarct=owner;
        try {
            Thread.sleep(3000); // Sleep for 1000 milliseconds (1 second)
            mode=0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private static void initializeDevCards() {
        devCardList.put("Knights", 14);
        devCardList.put("Road Building", 2);
        devCardList.put("Year of Plenty", 2);
        devCardList.put("Monopoly", 2);
        devCardList.put("Victory Point", 5);
    }

    private static Integer[] randomNumbersBoard() {
        Integer[] catanNumbers = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
        Collections.shuffle(Arrays.asList(catanNumbers));
        return catanNumbers;
    }

    private static int randomCubes() {
        Integer[] cube1 = {1, 2, 3, 4, 5, 6};
        Integer[] cube2 = {1, 2, 3, 4, 5, 6};
        Collections.shuffle(Arrays.asList(cube1));
        Collections.shuffle(Arrays.asList(cube2));
        return cube1[0] + cube2[0];
    }

    private static String chooseRandomCard() {
        List<String> availableCards = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : devCardList.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                availableCards.add(entry.getKey());
            }
        }

        if (availableCards.isEmpty()) {
            return "No cards available.";
        }

        String chosenCard = availableCards.get(random.nextInt(availableCards.size()));
        devCardList.put(chosenCard, devCardList.get(chosenCard) - 1);
        if (devCardList.get(chosenCard) == 0) {
            devCardList.remove(chosenCard);
        }

        return "You chose: " + chosenCard + ". Remaining cards: " + devCardList;
    }
}

class ImagePanel extends JPanel {
    private BufferedImage image;

    public ImagePanel() {
        try {
            image = ImageIO.read(new File("C:/prog/workspace java/catan/src/board.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double panelAspectRatio = (double) panelWidth / panelHeight;
        double imageAspectRatio = (double) imageWidth / imageHeight;

        int drawWidth;
        int drawHeight;

        if (panelAspectRatio > imageAspectRatio) {
            drawHeight = panelHeight;
            drawWidth = (int) (drawHeight * imageAspectRatio);
        } else {
            drawWidth = panelWidth;
            drawHeight = (int) (drawWidth / imageAspectRatio);
        }

        int drawX = (panelWidth - drawWidth) / 2;
        int drawY = (panelHeight - drawHeight) / 2;

        g.drawImage(image, drawX, drawY, drawWidth, drawHeight, this);
    }
}