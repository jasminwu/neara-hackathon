package org.example;

import dev.robocode.tankroyale.botapi.events.*;

public abstract class State {
    private static final int OFFSET = 100;

    // State implementations should also store a reference to the context
    protected Rizzler context;

    public State(Rizzler context) {
        this.context = context;
    }

    public boolean willCollide() {
        double x = context.getX();
        double y = context.getY();

        double width = context.getArenaWidth();
        double height = context.getArenaHeight();

        return x < OFFSET || x > width - OFFSET || y < OFFSET || y > height - OFFSET;
    }

    // Events to listen for
    public void whileRunning() {
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
