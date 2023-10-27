package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

public abstract class State {
    // State implementations should also store a reference to the context
    protected Rizzler context;

    public State(Rizzler context) {
        this.context = context;
    }

    // Events to listen for
    public void whileRunning() {
        checkForWallCollision();

    }

    private void checkForWallCollision() {
        if (isSpaceLeft()) {
            int width = context.getArenaWidth();
            int height = context.getArenaHeight();

            if (willCollide(width, height)) {
                context.turnRight(180);
            }

        }

    }

    private boolean isSpaceLeft() {
        return context.getDistanceRemaining() > 0;
    }

    private static final double MAX_DISTANCE_WITHOUT_COLLIDE = 324;

    private boolean willCollide(int width, int height) {
        double x = context.getX();
        double y = context.getY();
        double dir = context.getDirection();

        if (goingUp(dir)) {
            return calculateDistanceSquared(x, y, x, height) < MAX_DISTANCE_WITHOUT_COLLIDE;
        } else if (135 < dir && dir < 225) {
            // Going left
            return calculateDistanceSquared(x, y, 0, y) < MAX_DISTANCE_WITHOUT_COLLIDE;
        } else if (225 < dir && dir < 315) {
            // Going down
            return calculateDistanceSquared(x, y, x, 0) < MAX_DISTANCE_WITHOUT_COLLIDE;
        } else {
            // Going right
            return calculateDistanceSquared(x, y, width, y) < MAX_DISTANCE_WITHOUT_COLLIDE;
        }

    }

    private boolean goingUp(double dir) {
        return 45 < dir && dir < 135;
    }

    private boolean goingLeft(double dir) {
        return 135 < dir && dir < 225;
    }

    private double calculateDistanceSquared(double x, double y, double x2, double y2) {
        return Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2);
    }

    public void onHitWall(HitWallEvent e) {

    }

    public void onHitBot(HitBotEvent e) {

    }

    public void onHitByBullet(HitByBulletEvent e) {

    }

    public void onBulletFired(BulletFiredEvent e) {

    }

    public void onScannedBot(ScannedBotEvent e) {

    }

}