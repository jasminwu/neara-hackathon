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
    public void onRun() {

    }
    public void whileRunning() {
        System.out.print("hi");
        
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
