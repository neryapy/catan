import java.util.HashSet;
import java.util.Set;

public class lineHexagon {
    private int index;
    private boolean road =false;
    private Set<Integer> connectedHexagons=new HashSet<>();
    public lineHexagon(int index){
        this.index=index;
    }
    public void setRoad(boolean road) {
        this.road = road;
    }
    public boolean isRoad() {
        return road;
    }
    public int getIndex() {
        return index;
    }
    public void connectHexagon(int hexIndex) {
        connectedHexagons.add(hexIndex);
    }
    public boolean hasRoad(){
        return road;
    }
    public Set<Integer> getConnectedHexagons() {
        return connectedHexagons;
    }

    public boolean isSharedWithHexagon(int hexIndex) {
        return connectedHexagons.contains(hexIndex);
    }
}
