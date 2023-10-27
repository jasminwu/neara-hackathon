package org.example;

import dev.robocode.tankroyale.botapi.events.*;

// Causes the bot to focus on one target, predicting its moves
// Radar fixes on the target
// downside: tunnel vision
public class PredictionShotState extends State {

    // Prediction Variables
    private boolean targetViable = true;
    private int targetLastSeen = -999;
    private int targetSpeed = 0;
    private int targetDirection = 0;

    // CONSTRUCTOR

    public PredictionShotState(Rizzler context) {
        super(context);
    }
    
    // STATE INTERFACE
    
    @Override
    public void whileRunning() {
        List<ScannedBotEvent> scans = context.getScans();
    }

    @Override
    public void onScannedBot(ScannedBotEvent e) {
        context.addScan(e);
        
        // DETERMINE FIRE POWER

        // predict the location of the scanned bot and then
    }

}
