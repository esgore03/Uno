package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.io.IOException;

/**
 * Represents a stage displayed when the player loses the game.
 */
public class LoseStage extends Stage {

    /**
     * Constructs a new instance of LoseStage.
     *
     * @throws IOException if an error occurs while loading the FXML file for the game interface.
     */
    public LoseStage() throws IOException {
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
        setTitle("Perdiste"); // Sets the title of the stage
        getIcons().add(
                new Image(
                        String.valueOf(getClass().getResource(EISCUnoEnum.FAVICON.getFilePath()))));
        setScene(scene); // Sets the scene for the stage
        setResizable(false); // Disallows resizing of the stage
        show(); // Displays the stage
    }

    /**
     * Closes the instance of LoseStage.
     * This method is used to clean up resources when the game stage is no longer needed.
     */
    public static void deleteInstance() {
        LoseStageHolder.INSTANCE.close();
        LoseStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the singleton instance of LoseStage.
     *
     * @return the singleton instance of LoseStage.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static LoseStage getInstance() throws IOException {
        return LoseStageHolder.INSTANCE != null ?
                LoseStageHolder.INSTANCE :
                (LoseStageHolder.INSTANCE = new LoseStage());
    }

    /**
     * Holder class for the singleton instance of LoseStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class LoseStageHolder {
        private static LoseStage INSTANCE;
    }
}