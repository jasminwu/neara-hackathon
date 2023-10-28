package org.example;

import dev.robocode.tankroyale.botapi.events.*;

public class RamState extends State {

    private int turnDirection = 1;

    public RamState(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {
        System.out.print("im ramming");

        context.turnLeft(5 * turnDirection);

        if (willCollide()) {
            context.setTurnRight(90);
        }

    }

    // We scanned another bot -> go ram it
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        turnToFaceTarget(e.getX(), e.getY());

        var distance = context.distanceTo(e.getX(), e.getY());
        context.forward(distance + 5);

        context.rescan(); // Might want to move forward again!
    }

    @Override
    public void onHitBot(HitBotEvent e) {
        turnToFaceTarget(e.getX(), e.getY());

        // Determine a shot that won't kill the bot...
        // We want to ram him instead for bonus points
        if (e.getEnergy() > 16) {
            context.fire(3);
        } else if (e.getEnergy() > 10) {
            context.fire(2);
        } else if (e.getEnergy() > 4) {
            context.fire(1);
        } else if (e.getEnergy() > 2) {
            context.fire(.5);
        } else if (e.getEnergy() > .4) {
            context.fire(.1);
        }
        context.forward(40); // Ram him again!
    }

    // Method that turns the bot to face the target at coordinate x,y, but also sets
    // the
    // default turn direction used if no bot is being scanned within in the run()
    // method.
    private void turnToFaceTarget(double x, double y) {
        var bearing = context.bearingTo(x, y);
        if (bearing >= 0) {
            turnDirection = 1;
        } else {
            turnDirection = -1;
        }
        context.turnLeft(bearing);
    }

}
