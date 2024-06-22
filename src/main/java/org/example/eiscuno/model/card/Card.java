package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a playing card in the Uno game.
 * Each card has a URL for its image, a value (such as number or special card), and a color.
 */
public class Card {
    private String url; // The URL of the card's image
    private String value; // The value of the card (e.g., number, special card value)
    private String color; // The color of the card
    private Image image; // The JavaFX Image object representing the card's image
    private ImageView cardImageView; // The JavaFX ImageView object for displaying the card's image

    /**
     * Constructs a new Card object with the specified URL, value, and color.
     *
     * @param url    the URL of the card's image
     * @param value  the value of the card
     * @param color  the color of the card
     */
    public Card(String url, String value, String color) {
        this.url = url;
        this.value = value;
        this.color = color;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
    }

    /**
     * Creates a JavaFX ImageView object for the card's image with predefined settings.
     *
     * @return the ImageView object for the card's image
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }

    /**
     * Retrieves the JavaFX ImageView object for displaying the card's image.
     *
     * @return the ImageView object for the card's image
     */
    public ImageView getCard() {
        return cardImageView;
    }

    /**
     * Retrieves the JavaFX Image object representing the card's image.
     *
     * @return the Image object for the card's image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Retrieves the value of the card.
     *
     * @return the value of the card
     */
    public String getValue() {
        return value;
    }

    /**
     * Retrieves the color of the card.
     *
     * @return the color of the card
     */
    public String getColor() {
        return color;
    }
}
