package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

// ------------------------------------------------------------------
// Rizzler
// ------------------------------------------------------------------
// This is very high quality coding
// ------------------------------------------------------------------
public class Rizzler extends BaseBot {

    private State state;

    public boolean movingForward;

    // The main method starts our bot
    public static void main(String[] args) {
        new Rizzler().start();
    }

    // Constructor, which loads the bot config file
    Rizzler() {
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
            go();
        }
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

    // UTILITY METHODS //////////////////////////////////

    public void setState(State state) {
        this.state = state;
    }

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