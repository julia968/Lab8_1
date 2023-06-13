package gui;

import controller.PersonControllerImpl;
import gui.controllers.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.CurrentUserManager;
import java.io.IOException;
import l10n.Languages;
import java.util.ResourceBundle;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        CurrentUserManager userManager = new CurrentUserManager();
        PersonControllerImpl controller = new PersonControllerImpl(userManager);
        new LoginController(1080, 717, userManager, controller).launchLoginScene(stage);
    }

    private static ResourceBundle appLanguage = Languages.ru;

    public static ResourceBundle getAppLanguage() {
        return appLanguage;
    }

    public static void setAppLanguage(ResourceBundle appLanguage) {
        Launcher.appLanguage = appLanguage;
    }

}