package org.example;

import dev.robocode.tankroyale.botapi.events.*;

import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

// Causes the bot to focus on one target, predicting its moves
// Radar fixes on the target
// downside: tunnel vision
public class PredictionShotState extends State {
    // PARAMETERS
    // how many turns it takes for a gun to reach a certain position
    private static int GUN_TURN_TURNS = 2;
    private static int RADAR_TURN_TURNS = 1;
    // because gun turns twice as fast as tank
    private static double GUN_TANK_TURN_RATE_RATIO = 2;
    private static double LINEARITY_THRESHOLD = 0.55;
    private static double CONSECUTIVE_TURNS = 10; 
    private static double MAX_FIREPOWER = 2.5; // MAX 3
    private static double COCKINESS = 0.5;
    private static double TIME_BEFORE_FALLBACK = 10;

    // MORE CONSTANTS KINDA
    private State FALLBACK_STATE; // check definition in constructor

    // Prediction Variables
    private boolean aiming = false;
    private double firepower = 2;
    private ScannedBotEvent scan;

    // CONSTRUCTOR

    public PredictionShotState(Rizzler context) {
        super(context);
        System.out.println("Constructing state");

        // MORE CONSTANTS
        scan = new ScannedBotEvent(0, 0, 0, 0, 0, 0, 0, 0);
        FALLBACK_STATE = this;
    }

    // STATE INTERFACE

    @Override
    public void whileRunning() {
        System.out.print("rizzing rn");
        
        if (aiming && context.getTurnNumber() - scan.getTurnNumber() >= GUN_TURN_TURNS) {
            context.fire(firepower);
            context.setTurnGunLeft(0);
            aiming = false;
        }

        if (!aiming && context.getTurnNumber() - scan.getTurnNumber() >= TIME_BEFORE_FALLBACK) {
            context.setState(FALLBACK_STATE);
            System.out.println("Fallback state used due to no rizz");
        }
        
        // In your implementation
        context.rescan();
        // make sure to call super.whileRunning()
    }
    
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        System.out.println("A bot has been scanned");
        if (aiming) {
            return;
        }

        // Initialise
        context.addScan(e);
        scan = e;
        
        // Switch to another state if bot is too unpredictable
        double confidence = predictionConfidence();
        if (confidence < LINEARITY_THRESHOLD) {
            context.setState(FALLBACK_STATE);
            System.out.println("Fallback state used due to no confidence");
        }


        // FIREPOWER SHOULD GO UP WITH CONFIDENCE
        firepower = confidence * MAX_FIREPOWER;
        aiming = true;

        // FIND DISPLACEMENT FROM SELF TO BOT
        Vector2D rBot = new Vector2D(scan.getX(), scan.getY());
        Vector2D rSelf = new Vector2D(context.getX(), context.getY());
        Vector2D disp = rBot.subtract(rSelf);
        // FIND VELOCITY OF BOT
        Vector2D vel = Vector2D.fromPolar(scan.getSpeed(), scan.getDirection());
        
        // FIND IDEAL SHOOTING ANGLE // PREDICT WHERE BOT WILL BE IN 3 TURNS
        // the total angle that the gun must point to
        double aimAngle = calcPredictGunAngle(disp, vel);
        // split into an angle for tank and an angle for the gun
        double tankTurnAngle = aimAngle * (1 - (GUN_TANK_TURN_RATE_RATIO/(1 + GUN_TANK_TURN_RATE_RATIO)));
        double gunTurnAngle = aimAngle * (GUN_TANK_TURN_RATE_RATIO/(1 + GUN_TANK_TURN_RATE_RATIO));

        // FIND IDEAL RADAR ANGLE // PREDICT WHERE GUY WILL BE IN 3 TURNS
        double radarAngle = calcPredictRadarAngle(disp, vel, aimAngle);

        // PUSH THESE INSTRUCTIONS
        context.stop();
        context.setTurnLeft(tankTurnAngle);
        context.setTurnGunLeft(gunTurnAngle);
        context.setTurnRadarLeft(radarAngle);
    }

    // Utility classes

    // On a scale of 0 to 1, how confident are we that the bot is moving in a straight line
    public double predictionConfidence() {
        List<ScannedBotEvent> scans = context.getScans();
        List<ScannedBotEvent> relevantScans = scans.stream().filter(x -> x.getScannedBotId() == scan.getScannedBotId()).collect(Collectors.toList());
        
        // assume it is a line of the size is less than 3
        if (relevantScans.size() < 3) {
            return COCKINESS; // not sure
        }

        // Get scan infos
        int n = relevantScans.size();
        ScannedBotEvent scanC = relevantScans.get(n - 1);
        ScannedBotEvent scanB = relevantScans.get(n - 2);
        ScannedBotEvent scanA = relevantScans.get(n - 3);

        // Find positions
        Vector2D posC = new Vector2D(scanC.getX(), scanC.getY());
        Vector2D posB = new Vector2D(scanB.getX(), scanB.getY());
        Vector2D posA = new Vector2D(scanA.getX(), scanA.getY());

        // Find displacement vectors
        Vector2D vecAB = posB.subtract(posA);
        Vector2D vecBC = posC.subtract(posB);

        // Find time intervals
        double timeIntervalBC = scanC.getTurnNumber() - scanB.getTurnNumber();
        double timeIntervalAB = scanB.getTurnNumber() - scanA.getTurnNumber();
        
        // CHECK IF SCANS ARE CLOSE TOGETHER ENOUGH
        if (    timeIntervalBC > CONSECUTIVE_TURNS ||
                timeIntervalAB > CONSECUTIVE_TURNS) {
            return COCKINESS;
        }

        // find velocity vectors
        Vector2D velAB = vecAB.scalarMult(1/timeIntervalAB);
        Vector2D velBC = vecBC.scalarMult(1/timeIntervalBC);

        // WEIRD DOT PRODUCT THING TO DETERMINE PARALLELNESS
        // Put the average velocities into the dot product
        double dotprod = Vector2D.dot(velAB, velBC);

        // check how similar the velAB and velBC are to each other
        // weird sketchy arbitrary equation
        return (dotprod * dotprod) / Math.pow(Math.max(velAB.magnitude(), velBC.magnitude()), 2);
    }

    // predict the angle at which you must fire
    public double calcPredictGunAngle(Vector2D disp, Vector2D vel) {
        boolean anticlockwise = Vector2D.crossProdIsOutwards(disp, vel);

        // DETERMINE RADIAL AND TANGENTIAL COMPONENTS
        double radialVel = Vector2D.dot(vel, disp) / disp.magnitude();
        double tangVel = Math.sqrt(Math.pow(vel.magnitude(), 2) - Math.pow(radialVel, 2));

        double bulletVel = 20 - 3 * firepower;

        // used weird approximation
        // ignored radial motion of the bot
        double theta = Math.toDegrees(tangVel * (GUN_TURN_TURNS + disp.magnitude() / bulletVel) / disp.magnitude());
        if (!anticlockwise) {
            theta = -theta;
        }

        // now theta is the angle relative to the displacement vector
        // find absolute angle
        double absDirection = disp.direction() + theta;
        // now convert to relative direction from gun
        double gunDirection = context.getGunDirection();
        double relDirection = absDirection - gunDirection;

        // now shift it so that it is between -180 and 180
        if (relDirection > 180) {
            relDirection -= 180;
        }

        System.out.println("Gun Turn");
        System.out.println(relDirection);
        return relDirection;
    }

    public double calcPredictRadarAngle(Vector2D disp, Vector2D vel, double aimAngle) {
        boolean anticlockwise = Vector2D.crossProdIsOutwards(disp, vel);

        // DETERMINE RADIAL AND TANGENTIAL COMPONENTS
        double radialVel = Vector2D.dot(vel, disp) / disp.magnitude();
        double tangVel = Math.sqrt(Math.pow(vel.magnitude(), 2) - Math.pow(radialVel, 2));

        System.out.println("TANGEVELRADIALVEL");
        System.out.println(tangVel);

        // used weird approximation
        // ignored radial motion of the bot
        double theta = tangVel * 1 / disp.magnitude();
        if (!anticlockwise) {
            theta = -theta;
        }

        // now theta is the angle relative to the displacement vector
        // find absolute angle
        double absDirection = disp.direction() + theta;
        double radarDirection = context.getRadarDirection();
        double relDirection = absDirection - radarDirection;

        // HOWEVER WE MUST ACCOUNT FOR THE TURNING OF THE GUN
        relDirection -= aimAngle;

        // now shift it so that it is between -180 and 180
        if (relDirection > 180) {
            relDirection -= 180;
        }
        System.out.println("Radar Turn");
        System.out.println(relDirection);
        return relDirection;
    }
}
