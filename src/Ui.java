import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
//385
public class Ui extends JPanel {

    private static final int HEX_RADIUS = 50; // Radius of the getHexagonResources()
    private static final int X_SPACING = -15; // Spacing between getHexagonResources() horizontally
    private static final int Y_SPACING = 0;  // Spacing between rows of getHexagonResources()
    private static final int HEX_HEIGHT = 75; // Height of each hexagon
    private ArrayList<Point> centerPoints = new ArrayList<>();
    private ArrayList<Point> coloredVertices = new ArrayList<>();
    private List<Line2D> roadLines=new ArrayList<>();
    private ArrayList<JPanel> resourceCircles = new ArrayList<>();
    private JPanel circlePanel;
    private int sumDice;
    private Point[] hexMiddlePoints;
    private Board board;
    private JLabel cubesJLabel = new JLabel();
    private JLabel playerPlayLabel;
    private JFrame frame = new JFrame("CATAN");
    private String ownIp="";
    private Village villageclicked;
    private List<Road> roadsDrawed= new ArrayList<>();
    private List<Integer> villagesPlayersPlace=new ArrayList<>();
    private List<Integer> roadsPlayersPlace=new ArrayList<>();
    public Ui(Board board) {
        if(board.getHexagonResources()!=null){
            this.board=board;
            setupUI();
        }
        else{
            System.out.println("the ui was not created becasue");
        }
    }
    public void setOwnIp(String ip){
        this.ownIp=ip;
    }
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 45));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 145, 255));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Set rounded corners and shadow effect
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 125, 235), 2), // Outer border for button outline
                BorderFactory.createEmptyBorder(10, 20, 10, 20))); // Inner padding
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(35, 125, 235));
                button.setForeground(new Color(220, 220, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(45, 145, 255));
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clear old center points to avoid duplication during resizing
        centerPoints.clear();

        // Draw getHexagonResources() and calculate their center points
        int rowCount = 0;
        int hexCount = 0;
        for (int i = 0; i < board.getHexagonResources().size(); i++) {
            int rowHexCount = getHexCountForRow(rowCount);
            for (int j = 0; j < rowHexCount; j++) {
                float x = calculateHexX(j, rowCount) - 200;
                int y = calculateHexY(rowCount) - 200;
                centerPoints.add(new Point((int) x, y));
                HexagonResource hex = board.getHexagonResources().get(hexCount);
                drawBeautifulHexagon(g2d, (int) x, y, HEX_RADIUS, getResourceColor(hex.getResourceType()), hex.getResourceType(), hex.getNumber());
                hexMiddlePoints[hexCount]=new Point((int) x, y);
                hexCount++;
            }
            rowCount++;
        }
        

        // Draw vertices based on color (use the stored list of vertices)
        for (int i=0; i<coloredVertices.size(); i++) {
            if(coloredVertices.size()>0&&villagesPlayersPlace.size()>0){
                Point vertex = coloredVertices.get(i);
                if(villagesPlayersPlace.get(i)==0) g2d.setColor(new Color(255, 94, 0));
                else if(villagesPlayersPlace.get(i)==1) g2d.setColor(new Color(169, 199, 156));
                else if(villagesPlayersPlace.get(i)==2) g2d.setColor(new Color(207, 0, 0));
                else if(villagesPlayersPlace.get(i)==3) g2d.setColor(new Color(21, 174, 171));
                g2d.fillOval(vertex.x - 5, vertex.y - 5, 10, 10); // Draw vertex as a small circle
            }
        }
        if(roadLines!=null){
            for (int i=0; i<roadLines.size(); i++){
                if(roadLines.size()>0&&roadsPlayersPlace.size()>0){
                Line2D line = roadLines.get(i);
                if(roadsPlayersPlace.get(i)==0) g2d.setColor(new Color(255, 94, 0));
                else if(roadsPlayersPlace.get(i)==1) g2d.setColor(new Color(169, 199, 156));
                else if(roadsPlayersPlace.get(i)==2) g2d.setColor(new Color(207, 0, 0));
                else if(roadsPlayersPlace.get(i)==3) g2d.setColor(new Color(21, 174, 171));

                g2d.setStroke(new BasicStroke(4));
                g2d.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
                }
            }    
        }
        Point[] fPoints = {
            new Point(37, 27),//fine
            new Point(0, -24),
            new Point(-48, 27),//fine
            new Point(-48, -27),//fine
            new Point(0, 15),
            new Point(37, -27)//fine
        };
        System.out.println(hexMiddlePoints.length);
        int num1 = 0;
        while (num1 < 6) { // Replace 6 with your desired upper limit for num1
            int num2 = 0;
            while (num2 < 6) { // Replace 6 with your desired upper limit for num2
                for (int i = 0; i < hexMiddlePoints.length - 1; i++) {
                    if ((hexMiddlePoints[i].getX() + fPoints[num2].x) == (hexMiddlePoints[i + 1].getX() + fPoints[num1].x)&&(hexMiddlePoints[i].getY() + fPoints[num2].y) == (hexMiddlePoints[i + 1].getY() + fPoints[num1].y)) {
                        board.getHexagonResources().get(i).vertices.get(num2).connectHexagon(i + 1, num1);
                        //System.out.println("hexagon: " + i + ", vertex: " + num2 + " -> hexagon: " + (i + 1) + ", vertex: " + num1);
                    }
                    //System.out.println("debug: " + (hexMiddlePoints[i].getY() + fPoints[num2].y) + " -> " + (hexMiddlePoints[i + 1].getY() + fPoints[num1].y)+"| hexagon: " + i + ", vertex: " + num2 + " -> hexagon: " + (i + 1) + ", vertex: " + num1);
                }
                num2++;
            }
            num1++;
        }
        
   }
   
    

    // Improved method to draw getHexagonResources() with gradient fill and shadows
    private void drawBeautifulHexagon(Graphics2D g2d, int x, int y, int radius, Color color, String resource, int number) {
        Polygon hexagon = new Polygon();
        double rotationAngle = Math.toRadians(90);
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i) + rotationAngle;
            int px = (int) (x + radius * Math.cos(angle));
            int py = (int) (y + radius * Math.sin(angle));
            hexagon.addPoint(px, py);
        }

        // Add shadow effect
        g2d.setColor(new Color(0, 0, 0, 50)); // Semi-transparent black for shadow
        g2d.translate(3, 3); // Slight offset for the shadow
        g2d.fillPolygon(hexagon);
        g2d.translate(-3, -3); // Move back to original position

        // Fill hexagon with gradient
        GradientPaint gradient = new GradientPaint(x, y - radius, color.brighter(), x, y + radius, color.darker());
        g2d.setPaint(gradient);
        g2d.fillPolygon(hexagon);

        // Draw hexagon border
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(hexagon);

        // Draw text (resource type and number)
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();

        int resourceWidth = metrics.stringWidth(resource);
        g2d.drawString(resource, x - resourceWidth / 2, y - 10);

        String numberStr = Integer.toString(number);
        int numberWidth = metrics.stringWidth(numberStr);
        g2d.drawString(numberStr, x - numberWidth / 2, y + 15);
    }

    private int getHexCountForRow(int row) {
        switch (row) {
            case 0:
                return 3;
            case 1:
                return 4;
            case 2:
                return 5;
            case 3:
                return 4;
            case 4:
                return 3;
            default:
                return 0;
        }
    }

    private float calculateHexX(int colIndex, int rowIndex) {
        int centerX = getWidth() / 2 + 40;
        int rowHexCount = getHexCountForRow(rowIndex);
        float baseOffset = (float) (centerX - (rowHexCount * (HEX_RADIUS + X_SPACING)) / 1.5);

        int rowOffset = 0;
        switch (rowIndex) {
            case 0:
                rowOffset = 20;
                break;
            case 2:
                rowOffset = -20;
                break;
            case 4:
                rowOffset = 20;
                break;
        }
        return baseOffset + rowOffset + colIndex * (HEX_RADIUS * 2 + X_SPACING);
    }

    private int calculateHexY(int rowIndex) {
        int centerY = getHeight() / 2;
        return centerY + rowIndex * (HEX_HEIGHT + Y_SPACING);
    }

    private Color getResourceColor(String resourceType) {
        switch (resourceType.toLowerCase()) {
            case "wool":
                return new Color(144, 238, 144);
            case "grain":
                return new Color(255, 223, 0);
            case "lumber":
                return new Color(112, 56, 29);
            case "brick":
                return new Color(210, 105, 30);
            case "ore":
                return new Color(169, 169, 169);
            default:
                return Color.GRAY;
        }
    }

    private void colorAllVertices() {

        int hexIndex=0;
        villagesPlayersPlace.clear();
        coloredVertices.clear(); // Clear previous vertices if any
        // Loop through each hexagon and its vertices
        for (hexIndex = 0; hexIndex < board.getHexagons().size(); hexIndex++) {
            Point hexCenter = centerPoints.get(hexIndex); // Get the center point of the hexagon

            // Get the vertices of the current hexagon
            for (int vertexIndex = 0; vertexIndex < 6; vertexIndex++) {
                double angle = Math.toRadians(60 * vertexIndex) + Math.toRadians(90); // Calculate angle for each vertex
                int px = (int) (hexCenter.x + HEX_RADIUS * Math.cos(angle));   // Calculate x based on angle
                int py = (int) (hexCenter.y + HEX_RADIUS * Math.sin(angle));   // Calculate y based on angle
                Point vertex = new Point(px, py);

                // Check if the vertex has a village
                boolean hasVillage = board.getHexagons().get(hexIndex).getVertices().get(vertexIndex).getVillage();
                villagesPlayersPlace.add(playerPlayNum(board.getPlayers()));
                coloredVertices.add(new Point(px, py));
            }
        }

        repaint(); // Trigger a repaint to actually draw the colored vertices
    }
    private void clearClickedVertex(Point clickPoint) {
            board.players.get(playerPlayNum(board.getPlayers())).setBuildVillage(true);
            int hexClicked = -1;
            int vertexClicked = -1;
            villagesPlayersPlace.clear();
            coloredVertices.clear();
            for(int i=0; i<board.getHexagons().size(); i++){
                if(hexMiddlePoints[i].x-55<=clickPoint.x && hexMiddlePoints[i].x+55>=clickPoint.x&&hexMiddlePoints[i].y-55<=clickPoint.y&&hexMiddlePoints[i].y+55>=clickPoint.y){
                    hexClicked=i;
                    System.out.println("hex clicked: "+hexClicked);
                }
            }
            if(hexClicked==-1){
                System.out.println("no hex was clicked");
                return;
            }
            if(hexMiddlePoints[hexClicked].x+60>=clickPoint.x&&hexMiddlePoints[hexClicked].x+40<=clickPoint.x&&hexMiddlePoints[hexClicked].y+35>=clickPoint.y&&hexMiddlePoints[hexClicked].y+15<=clickPoint.y){
                vertexClicked=0;
                System.out.println("vertex 0");}
            if(hexMiddlePoints[hexClicked].x+10>=clickPoint.x&&hexMiddlePoints[hexClicked].x-10<=clickPoint.x&&hexMiddlePoints[hexClicked].y+60>=clickPoint.y&&hexMiddlePoints[hexClicked].y+40<=clickPoint.y){
                vertexClicked=1;
                System.out.println("vertex 1");}
            if(hexMiddlePoints[hexClicked].x-40>=clickPoint.x&&hexMiddlePoints[hexClicked].x-60<=clickPoint.x&&hexMiddlePoints[hexClicked].y+35>=clickPoint.y&&hexMiddlePoints[hexClicked].y+15<=clickPoint.y){
                vertexClicked=2;
                System.out.println("vertex 2");}
            if(hexMiddlePoints[hexClicked].x-40>=clickPoint.x&&hexMiddlePoints[hexClicked].x-60<=clickPoint.x&&hexMiddlePoints[hexClicked].y-15>=clickPoint.y&&hexMiddlePoints[hexClicked].y-35<=clickPoint.y){
                vertexClicked=3;
                System.out.println("vertex 3");}
            if(hexMiddlePoints[hexClicked].x+10>=clickPoint.x&&hexMiddlePoints[hexClicked].x-10<=clickPoint.x&&hexMiddlePoints[hexClicked].y-40>=clickPoint.y&&hexMiddlePoints[hexClicked].y-60<=clickPoint.y){
                vertexClicked=4;
                System.out.println("vertex 4");}
            if(hexMiddlePoints[hexClicked].x+60>=clickPoint.x&&hexMiddlePoints[hexClicked].x+40<=clickPoint.x&&hexMiddlePoints[hexClicked].y-15>=clickPoint.y&&hexMiddlePoints[hexClicked].y-35<=clickPoint.y){
                vertexClicked=5;
                System.out.println("vertex 5");}
            System.out.println();
            villageclicked=new Village(hexClicked, vertexClicked,playerPlayNum(board.getPlayers()));
            board.players.get(playerPlayNum(board.getPlayers())).villages.add(villageclicked);
            board.addVillageInHexagon(villageclicked);
            addVillageToDraw(villageclicked);
            repaint();
            board.players.get(playerPlayNum(board.getPlayers())).setBuildVillage(false);
    }
    private int playerPlayNum(ArrayList<Player> getPlayers) {
        for (int i = 0; i < getPlayers.size(); i++) {if (getPlayers.get(i).isPlayerPlay()) {return i;}}
        return -1;
    }
    public void colorResourceAsRed(JPanel circlePanel) {
        // Set the inside of the circle to red
        circlePanel.setBackground(Color.RED);
    
        // Create a timer to revert the color back after 2 seconds
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Revert the background color back to the original resource color
                Color originalColor=new Color(60, 63, 65);
                // Reset the background color
                circlePanel.setBackground(originalColor);
            }
        });
        timer.setRepeats(false); // Only execute once
        timer.start(); // Start the timer
    }
    private int randomDie(){    
        ArrayList<Integer> Cube1=new ArrayList<>();
        for(int i=1; i<6; i++){Cube1.add(i);}
        Collections.shuffle(Cube1);
        int c1=Cube1.get(0);
        Collections.shuffle(Cube1);
        return c1+Cube1.get(0);
    }

    private void BuildRoad() {
        Point[] fPoints = {
            new Point(45, 25), //fine
            new Point(0, 50),
            new Point(-45, 27), //fine
            new Point(-45, -27), //fine
            new Point(0, -50),
            new Point(45, -25) //fine
        };

        for (int i = 0; i < hexMiddlePoints.length; i++) {
            for (int k = 0; k < fPoints.length; k++) {

                if(board.getHexagons().get(i).vertices.get(k).getVillage()||board.getHexagons().get(i).vertices.get((k+1)%6).getVillage()){
                    roadLines.add(new Line2D.Float(
                        hexMiddlePoints[i].x + fPoints[k].x, hexMiddlePoints[i].y + fPoints[k].y,
                        hexMiddlePoints[i].x + fPoints[(k + 1) % 6].x, hexMiddlePoints[i].y + fPoints[(k + 1) % 6].y
                    ));
                    roadsDrawed.add(new Road(i, k, (k + 1) % 6));
                    roadsPlayersPlace.add(playerPlayNum(board.getPlayers()));
                    JButton roadButton = new JButton("Road");
                    System.out.println("width="+Math.sqrt(Math.pow((hexMiddlePoints[i].x + fPoints[k].x-(hexMiddlePoints[i].x + fPoints[(k + 1) % 6].x)),2)+Math.pow((hexMiddlePoints[i].y + fPoints[k].y-(hexMiddlePoints[i].y + fPoints[(k + 1) % 6].y)),2)));
                    System.out.println("height=");
                    board.players.get(playerPlayNum(board.getPlayers())).setBuildRoad(true);
                }
            }
        }
        
        repaint();

    }
    private void addRoad(Point p){
        int v1=-1,v2=-1,hi=-1;
        System.out.println("you entered");
        Point[] fPoints = {
            new Point(45, 25), //fine
            new Point(0, 50),
            new Point(-45, 27), //fine
            new Point(-45, -27), //fine
            new Point(0, -50),
            new Point(45, -25) //fine
        };
        System.out.println("size of roadlines"+roadLines.size());
        for (int i=0; i<roadLines.size(); i++)
        {
            if (roadLines.get(i).ptSegDist(p.getX(), p.getY()) < 5) { // If the click is within 5 pixels of the line
                for(Road actualRoad:roadsDrawed){
                    System.out.println("actual road:"+(hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv1()].y)+" "+p.y);
                    if(hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv1()].y<=p.y&&hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv2()].y>=p.y){
                        System.out.println("hexindex of road :"+actualRoad.getIndexHexagon()+" v1: "+actualRoad.getv1()+" v2: "+actualRoad.getv2());
                        roadLines=new ArrayList<>();
                        roadLines.add(new Line2D.Float(
                            hexMiddlePoints[actualRoad.getIndexHexagon()].x + fPoints[actualRoad.getv1()].x, hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv1()].y,
                            hexMiddlePoints[actualRoad.getIndexHexagon()].x + fPoints[actualRoad.getv2()].x, hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv2()].y
                        ));
                        roadsPlayersPlace=new ArrayList<>();
                        roadsPlayersPlace.add(playerPlayNum(board.getPlayers()));
                        board.addRoad(actualRoad);
                    }
                }
                System.out.println("road was clicked:"+i);
            }

        }
        repaint();
        board.players.get(playerPlayNum(board.getPlayers())).setBuildRoad(false);
    }
    
    private void setupUI() {
        // Code to initialize the UI
        
        //create the connected vertices:
        hexMiddlePoints = new Point[board.getHexagonResources().size()];
        //end create the connected vertices.
        this.setBackground(new Color(14, 150, 212));
        // Create the window (JFrame) when Ui is instantiated
        int hexagonCount = board.getHexagons().size();
        int rowIndex = 0;
        int rowHexCount = getHexCountForRow(rowIndex);
        
        System.out.println();
        // Set the layout for custom button placement
        this.setLayout(null);
        // Create a panel for the right-side menu with a background color
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 600));
        rightPanel.setBackground(new Color(60, 63, 65));  // Dark background color for the panel

        // Create the "BUILD" label with styling
        JLabel buildLabel = new JLabel("BUILD");
        playerPlayLabel = new JLabel("Player play: " + (playerPlayNum(board.getPlayers())+1));
        playerPlayLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerPlayLabel.setForeground(Color.WHITE);
        buildLabel.setFont(new Font("Arial", Font.BOLD, 24));
        buildLabel.setForeground(Color.WHITE);
        buildLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(playerPlayLabel);
        rightPanel.add(buildLabel);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        // Create beautiful buttons with rounded corners and hover effects
        JButton villageButton = createStyledButton("Village");
        JButton cityButton = createStyledButton("City");
        JButton roadButton = createStyledButton("Road");
        JButton buyDevcardButton = createStyledButton("Dev card");
        // Add buttons to the panel
        rightPanel.add(buyDevcardButton);
        rightPanel.add(villageButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(cityButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(roadButton);
// Add this code after creating and positioning the "Road" button in the constructor:

// Create a panel to hold resource circles
        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Horizontal alignment with padding
        resourcePanel.setBounds(20, 43, 250, 50); // Position below the "Road" button
        resourcePanel.setBackground(new Color(60, 63, 65)); // Match the background color of the right panel

// Add the resource panel to the rightPanel
        rightPanel.add(resourcePanel);
        villageButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if(sumDice==0){
                    board.players.get(playerPlayNum(board.getPlayers())).setBuildVillage(true);
                    colorAllVertices();
                    sumDice=-1;
                }
                else{
                    if(board.getPlayers().get(playerPlayNum(board.getPlayers())).getIp()==ownIp){
                    //ArrayList<Resource> r=new ArrayList<>();
                    //r.add(new Resource("grain"));
                    //r.add(new Resource("wool"));
                    //r.add(new Resource("lumber"));
                    //r.add(new Resource("brick"));
                    //board.players.get(playerPlayNum(board.getPlayers())).addResources(r);
                    if(board.getPlayers().get(playerPlayNum(board.getPlayers())).villages.size()<2){
                        board.players.get(playerPlayNum(board.getPlayers())).setBuildVillage(true);
                        board.players.get(playerPlayNum(board.getPlayers())).addVillage(villageclicked);
                        System.out.println("-----------------------------player: "+playerPlayNum(board.getPlayers())+" villages: "+board.players.get(playerPlayNum(board.getPlayers())).villages.size());
                    }
                    if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("grain"))){colorResourceAsRed(resourceCircles.get(0));}
                    if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("brick"))){colorResourceAsRed(resourceCircles.get(3));}
                    if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("lumber"))){colorResourceAsRed(resourceCircles.get(4));}
                    if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("wool"))){colorResourceAsRed(resourceCircles.get(1));}
                    else{

                        board.players.get(playerPlayNum(board.getPlayers())).setBuildVillage(true);
                        if(villageclicked!=null) board.players.get(playerPlayNum(board.getPlayers())).addVillage(villageclicked);
                        System.out.println("player: "+playerPlayNum(board.getPlayers())+" villages: "+board.players.get(playerPlayNum(board.getPlayers())).villages.size());
                    }
                }
            }
        }
        });
        rightPanel.add(resourcePanel);
        roadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                board.players.get(playerPlayNum(board.getPlayers())).addResource(new Resource("brick"));
                board.players.get(playerPlayNum(board.getPlayers())).addResource(new Resource("lumber"));

                if(board.getPlayers().get(playerPlayNum(board.getPlayers())).getIp()==ownIp){
                if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("brick"))){colorResourceAsRed(resourceCircles.get(3));}
                if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("lumber"))){colorResourceAsRed(resourceCircles.get(4));}
                if(board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("lumber"))&&board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("brick"))){
                    BuildRoad();
                }
            }
            }
        });
        cityButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if(board.getPlayers().get(playerPlayNum(board.getPlayers())).getIp()==ownIp){
                // Retrieve the current player's resources
                ArrayList<Resource> resources = board.getPlayers().get(playerPlayNum(board.getPlayers())).resources;

                // Count the occurrences of "grain" and "ore" in the resources list
                long grainCount = resources.stream().filter(r -> r.equals(new Resource("grain"))).count();
                long oreCount = resources.stream().filter(r -> r.equals(new Resource("ore"))).count();
                // Check if the player has less than the required amount of each resource
                if (grainCount < 2) {
                    colorResourceAsRed(resourceCircles.get(0)); // Assuming grain circle is at index 0
                }
                if (oreCount < 3) {
                    colorResourceAsRed(resourceCircles.get(2)); // Assuming ore circle is at index 2
                }
            }
            }
        });
        buyDevcardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("grain"))){colorResourceAsRed(resourceCircles.get(0));}
                if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("ore"))){colorResourceAsRed(resourceCircles.get(2));}
                if(!board.getPlayers().get(playerPlayNum(board.getPlayers())).resources.contains(new Resource("wool"))){colorResourceAsRed(resourceCircles.get(1));}
                board.getPlayers().get(playerPlayNum(board.getPlayers())).buyDevCard();
                rightPanel.repaint();
            }
        });
        JButton endTurnButton = createStyledButton("End My Turn");
        endTurnButton.setPreferredSize(new Dimension(120, 40));
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);

// Align to the bottom with padding
        rightPanel.add(Box.createVerticalGlue()); // Pushes the button to the bottom
        rightPanel.add(endTurnButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

// Set an action for the "End My Turn" button
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    
                    if(board.getplayers().get(playerPlayNum(board.getPlayers())).getIp()==ownIp&&board.players.get(playerPlayNum(board.getPlayers())).getDiceTurned()==true&&board.players.get(playerPlayNum(board.getPlayers())).getBuildVillage()==false&&board.players.get(playerPlayNum(board.getPlayers())).getBuildRoad()==false){
                    
                    if(playerPlayNum(board.getPlayers())+1==board.getPlayers().size()){
                        board.players.get(board.getPlayers().size()-1).setPlayerPlay(false);
                        board.players.get(0).setPlayerPlay(true);
                    }
                    else {
                        board.setPlayersPlay(playerPlayNum(board.getPlayers()) + 1, true);
                        board.setPlayersPlay(playerPlayNum(board.getPlayers()), false);
                    }
                    System.out.println("Turn Ended "+(playerPlayNum(board.getPlayers())+1));
                    playerPlayLabel.setText("Player play: " + (playerPlayNum(board.getPlayers())+1));
                    System.out.println("Player play by ui: " + (playerPlayNum(board.getPlayers())+1));
                    board.players.get(playerPlayNum(board.getPlayers())).setDiceTurned(false);
                    repaint();
                }
            }
        });
//
        cubesJLabel.setFont(new Font("Arial", Font.BOLD, 17));
        cubesJLabel.setForeground(Color.WHITE);
        cubesJLabel.setText("the sum of the dice: NULL");
        JButton rollDie = createStyledButton("Roll a die");
        rollDie.setPreferredSize(new Dimension(120, 40));
        rollDie.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Align to the bottom with padding
        rightPanel.add(Box.createVerticalGlue()); // Pushes the button to the bottom
        rightPanel.add(rollDie);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Set an action for the "End My Turn" button
        rollDie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(board.getplayers().get(playerPlayNum(board.getPlayers())).getIp()==ownIp&&board.players.get(playerPlayNum(board.players)).getDiceTurned()==false){

                sumDice=randomDie();
                board.setSumDice(sumDice);
                cubesJLabel.setText("the sum of the dice:"+sumDice);
                System.out.println("the sum of the dice: "+sumDice);
                rightPanel.repaint();
                board.players.get(playerPlayNum(board.getPlayers())).setDiceTurned(true);
            }
        }
        });
//
// Add a mouse listener to detect when a vertex is clicked to clear the color
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if(board.getPlayers().get(playerPlayNum(board.getPlayers())).getBuildVillage()) clearClickedVertex(evt.getPoint());
                else if(board.getPlayers().get(playerPlayNum(board.getPlayers())).getBuildRoad()) addRoad(evt.getPoint());
            }});
        // Panel and button setup
        rightPanel.setLayout(null); // Set layout to null for absolute positioning

// "BUILD" Label
        buildLabel.setBounds(90, 20, 120, 40); // Position "BUILD" label at the top center
        rightPanel.add(buildLabel);

// Village Button
        villageButton.setBounds(20, 80, 120, 45); // Positioned below "BUILD" label at the bottom-left
        rightPanel.add(villageButton);

// City Button
        cityButton.setBounds(150, 80, 120, 45); // Positioned to the right of "Village" button
        rightPanel.add(cityButton);

// Road Button
        roadButton.setBounds(20, 140, 120, 45); // Positioned below "Village" button
        rightPanel.add(roadButton);
// Buy dev card button
        buyDevcardButton.setBounds(150, 140, 120, 45);
        rightPanel.add(buyDevcardButton);
// End Turn Button
        endTurnButton.setBounds(140, 515, 150, 40); // Positioned at the bottom center
        rightPanel.add(endTurnButton);
// Roll a die Button
        rollDie.setBounds(10, 515, 120, 40); // Positioned at the bottom center
        rightPanel.add(rollDie);

        playerPlayLabel.setBounds(90, -5, 200, 40); // Adjust position and size as needed
        rightPanel.add(playerPlayLabel);

        cubesJLabel.setBounds(20, 480, 250, 40); // Adjust position and size as needed
        rightPanel.add(cubesJLabel);

        Color[] colors = {
                getResourceColor("grain"),
                getResourceColor("wool"),
                getResourceColor("ore"),
                getResourceColor("brick"),
                getResourceColor("lumber")
                
        };


        
        for (int i = 0; i < colors.length; i++) {
            String resourceType = ""; // Assign resource types based on your game logic order
            switch (i) {
                case 0: resourceType = "grain"; break;
                case 1: resourceType = "wool"; break;
                case 2: resourceType = "ore"; break;
                case 3: resourceType = "brick"; break;
                case 4: resourceType = "lumber"; break;
            }

            int finalI = i;
            String finalResourceType = resourceType;
            circlePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(colors[finalI]); // Set color for the circle
                    g.fillOval(0, 0, 30, 30); // Draw the circle with the resource color

                    // Set border color to the same gray as the menu
                    g.setColor(new Color(60, 63, 65));
                    g.drawOval(0, 0, 30, 30); // Draw the border of the circle

                    // Count the resource quantity
                    int resourceCount = 0;
                    for (Resource res : board.getPlayers().get(playerPlayNum(board.getPlayers())).resources) {
                        if (res.getType().equalsIgnoreCase(finalResourceType)) {
                            resourceCount++;
                        }
                    }

                    // Draw the resource count inside the circle
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 12));
                    g.drawString(String.valueOf(resourceCount), 12, 20); // Display the count in the center
                }
            };
            circlePanel.setBackground(new Color(60, 63, 65)); // Match the gray menu color
            circlePanel.setOpaque(true); // Ensure the background color is applied



            circlePanel.setPreferredSize(new Dimension(30, 30));
            resourceCircles.add(circlePanel);
            resourcePanel.add(circlePanel);
            repaint();
        }
        rightPanel.add(resourcePanel);

        // Add action listener for End Turn button
        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // existing end-turn code ...

                // Refresh the circles to show updated colors or numbers if needed
                for (JPanel circle : resourceCircles) {
                    circle.repaint(); // Redraw each circle
                }
            }
        });

        // Set frame properties and add components
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER); // Add the hexagon drawing panel
        frame.add(rightPanel, BorderLayout.EAST); // Add the right panel with buttons
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private void addVillageToDraw(Village village){
        Point[] fPoints = {
            new Point(45, 25),//fine
            new Point(0, 45),
            new Point(-45, 25),//fine
            new Point(-45, -25),//fine
            new Point(0, -45),
            new Point(45, -25)//fine
        };
        Point vertex = new Point(hexMiddlePoints[village.index].x+fPoints[village.getVertex()].x, hexMiddlePoints[village.index].y+fPoints[village.getVertex()].y);
        villagesPlayersPlace.add(playerPlayNum(board.getPlayers()));
        coloredVertices.add(vertex);
    }
    public void updateAll(Board board){
        this.board=board;
        this.sumDice=board.getSumDice();
        this.playerPlayLabel.setText("Player play: " + (playerPlayNum(board.getPlayers())+1));
        this.cubesJLabel.setText("the sum of the dice: "+sumDice);
        
        for(int k=0; k<board.getHexagons().size(); k++){
            for(int j=0; j<board.getHexagons().get(k).getVertices().size(); j++){
                if(board.getHexagons().get(k).getVertices().get(j).getVillage()){
                    addVillageToDraw(new Village(k, j, playerPlayNum(board.getPlayers())));
                }
                //System.out.println("hexagon: "+k+", vertex: "+j+" -> village: "+board.getHexagons().get(k).getVertices().get(j).getVillage());
            }
        }
        this.frame.repaint();
    }
}