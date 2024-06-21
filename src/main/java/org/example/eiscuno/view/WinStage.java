package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.io.IOException;

/**
 * Represents a stage displayed when the player wins the game.
 */
public class WinStage extends Stage {

    /**
     * Constructs a new instance of WinStage.
     *
     * @throws IOException if an error occurs while loading the FXML file for the game interface.
     */
    public WinStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/win-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            // Re-throwing the caught IOException
            throw new IOException("Error while loading FXML file", e);
        }

        Scene scene = new Scene(root);

        // Configuring the stage
        setTitle("Ganaste"); // Sets the title of the stage
        getIcons().add(
                new Image(
                        String.valueOf(getClass().getResource(EISCUnoEnum.FAVICON.getFilePath()))));
        setScene(scene); // Sets the scene for the stage
        setResizable(false); // Disallows resizing of the stage
        show(); // Displays the stage
    }

    /**
     * Closes the instance of WinStage.
     * This method is used to clean up resources when the game stage is no longer needed.
     */
    public static void deleteInstance() {
        WinStageHolder.INSTANCE.close();
        WinStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the singleton instance of WinStage.
     *
     * @return the singleton instance of WinStage.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static WinStage getInstance() throws IOException {
        return WinStageHolder.INSTANCE != null ?
                WinStageHolder.INSTANCE :
                (WinStageHolder.INSTANCE = new WinStage());
    }

    /**
     * Holder class for the singleton instance of WinStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class WinStageHolder {
        private static WinStage INSTANCE;
    }
}