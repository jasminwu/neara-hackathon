package org.example;

import dev.robocode.tankroyale.botapi.events.*;

public class Exp1State extends State {
    public Exp1State(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {
        System.out.print("love yourself");
        context.go();
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        context.setState(new PredictionShotState(context));
    }
    
}
