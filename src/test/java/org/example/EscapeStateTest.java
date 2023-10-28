package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EscapeStateTest {
    @Test
    @Tag("1-1")
    @DisplayName("Test bot trying to escape a ramming bot")
    public void checkForRamming() {
        Rizzler ourBot = new Rizzler();
        ourBot.setEnergy(100);
        int id = ourBot.getMyId();

        RamFire otherBot = new RamFire();
        int otherId = otherBot.getMyId();

        ScannedBotEvent​ e1 = new ScannedBotEvent​(3, id, otherId, otherBot.getEnergy(), otherBot.getX(),
                otherBot.getY(), otherBot.getDirection(), otherBot.getMaxSpeed());
    }

}