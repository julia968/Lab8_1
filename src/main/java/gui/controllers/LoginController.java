package gui.controllers;

import controller.PersonControllerImpl;
import gui.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.CurrentUserManager;

import java.net.URL;
import java.util.ResourceBundle;

import static gui.utility.SpecialWindows.showConfirmationDialog;

public class LoginController implements Initializable {
    @FXML
    private Label label_person_collection;
    @FXML
    private Label label_phrase;
    @FXML
    private Label label_welcome;
    @FXML
    private Label label_fill;
    @FXML
    private Label label_login;
    @FXML
    private Label label_password;
    @FXML
    private Label label_no_acc;
    @FXML
    private Label label_error_msg;
    @FXML
    private TextField tf_login;
    @FXML
    private PasswordField pf_password;
    @FXML
    private Button button_login;
    @FXML
    private Button button_signup;
    @FXML
    private Button close_button;
    private final CurrentUserManager userManager;
    private final PersonControllerImpl controller;
    private final double width;
    private final double height;
    private final Scene scene;
    private final Parent parent;
    private Stage stage;
    private boolean login = true;
    public LoginController(double width, double height, CurrentUserManager userManager, PersonControllerImpl controller) {
        this.userManager = userManager;
        this.controller = controller;
        this.width = width;
        this.height = height;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = fxmlLoader.load();
            scene = new Scene(parent, this.width, this.height);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }
    public void launchLoginScene(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);

        stage.hide();
        stage.show();
    }

    public void closeButtonOnAction() {
        if (showConfirmationDialog(Launcher.getAppLanguage().getString("u_exit"))) {
            Stage stage = (Stage) close_button.getScene().getWindow();
            stage.close();
        } else {
            stage.setAlwaysOnTop(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_login.setOnAction(event -> loginButtonOnAction());
        button_signup.setOnAction(event -> signupButtonOnAction());
        close_button.setOnAction(event -> closeButtonOnAction());
        setLang();
    }

    private boolean checkFields() {
        if (!tf_login.getText().isBlank() && !pf_password.getText().isBlank()) {
            label_error_msg.setText(Launcher.getAppLanguage().getString("attempt"));
            return true;
        }
        if (tf_login.getText().isBlank() || pf_password.getText().isBlank()) {
            label_error_msg.setText(Launcher.getAppLanguage().getString("empty"));
        }
        return false;
    }
    public void loginButtonOnAction() {
        if (!login){
            if (checkFields()) {
                String username = tf_login.getText();
                if (!controller.getUserNameSet().contains(username)) {
                    userManager.setUserName(username);
                    controller.userRegister(username, pf_password.getText());
                    label_error_msg.setText(Launcher.getAppLanguage().getString("success"));
                    new MainController(width, height, userManager, controller).launchMainScene(stage);
                } else {
                    label_error_msg.setText(Launcher.getAppLanguage().getString("exists"));
                }
            }
        } else {
            if (checkFields()) {
                String username = tf_login.getText();
                if (controller.checkUserPassword(username, pf_password.getText())) {
                    userManager.setUserName(username);
                    label_error_msg.setText(Launcher.getAppLanguage().getString("success"));
                    new MainController(width, height, userManager, controller).launchMainScene(stage);
                } else {
                    label_error_msg.setText(Launcher.getAppLanguage().getString("invalid"));
                }
            }
        }

    }
    public void signupButtonOnAction() {
        if (login){
            login = false;
            label_no_acc.setText(Launcher.getAppLanguage().getString("have_acc"));
            label_fill.setText(Launcher.getAppLanguage().getString("reg_here"));
            button_login.setText(Launcher.getAppLanguage().getString("sign_up"));
            button_signup.setText(Launcher.getAppLanguage().getString("login"));
        } else {
            login = true;
            label_no_acc.setText(Launcher.getAppLanguage().getString("no_acc"));
            label_fill.setText(Launcher.getAppLanguage().getString("login_here"));
            button_login.setText(Launcher.getAppLanguage().getString("login"));
            button_signup.setText(Launcher.getAppLanguage().getString("sign_up"));
        }
    }

    private void setLang() {
        label_no_acc.setText(Launcher.getAppLanguage().getString("no_acc"));
        label_fill.setText(Launcher.getAppLanguage().getString("reg_here"));
        button_signup.setText(Launcher.getAppLanguage().getString("sign_up"));
        button_login.setText(Launcher.getAppLanguage().getString("login"));
        label_login.setText(Launcher.getAppLanguage().getString("label_login"));
        label_password.setText(Launcher.getAppLanguage().getString("label_password"));
        label_phrase.setText(Launcher.getAppLanguage().getString("label_phrase"));
        label_person_collection.setText(Launcher.getAppLanguage().getString("label_person_collection"));
        label_welcome.setText(Launcher.getAppLanguage().getString("label_welcome"));
    }
}
