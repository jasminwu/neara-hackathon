package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

public class CrazyState extends State {

    public CrazyState(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {
        context.setForward(1);
        context.movingForward = true;

        // context.setTurnRight(90);
        // context.waitFor(new TurnCompleteCondition(context));
        // context.setTurnLeft(180);
        // context.waitFor(new TurnCompleteCondition(context));
        // context.setTurnRight(180);
        // context.waitFor(new TurnCompleteCondition(context));
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
