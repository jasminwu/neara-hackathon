package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;
import java.lang.Math;

// State should try to escape any ramming/charging bots
// Attack bots while escaping. Try to rotate away unless near edge in which implement
// something similar to spinning bot
public class EscapeState extends State {

    public EscapeState(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {

    }

    @Override
    public void onScannedBot(ScannedBotEvent e) {
        // Get the Scanned bot's position and compare to current position
        // Determine closeness
        // If very close, switch to defensive state. If moderately close to far, switch to offensive
        double xSelf = context.getX();
        double ySelf = context.getY();
        double xOther = e.getX();
        double yOther = e.getY();
        double distance = Math.sqrt(Math.pow(yOther - ySelf, 2) + Math.pow(yOther - ySelf, 2));
        // If the bot is stopped, fire at it. Also detect how far it is
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

    // This should either not be here or should be replaced by Jasmin's Wall Detection
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
