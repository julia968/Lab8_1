package gui.utility;

import gui.Launcher;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;


public class SpecialWindows {
    public static boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #ffa3e4");
        ButtonType yesButton = new ButtonType(Launcher.getAppLanguage().getString("yes"));
        ButtonType noButton = new ButtonType(Launcher.getAppLanguage().getString("no"));
        alert.getButtonTypes().setAll(yesButton, noButton);
        dialogPane.lookupButton(yesButton).setStyle("""
                    -fx-background-color: #f820e3;
                    -fx-background-radius: 10;
                    -fx-border-radius: 10;
                    -fx-border-color: #68034C;
                    -fx-cursor: HAND;
                    """);
        dialogPane.lookupButton(noButton).setStyle("""
                    -fx-background-color: #f820e3;
                    -fx-background-radius: 10;
                    -fx-border-radius: 10;
                    -fx-border-color: #68034C;
                    -fx-cursor: HAND;
                    """);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }


    public static void showInfoMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #ffa3e4");
        Button ok = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        ok.setStyle("""
                    -fx-background-color: #f820e3;
                    -fx-background-radius: 10;
                    -fx-border-radius: 10;
                    -fx-border-color: #68034C;
                    -fx-cursor: HAND;
                    """);
        alert.showAndWait();
    }
}

