package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.exception.UnoException;
import org.example.eiscuno.model.observer.EventManager;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreadPlayMachine extends Thread {
    private EventManager eventManager;
    private GameUno gameUno;
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(EventManager eventManager, GameUno gameUno, Table table, Player machinePlayer, ImageView tableImageView) {
        this.eventManager = eventManager;
        this.gameUno = gameUno;
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
    }

    public void run() {
        while (true){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Random random = new Random();
                double probability = 0.2; // Probability of a 20% chance it occurs.
                boolean takeCard = random.nextDouble() < probability;
                if(takeCard){
                    try {
                        gameUno.takeCard("MACHINE_PLAYER");
                    } catch (IllegalStateException e) {

                    }
                }
                else{
                    putCardOnTheTable();
                }
                hasPlayerPlayed = false;
                eventManager.notifyListenersTurnUpdate(hasPlayerPlayed);
                eventManager.notifyListenersCardsUpdate();
            }
        }
    }

    private void putCardOnTheTable(){
        ArrayList<Card> cardsMachinePlayer = machinePlayer.getCardsPlayer();
        boolean validPlacement = false;
        int i = 0;
        for(Card card : cardsMachinePlayer){
            try {
                i++;
                gameUno.playCard(card, "MACHINE_PLAYER");
                machinePlayer.removeCard(i);
                tableImageView.setImage(card.getImage());
                validPlacement = true;
            } catch (UnoException ignored) {
            }
        }
        if(!validPlacement){
            gameUno.takeCard("MACHINE_PLAYER");
        }
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
