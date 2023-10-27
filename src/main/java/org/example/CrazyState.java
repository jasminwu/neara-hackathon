package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

public class CrazyState extends State {

    public CrazyState(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {
        // Tell the game we will want to move ahead 40000 -- some large number
        context.setForward(40000);
        context.movingForward = true;
        // Tell the game we will want to turn right 90
        context.setTurnRight(90);
        // At this point, we have indicated to the game that *when we do something*,
        // we will want to move ahead and turn right. That's what "set" means.
        // It is important to realize we have not done anything yet!
        // In order to actually move, we'll want to call a method that takes real time, such as
        // waitFor.
        // waitFor actually starts the action -- we start moving and turning.
        // It will not return until we have finished turning.
        context.waitFor(new TurnCompleteCondition(context));
        // Note: We are still moving ahead now, but the turn is complete.
        // Now we'll turn the other way...
        context.setTurnLeft(180);
        // ... and wait for the turn to finish ...
        context.waitFor(new TurnCompleteCondition(context));
        // ... then the other way ...
        context.setTurnRight(180);
        // ... and wait for that turn to finish.
        context.waitFor(new TurnCompleteCondition(context));
        // then back to the top to do it all again.
    }

    @Override
    public void onScannedBot(ScannedBotEvent e) {
        context.fire(1);
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
