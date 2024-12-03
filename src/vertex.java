import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class vertex {
    private int index;
    private boolean village;
    private boolean city;
    
    HashMap<Integer, Integer> connectedHexagons = new HashMap<Integer, Integer>();
    private vertex vertexPoint;
    public vertex(int index) {
        this.index = index;
        this.village=false;
        this.city=false;
    }
    public int getIndex() {
        return index;
    }

    public boolean getCity() {
        return city;
    }

    public boolean getVillage() {
        return village;
    }
    public void setVillage(boolean village) {
        this.village = village;
    }

    public void setCity(boolean city) {
        this.city = city;
    }
    public void connectHexagon(int hexIndex, int vertexIndex) {
        connectedHexagons.put(hexIndex, vertexIndex);
    }

    public Map<Integer, Integer> getConnectedHexagons() {
        return connectedHexagons;
    }
}
