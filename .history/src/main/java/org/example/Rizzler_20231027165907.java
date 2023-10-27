package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

import org.example.CrazyState;

import java.util.List;

// ------------------------------------------------------------------
// Rizzler
// ------------------------------------------------------------------
// This is very high quality coding
// ------------------------------------------------------------------
public class Rizzler extends Bot {
    public static final double TURN_DEGREES = 90;
    public static final double MAX_DISTANCE_WITHOUT_COLLIDE = 324;

    private State state;
    private List<ScannedBotEvent> scans;

    public boolean movingForward;

    // The main method starts our bot
    public static void main(String[] args) {
        new Rizzler().start();
    }

    // Constructor, which loads the bot config file
    public Rizzler() {
        super(BotInfo.fromFile("Rizzler.json"));
        // TODO: CHANGE THIS
        this.state = new CrazyState(this);
    }

    // Called when a new round is started -> initialize and do some movement
    public void run() {
        // Set colors
        setBodyColor(Color.fromString("#00C800")); // lime
        setTurretColor(Color.fromString("#009632")); // green
        setRadarColor(Color.fromString("#006464")); // dark cyan
        setBulletColor(Color.fromString("#FFFF64")); // yellow
        setScanColor(Color.fromString("#FFC8C8")); // light red

        // Loop while as long as the bot is running
        while (isRunning()) {
            checkForWallCollision();
            state.whileRunning();
            go();
        }
    }

    private void checkForWallCollision() {
        if (isSpaceLeft()) {

            if (willCollide(getArenaWidth(), getArenaHeight())) {
                setTurnRight(TURN_DEGREES);
            }
        }
    }

    private boolean isSpaceLeft() {
        return getDistanceRemaining() > 0;
    }

    private boolean willCollide(int width, int height) {
        double x = getX();
        double y = getY();
        double dir = getDirection();

        if (goingUp(dir)) {

            // Calculate distance to closest point on top wall
            double angle = Math.abs(90 - dir);
            double relevantY = 0;
            double extraX = distanceTo(x, 0) / Math.cos(angle);
            double relevantX = (90 - dir > 0) ? x + extraX : x - extraX;

            return distanceTo(relevantX, relevantY) < 18;

        } else if (goingLeft(dir)) {

            // Calculate distance to closest point on left wall
            double angle = Math.abs(180 - dir);
            double relevantX = 0;
            double extraY = distanceTo(0, y) / Math.cos(angle);

            double relevantY = (dir - 180 > 0) ? y + extraY : y - extraY;

            return distanceTo(relevantX, relevantY) < 18;

        } else if (goingDown(dir)) {
            // Calculate distance to closest point on bottom wall
            double angle = Math.abs(180 - dir);
            double relevantX = 0;
            double extraY = distanceTo(0, y) / Math.cos(angle);

            double relevantY = (dir - 180 > 0) ? y + extraY : y - extraY;

            return distanceTo(relevantX, relevantY) < 18;
        } else {
            // Calculate distance to closest point on left wall
            double angle = Math.abs(180 - dir);
            double relevantX = 0;
            double extraY = distanceTo(0, y) / Math.cos(angle);

            double relevantY = (dir - 180 > 0) ? y + extraY : y - extraY;

            return distanceTo(relevantX, relevantY) < 18;

        }

    }

    private boolean goingUp(double dir) {
        return 45 <= dir && dir < 45 + 90;
    }

    private boolean goingLeft(double dir) {
        return 45 + 90 <= dir && dir < 45 + 180;
    }

    private boolean goingDown(double dir) {
        return 45 + 180 <= dir && dir < 360 - 45;
    }

    private double calculateDistanceSquared(double x, double y, double x2, double y2) {
        return Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2);
    }

    // Events

    // We collided with a wall -> reverse the direction
    @Override
    public void onHitWall(HitWallEvent e) {
        state.onHitWall(e);
    }

    // We scanned another bot -> fire!
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        state.onScannedBot(e);
    }

    // We hit another bot -> back up!
    @Override
    public void onHitBot(HitBotEvent e) {
        state.onHitBot(e);
    }

    @Override
    public void onBulletFired(BulletFiredEvent e) {
        state.onBulletFired(e);
    }

    // GAME START AND END

    @Override
    public void onWonRound(WonRoundEvent e) {

    }

    // GETTERS AND SETTERS //////////////////////////////

    public void setState(State state) {
        this.state = state;
    }

    public void addScan(ScannedBotEvent e) {
        this.scans.add(e);
    }

    public List<ScannedBotEvent> getScans() {
        return this.scans;
    }

    // UTILITY METHODS //////////////////////////////////

    // ReverseDirection: Switch from ahead to back & vice versa
    public void reverseDirection() {
        if (movingForward) {
            setBack(40000);
            movingForward = false;
        } else {
            setForward(40000);
            movingForward = true;
        }
    }

}