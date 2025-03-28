public class Resource {
    private String type;
    public Resource(String type){
        this.type=type;
    }
    public String getType() {return type;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return type.equals(resource.type);
    }
    public void setType(String type) {
        this.type = type;
    }
    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
