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
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Ui extends JPanel{
    private JPanel UseDevCard = new JPanel();
    private static final int HEX_RADIUS = 50; // Radius of the getHexagons()
    private static final int X_SPACING = -15; // Spacing between getHexagons() horizontally
    private static final int Y_SPACING = 0;  // Spacing between rows of getHexagons()
    private static final int HEX_HEIGHT = 75; // Height of each hexagon
    private ArrayList<Point> centerPoints = new ArrayList<>();
    private int playerasigned=-1;
    private boolean useKnight = false;
    private boolean useRobber = false;
    private boolean useroadBuilding=false;
    private ArrayList<Point> OptionVillage = new ArrayList<>();
    private List<Line2D> roadLines=new ArrayList<>();
    private ArrayList<JPanel> resourceCircles = new ArrayList<>();
    private JPanel circlePanel;
    private int sumDice;
    private Point[] hexMiddlePoints;
    public Board board;
    private JLabel cubesJLabel = new JLabel();
    private JLabel playerPlayLabel;
    private JFrame GameFrame = new JFrame("CATAN");
    private JFrame DevCardMenu = new JFrame("Development Cards");
    private JFrame SendPropositionResFrame= new JFrame("Proposition sender");
    private JFrame GetPropositionResFrame= new JFrame("Proposition receiver");
    private JPanel usedDevCardPanel=new JPanel();
    private String ownIp="";
    private Village villageclicked;
    private List<Road> roadsDrawed= new ArrayList<>();
    private List<Point> CityToDraw=new ArrayList<>();
    private boolean HasHost=false;
    private ArrayList<Resource> getResourcesExchange=new ArrayList<>();
    private ArrayList<Resource> giveResourcesExchange=new ArrayList<>();
    private JPanel rightPanel = new JPanel();
    private List<JLabel> publicPointsLabels = new ArrayList<>();
    private List<JLabel> resourcesPlayerLabels = new ArrayList<>();
    private List<JLabel> unusedDevCardLabels = new ArrayList<>();
    private List<JLabel> usedDevCardLabels = new ArrayList<>();
    
    Point[] fPoints = {
        new Point(45, 25),
        new Point(0, 50),
        new Point(-45, 25),
        new Point(-45, -25), 
        new Point(0, -50),
        new Point(45, -25) ,
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
    public void setHasHost(boolean host){
        this.HasHost=host;
        if(host) sendState();
    }
    public int getPlayerAssigned(){
        return playerasigned;
    }
    public void AssignPlayer(int playerAssigned){
        this.playerasigned=playerAssigned;
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
            if(OptionVillage.size()>0){
                if(board.getPlayerPlay().getNumber()==0) g2d.setColor(new Color(255, 94, 0));
                else if(board.getPlayerPlay().getNumber()==1) g2d.setColor(new Color(169, 199, 156));
                else if(board.getPlayerPlay().getNumber()==2) g2d.setColor(new Color(207, 0, 0));
                else if(board.getPlayerPlay().getNumber()==3) g2d.setColor(new Color(21, 174, 171));
                
                g2d.setStroke(new BasicStroke(3)); // 3 is the thickness in pixels
                g2d.drawOval(OptionVillage.get(i).x - 5, OptionVillage.get(i).y - 5, 10, 10); // Draw vertex as a small circle
            }
        }}
        for(Player p:board.getPlayers()){
            
            if(p.getNumber()==0) g2d.setColor(new Color(255, 94, 0));
            else if(p.getNumber()==1) g2d.setColor(new Color(169, 199, 156));
            else if(p.getNumber()==2) g2d.setColor(new Color(207, 0, 0));
            else if(p.getNumber()==3) g2d.setColor(new Color(21, 174, 171));
            for(Village v:p.getVillages()) g2d.fillOval(convertVillageToPoint(v).x - 5, convertVillageToPoint(v).y - 5, 10, 10);
            for(City c:p.getCities()) g2d.fillRect(convertCityToPoint(c).x-10, convertCityToPoint(c).y-10, 20, 20);
            for(Road r:p.getRoads()) {
                System.out.println("road: "+r.getIndexHexagon()+" v1: "+r.getv1()+" v2: "+r.getv2());
                g2d.setStroke(new BasicStroke(6));
                g2d.drawLine((int) convertRoadToLine2D(r).getX1(), (int) convertRoadToLine2D(r).getY1(), (int) convertRoadToLine2D(r).getX2(), (int) convertRoadToLine2D(r).getY2());
            }
        }
        for (Line2D line:roadLines){
            if(board.getPlayerPlay().getNumber()==0) g2d.setColor(new Color(255, 94, 0));
            else if(board.getPlayerPlay().getNumber()==1) g2d.setColor(new Color(169, 199, 156));
            else if(board.getPlayerPlay().getNumber()==2) g2d.setColor(new Color(207, 0, 0));
            else if(board.getPlayerPlay().getNumber()==3) g2d.setColor(new Color(21, 174, 171));
            g2d.setStroke(new BasicStroke(6));
            g2d.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
        }    
        if(CityToDraw!=null){
            if(board.getPlayerPlay().getNumber()==0) g2d.setColor(new Color(255, 94, 0));
            else if(board.getPlayerPlay().getNumber()==1) g2d.setColor(new Color(169, 199, 156));
            else if(board.getPlayerPlay().getNumber()==2) g2d.setColor(new Color(207, 0, 0));
            else if(board.getPlayerPlay().getNumber()==3) g2d.setColor(new Color(21, 174, 171));
            for(Point ActualPoint:CityToDraw){
                g2d.setStroke(new BasicStroke(5)); // 3 is the thickness in pixels
                g2d.drawRect(ActualPoint.x - 10, ActualPoint.y - 10, 20, 20);
                g2d.setStroke(new BasicStroke(1)); // 3 is the thickness in pixels
            }
        }
        for(int k=0; k<board.getPlayerPlay().playerDevCards.size(); k++){if(useKnight&&board.getPlayerPlay().playerDevCards.get(k).getType()==("knight")&&!board.getPlayerPlay().playerDevCards.get(k).getUsed()){
            g2d.setColor(Color.BLACK);
            for(Point i:hexMiddlePoints) g2d.fillOval(i.x-10, i.y+20, 20, 20);
        }}
        if(useRobber){
            g2d.setColor(Color.BLACK);
            for(Point i:hexMiddlePoints) g2d.fillOval(i.x-10, i.y+20, 20, 20);
        }
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
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.translate(3, 3);
        g2d.fillPolygon(hexagon);
        g2d.translate(-3, -3);
    
        // Fill hexagon with gradient
        GradientPaint gradient = new GradientPaint(x, y - radius, color.brighter(), x, y + radius, color.darker());
        g2d.setPaint(gradient);
        g2d.fillPolygon(hexagon);
    
        // Draw hexagon border
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(hexagon);
    
        // Draw resource text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics metrics = g2d.getFontMetrics();
        int resourceWidth = metrics.stringWidth(resource);
        g2d.drawString(resource, x - resourceWidth / 2, y - 10);
        if (number == 8 || number == 6) g2d.setColor(Color.RED);
        String numberStr = Integer.toString(number);
        if(number == 0) numberStr = Integer.toString(7);
        int numberWidth = metrics.stringWidth(numberStr);
        g2d.drawString(numberStr, x - numberWidth / 2, y + 15);
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        String probabilityStr = getProbabilityString(number);
        int probWidth = metrics.stringWidth(probabilityStr);
        g2d.setColor(new Color(48, 93, 221));
        g2d.drawString(probabilityStr, x - probWidth / 2+32, y+15);
    }
    
    private String getProbabilityString(int number) {
        switch (number) {
            case 0: return "16.7%";
            case 2:
            case 12: return "2.8%";
            case 3:
            case 11: return "5.6%";
            case 4:
            case 10: return "8.3%";
            case 5:
            case 9:  return "11.1%";
            case 6:
            case 8:  return "13.9%";
            default: return "";
        }
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
        if(board.getPlayerPlay().getBuildRoad()==false&&board.getPlayerPlay().getBuildCity()==false&&board.getPlayerPlay().getBuildVillage()==false){
            // Loop through each hexagon and its vertices
            for (int hexIndex = 0; hexIndex < board.getHexagons().size(); hexIndex++) {
                Point hexCenter = centerPoints.get(hexIndex); // Get the center point of the hexagon
                // Get the vertices of the current hexagon
                for (int vertexIndex = 0; vertexIndex < 6; vertexIndex++) {
                        double angle = Math.toRadians(60 * vertexIndex) + Math.toRadians(90); // Calculate angle for each vertex
                        int px = (int) (hexCenter.x + HEX_RADIUS * Math.cos(angle));   // Calculate x based on angle
                        int py = (int) (hexCenter.y + HEX_RADIUS * Math.sin(angle));   // Calculate y based on angle
                        // Check if the vertex has a village
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
            if(hexMiddlePoints[hexClicked].x+60>=clickPoint.x&&hexMiddlePoints[hexClicked].x+40<=clickPoint.x&&hexMiddlePoints[hexClicked].y+35>=clickPoint.y&&hexMiddlePoints[hexClicked].y+15<=clickPoint.y) if(!board.getHexagons().get(hexClicked).getVertices().get(5).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(1).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(5).getCity()&&!board.getHexagons().get(hexClicked).getVertices().get(1).getCity()) vertexClicked=0;
            if(hexMiddlePoints[hexClicked].x+10>=clickPoint.x&&hexMiddlePoints[hexClicked].x-10<=clickPoint.x&&hexMiddlePoints[hexClicked].y+60>=clickPoint.y&&hexMiddlePoints[hexClicked].y+40<=clickPoint.y) if(!board.getHexagons().get(hexClicked).getVertices().get(0).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(2).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(0).getCity()&&!board.getHexagons().get(hexClicked).getVertices().get(2).getCity())vertexClicked=1;
            if(hexMiddlePoints[hexClicked].x-40>=clickPoint.x&&hexMiddlePoints[hexClicked].x-60<=clickPoint.x&&hexMiddlePoints[hexClicked].y+35>=clickPoint.y&&hexMiddlePoints[hexClicked].y+15<=clickPoint.y) if(!board.getHexagons().get(hexClicked).getVertices().get(1).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(3).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(1).getCity()&&!board.getHexagons().get(hexClicked).getVertices().get(3).getCity())vertexClicked=2;
            if(hexMiddlePoints[hexClicked].x-40>=clickPoint.x&&hexMiddlePoints[hexClicked].x-60<=clickPoint.x&&hexMiddlePoints[hexClicked].y-15>=clickPoint.y&&hexMiddlePoints[hexClicked].y-35<=clickPoint.y) if(!board.getHexagons().get(hexClicked).getVertices().get(2).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(4).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(2).getCity()&&!board.getHexagons().get(hexClicked).getVertices().get(4).getCity())vertexClicked=3;
            if(hexMiddlePoints[hexClicked].x+10>=clickPoint.x&&hexMiddlePoints[hexClicked].x-10<=clickPoint.x&&hexMiddlePoints[hexClicked].y-40>=clickPoint.y&&hexMiddlePoints[hexClicked].y-60<=clickPoint.y) if(!board.getHexagons().get(hexClicked).getVertices().get(3).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(5).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(3).getCity()&&!board.getHexagons().get(hexClicked).getVertices().get(5).getCity())vertexClicked=4;
            if(hexMiddlePoints[hexClicked].x+60>=clickPoint.x&&hexMiddlePoints[hexClicked].x+40<=clickPoint.x&&hexMiddlePoints[hexClicked].y-15>=clickPoint.y&&hexMiddlePoints[hexClicked].y-35<=clickPoint.y) if(!board.getHexagons().get(hexClicked).getVertices().get(4).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(0).getVillage()&&!board.getHexagons().get(hexClicked).getVertices().get(4).getCity()&&!board.getHexagons().get(hexClicked).getVertices().get(0).getCity())vertexClicked=5;

            if(hexClicked!=-1&&vertexClicked!=-1){
                villageclicked=new Village(hexClicked, vertexClicked,playerPlayNum(board.getPlayers()));
                board.addVillage(villageclicked);
            }
            board.getPlayerPlay().setBuildVillage(false);
            
            UpdatePlayerStatus();
            GameFrame.repaint();
            sendState();
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
                    //simple add roads
                    if(board.getHexagons().get(i).vertices.get(k).getVillage()||board.getHexagons().get(i).vertices.get((k+1)%6).getVillage()||board.getHexagons().get(i).vertices.get(k).getCity()||board.getHexagons().get(i).vertices.get((k+1)%6).getCity()){
                        roadLines.add(new Line2D.Float(
                            hexMiddlePoints[i].x + fPoints[k].x, hexMiddlePoints[i].y + fPoints[k].y,
                            hexMiddlePoints[i].x + fPoints[(k + 1) % 6].x, hexMiddlePoints[i].y + fPoints[(k + 1) % 6].y
                        ));
                        roadsDrawed.add(new Road(i, k, (k + 1) % 6, playerPlayNum(board.getPlayers())));
                        board.getPlayerPlay().setBuildRoad(true);
                    }//hard add roads

                }
            }
            System.out.println(board.getPlayerPlay().getVillages().size());
            for (Village v : board.getPlayerPlay().getVillages()) {
                for(int hi=0; hi<board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedHexagons().size(); hi++){
                    roadLines.add(new Line2D.Float(

                        hexMiddlePoints[board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedHexagons().get(hi)].x + fPoints[board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedVertices().get(hi)].x, hexMiddlePoints[board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedHexagons().get(hi)].y + fPoints[board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedVertices().get(hi)].y,
                        hexMiddlePoints[board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedHexagons().get(hi)].x + fPoints[(board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedVertices().get(hi)+1)%6].x, hexMiddlePoints[board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedHexagons().get(hi)].y + fPoints[(board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedVertices().get(hi)+1)%6].y
                    ));
                    roadsDrawed.add(new Road(board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedHexagons().get(hi), board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedVertices().get(hi), (board.getHexagons().get(v.getIndex()).getVertex(v.getVertex()).getCoonectedVertices().get(hi)+1)%6, playerPlayNum(board.getPlayers())));
                }
            }
            System.out.println("cd : "+board.getPlayerPlay().getRoads().size());
            for(Road r:board.getPlayerPlay().getRoads()){
                System.out.println("roadg: "+r.getIndexHexagon()+" v1: "+r.getv2()+" v2: "+(r.getv2()+1)%6);
                roadLines.add(new Line2D.Float(
                    hexMiddlePoints[r.getIndexHexagon()].x + fPoints[r.getv2()].x, hexMiddlePoints[r.getIndexHexagon()].y + fPoints[r.getv2()].y,
                    hexMiddlePoints[r.getIndexHexagon()].x + fPoints[(r.getv2() + 1) % 6].x, hexMiddlePoints[r.getIndexHexagon()].y + fPoints[(r.getv2() + 1) % 6].y
                ));
                roadsDrawed.add(new Road(r.getIndexHexagon(), r.getv2(), (r.getv2() + 1) % 6, playerPlayNum(board.getPlayers())));
                board.getPlayerPlay().setBuildRoad(true);
                for(int hi=0; hi<board.getHexagons().get(r.getIndexHexagon()).getVertex(r.getv2()).getCoonectedHexagons().size(); hi++){
                    int rhi=board.getHexagons().get(r.getIndexHexagon()).getVertex(r.getv2()).getCoonectedHexagons().get(hi);
                    int rvhi=board.getHexagons().get(r.getIndexHexagon()).getVertex(r.getv2()).getCoonectedVertices().get(hi);
                    roadLines.add(new Line2D.Float(
                    hexMiddlePoints[rhi].x + fPoints[rvhi].x, hexMiddlePoints[rhi].y + fPoints[rvhi].y,
                    hexMiddlePoints[rhi].x + fPoints[(rvhi + 1) % 6].x, hexMiddlePoints[rhi].y + fPoints[(rvhi + 1) % 6].y
                ));
                System.out.println("roadgdfs: "+rhi+" v1: "+rvhi+" v2: "+(rvhi+1)%6);
                roadsDrawed.add(new Road(rhi, rvhi, (rvhi + 1) % 6, playerPlayNum(board.getPlayers())));
                board.getPlayerPlay().setBuildRoad(true);
                }
            }
            GameFrame.repaint();
            sendState();
        }

    }
    private Point convertVillageToPoint(Village v){return new Point(hexMiddlePoints[v.index].x+fPoints[v.getVertex()].x, hexMiddlePoints[v.index].y+fPoints[v.getVertex()].y);}
    private Point convertCityToPoint(City v){return new Point(hexMiddlePoints[v.index].x+fPoints[v.getVertex()].x, hexMiddlePoints[v.index].y+fPoints[v.getVertex()].y);}
    private void addRoad(Point p) {
        for (int i = 0; i < roadLines.size(); i++) {
            if (roadLines.get(i).ptSegDist(p) < 7) {
                for(Player g:board.getPlayers()) for(Road r:g.getRoads()) if(r.getIndexHexagon()==roadsDrawed.get(i).getIndexHexagon()&&r.getv1()==roadsDrawed.get(i).getv1()&&r.getv2()==roadsDrawed.get(i).getv2()) return;
                board.addRoad(roadsDrawed.get(i));
                break; // stop after first match
            }
        }
        board.getPlayerPlay().resources.remove(new Resource("brick"));
        board.getPlayerPlay().resources.remove(new Resource("lumber"));
        roadsDrawed.clear();
        roadLines.clear();
        rightPanel.repaint();
        repaint();
        board.getPlayerPlay().setBuildRoad(false);
        if (useroadBuilding) {
            BuildRoad();
            useroadBuilding = false;
        }
        sendState();
    }
    
    private Line2D convertRoadToLine2D(Road road) {
        return new Line2D.Float(
            hexMiddlePoints[road.getIndexHexagon()].x + fPoints[road.getv1()].x, hexMiddlePoints[road.getIndexHexagon()].y + fPoints[road.getv1()].y,
            hexMiddlePoints[road.getIndexHexagon()].x + fPoints[road.getv2()].x, hexMiddlePoints[road.getIndexHexagon()].y + fPoints[road.getv2()].y
        );
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
                    board.addCity(new City(i, k, playerPlayNum(board.getPlayers())));
                    System.out.println("hex: "+i+" vertex: "+k);
                    board.getPlayerPlay().setBuildCity(false);
                    rightPanel.repaint();
                    repaint();
                    sendState();
                    return;
                }
            }
        }

        board.getPlayerPlay().setBuildCity(false);
        repaint();
        sendState();

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
        sendState();
    }
    private void useRobber(Point p){
        System.out.println("robber was clicked");
        for(int i=0; i<hexMiddlePoints.length; i++){
            if(hexMiddlePoints[i].x-20<=p.x&&hexMiddlePoints[i].x>=p.x&&hexMiddlePoints[i].y+20<=p.y&&hexMiddlePoints[i].y+40>=p.y){
                board.getPlayerPlay().useRobber(i);
                useRobber=false;
            }
        }
        GameFrame.repaint();
        sendState();
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
        usedDevCardPanel.removeAll(); // Clear previous used dev card panel
        usedDevCardPanel.add(new JLabel("used: ")); // Add an empty label to avoid layout issues
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
                sendState();
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
                sendState();
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
                sendState();
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
                sendState();
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
        int k=0;
        for(int i=0; i<board.getPlayerPlay().playerDevCards.size(); i++){ if(board.getPlayerPlay().playerDevCards.get(i).getUsed()&&board.getPlayerPlay().playerDevCards.get(i).getType()=="knight"){k++;}}
        usedDevCardPanel.setBounds(350, 10, 200, 100);
        usedDevCardPanel.setLayout(new BoxLayout(usedDevCardPanel, BoxLayout.Y_AXIS));
        usedDevCardPanel.add(new JLabel("knight: " + k));
        k=0;
        for(int i=0; i<board.getPlayerPlay().playerDevCards.size(); i++){ if(board.getPlayerPlay().playerDevCards.get(i).getUsed()&&board.getPlayerPlay().playerDevCards.get(i).getType()=="victory point"){k++;}}
        usedDevCardPanel.add(new JLabel("victory point: " + k));
        k=0;
        for(int i=0; i<board.getPlayerPlay().playerDevCards.size(); i++){ if(board.getPlayerPlay().playerDevCards.get(i).getUsed()&&board.getPlayerPlay().playerDevCards.get(i).getType()=="monopoly"){k++;}}
        usedDevCardPanel.add(new JLabel("monopoly: " + k));
        k=0;
        for(int i=0; i<board.getPlayerPlay().playerDevCards.size(); i++){ if(board.getPlayerPlay().playerDevCards.get(i).getUsed()&&board.getPlayerPlay().playerDevCards.get(i).getType()=="year of plenty"){k++;}}
        usedDevCardPanel.add(new JLabel("year of plenty: " + k));
        // Refresh UI
        UseDevCard.revalidate();
        UseDevCard.repaint();
        sendState();
    }
    public void getProposition(ArrayList<Resource> giveResources, ArrayList<Resource> getResources) {
        GetPropositionResFrame= new JFrame("Proposition receiver");
        List<String> AllResources = new ArrayList<>();
        AllResources.add("grain");
        AllResources.add("lumber");
        AllResources.add("wool");
        AllResources.add("brick");
        AllResources.add("ore");
        Map<String, Integer> giveCounts = new HashMap<>();
        Map<String, Integer> getCounts = new HashMap<>();
            
        for (String type : AllResources) {
            giveCounts.put(type, 0);
            getCounts.put(type, 0);
        }
        
        for (Resource r : giveResources) {
            giveCounts.put(r.getType(), giveCounts.get(r.getType()) + 1);
        }
        for (Resource r : getResources) {
            getCounts.put(r.getType(), getCounts.get(r.getType()) + 1);
        }
        JPanel getResPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < AllResources.size(); i++) {
                    String type = AllResources.get(i);
                    int count = getCounts.getOrDefault(type, 0);
        
                    g.setColor(getResourceColor(type));
                    int x = 30 + i * 90;
                    int y = 0;
                    g.fillOval(x, y, 60, 60);
        
                    // Draw count in the center
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 25));
                    FontMetrics fm = g.getFontMetrics();
                    String text = String.valueOf(count);
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent();
                    g.drawString(text, x + (60 - textWidth) / 2, y + (60 + textHeight) / 2 - 4);
                }
            }
        };
        
        JPanel giveResPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < AllResources.size(); i++) {
                    String type = AllResources.get(i);
                    int count = giveCounts.getOrDefault(type, 0);
        
                    g.setColor(getResourceColor(type));
                    int x = 30 + i * 90;
                    int y = 0;
                    g.fillOval(x, y, 60, 60);
        
                    // Draw count in the center
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 25));
                    FontMetrics fm = g.getFontMetrics();
                    String text = String.valueOf(count);
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent();
                    g.drawString(text, x + (60 - textWidth) / 2, y + (60 + textHeight) / 2 - 4);
                }
            }
        };
        JLabel getResLabel = new JLabel("Get resources:");
        JLabel giveResLabel = new JLabel("Give resources:");
        getResLabel.setFont(new Font("Arial", Font.BOLD, 20));
        giveResLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JButton acceptButton = createStyledButton("accept"); 
        acceptButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sendResponseExchange(true);
                GetPropositionResFrame.dispose();
                updateAll(board);
            }});
        JButton declineButton = createStyledButton("decline");
        declineButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sendResponseExchange(false);
                GetPropositionResFrame.dispose();

            }});
        int frameWidth = 480;
        int labelWidth = 180;
        int centerX = (frameWidth - labelWidth) / 2;
        getResLabel.setBounds(centerX, 100, labelWidth, 30);
        giveResLabel.setBounds(centerX, 0, labelWidth, 30);
        GetPropositionResFrame.setLayout(null);
        GetPropositionResFrame.setSize(480, 270);
        GetPropositionResFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        giveResPanel.setBounds(0, 30, 480, 60);
        getResPanel.setBounds(0, 130, 480, 60);
        acceptButton.setBounds(GetPropositionResFrame.getWidth()/2+5, 195, 100, 30);
        declineButton.setBounds(GetPropositionResFrame.getWidth()/2-105, 195, 100, 30);
        GetPropositionResFrame.add(declineButton);
        GetPropositionResFrame.add(acceptButton);
        GetPropositionResFrame.add(giveResPanel);
        GetPropositionResFrame.add(getResPanel);
        GetPropositionResFrame.add(giveResLabel);
        GetPropositionResFrame.add(getResLabel);
        GetPropositionResFrame.repaint();
        GetPropositionResFrame.setVisible(true);
        sendState();
    }
    private void SendProposition(ArrayList<Resource> getResources, ArrayList<Resource> givResources) {
        for (String host : board.getHostAddresses()) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, board.getPort() + 1), 15); // 5-second timeout

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                // Package the proposition into a list of two ArrayLists
                ArrayList<ArrayList<Resource>> proposal = new ArrayList<>();
                proposal.add(getResources); // What I want
                proposal.add(givResources); // What I give

                giveResourcesExchange = givResources;
                getResourcesExchange = getResources;
                oos.writeObject(proposal);
                oos.flush();
                oos.close();
            } catch (Exception e) {
                System.err.println("Failed to send to host: " + host);
                e.printStackTrace();
            }
            
        }
        if(!HasHost){
            for (String host : board.getHostAddresses()) {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, board.getPort() + 2), 2000); // 5-second timeout
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    // Package the proposition into a list of two ArrayLists
                    ArrayList<ArrayList<Resource>> proposal = new ArrayList<>();
                    proposal.add(getResources); // What I want
                    proposal.add(givResources); // What I give

                    oos.writeObject(proposal);
                    oos.flush();

                    System.out.println("Sent proposition to " + host);

                    oos.close();
                } catch (Exception e) {
                    System.err.println("Failed to send to host: " + host);
                    e.printStackTrace();
                }
            }
        }
    
    }
    private void sendResponseExchange(boolean response) {
        for(int i=0; i<board.getPlayers().size(); i++){
            System.out.println(board.getIpByPlayer(i));
        }
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(board.getIpByPlayer(0), board.getPort() + 3), 2000); // 5-second timeout

            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                oos.writeBoolean(response);
                oos.flush();
                System.out.println("boolean sent to " + board.getIpByPlayer(0));
            }

        } catch (Exception e) {
            System.err.println("Failed to send to host: " + board.getIpByPlayer(0));
            e.printStackTrace();
        }
    }
    public void getReponseExchange(boolean response, String ip) {
        System.out.println("response: "+response+" ip: "+ip+" "+(response&&giveResourcesExchange!=null&&getResourcesExchange!=null));
        System.out.println("ip: "+board.getIpByPlayer(1));
        System.out.println(board.getPlayers().get(1).getIp()==ip);
        if (response&&giveResourcesExchange!=null&&getResourcesExchange!=null) {
            for (int i= 0; i < board.getPlayers().size(); i++) {
                if (board.getIpByPlayer(i).equals(ip)) {
                    board.exchangeResources(board.getPlayers().get(i), board.getPlayerPlay(), giveResourcesExchange, getResourcesExchange);
                    GameFrame.repaint();
                }
            }
        }
        else{
            giveResourcesExchange.clear();
            getResourcesExchange.clear();
        }
    }
    public void sendState(){
        UpdatePlayerStatus();
        System.out.println("ownIp:"+ownIp);
        for (Player p : board.getPlayers()) {
            if (!p.getIp().equals(ownIp)) {
                System.out.println("send state to: "+p.getIp());
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(p.getIp(), board.getPort()), 500); // 5-second timeout
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(board);
                    oos.flush();
                    oos.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
    }
    }
    private void DrawStatusPlayers(int NumberOfPlayers){
        publicPointsLabels.clear(); // Make sure no duplicates if this is ever called again
    
        for(int i=0; i<NumberOfPlayers; i++){
            JPanel playerPanel = new JPanel();
            if(i==0) playerPanel.setBounds(rightPanel.getX()+20, rightPanel.getY()+250, 120,100);
            if(i==1) playerPanel.setBounds(rightPanel.getX()+150, rightPanel.getY()+250, 120,100);
            if(i==2) playerPanel.setBounds(rightPanel.getX()+20, rightPanel.getY()+360, 120,100);
            if(i==3) playerPanel.setBounds(rightPanel.getX()+150, rightPanel.getY()+360, 120,100);
    
            JLabel playerName = new JLabel("Player: "+(i+1));
            playerName.setFont(new Font("Arial", Font.BOLD, 22));
            playerName.setForeground(Color.WHITE);
    
            JLabel PublicPoints = new JLabel("Public points: "+board.getPlayers().get(i).getPublicPoints());
            PublicPoints.setFont(new Font("Arial", Font.BOLD, 16));
            PublicPoints.setForeground(Color.WHITE);
    
            publicPointsLabels.add(PublicPoints); // Save reference to label for future updates
    
            JLabel resourcesPlayerLabel = new JLabel("Resources: "+board.getPlayers().get(i).getResources().size());
            resourcesPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            resourcesPlayerLabel.setForeground(Color.WHITE);
    
            resourcesPlayerLabels.add(resourcesPlayerLabel); // Save reference to label for future updates
            int UnusedDevcardCount=0;
            int UsedDevcardCount=0;
            for(devCard d:board.getPlayers().get(i).playerDevCards){
                if(!d.getUsed()) UnusedDevcardCount++;
                if(d.getUsed()) UsedDevcardCount++;
            }
            JLabel UnusedDevcard = new JLabel("unused dev-card: "+UnusedDevcardCount);
            UnusedDevcard.setFont(new Font("Arial", Font.BOLD, 12));
            UnusedDevcard.setForeground(Color.WHITE);
            JLabel usedDevcard = new JLabel("used dev-card: "+UsedDevcardCount);
            usedDevcard.setFont(new Font("Arial", Font.BOLD, 12));
            usedDevcard.setForeground(Color.WHITE);
            unusedDevCardLabels.add(UnusedDevcard);
            usedDevCardLabels.add(usedDevcard);
            
            playerPanel.add(playerName);
            playerPanel.add(PublicPoints);
            playerPanel.add(resourcesPlayerLabel);
            
            playerPanel.add(UnusedDevcard);
            playerPanel.add(usedDevcard);
            playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
            playerPanel.setBackground(new Color(60, 63, 65));
    
            rightPanel.add(playerPanel);
        }
    
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    private void UpdatePlayerStatus() {
        int playerCount = board.getPlayers().size();
    
        int safePublicCount = Math.min(publicPointsLabels.size(), playerCount);
        for (int i = 0; i < safePublicCount; i++) {
            publicPointsLabels.get(i).setText("Public points: " + board.getPlayers().get(i).getPublicPoints());
        }
    
        int safeResourcesCount = Math.min(resourcesPlayerLabels.size(), playerCount);
        for (int i = 0; i < safeResourcesCount; i++) {
            resourcesPlayerLabels.get(i).setText("Resources: " + board.getPlayers().get(i).getResources().size());
        }
    
        int safeUnusedDevCount = Math.min(unusedDevCardLabels.size(), playerCount);
        int safeUsedDevCount = Math.min(usedDevCardLabels.size(), playerCount);
        for (int i = 0; i < Math.min(safeUnusedDevCount, safeUsedDevCount); i++) {
            int unusedCount = 0;
            int usedCount = 0;
            for (devCard d : board.getPlayers().get(i).playerDevCards) {
                if (!d.getUsed()) unusedCount++;
                if (d.getUsed()) usedCount++;
            }
            unusedDevCardLabels.get(i).setText("unused dev-card: " + unusedCount);
            usedDevCardLabels.get(i).setText("used dev-card: " + usedCount);
        }
    }
    
    private void setupUI() {
        hexMiddlePoints = new Point[board.getHexagons().size()];
        this.setBackground(new Color(14, 150, 212));
        this.setLayout(null);
        // Create a panel for the right-side menu with a background color
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 600));
        rightPanel.setBackground(new Color(60, 63, 65));  // Dark background color for the panel

        // Create the "BUILD" label with styling
        JLabel buildLabel = new JLabel("your resources:");
        playerPlayLabel = new JLabel("Player play: " + (playerPlayNum(board.getPlayers())+1));
        playerPlayLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerPlayLabel.setForeground(Color.WHITE);
        buildLabel.setFont(new Font("Arial", Font.BOLD, 18));
        buildLabel.setForeground(Color.WHITE);
        buildLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(playerPlayLabel);
        rightPanel.add(buildLabel);
        DrawStatusPlayers(board.getPlayers().size());
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        // Create beautiful buttons with rounded corners and hover effects
        JButton villageButton = createStyledButton("Village");
        JButton cityButton = createStyledButton("City");
        JButton roadButton = createStyledButton("Road");
        JButton DevCardManagerButton = createStyledButton("Dev card");
        JButton ExchangeResButton= createStyledButton("Exchange resources");
        // Add buttons to the panel
        rightPanel.add(DevCardManagerButton);
        rightPanel.add(villageButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(cityButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(roadButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(ExchangeResButton);
        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Horizontal alignment with padding
        resourcePanel.setBounds(20, 43, 250, 50); // Position below the "Road" button
        resourcePanel.setBackground(new Color(60, 63, 65)); // Match the background color of the right panel

// Add the resource panel to the rightPanel
        rightPanel.add(resourcePanel);
        villageButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                
                System.out.println("own ip:"+board.getPlayerPlay().getIp().equals(ownIp));
                System.out.println("IP:"+board.getPlayerPlay().getIp()+"own ip:"+ownIp);
                if(board.getPlayerPlay().getIp().equals(ownIp)){
                        System.out.println("village button pressed");
                        if(!board.getPlayerPlay().resources.contains(new Resource("grain"))){colorResourceAsRed(resourceCircles.get(0));}
                        if(!board.getPlayerPlay().resources.contains(new Resource("brick"))){colorResourceAsRed(resourceCircles.get(3));}
                        if(!board.getPlayerPlay().resources.contains(new Resource("lumber"))){colorResourceAsRed(resourceCircles.get(4));}
                        if(!board.getPlayerPlay().resources.contains(new Resource("wool"))){colorResourceAsRed(resourceCircles.get(1));}
                        else{
                            System.out.println("build village");
                            buildVillage();
                        }
            }
        }
        });
        rightPanel.add(resourcePanel);
        roadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {

                if(board.getPlayerPlay().getIp().equals(ownIp)){
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

                if(board.getPlayerPlay().getIp().equals(ownIp)){
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
        DevCardMenu.revalidate();
        DevCardMenu.repaint();
        
        // Clear previous buttons from UseDevCard
        UseDevCard.removeAll();
            
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
                sendState();
            });
            DevCardMenu.add(usedDevCardPanel); // Add it once
            DevCardMenu.add(buyDevCard);
            DevCardMenu.setVisible(true);
            DevCardMenu.repaint();
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
                    System.out.println("own"+ownIp);
                    if(board.getPlayerPlay().getIp().equals(ownIp)&&board.getPlayerPlay().getDiceTurned()==true&&board.getPlayerPlay().getBuildVillage()==false&&board.getPlayerPlay().getBuildRoad()==false){
                    
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
                    sendState();
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
                if(board.getPlayerPlay().getIp().equals(ownIp)&&board.getPlayerPlay().getDiceTurned()==false&&board.getPlayerPlay().getBuildVillage()==false&&board.getPlayerPlay().getBuildRoad()==false){
                    //sumDice=RandomDice();
                    sumDice=8;
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
                    sendState();
                }
            }
        });
        ExchangeResButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                System.out.println("Exchange resources button pressed");
                if(board.getPlayerPlay().getIp().equals(ownIp)||true){
                    
                    List<String> AllResources = new ArrayList<>();
                    AllResources.add("grain");
                    AllResources.add("lumber");
                    AllResources.add("wool");
                    AllResources.add("brick");
                    AllResources.add("ore");
                    SendPropositionResFrame=new JFrame("Send resources proposition");
                    SendPropositionResFrame.setSize(480, 270);
                    SendPropositionResFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    List<JComboBox<Integer>> ComboBoxesGet = new ArrayList<>();
                    List<JComboBox<Integer>> ComboBoxesGive = new ArrayList<>(); 
                    SendPropositionResFrame.setVisible(true);
                    JPanel getResPanel = new JPanel(null) {  // Use null layout for absolute positioning
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                        
                            // Example: draw a circle for each resource
                            for (int i = 0; i < AllResources.size(); i++) {
                                // You can modify this to get a color based on the resource
                                g.setColor(getResourceColor(AllResources.get(i)));
                                int x = 30 + i * 90; // Horizontal spacing
                                int y = 30;
                                g.fillOval(x, y, 60, 60);
                            
                                // If this is the first time, create a combo box for the current circle
                                if (ComboBoxesGet.size() <= i) {
                                    JComboBox<Integer> numberComboBox = new JComboBox<>();
                                    for (int num = 0; num <= 10; num++) {
                                        numberComboBox.addItem(num); // Populate the combo box with numbers 0 to 10
                                    }
                                    numberComboBox.setBounds(x + 10, y + 15, 40, 30); // Position inside the circle
                                    numberComboBox.setSelectedIndex(0); // Set the default value to 0
                                
                                    // Add the combo box to the panel and the list
                                    this.add(numberComboBox);
                                    ComboBoxesGet.add(numberComboBox);
                                
                                    // ActionListener to capture the selected number
                                    numberComboBox.addActionListener(e -> {
                                        int selectedNumber = (int) numberComboBox.getSelectedItem();
                                        System.out.println("Selected number: " + selectedNumber);
                                        // You can store the selected number as needed
                                    });
                                }
                            }
                        }
                    };
                    
                    JPanel giveResPanel = new JPanel(null) {  // Use null layout for absolute positioning
                            @Override
                            protected void paintComponent(Graphics g) {
                                super.paintComponent(g);
                            
                                // Example: draw a circle for each resource
                                for (int i = 0; i < AllResources.size(); i++) {
                                    // You can modify this to get a color based on the resource
                                    g.setColor(getResourceColor(AllResources.get(i)));
                                    int x = 30 + i * 90; // Horizontal spacing
                                    int y = 30;
                                    g.fillOval(x, y, 60, 60);
                                
                                    // If this is the first time, create a combo box for the current circle
                                    if (ComboBoxesGive.size() <= i) {
                                        JComboBox<Integer> numberComboBox = new JComboBox<>();
                                        for (int num = 0; num <= 10; num++) {
                                            numberComboBox.addItem(num); // Populate the combo box with numbers 0 to 10
                                        }
                                        numberComboBox.setBounds(x + 10, y + 15, 40, 30); // Position inside the circle
                                        numberComboBox.setSelectedIndex(0); // Set the default value to 0
                                    
                                        // Add the combo box to the panel and the list
                                        this.add(numberComboBox);
                                        ComboBoxesGive.add(numberComboBox);
                                    
                                        // ActionListener to capture the selected number
                                        numberComboBox.addActionListener(e -> {
                                            int selectedNumber = (int) numberComboBox.getSelectedItem();
                                            System.out.println("Selected number: " + selectedNumber);
                                            // You can store the selected number as needed
                                        });
                                    }
                                }
                            }
                        };
                    JButton SendPropotition = createStyledButton("send proposition");
                    SendPropotition.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            ArrayList<Resource> getResources = new ArrayList<>(), giveResources = new ArrayList<>();
                            String[] resourceTypes = {"grain", "lumber", "wool", "brick", "ore"};
                            for (int i = 0; i < ComboBoxesGive.size(); i++) {
                                int amount = (int) ComboBoxesGive.get(i).getSelectedItem();
                                for (int j = 0; j < amount; j++) giveResources.add(new Resource(resourceTypes[i]));}
                            for (int i = 0; i < ComboBoxesGet.size(); i++) {
                                int amount = (int) ComboBoxesGet.get(i).getSelectedItem();
                                for (int j = 0; j < amount; j++) getResources.add(new Resource(resourceTypes[i]));}
                            SendProposition(getResources, giveResources);
                        }
                    });
                    JLabel getResLabel = new JLabel("Get resources:");
                    JLabel giveResLabel = new JLabel("Give resources:");
                    getResLabel.setFont(new Font("Arial", Font.BOLD, 20));
                    giveResLabel.setFont(new Font("Arial", Font.BOLD, 20));
                    getResLabel.setBounds(SendPropositionResFrame.getWidth()/2-90, 100, 180, 30);
                    giveResLabel.setBounds(SendPropositionResFrame.getWidth()/2-90, 0, 180, 30);
                    getResPanel.setBounds(0, 100, 470, 90);
                    giveResPanel.setBounds(10, 70, 200, 50);
                    SendPropotition.setBounds(SendPropositionResFrame.getWidth()/2-100, 195, 200, 30);
                    SendPropositionResFrame.add(getResLabel);
                    SendPropositionResFrame.add(giveResLabel);
                    SendPropositionResFrame.add(SendPropotition);
                    SendPropositionResFrame.add(getResPanel);
                    SendPropositionResFrame.add(giveResPanel);
                }
            
            }
        });

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
        buildLabel.setBounds(90, 20, 150, 40); // Position "BUILD" label at the top center
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
// Exchange resources button
        ExchangeResButton.setBounds(20, 200, 250, 45); // Positioned below "Road" button
        rightPanel.add(ExchangeResButton);
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
                    while(ownIp==""){
                        try {
                            Thread.sleep(1); // 200 milliseconds delay
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int PlayerAsigned = -1;

                    for (Player p : board.getPlayers()) {
                        if (ownIp.equals(p.getIp())) {
                            PlayerAsigned = p.getNumber();
                        }
                    }

                    if (PlayerAsigned != -1) {
                        for (Resource res : board.getPlayers().get(PlayerAsigned).resources) {
                            if (res.getType().equalsIgnoreCase(finalResourceType)) {
                                resourceCount++;
                            }
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
        if(!GameFrame.isVisible()) GameFrame.setVisible(true);
    }
    public void updateAll(Board board){
        
        this.board=board;
        for(int i=0; i<board.getPlayers().size(); i++){
            if(board.getPlayers().get(i).getIp()==ownIp){
                ownIp=board.getPlayers().get(i).getIp();
            }
            System.out.println("player ip: "+board.getPlayers().get(i).getIp());
        }
        sumDice=board.getSumDice();
        playerPlayLabel.setText("Player play: " + (playerPlayNum(board.getPlayers())+1));
        cubesJLabel.setText("the sum of the dice: "+sumDice);
        UpdatePlayerStatus();
        rightPanel.repaint();
        GameFrame.repaint();
    }
}