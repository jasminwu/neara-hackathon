package org.example;

import java.lang.Math;

public class Vector2D {
    
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Basic Operations
    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public double direction() {
        return Math.toDegrees(Math.atan2(y, x));
    }

    public static double dot(Vector2D a, Vector2D b) {
        return a.x * b.x + a.y * b.y;
    }

    public static Vector2D fromPolar(double r, double theta) {
        return new Vector2D(r * Math.cos(Math.toRadians(theta)), r * Math.sin(Math.toRadians(theta)));
    }

    public Vector2D scalarMult(double l) {
        return new Vector2D(x * l, y * l);
    }

    public static boolean crossProdIsOutwards(Vector2D a, Vector2D b) {
        return a.x * b.y - a.y * b.x > 0;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }



}
