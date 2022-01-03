package impGraph;

import api.GeoLocation;

/**
 * via GeoLocation interface
 * represent points in 3D plane
 * in this programme we gonna use only x and y cordinates
 */

public class Point3D implements GeoLocation {
    double _x, _y;

    /**
     * constructor
     * @param x - cord
     * @param y - cord
     */
    public Point3D(double x, double y) {
        this._x = x;
        this._y = y;
    }

    /**
     * deep copy
     * @param location - point location
     */
    public Point3D(GeoLocation location) {
        this._x = location.x();
        this._y = location.y();
    }
    // getters
    @Override
    public double x() {
        return this._x;
    }

    @Override
    public double y() {
        return this._y;
    }

    /**
     * cal distance from curr point to input location
     * @param g - point
     * @return - distance (double)
     */
    @Override
    public double distance(GeoLocation g) {
        double deltaX = this._x - g.x();
        double deltaY = this._y - g.y();
        return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    }
}
