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
import java.awt.GridLayout;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
//385
public class Ui extends JPanel {
    private JPanel UseDevCard = new JPanel();
    private static final int HEX_RADIUS = 50; // Radius of the getHexagons()
    private static final int X_SPACING = -15; // Spacing between getHexagons() horizontally
    private static final int Y_SPACING = 0;  // Spacing between rows of getHexagons()
    private static final int HEX_HEIGHT = 75; // Height of each hexagon
    private ArrayList<Point> centerPoints = new ArrayList<>();
    private boolean useKnight = false;
    private boolean useRobber = false;
    private boolean useroadBuilding=false;
    private ArrayList<Point> OptionVillage = new ArrayList<>();
    private List<Line2D> roadLines=new ArrayList<>();
    private List<Line2D> PlacedRoadsLine=new ArrayList<>();
    private ArrayList<JPanel> resourceCircles = new ArrayList<>();
    private JPanel circlePanel;
    private int sumDice;
    private Point[] hexMiddlePoints;
    private Board board;
    private JLabel cubesJLabel = new JLabel();
    private JLabel playerPlayLabel;
    private JFrame GameFrame = new JFrame("CATAN");
    private JFrame DevCardMenu = new JFrame("Development Cards");
    private String ownIp="";
    private Village villageclicked;
    private List<Point> villagesToDraw=new ArrayList<>();
    private List<Integer> villagesPlayersTOdraw=new ArrayList<>();
    private List<Road> roadsDrawed= new ArrayList<>();
    private List<Point> CityToDraw=new ArrayList<>();
    private List<Point> FinnalyPlaccedCity=new ArrayList<>();
    private List<Integer> CitysPlayersPlace=new ArrayList<>();
    private List<Integer> villagesPlayersPlace=new ArrayList<>();
    private List<Integer> PermanatntRoadsPlayersPlace=new ArrayList<>();
    private List<Integer> roadsPlayersPlace=new ArrayList<>();
    Point[] fPoints = {
        new Point(45, 25), //fine
        new Point(-3, 50),
        new Point(-45, 27), //fine
        new Point(-45, -27), //fine
        new Point(-3, -50),
        new Point(45, -25) //fine
    };
    public Ui(Board board) {
        if(board.getHexagons()!=null){
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

        // Draw getHexagons() and calculate their center points
        int rowCount = 0;
        int hexCount = 0;
        for (int i = 0; i < board.getHexagons().size(); i++) {
            int rowHexCount = getHexCountForRow(rowCount);
            for (int j = 0; j < rowHexCount; j++) {
                float x = calculateHexX(j, rowCount) - 200;
                int y = calculateHexY(rowCount) - 200;
                centerPoints.add(new Point((int) x, y));
                HexagonResource hex = board.getHexagons().get(hexCount);
                drawBeautifulHexagon(g2d, (int) x, y, HEX_RADIUS, getResourceColor(hex.getResourceType()), hex.getResourceType(), hex.getNumber());
                hexMiddlePoints[hexCount]=new Point((int) x, y);
                hexCount++;
            }
            rowCount++;
        }
        

        // Draw vertices based on color (use the stored list of vertices)
        if(OptionVillage.size()>0){
        for (int i=0; i<OptionVillage.size(); i++) {
            if(OptionVillage.size()>0&&villagesPlayersPlace.size()>0){
                Point vertex = OptionVillage.get(i);
                if(villagesPlayersPlace.get(i)==0) g2d.setColor(new Color(255, 94, 0));
                else if(villagesPlayersPlace.get(i)==1) g2d.setColor(new Color(169, 199, 156));
                else if(villagesPlayersPlace.get(i)==2) g2d.setColor(new Color(207, 0, 0));
                else if(villagesPlayersPlace.get(i)==3) g2d.setColor(new Color(21, 174, 171));
                g2d.fillOval(vertex.x - 5, vertex.y - 5, 10, 10); // Draw vertex as a small circle
            }
        }}
        if(villagesToDraw.size()>0){
            for (int i=0; i<villagesToDraw.size(); i++) {
                if(villagesToDraw.size()==villagesPlayersTOdraw.size()&&villagesPlayersTOdraw.size()>0){
                    Point vertex = villagesToDraw.get(i);
                    if(villagesPlayersTOdraw.get(i)==0) g2d.setColor(new Color(255, 94, 0));
                    else if(villagesPlayersTOdraw.get(i)==1) g2d.setColor(new Color(169, 199, 156));
                    else if(villagesPlayersTOdraw.get(i)==2) g2d.setColor(new Color(207, 0, 0));
                    else if(villagesPlayersTOdraw.get(i)==3) g2d.setColor(new Color(21, 174, 171));
                    g2d.fillOval(vertex.x - 5, vertex.y - 5, 10, 10); // Draw vertex as a small circle
                }
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

                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
                }
            }    
        }
        if(PlacedRoadsLine!=null){
            for (int i=0; i<PlacedRoadsLine.size(); i++){
                if(PlacedRoadsLine.size()==PermanatntRoadsPlayersPlace.size()&&PermanatntRoadsPlayersPlace.size()>0){
                Line2D line = PlacedRoadsLine.get(i);
                if(PermanatntRoadsPlayersPlace.get(i)==0) g2d.setColor(new Color(255, 94, 0));
                else if(PermanatntRoadsPlayersPlace.get(i)==1) g2d.setColor(new Color(169, 199, 156));
                else if(PermanatntRoadsPlayersPlace.get(i)==2) g2d.setColor(new Color(207, 0, 0));
                else if(PermanatntRoadsPlayersPlace.get(i)==3) g2d.setColor(new Color(21, 174, 171));

                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
                }
            }    
        }
        if(CityToDraw!=null&&CitysPlayersPlace!=null){
            for (int i=0; i<CitysPlayersPlace.size(); i++){
                if(CitysPlayersPlace.get(i)==0) g2d.setColor(new Color(255, 94, 0));
                else if(CitysPlayersPlace.get(i)==1) g2d.setColor(new Color(169, 199, 156));
                else if(CitysPlayersPlace.get(i)==2) g2d.setColor(new Color(207, 0, 0));
                else if(CitysPlayersPlace.get(i)==3) g2d.setColor(new Color(21, 174, 171));
            }
            for(Point ActualPoint:CityToDraw){
                g2d.fillRect(ActualPoint.x-10, ActualPoint.y-10, 20, 20);

            }
        }
        if(FinnalyPlaccedCity!=null){
            for(Point ActualPoint:FinnalyPlaccedCity){
                g2d.fillRect(ActualPoint.x-10, ActualPoint.y-10, 20, 20);
            }
        }
        for(int k=0; k<board.getPlayerPlay().playerDevCards.size(); k++){if(useKnight&&board.getPlayerPlay().playerDevCards.get(k).getType()==("knight")&&!board.getPlayerPlay().playerDevCards.get(k).getUsed()){
            g2d.setColor(Color.BLACK);
            for(Point i:hexMiddlePoints) g2d.fillOval(i.x-10, i.y+20, 20, 20);
        }}
        for(int i=0; i<board.getHexagons().size(); i++){
            if(board.getHexagons().get(i).HasRobber()){
                g2d.setColor(Color.BLACK);
                g2d.fillOval(hexMiddlePoints[i].x-10, hexMiddlePoints[i].y+20, 20, 20);
            }
        }
   }
   
    

    // Improved method to draw getHexagons() with gradient fill and shadows
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

    private void buildVillage() {
        if(board.getPlayerPlay().getBuildRoad()==false&&board.getPlayerPlay().getBuildCity()==false&&board.getPlayerPlay().getBuildVillage()==true){
            int hexIndex=0;
            villagesPlayersPlace.clear();
            // Loop through each hexagon and its vertices
            for (hexIndex = 0; hexIndex < board.getHexagons().size(); hexIndex++) {
                Point hexCenter = centerPoints.get(hexIndex); // Get the center point of the hexagon
                // Get the vertices of the current hexagon
                for (int vertexIndex = 0; vertexIndex < 6; vertexIndex++) {
                    double angle = Math.toRadians(60 * vertexIndex) + Math.toRadians(90); // Calculate angle for each vertex
                    int px = (int) (hexCenter.x + HEX_RADIUS * Math.cos(angle));   // Calculate x based on angle
                    int py = (int) (hexCenter.y + HEX_RADIUS * Math.sin(angle));   // Calculate y based on angle
                    // Check if the vertex has a village
                    villagesPlayersPlace.add(playerPlayNum(board.getPlayers()));
                    OptionVillage.add(new Point(px, py));
                }
            }

            board.getPlayerPlay().setBuildVillage(true);
            repaint(); // Trigger a repaint to actually draw the colored vertices
        }
    }
    private void addVillage(Point clickPoint) {
            if(board.getPlayerPlay().getBuildVillage()){
            OptionVillage.clear(); // Clear previous vertices if any
            int hexClicked = -1;
            int vertexClicked = -1;
            villagesPlayersPlace.clear();
            OptionVillage.clear();
            for(int i=0; i<board.getHexagons().size(); i++){
                if(hexMiddlePoints[i].x-55<=clickPoint.x && hexMiddlePoints[i].x+55>=clickPoint.x&&hexMiddlePoints[i].y-55<=clickPoint.y&&hexMiddlePoints[i].y+55>=clickPoint.y){
                    hexClicked=i;
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
            board.getPlayerPlay().villages.add(villageclicked);
            System.out.println("hex index:"+hexClicked);
            board.addVillageInHexagon(villageclicked);
            addVillageToDraw(villageclicked);
            repaint();
            
            board.getPlayerPlay().setBuildVillage(false);
            }
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
    private int RandomDice(){    
        ArrayList<Integer> Cube1=new ArrayList<>();
        for(int i=1; i<6; i++){Cube1.add(i);}
        Collections.shuffle(Cube1);
        int c1=Cube1.get(0);
        Collections.shuffle(Cube1);
        return c1+Cube1.get(0);
    }

    private void BuildRoad() {
        if(board.getPlayerPlay().getBuildCity()==false&&board.getPlayerPlay().getBuildVillage()==false&&board.getPlayerPlay().getBuildRoad()==false){
            for (int i = 0; i < hexMiddlePoints.length; i++) {
                for (int k = 0; k < fPoints.length; k++) {
                    if(board.getHexagons().get(i).vertices.get(k).getVillage()||board.getHexagons().get(i).vertices.get((k+1)%6).getVillage()||board.getHexagons().get(i).vertices.get(k).getCity()||board.getHexagons().get(i).vertices.get((k+1)%6).getCity()){

                        roadLines.add(new Line2D.Float(
                            hexMiddlePoints[i].x + fPoints[k].x, hexMiddlePoints[i].y + fPoints[k].y,
                            hexMiddlePoints[i].x + fPoints[(k + 1) % 6].x, hexMiddlePoints[i].y + fPoints[(k + 1) % 6].y
                        ));
                        roadsDrawed.add(new Road(i, k, (k + 1) % 6));
                        roadsPlayersPlace.add(playerPlayNum(board.getPlayers()));
                        board.getPlayerPlay().setBuildRoad(true);
                    }
                }
            }
            board.getPlayerPlay().resources.remove(new Resource("brick"));
            board.getPlayerPlay().resources.remove(new Resource("lumber"));
            GameFrame.repaint();
        }

    }
    private void addRoad(Point p){
        
        for (int i=0; i<roadLines.size(); i++)
        {
            if (roadLines.get(i).ptSegDist(p.getX(), p.getY()) < 5) { // If the click is within 5 pixels of the line
                for(int j=0; j<roadsDrawed.size(); j++){
                    Road actualRoad=roadsDrawed.get(j);
                    System.out.println("actual road:"+(hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv1()].y)+" "+p.y);
                    if(hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv1()].y<=p.y&&hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv2()].y>=p.y){
                        System.out.println("hexindex of road :"+actualRoad.getIndexHexagon()+" v1: "+actualRoad.getv1()+" v2: "+actualRoad.getv2());
                        drawRoad(actualRoad);
                        board.addRoad(actualRoad);
                    }
                }
                System.out.println("road was clicked:"+i);
            }

        }
        repaint();
        board.getPlayerPlay().setBuildRoad(false);
        System.out.println("enter"+useroadBuilding);
        if(useroadBuilding){BuildRoad();
        System.out.println("enter");
        useroadBuilding=false;}
    }
    private void drawRoad(Road actualRoad){
        PlacedRoadsLine.add(new Line2D.Float(
            hexMiddlePoints[actualRoad.getIndexHexagon()].x + fPoints[actualRoad.getv1()].x, hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv1()].y,
            hexMiddlePoints[actualRoad.getIndexHexagon()].x + fPoints[actualRoad.getv2()].x, hexMiddlePoints[actualRoad.getIndexHexagon()].y + fPoints[actualRoad.getv2()].y
        ));
        PermanatntRoadsPlayersPlace.add(playerPlayNum(board.getPlayers()));
        roadLines.clear();
        roadsDrawed.clear();
        roadsPlayersPlace.clear();
        roadsPlayersPlace.add(playerPlayNum(board.getPlayers()));
        GameFrame.repaint();
    }
    private void buildCity(){
        int graincount=0;
        int orecount=0;
        for(Resource r:board.getPlayerPlay().getResources()){if(r.getType().equals("grain")) graincount++;
            if(r.getType().equals("ore")) orecount++;}
        if(graincount>=2&&orecount>=3){
            if(board.getPlayerPlay().getBuildRoad()==false&&board.getPlayerPlay().getBuildVillage()==false){
                for(int i=0; i<board.getHexagons().size(); i++){
                    for(int k=0; k<board.getHexagons().get(i).getVertices().size(); k++){
                        if(board.getHexagons().get(i).getVertex(k).getVillage()){
                            for(Village v:board.getPlayerPlay().getVillages()){
                                if(v.index==i&&v.getVertex()==k){
                                    CityToDraw.add(new Point(hexMiddlePoints[i].x+fPoints[k].x, hexMiddlePoints[i].y+fPoints[k].y));
                                    CitysPlayersPlace.add(playerPlayNum(board.getPlayers()));
                                }
                            }
                        }
                    }
                }
                board.getPlayerPlay().setBuildCity(true);
                repaint();
            }
        }
    }
    private void addCity(Point point){
        for(int i=0; i<hexMiddlePoints.length; i++){
            for(int k=0; k<fPoints.length; k++){
                if(hexMiddlePoints[i].x+fPoints[k].x-10<=point.x&&hexMiddlePoints[i].x+fPoints[k].x+10>=point.x&&hexMiddlePoints[i].y+fPoints[k].y-10<=point.y&&hexMiddlePoints[i].y+fPoints[k].y+10>=point.y)
                {
                    CityToDraw.clear();
                    CitysPlayersPlace.clear();
                    addCityToDraw(new City(i, k, playerPlayNum(board.getPlayers())));
                    board.addCity(new City(i, k, playerPlayNum(board.getPlayers())));
                    System.out.println("hex: "+i+" vertex: "+k);
                }
            }
        }

        board.getPlayerPlay().setBuildCity(false);
        repaint();

    }
    private void useKnight(Point p){
        for(int i=0; i<hexMiddlePoints.length; i++){
            if(hexMiddlePoints[i].x-20<=p.x&&hexMiddlePoints[i].x>=p.x&&hexMiddlePoints[i].y+20<=p.y&&hexMiddlePoints[i].y+40>=p.y){
                devCard d=new devCard("knight");
                d.setKnight(i);
                board.getPlayerPlay().useDevCard(d);
                for(HexagonResource h:board.getHexagons()){
                    if(h.HasRobber()){
                        System.out.println("hexagon with robber: "+h.getIndex());
                    }
                }
                useKnight=false;
                for(int k=0; k<board.getPlayerPlay().playerDevCards.size(); k++){
                    System.out.println(k+" "+board.getPlayerPlay().playerDevCards.get(k).getType()+" "+board.getPlayerPlay().playerDevCards.get(k).getUsed());
                }
            }
        }
        DevCardMenu.repaint();
        GameFrame.repaint();
    }
    private void useRobber(Point p){
        for(int i=0; i<hexMiddlePoints.length; i++){
            if(hexMiddlePoints[i].x-20<=p.x&&hexMiddlePoints[i].x>=p.x&&hexMiddlePoints[i].y+20<=p.y&&hexMiddlePoints[i].y+40>=p.y){
                board.getPlayerPlay().useRobber(i);
                useRobber=false;
            }
        }
        GameFrame.repaint();
    }
private Resource ResourceSelect() {
    Resource ResSelected = new Resource("null");

    JDialog ResSelect = new JDialog((JFrame) null, "Select a resource", true); // Modal dialog
    ResSelect.setSize(400, 100);
    ResSelect.setLayout(new GridLayout(1, 5));

    List<JButton> ResButtons = new ArrayList<>();
    String[] resources = {"grain", "lumber", "wool", "brick", "ore"};

    for (String res : resources) {
        JButton button = new JButton(res);
        button.addActionListener(e -> {
            ResSelected.setType(res);
            ResSelect.dispose(); // Close the dialog after selecting
        });

        button.setBackground(getResourceColor(res));
        ResButtons.add(button);
        ResSelect.add(button);
    }

    ResSelect.setLocationRelativeTo(null);
    ResSelect.setVisible(true);

    return ResSelected;
}
  
    private void updateDevCardButtons() {
        UseDevCard.removeAll(); // Clear previous buttons
        int knightCount = 0,VictoryPointCount=0,monopolyCount=0, yearOfPlentyCount=0,roadBuildingCount=0;
        for (devCard card : board.getPlayerPlay().playerDevCards) {
            if (card.getType()=="knight" && !card.getUsed()) {
                knightCount++;
            }
        }
        if (knightCount > 0) {
            JButton KnightButton = createStyledButton("knight: " + knightCount);
            KnightButton.addActionListener(e -> {
                useKnight = true;
                updateDevCardButtons(); // Refresh knights after use
                GameFrame.repaint();
            });
            UseDevCard.add(KnightButton);}
        for (devCard card : board.getPlayerPlay().playerDevCards) {
            if (card.getType()=="victory point" && !card.getUsed()) {
                VictoryPointCount++;
            }
        }
        if (VictoryPointCount > 0) {
            JButton VictoryPointButton = createStyledButton("victory point: " + VictoryPointCount);
            VictoryPointButton.setPreferredSize(new Dimension(200, 40)); // Set width and height
            VictoryPointButton.addActionListener(e -> {
                board.getPlayerPlay().useDevCard(new devCard("victory point"));
                updateDevCardButtons(); // Refresh buttons after use
                GameFrame.repaint();
            });
            UseDevCard.add(VictoryPointButton);
        }
        for (devCard card : board.getPlayerPlay().playerDevCards) {
            if (card.getType()=="monopoly" && !card.getUsed()) {
                monopolyCount++;
            }
        }
        if (monopolyCount > 0) {
            JButton MonopolyButton = createStyledButton("monopoly: " + monopolyCount);
            MonopolyButton.setPreferredSize(new Dimension(150, 40)); // Set width and height
            MonopolyButton.addActionListener(e -> {
                devCard monopolyCard = new devCard("monopoly");
                monopolyCard.setResSteal(ResourceSelect());
                board.getPlayerPlay().useDevCard(monopolyCard);
                updateDevCardButtons();
                GameFrame.repaint();
            });
            UseDevCard.add(MonopolyButton);
        }
        for (devCard card : board.getPlayerPlay().playerDevCards) {
            if (card.getType()=="year of plenty" && !card.getUsed()) {
                yearOfPlentyCount++;
            }
        }
        if (yearOfPlentyCount > 0) {
            JButton yearOfPlentyButton = createStyledButton("year of plenty: " + yearOfPlentyCount);
            yearOfPlentyButton.setPreferredSize(new Dimension(180, 40)); // Set width and height
            yearOfPlentyButton.addActionListener(e -> {
                devCard d = new devCard("year of plenty");
                d.setPlentyResources(ResourceSelect(), ResourceSelect());
                board.getPlayerPlay().useDevCard(d);
                updateDevCardButtons();
                GameFrame.repaint();
            });
            UseDevCard.add(yearOfPlentyButton);
        }
        
        for (devCard card : board.getPlayerPlay().playerDevCards) {
            if (card.getType()=="road building" && !card.getUsed()) {
                roadBuildingCount++;
            }
        }
        if (roadBuildingCount > 0) {
            JButton roadBuildingButton = createStyledButton("road building: " + roadBuildingCount);
            roadBuildingButton.setPreferredSize(new Dimension(180, 40)); // Set width and height
            roadBuildingButton.addActionListener(e -> {
                board.getPlayerPlay().getPlayerDevCards().get(board.getPlayerPlay().getPlayerDevCards().indexOf(new devCard("road building"))).setUsed();
                useroadBuilding=true;
                BuildRoad();
                

                updateDevCardButtons();
                GameFrame.repaint();
            });
            UseDevCard.add(roadBuildingButton);
        }
        // Refresh UI
        UseDevCard.revalidate();
        UseDevCard.repaint();
    }
        
    private void setupUI() {
        hexMiddlePoints = new Point[board.getHexagons().size()];
        this.setBackground(new Color(14, 150, 212));
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
        JButton DevCardManagerButton = createStyledButton("Dev card");
        // Add buttons to the panel
        rightPanel.add(DevCardManagerButton);
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
                if(board.getPlayerPlay().getIp()==ownIp){
                    if(board.getPlayerPlay().getVillages().size()<2){
                        board.getPlayerPlay().setBuildVillage(true);
                        buildVillage();
                    }
                    else{
                        if(!board.getPlayerPlay().resources.contains(new Resource("grain"))){colorResourceAsRed(resourceCircles.get(0));}
                        if(!board.getPlayerPlay().resources.contains(new Resource("brick"))){colorResourceAsRed(resourceCircles.get(3));}
                        if(!board.getPlayerPlay().resources.contains(new Resource("lumber"))){colorResourceAsRed(resourceCircles.get(4));}
                        if(!board.getPlayerPlay().resources.contains(new Resource("wool"))){colorResourceAsRed(resourceCircles.get(1));}
                        else{
                            buildVillage();
                            System.out.println("player: "+playerPlayNum(board.getPlayers())+" villages: "+board.getPlayerPlay().villages.size());
                        }
                    }
            }
        }
        });
        rightPanel.add(resourcePanel);
        roadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {

                if(board.getPlayerPlay().getIp()==ownIp){
                if(!board.getPlayerPlay().resources.contains(new Resource("brick"))){colorResourceAsRed(resourceCircles.get(3));}
                if(!board.getPlayerPlay().resources.contains(new Resource("lumber"))){colorResourceAsRed(resourceCircles.get(4));}
                if(board.getPlayerPlay().resources.contains(new Resource("lumber"))&&board.getPlayerPlay().resources.contains(new Resource("brick"))){
                    BuildRoad();
                }
            }
            }
        });
        cityButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {

                if(board.getPlayerPlay().getIp()==ownIp){
                // Retrieve the current player's resources
                ArrayList<Resource> resources = board.getPlayerPlay().resources;
                
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
                else if (grainCount >= 2 && oreCount >= 3) {
                    buildCity();
                }
            }
            }
        });

        UseDevCard.setLayout(new FlowLayout()); // Make sure buttons are placed correctly

        DevCardManagerButton.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        // Clear previous buttons from UseDevCard
        UseDevCard.removeAll();
        
        if (!board.getPlayerPlay().resources.contains(new Resource("grain"))) {
            colorResourceAsRed(resourceCircles.get(0));
        }
        if (!board.getPlayerPlay().resources.contains(new Resource("ore"))) {
            colorResourceAsRed(resourceCircles.get(2));
        }
        if (!board.getPlayerPlay().resources.contains(new Resource("wool"))) {
            colorResourceAsRed(resourceCircles.get(1));
        }

        if (board.getPlayerPlay().resources.contains(new Resource("grain")) &&
            board.getPlayerPlay().resources.contains(new Resource("ore")) &&
            board.getPlayerPlay().resources.contains(new Resource("wool"))) {
            
            DevCardMenu.setSize(500, 300);
            DevCardMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            DevCardMenu.setLayout(null); 

            JButton buyDevCard = createStyledButton("Buy Dev Card");

            // Position UseDevCard panel
            UseDevCard.setBounds(180, 20, 150, 250);
            DevCardMenu.add(UseDevCard); // Add it once

            updateDevCardButtons(); // Check and update knights dynamically

            // Buy Dev Card Button
            buyDevCard.setBounds(20, 20, 150, 30);
            buyDevCard.addActionListener(e -> {
                board.buyDevCard(playerPlayNum(board.getPlayers())); 
                updateDevCardButtons(); // Refresh dev cards after buying
                GameFrame.repaint();
            });

            DevCardMenu.add(buyDevCard);
            DevCardMenu.setVisible(true);
            DevCardMenu.repaint();
        }
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
                    if(board.getPlayerPlay().getIp()==ownIp&&board.getPlayerPlay().getDiceTurned()==true&&board.getPlayerPlay().getBuildVillage()==false&&board.getPlayerPlay().getBuildRoad()==false){
                    
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
                    board.getPlayerPlay().setDiceTurned(false);
                    repaint();
                }
            }
        });
//
        cubesJLabel.setFont(new Font("Arial", Font.BOLD, 17));
        cubesJLabel.setForeground(Color.WHITE);
        cubesJLabel.setText("the sum of the dice: 0");
        JButton RollDice = createStyledButton("Roll Dice");
        RollDice.setPreferredSize(new Dimension(120, 40));
        RollDice.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Align to the bottom with padding
        rightPanel.add(Box.createVerticalGlue()); // Pushes the button to the bottom
        rightPanel.add(RollDice);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Set an action for the "End My Turn" button
        RollDice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(board.getPlayerPlay().getIp()==ownIp&&board.getPlayerPlay().getDiceTurned()==false&&board.getPlayerPlay().getBuildVillage()==false&&board.getPlayerPlay().getBuildRoad()==false){
                    sumDice=RandomDice();
                    board.setSumDice(sumDice);
                    cubesJLabel.setText("the sum of the dice:"+sumDice);
                    rightPanel.repaint();
                    board.getPlayerPlay().setDiceTurned(true);
                    if(sumDice==7){
                        useRobber=true;
                        GameFrame.repaint();
                    }
                    board.addResourceByDice(sumDice);
                    repaint();
                }
            }
        });
//
// Add a mouse listener to detect when a vertex is clicked to clear the color
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if(board.getPlayerPlay().getBuildVillage()) addVillage(evt.getPoint());
                else if(board.getPlayerPlay().getBuildRoad()) addRoad(evt.getPoint());
                else if(board.getPlayerPlay().getBuildCity()) addCity(evt.getPoint());
                else if(useKnight) useKnight(evt.getPoint());
                else if(useRobber) useRobber(evt.getPoint());
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
        DevCardManagerButton.setBounds(150, 140, 120, 45);
        rightPanel.add(DevCardManagerButton);
// End Turn Button
        endTurnButton.setBounds(140, 515, 150, 40); // Positioned at the bottom center
        rightPanel.add(endTurnButton);
// Roll a dice Button
        RollDice.setBounds(10, 515, 120, 40); // Positioned at the bottom center
        rightPanel.add(RollDice);

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
                    for (Resource res : board.getPlayerPlay().resources) {
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
        GameFrame.setLayout(new BorderLayout());
        GameFrame.add(this, BorderLayout.CENTER); // Add the hexagon drawing panel
        GameFrame.add(rightPanel, BorderLayout.EAST); // Add the right panel with buttons
        GameFrame.setSize(1000, 600);
        GameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameFrame.setVisible(true);
    }
    private void addVillageToDraw(Village village){
        Point vertex = new Point(hexMiddlePoints[village.index].x+fPoints[village.getVertex()].x, hexMiddlePoints[village.index].y+fPoints[village.getVertex()].y);
        villagesPlayersTOdraw.add(playerPlayNum(board.getPlayers()));
        villagesToDraw.add(vertex);
    }
    private void addCityToDraw(City city) {
        if (city.index >= hexMiddlePoints.length || city.vertex >= fPoints.length) {
            System.out.println("Invalid indices: city.index=" + city.index + ", city.vertex=" + city.vertex);
            return;
        }
        
        Point cityPoint = new Point(hexMiddlePoints[city.index].x + fPoints[city.vertex].x,
                                    hexMiddlePoints[city.index].y + fPoints[city.vertex].y);
        FinnalyPlaccedCity.add(cityPoint);
        
        if (villagesToDraw.contains(cityPoint)) {
            villagesToDraw.remove(cityPoint);
        } else {
            System.out.println("Warning: Point not found in villagesToDraw: " + cityPoint);
        }
        
        int playerNum = playerPlayNum(board.getPlayers());
        if (villagesPlayersTOdraw.contains(playerNum)) {
            villagesPlayersTOdraw.remove(Integer.valueOf(playerNum));
        } else {
            System.out.println("Warning: Player number not found in villagesPlayersTOdraw: " + playerNum);
        }
        
        CitysPlayersPlace.add(playerNum);
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
                if(board.getHexagons().get(k).getVertices().get(j).getCity()){
                    addCityToDraw(new City(k, j, playerPlayNum(board.getPlayers())));
                }
                if(board.getHexagons().get(k).getVertices().get(j).getRoad()){
                    if(j==5){if(board.getHexagons().get(k).getVertices().get(j).getRoad()&&board.getHexagons().get(k).getVertices().get(0).getRoad()){
                        drawRoad(new Road(k, j, 0));
                    }}
                    
                    else if(board.getHexagons().get(k).getVertices().get(j).getRoad()&&board.getHexagons().get(k).getVertices().get(j+1).getRoad()){
                        System.out.println("entger");
                        drawRoad(new Road(k, j, j+1));}
                }
            }
        }
        this.GameFrame.repaint();
    }
}