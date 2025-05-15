import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class vertex  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int index;
    private boolean village;
    private boolean city;
    private Boolean road=false;
    private List<Integer> CoonectedVertices=new ArrayList<>();
    
    private List<Integer> CoonectedHexagons=new ArrayList<>();
    public vertex(int index) {
        this.index = index;
        this.village=false;
        this.city=false;
    }
    public void setRoad(Boolean road) {
        this.road = road;
    }
    public List<Integer> getCoonectedVertices(){
        return CoonectedVertices;
    }    
    public List<Integer> getCoonectedHexagons(){
        return CoonectedHexagons;
    }
    public Boolean getRoad() {
        return road;
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
}
