package org.example;

import dev.robocode.tankroyale.botapi.events.*;

public abstract class State {
    // State implementations should also store a reference to the context
    protected Rizzler context;

    public State(Rizzler context) {
        this.context = context;
    }

    private boolean willCollide(int width, int height) {
        double x = getX();
        double y = getY();

        return x < 20 || x > width - 20 || y < 20 || y > height - 20;
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
