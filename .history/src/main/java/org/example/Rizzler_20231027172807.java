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
            state.whileRunning();

            if (isSpaceLeft()) {
                if (willCollide(getArenaWidth(), getArenaHeight())) {
                    setTurnRight(TURN_DEGREES);
                }
            }

            go();
        }
    }

    private boolean isSpaceLeft() {
        return getDistanceRemaining() > 0;
    }

    private boolean willCollide(int width, int height) {
        double x = getX();
        double y = getY();
        double dir = getDirection();
        double relevantX, relevantY;

        if (goingUp(dir)) {
            double angle = Math.abs(90 - dir);
            relevantY = 0;
            relevantX = calculateRelevantX(x, angle);

        } else if (goingLeft(dir)) {
            double angle = Math.abs(180 - dir);
            relevantX = 0;
            relevantY = calculateRelevantY(y, angle);

        } else if (goingDown(dir)) {
            double angle = Math.abs(90 + 180 - dir);
            relevantY = height;
            relevantX = calculateRelevantX(x, angle);

        } else {
            double angle = Math.min(dir, 360 - dir);
            relevantX = width;
            relevantY = calculateRelevantY(y, angle);
        }

        return distanceTo(relevantX, relevantY) < 18;
    }

    private double calculateRelevantX(double x, double angle) {
        double extraX = distanceTo(x, 0) / Math.cos(angle);
        return (angle == Math.abs(90 - getDirection())) ? x + extraX : x - extraX;
    }

    private double calculateRelevantY(double y, double angle) {
        double extraY = distanceTo(0, y) / Math.cos(angle);
        return (angle == Math.abs(180 - getDirection())) ? y + extraY : y - extraY;
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