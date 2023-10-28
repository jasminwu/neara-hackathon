package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

public class RandomState extends State {

    boolean peek;
    public RandomState(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {
        if (willCollide()) {
            context.reverseDirection();
        } else {
            peek = false;
            // When rotating, you will autonamtically scan you prevent rescan by turning off peek
            System.out.print("im Random");
            context.setTurnGunRight(400);
            // Tell the game we will want to move ahead 40000 -- some large number
            context.setForward(400);
            context.movingForward = true;
            // Tell the game we will want to turn right 90
            context.setTurnRight(90);
            context.setTurnLeft(180);
            // ... and wait for the turn to finish ...
            // ... and wait for that turn to finish.
            // then back to the top to do it all again.
            context.go();
            peek = true;
        }
    }

    @Override
    public void onHitBot(HitBotEvent e) {
        // If we're moving into the other bot, scan and depending on the action, move in acertain direction
        if (e.isRammed()) {
            context.rescan();
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        // SET THE STATE TO PREDICTION SHOT STATE
//        context.setState(new PredictionShotState(context));
        context.setTurnRate(5);
        context.reverseDirection();
    }

    // We scanned another bot -> fire!
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        context.fire(2);
        if (Math.abs(context.getDirection() - context.getRadarDirection()) < 90) {
            // Radar is approximately in the same direction as the bot's movement
            // Check if the rammed bot is ahead or behind radar
            context.reverseDirection();
        } else {
            // Radar and Movement direction are opposite directions
            // Consider setting direction such that it is accelerating/decelerating/rotating
            // so predicting is easier
            context.setTurnRate(10);
            context.setTurnRight(400);
            context.setForward(4000);
        }
        if (peek) {
            context.rescan();
        }
    }

    // Condition that is triggered when the turning is complete
    public static class TurnCompleteCondition extends Condition {
        private final IBot bot;

        public TurnCompleteCondition(IBot bot) {
            this.bot = bot;
        }

        @Override
        public boolean test() {
            // turn is complete when the remainder of the turn is zero
            return bot.getTurnRemaining() == 0;
        }
    }

}
