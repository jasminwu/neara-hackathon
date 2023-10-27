package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

import java.util.List;

// ------------------------------------------------------------------
// Rizzler
// ------------------------------------------------------------------
// This is very high quality coding
// ------------------------------------------------------------------
public class Rizzler extends Bot {

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

        while (isRunning()) {
            if (willCollide(getArenaWidth(), getArenaHeight())) {
                setTurnRight(90);
            }
            state.whileRunning();

            go();
        }
    }

    private boolean isSpaceLeft() {
        return getDistanceRemaining() > 0;
    }

    private boolean willCollide(int width, int height) {
        double x = getX();
        double y = getY();

        return x < 50 || x > width - 50 || y < 50 || y > height - 50;
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