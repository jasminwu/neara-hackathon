package org.example;

import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

public class Exp1State extends State {
    public Exp1State(Rizzler context) {
        super(context);
    }

    @Override
    public void whileRunning() {
        if (context.getDistanceRemaining() == 0) {
            context.setTurnRight(90);
            context.setForward(20);
        }
    }
    
}
