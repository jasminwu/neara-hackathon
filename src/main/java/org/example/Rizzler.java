package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;
import java.util.ArrayList;
import java.util.List;

// ------------------------------------------------------------------
// Rizzler
// ------------------------------------------------------------------
// This is very high quality coding
// ------------------------------------------------------------------
public class Rizzler extends Bot {

    public State state;
    public List<ScannedBotEvent> scans;

    public boolean movingForward;

    // The main method starts our bot
    public static void main(String[] args) {
        new Rizzler().start();
    }

    // Constructor, which loads the bot config file
    public Rizzler() {
        super(BotInfo.fromFile("Rizzler.json"));
        // TODO: DEFAULT STATEs
        this.scans = new ArrayList<ScannedBotEvent>();
    }

    // Called when a new round is started -> initialize and do some movement
    public void run() {
        // Set colors
        setBodyColor(Color.fromString("#00C800")); // lime
        setTurretColor(Color.fromString("#009632")); // green
        setRadarColor(Color.fromString("#006464")); // dark cyan
        setBulletColor(Color.fromString("#FFFF64")); // yellow
        setScanColor(Color.fromString("#FFC8C8")); // light red

        // TODO: DEFAULT STATE
        setState(new RandomState(this));

        // Loop while as long as the bot is running
        state.onRun();
        while (isRunning()) {
            state.whileRunning();

            if (state.willCollide()) {
                reverseDirection();
            }

            go();
        }
    }

    // Events

    // We collided with a wall -> reverse the direction
    @Override
    public void onHitWall(HitWallEvent e) {
        System.out.println("Hit a Wall");
        state.onHitWall(e);
    }

    // We scanned another bot -> fire!
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        System.out.println("I see you");
        state.onScannedBot(e);
    }

    // We hit another bot -> back up!
    @Override
    public void onHitBot(HitBotEvent e) {
        System.out.println("Hit bot");
        state.onHitBot(e);
    }

    @Override
    public void onBulletFired(BulletFiredEvent e) {
        System.out.println("Bang!");
        state.onBulletFired(e);
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        System.out.println("Ouch!");
        state.onHitByBullet(e);
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