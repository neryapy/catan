import java.io.Serializable;
import java.util.Objects;

public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;

    public Resource(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(type, resource.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
