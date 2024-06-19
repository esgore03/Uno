package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.example.eiscuno.model.exception.UnoException;
import org.example.eiscuno.model.machine.ThreadRefillDeck;
import org.example.eiscuno.model.observer.EventManager;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.observer.GameUnoObserver;
import org.example.eiscuno.view.GameUnoStage;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController{
    @FXML
    BorderPane gameBorderPane;

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView deckButtonImageView;

    @FXML
    private ImageView unoButtonImageView;

    @FXML
    private ImageView tableImageView;
    private EventManager eventManager;
    private GameUnoObserver gameUnoObserver;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    private boolean playerHasPlayed;
    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;
    private ThreadRefillDeck threadRefillDeck;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        setVisuals();

        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();
        printCardsMachinePlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), gameUno);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.eventManager, this.gameUno, this.machinePlayer, this.tableImageView);
        threadPlayMachine.start();

        threadRefillDeck = new ThreadRefillDeck(this.gameUno);
        threadRefillDeck.start();
    }

    private void setVisuals(){
        String imageUrl = String.valueOf(getClass().getResource(EISCUnoEnum.BACKGROUND_UNO.getFilePath()));
        String style = "-fx-background-image: url('" + imageUrl + "'); " + "-fx-background-size: cover;";
        deckButtonImageView.setImage(new Image(String.valueOf(getClass().getResource(EISCUnoEnum.DECK_OF_CARDS.getFilePath()))));
        unoButtonImageView.setImage(new Image(String.valueOf(getClass().getResource(EISCUnoEnum.BUTTON_UNO.getFilePath()))));
        gameBorderPane.setStyle(style);
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.eventManager = new EventManager();
        this.gameUnoObserver = new GameUnoObserver(this);
        eventManager.addListener(this.gameUnoObserver);
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this. eventManager, this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
        this.playerHasPlayed = false;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    public void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                if(!playerHasPlayed){
                    // Aqui deberian verificar si pueden en la tabla jugar esa carta
                    try {
                        gameUno.playCard(card, "HUMAN_PLAYER");
                        playerHasPlayed = true;
                        tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                        threadPlayMachine.setHasPlayerPlayed(this.playerHasPlayed);
                        printCardsHumanPlayer();
                    } catch (UnoException e){
                        System.out.println(e.getMessage());
                    };
                }
                else{
                    System.out.println("It's not your turn.");
                }
            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
        System.out.println("\nNumber of cards human player: " + humanPlayer.getCardsPlayer().size());
    }

    /**
     * Prints the machine player's cards on the grid pane.
     */
    public void printCardsMachinePlayer() {
        this.gridPaneCardsMachine.getChildren().clear();
        Card[] currentVisibleCardsMachinePlayer = this.gameUno.getCurrentVisibleCardsMachinePlayer();

        for (int i = 0; i < currentVisibleCardsMachinePlayer.length; i++) {
            ImageView cardImageView = new ImageView(new Image(String.valueOf(getClass().getResource(EISCUnoEnum.CARD_UNO.getFilePath()))));
            cardImageView.setY(16);
            cardImageView.setFitHeight(90);
            cardImageView.setFitWidth(70);
            this.gridPaneCardsMachine.add(cardImageView, i, 0);
        }

        System.out.println("Number of cards machine player: " + machinePlayer.getCardsPlayer().size());
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        if(!playerHasPlayed) {
            try {
                gameUno.takeCard("HUMAN_PLAYER");
                printCardsHumanPlayer();
                playerHasPlayed = true;
                threadPlayMachine.setHasPlayerPlayed(this.playerHasPlayed);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                playerHasPlayed = false;
            }
        }
        else{
            System.out.println("It's not your turn.");
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        gameUno.haveSungOne("HUMAN_PLAYER");
    }

    /**
     * Handles the action of pressing the exit button.
     *
     * @param event the action event
     */
    @FXML
    void onExitButtonClick(ActionEvent event){
        GameUnoStage.deleteInstance();
    }

    /**
     * Sets the playerHasPlayed attribute to indicate whether the player has made a move.
     *
     * @param playerHasPlayed true if the player has made a move, false otherwise.
     */
    public void setPlayerHasPlayed(boolean playerHasPlayed) {
        this.playerHasPlayed = playerHasPlayed;
    }
}
