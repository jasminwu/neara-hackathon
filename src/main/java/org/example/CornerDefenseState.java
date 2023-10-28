package org.example;

import dev.robocode.tankroyale.botapi.IBot;
import dev.robocode.tankroyale.botapi.events.*;

import java.util.ArrayList;
import java.util.List;

public class CornerDefenseState extends State {
    int height = 600;
    int width = 800;
    boolean atCorner;
    boolean atFacingRight;
    public CornerDefenseState(Rizzler context) {
        super(context);
        atCorner = false;
        atFacingRight = false;
    }

    @Override
    public void whileRunning() {
        if (!atCorner) {
            headToCoords(50, 50);
            atCorner = true;
            double degreesToTurn = context.radarBearingTo(width, height);
            // So now should be facing the right way??
            if (degreesToTurn > 0) {
                context.setTurnRadarRight(degreesToTurn);
            } else {
                context.setTurnRadarLeft(degreesToTurn);
            }
        } else {
            // Turn the head to face the right direction - upper right

            headToCoords(200, 50);
            headToCoords(50, 200);
            headToCoords(50, 50);
        }


        // Otherwise just turns right and left
    }

    @Override
    public void onScannedBot(ScannedBotEvent e) {
        context.fire(1);
    }

    @Override
    public void onHitBot(HitBotEvent e) {
        // If we're moving into the other bot, reverse!
        if (e.isRammed()) {
            // context.reverseDirection();
        }
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        //context.reverseDirection();
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        //context.setState(new PredictionShotState(context));
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

    private void headToNearestCorner() {
        System.out.println("Trying to get to a corner");
        headToCoords(1, 1);
        System.out.println("I've reached 1!!");
        double x = context.getX();
        double y = context.getY();
        // Now need to head to that corner!!! with physics
        int corner = indexOfClosestCorner(x, y, height, width);
        int targetX;
        int targetY;

        switch (corner) {
            case 0:
                // Top left
                targetX = 1;
                targetY = 1;
                break;
            case 1:
                targetX = width - 1;
                targetY = 1;
                break;
            case 2:
                targetX = 1;
                targetY = height - 1;
                break;
            default:
                // Goes to the bottom right.
                targetX = width - 1;
                targetY = height - 1;
        }

        headToCoords(targetX, targetY);
        atCorner = true;
        System.out.println("Bot is now at a corner!!!");

    }

    private void headToCoords(int x, int y) {
        while (context.getX() != x && context.getY() != y) {
            double degreesToTurn = context.radarBearingTo(x,y);
            if (degreesToTurn > 0) {
                context.turnRight(degreesToTurn);
            } else {
                context.turnLeft(degreesToTurn);
            }

            double distance = context.distanceTo(x, y);
            context.setForward(distance);
        }
    }
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
