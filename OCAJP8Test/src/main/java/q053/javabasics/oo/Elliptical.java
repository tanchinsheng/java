package q053.javabasics.oo;

class Elliptical {

    public int radiusA, radiusB;
    public int sum = 100;

    public void setRadius(int r) {
        if (r > 99) {
            throw new IllegalArgumentException();
        }
        radiusA = r;
        radiusB = sum - radiusA;

    }
}
