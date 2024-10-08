package com.outcast.rpg.util.physics.raycast;

public abstract class Geometry {
    public abstract Intersection<Ray, Geometry> intersection(Ray ray);
}