package com.outcast.rpg.util.physics.raycast;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class Ray {

    public final Location origin, end;
    public final Vector direction;
    public final double length;

    public Ray(Location origin, Vector direction, double length) {
        this.origin = origin;
        this.direction = direction.normalize();
        this.length = length;
        this.end = origin.clone().add(this.direction.clone().multiply(length));
    }

    public static Ray normal(Location origin, Vector direction) {
        return new Ray(origin, direction.normalize(), 1.0);
    }

}
