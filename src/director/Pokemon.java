package director;

import api.GeoLocation;
import impGraph.Point3D;

public class Pokemon {
    double value;
    int type;
    GeoLocation pos;

    public Pokemon(double v, int t, GeoLocation p){
        this.value = v;
        this.type = t;
        this.pos = p;
    }

    public Pokemon(double v, int t, double x, double y){
        this.value = v;
        this.type = t;
        this.pos = new Point3D(x, y);
    }
    public Pokemon(){
        this.value = 0;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GeoLocation getPos() {
        return pos;
    }

    public void setPos(GeoLocation pos) {
        this.pos = pos;
    }
}
