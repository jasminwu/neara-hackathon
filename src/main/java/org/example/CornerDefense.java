package org.example;

import dev.robocode.tankroyale.botapi.IBot;
import dev.robocode.tankroyale.botapi.events.Condition;
import dev.robocode.tankroyale.botapi.events.HitBotEvent;
import dev.robocode.tankroyale.botapi.events.HitWallEvent;
import dev.robocode.tankroyale.botapi.events.ScannedBotEvent;
import java.util.ArrayList;
import java.util.List;

// All the different corners:
// 0: 1, 1
// 1: 1, height - 1
// 2: width - 1, 1
// 3: width - 1, height - 1

// very simple
public class CornerDefense extends State {
    public CornerDefense(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {
        // Heads to a corner-so it doesn't get cornered, and then scans 90deg, and shoots depending on that. But
        // shouldn't it keep moving?
        // If a bot gets too close, moves in some random pattern (yikes) to another corner, along the wall - if it gets
        // hit twice along that road, then

        // Calculating the nearest corner
        int height = context.getArenaHeight();
        int width = context.getArenaWidth();

        double x = context.getX();
        double y = context.getY();

        int corner = indexOfClosestCorner(x, y, height, width);
        // Calculates distance to each corner

    }

    @Override
    public void onScannedBot(ScannedBotEvent e) {
        // Calculates their next position, blah blah blah and shoots them blah blah blah
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

//    private void goCorner() {
//        // We don't want to stop when we're just turning...
//        stopWhenSeeEnemy = false;
//        // Turn to face the wall towards our desired corner
//        turnLeft(calcBearing(corner));
//        // Ok, now we don't want to crash into any bot in our way...
//        stopWhenSeeEnemy = true;
//        // Move to that wall
//        forward(5000);
//        // Turn to face the corner
//        turnRight(90);
//        // Move to the corner
//        forward(5000);
//        // Turn gun to starting point
//        turnGunRight(90);
//    }

    private int indexOfClosestCorner(double x, double y, int height, int width) {
        List<Double> distances = new ArrayList<>();

        // Calculate distances to each of the four corners and add them to the list
        distances.add(Math.sqrt(Math.pow(1 - y, 2) + Math.pow(1 - x, 2)));
        distances.add(Math.sqrt(Math.pow(height - 1 - y, 2) + Math.pow(1 - x, 2)));
        distances.add(Math.sqrt(Math.pow(1 - y, 2) + Math.pow(width - 1 - x, 2)));
        distances.add(Math.sqrt(Math.pow(height - 1 - y, 2) + Math.pow(width - 1 - x, 2)));

        // Find the index of the minimum distance
        int minIndex = 0;
        double minDistance = distances.get(0);

        for (int i = 1; i < distances.size(); i++) {
            if (distances.get(i) < minDistance) {
                minIndex = i;
                minDistance = distances.get(i);
            }
        }

        return minIndex;
    }
}
