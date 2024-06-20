package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;

    private GameUno gameUno;

    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, GameUno gameUno){
        this.gameUno = gameUno;
        this.cardsPlayer = cardsPlayer;
    }

    @Override
    public void run(){
        while (true){
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
        }
    }

    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){
            System.out.println("UNO");
            gameUno.haveSungOne("MACHINE_PLAYER");
        }
    }
}
