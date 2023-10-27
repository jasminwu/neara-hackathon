package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

// Causes the bot to focus on one target, predicting its moves
// Radar fixes on the target
// downside: tunnel vision
public class FutureSightState extends State {

    // Initialisation Variables
    private boolean startInit = false;
    private boolean endInit = false;

    // Prediction Variables
    private boolean targetViable = false;
    private int targetLastSeen = -999;
    private int targetSpeed = 0;
    private int targetDirection = 0;

    // CONSTRUCTOR

    public FutureSightState(Rizzler context) {
        super(context);

    }
    
    // STATE INTERFACE

    
    @Override
    public void whileRunning() {
            // Has been initialised
            System.out.print("KILL YOURSELF");
            context.go();

        if (!startInit) {
            // completely stop
            context.setTurnGunLeft(0);
            context.setForward(0);

            initState();
            startInit = true;
        } 
        if (!endInit) {
            endInit = true;
            return;
        }
    }

    @Override
    public void onHitBot(HitBotEvent e) {
        // transition to defensive state
        // context.setState(new EscapeState(context));
        context.setState(new CrazyState(context));
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        // transition into defensive state
        //context.setState(new EscapeState(context));
        context.setState(new CrazyState(context));
    }

    @Override
    public void onScannedBot(ScannedBotEvent e) {
        context.addScan(e);
        // predict the location of the scanned bot and then
    }

    // INITIALISATION
    public void initState() {
        double currentDirection = context.getDirection();
        // we want to make the direction 180 degrees
        context.setTurnRight(180 - currentDirection);

    }

}
