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
            // Find the dimensions of the map in advance?
            // Δx2 + Δy2 and see if it is lesser than 324
            // If this occurs then, turn 180 degrees
            int width = context.getArenaWidth();
            int height = context.getArenaHeight();

        }

    }

    private boolean isSpaceLeft() {
        return context.getDistanceRemaining() > 0;
    }

    private boolean willCollide(int width, int height) {
        double x = context.getX();
        double y = context.getY();



        if (context.distanceTo​(x, (double)0))

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
