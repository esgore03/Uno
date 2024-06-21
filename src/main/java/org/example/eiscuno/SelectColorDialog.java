package org.example.eiscuno;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SelectColorDialog {
    private static String selectedColor;

    public static String display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Selecciona un color");
        window.setMinWidth(250);

        Button redButton = new Button("Rojo");
        Button greenButton = new Button("Verde");
        Button blueButton = new Button("Azul");
        Button yellowButton = new Button("Amarillo");

        redButton.setOnAction(e -> {
            selectedColor = "RED";
            window.close();
        });
        greenButton.setOnAction(e -> {
            selectedColor = "GREEN";
            window.close();
        });
        blueButton.setOnAction(e -> {
            selectedColor = "BLUE";
            window.close();
        });
        yellowButton.setOnAction(e -> {
            selectedColor = "YELLOW";
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(redButton, greenButton, blueButton, yellowButton);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return selectedColor;
    }
}