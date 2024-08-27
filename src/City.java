public class City {
    private int locationRes;
    private int locationVertex;

    public City(int locationRes, int locationVertex) {
        this.locationRes = locationRes;
        this.locationVertex = locationVertex;
    }
    public void printVertex() {
        System.out.println("City at resource index: " + locationRes + ", vertex: " + locationVertex);
    }
}
