package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

public class CrazyState extends State {

    public CrazyState(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {
        System.out.print("im crazy");
        // Tell the game we will want to move ahead 40000 -- some large number
        context.setForward(40000);
        context.movingForward = true;

        context.setTurnRight(90);
        if (willCollide()) {
            context.setTurnRight(180);
        }
        context.waitFor(new TurnCompleteCondition(context));
        context.setTurnLeft(180);
        if (willCollide()) {
            context.setTurnRight(180);
        }
        context.waitFor(new TurnCompleteCondition(context));
        context.setTurnRight(180);
        if (willCollide()) {
            context.setTurnRight(180);
        }
        context.waitFor(new TurnCompleteCondition(context));
        // then back to the top to do it all again.

    }

    @Override
    public void onScannedBot(ScannedBotEvent e) {
        context.fire(1);
        context.setState(new Exp1State(context));
    }

    @Override
    public void onHitBot(HitBotEvent e) {
        // If we're moving into the other bot, reverse!
        if (e.isRammed()) {
            context.reverseDirection();
        }
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        context.reverseDirection();
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        context.setState(new PredictionShotState(context));
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
