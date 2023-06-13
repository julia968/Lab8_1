package gui.controllers;

import Classes.*;
import controller.PersonControllerImpl;
import gui.utility.GUIValidators;
import gui.Launcher;
import gui.utility.SpecialWindows;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import l10n.Languages;
import services.CurrentUserManager;

import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static com.sun.javafx.util.Utils.clamp;
import static gui.controllers.MainController.parseNickNameToImage;

public class MapController implements Initializable {
    @FXML
    private Button button_profile;
    @FXML
    private Button button_all;
    @FXML
    private Button button_info;
    @FXML
    private Button button_exit;
    @FXML
    private Pane pane_map;
    private static final int WIDTH = 732;
    private static final int HEIGHT = 509;
    private static final double CENTER_X = WIDTH / 2.0;
    private static final double CENTER_Y = HEIGHT / 2.0;
    private static final double LOG_SCALE = 50;
    private static final double RADIUS = 30;
    private static final int NUM_STARS = 5;
    private static final double STAR_RADIUS = 10;
    private final CurrentUserManager userManager;
    private final PersonControllerImpl controller;
    private final double width;
    private final double height;
    private final Scene scene;
    private final Parent parent;
    private Stage stage;
    public MapController(double width, double height, CurrentUserManager userManager, PersonControllerImpl controller) {
        this.userManager = userManager;
        this.controller = controller;
        this.width = width;
        this.height = height;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_exit.setOnAction(event -> System.exit(1));
        button_profile.setOnAction(e -> openProfile());
        button_info.setOnAction(e -> SpecialWindows.
                showInfoMessage(Launcher.getAppLanguage().getString("help_heading")
                        ,Launcher.getAppLanguage().getString("help")));
        button_all.setOnAction(event -> setButton_all());
        drawAxes();
        controller.getPersonList().forEach(this::displayPerson);

    }
    private void drawAxes() {
        Line xAxis = new Line(0, CENTER_Y, WIDTH, CENTER_Y);
        Line yAxis = new Line(CENTER_X, 0, CENTER_X, HEIGHT);
        pane_map.getChildren().addAll(xAxis, yAxis);
    }

    private static Color generateColor(String input) {
        int hashCode = input.hashCode();
        int red = (hashCode & 0xFF0000) >> 16;
        int green = (hashCode & 0x00FF00) >> 8;
        int blue = hashCode & 0x0000FF;
        return Color.rgb(red, green, blue);
    }
    private void displayPerson(Person person) {
        double scaledX = CENTER_X;
        double scaledY = CENTER_Y;

        Coordinates coordinates = person.getCoordinates();

        if (coordinates.getX() > 0) {
            scaledX += Math.log(coordinates.getX()) * LOG_SCALE;
        } else if (coordinates.getX() < 0) {
            scaledX -= Math.log(-coordinates.getX()) * LOG_SCALE;
        }

        if (coordinates.getY() > 0) {
            scaledY -= Math.log(coordinates.getY()) * LOG_SCALE;
        } else if (coordinates.getY() < 0) {
            scaledY += Math.log(-coordinates.getY()) * LOG_SCALE;
        }

        Bounds paneBounds = pane_map.getBoundsInLocal();
        double clampedX = clamp(scaledX, paneBounds.getMinX(), paneBounds.getMaxX());
        double clampedY = clamp(scaledY, paneBounds.getMinY(), paneBounds.getMaxY());
        Circle circle = new Circle(clampedX, clampedY, 5, generateColor(person.getCreator()));
        ImageView iv = new ImageView(new Image(parseTypeToImage(person.getEyeColor())));
        iv.setX(clampedX - 20);
        iv.setY(clampedY + 5);
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        circle.setOnMouseEntered(event -> pane_map.getChildren().add(iv));
        circle.setOnMouseExited(event -> pane_map.getChildren().remove(iv));
        circle.setOnMouseClicked(event -> {
            SpecialWindows.showInfoMessage("Барби " + person.getId(), person.toString());
            createStars(pane_map, circle.getCenterX(), circle.getCenterY(), parseHeightToStars(person.getHeight()));
        });
        pane_map.getChildren().add(circle);
    }

    private int parseHeightToStars(long height){
        if (height < 50){
            return 3;
        }if (height >50 && height < 150){
            return 5;
        }
        return 7;
    }
    private String parseTypeToImage(EyeColor eyeColor) {
        return switch (eyeColor) {
            case GREEN -> "/images/ava1.jpg";
            case BLACK -> "/images/ava2.jpg";
            case YELLOW -> "/images/ava3.jpg";
        };
    }
    private void setButton_all() {
        Stage primaryStage = new Stage();
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #ffa3e4");
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Button info = new Button(Launcher.getAppLanguage().getString("info"));
        info.setTextFill(Color.WHITE);
        info.setCursor(Cursor.HAND);
        info.setOnAction(event1 -> {
            info.getScene().getWindow().hide();
            showInfo();
        });
        gridPane.add(info, 0, 0);
        Button add = new Button(Launcher.getAppLanguage().getString("add_new"));
        add.setTextFill(Color.WHITE);
        add.setCursor(Cursor.HAND);
        add.setOnAction(event1 -> {
            add.getScene().getWindow().hide();
            openAddWindow();
        });
        gridPane.add(add, 0, 1);
        Button map = new Button(Launcher.getAppLanguage().getString("map"));
        map.setTextFill(Color.WHITE);
        map.setCursor(Cursor.HAND);
        map.setOnAction(event1 -> map.getScene().getWindow().hide());
        gridPane.add(map, 0, 4);
        Button table = new Button(Launcher.getAppLanguage().getString("table"));
        table.setTextFill(Color.WHITE);
        table.setCursor(Cursor.HAND);
        table.setOnAction(event1 -> new MainController(width, height, userManager, controller).launchMainScene(stage));
        gridPane.add(table, 0, 2);
        Button maxByName = new Button(Launcher.getAppLanguage().getString("longest"));
        maxByName.setTextFill(Color.WHITE);
        maxByName.setCursor(Cursor.HAND);
        maxByName.setOnAction(event1 -> {
            maxByName.getScene().getWindow().hide();
            try {
                SpecialWindows.showInfoMessage(Launcher.getAppLanguage().getString("longest"), controller.maxByName());
            } catch (NoSuchElementException e){
                SpecialWindows.showInfoMessage(Launcher.getAppLanguage().getString("warn"), Launcher.getAppLanguage().getString("bd_empty"));
            }
        });
        gridPane.add(maxByName, 0, 3);
        Scene scene = new Scene(gridPane, 300, 200);
        scene.getStylesheets().add("/css/style.css");
        Stream.of(info, add, map, table, maxByName).forEach(button -> {
            button.getStyleClass().add("submit-button");
            button.setPrefWidth(275);
        });
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void openAddWindow() {
        Stage primaryStage = new Stage();
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #ffa3e4");
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(20);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(20);
        ColumnConstraints column4 = new ColumnConstraints();
        column4.setPercentWidth(20);
        ColumnConstraints column5 = new ColumnConstraints();
        column5.setPercentWidth(20);
        gridPane.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

        Label label1 = new Label(Launcher.getAppLanguage().getString("name"));
        TextField textField1 = new TextField();
        textField1.setStyle("-fx-focus-color: #f820e3;");
        gridPane.add(label1, 0, 0);
        gridPane.add(textField1, 1, 0);

        Label label2 = new Label(Launcher.getAppLanguage().getString("x_c"));
        TextField textField2 = new TextField();
        textField2.setStyle("-fx-focus-color: #f820e3;");
        gridPane.add(label2, 0, 1);
        gridPane.add(textField2, 1, 1);

        Label label3 = new Label(Launcher.getAppLanguage().getString("y_c"));
        TextField textField3 = new TextField();
        textField3.setStyle("-fx-focus-color: #f820e3;");
        gridPane.add(label3, 2, 1);
        gridPane.add(textField3, 3, 1);

        Label label4 = new Label(Launcher.getAppLanguage().getString("x_l"));
        TextField textField4 = new TextField();
        textField4.setStyle("-fx-focus-color: #f820e3;");
        gridPane.add(label4, 0, 2);
        gridPane.add(textField4, 1, 2);

        Label label5 = new Label(Launcher.getAppLanguage().getString("y_l"));
        TextField textField5 = new TextField();
        textField5.setStyle("-fx-focus-color: #f820e3;");
        gridPane.add(label5, 2, 2);
        gridPane.add(textField5, 3, 2);

        Label label6 = new Label(Launcher.getAppLanguage().getString("height"));
        TextField textField6 = new TextField();
        textField6.setStyle("-fx-focus-color: #f820e3;");
        gridPane.add(label6, 0, 3);
        gridPane.add(textField6, 1, 3);

        Label label7 = new Label(Launcher.getAppLanguage().getString("country"));
        RadioButton radioButton1 = new RadioButton("UK");
        radioButton1.setStyle("-fx-focus-color: #f820e3");
        radioButton1.setUserData("UNITED_KINGDOM");
        RadioButton radioButton2 = new RadioButton("USA");
        radioButton2.setStyle("-fx-focus-color: #f820e3");
        radioButton2.setUserData("USA");
        RadioButton radioButton3 = new RadioButton("GERMANY");
        radioButton3.setStyle("-fx-focus-color: #f820e3");
        radioButton3.setUserData("GERMANY");
        RadioButton radioButton4 = new RadioButton("THAILAND");
        radioButton4.setStyle("-fx-focus-color: #f820e3");
        radioButton4.setUserData("THAILAND");
        ToggleGroup toggleGroup1 = new ToggleGroup();
        radioButton1.setToggleGroup(toggleGroup1);
        radioButton2.setToggleGroup(toggleGroup1);
        radioButton3.setToggleGroup(toggleGroup1);
        radioButton4.setToggleGroup(toggleGroup1);
        gridPane.add(label7, 0, 4);
        gridPane.add(radioButton1, 1, 4);
        gridPane.add(radioButton2, 2, 4);
        gridPane.add(radioButton3, 3, 4);
        gridPane.add(radioButton4, 4, 4);

        Label label8 = new Label(Launcher.getAppLanguage().getString("eyes_color"));
        RadioButton radioButton5 = new RadioButton("GREEN");
        radioButton5.setStyle("-fx-focus-color: #f820e3");
        radioButton5.setUserData("GREEN");
        RadioButton radioButton6 = new RadioButton("BLACK");
        radioButton6.setStyle("-fx-focus-color: #f820e3");
        radioButton6.setUserData("BLACK");
        RadioButton radioButton7 = new RadioButton("YELLOW");
        radioButton7.setStyle("-fx-focus-color: #f820e3");
        radioButton7.setUserData("YELLOW");
        ToggleGroup toggleGroup2 = new ToggleGroup();
        radioButton5.setToggleGroup(toggleGroup2);
        radioButton6.setToggleGroup(toggleGroup2);
        radioButton7.setToggleGroup(toggleGroup2);
        gridPane.add(label8, 0, 5);
        gridPane.add(radioButton7, 1, 5);
        gridPane.add(radioButton5, 2, 5);
        gridPane.add(radioButton6, 3, 5);

        Label label9 = new Label(Launcher.getAppLanguage().getString("bd"));
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-focus-color: #f820e3");
        gridPane.add(label9, 0, 6);
        gridPane.add(datePicker, 1, 6);

        Button button = new Button(Launcher.getAppLanguage().getString("add"));
        button.setCursor(Cursor.HAND);
        gridPane.add(button, 2, 7);

        Label id = new Label("                                 ID:"); //<^_^>
        TextField tf_id = new TextField();
        tf_id.setStyle("-fx-focus-color: #f820e3");
        gridPane.add(id, 3, 0);
        gridPane.add(tf_id, 4, 0);
        id.setVisible(false);
        tf_id.setVisible(false);

        button.setOnAction(event -> {
            List<Person> personList = controller.getPersonList();
            if (!id.isVisible()) {
                Stream.of(tf_id ,textField1, textField2, textField3, textField4, textField5, textField6).forEach(textField -> textField.setPromptText(""));
                Stream.of(label7, label8, label9).forEach(label -> label.setTextFill(Color.BLACK));
                boolean error = false;
                if (textField1.getText().isEmpty()) {
                    error = true;
                    textField1.setText("");
                    textField1.setPromptText(Launcher.getAppLanguage().getString("invalid"));
                }
                if (!GUIValidators.validateXCoordinate(textField2.getText())) {
                    error = true;
                    textField2.setPromptText(Launcher.getAppLanguage().getString("invalid"));
                    textField1.setText("");
                }
                if (!GUIValidators.validateYCoordinate(textField3.getText())) {
                    error = true;
                    textField3.setText("");
                    textField3.setPromptText(Launcher.getAppLanguage().getString("invalid"));
                }
                if (!GUIValidators.validateXLocation(textField4.getText())) {
                    error = true;
                    textField4.setText("");
                    textField4.setPromptText(Launcher.getAppLanguage().getString("invalid"));
                }
                if (!GUIValidators.validateYLocation(textField5.getText())) {
                    error = true;
                    textField5.setText("");
                    textField5.setPromptText(Launcher.getAppLanguage().getString("invalid"));
                }
                if (!GUIValidators.validateHeight(textField6.getText())) {
                    error = true;
                    textField6.setText("");
                    textField6.setPromptText(Launcher.getAppLanguage().getString("invalid"));
                }
                if (toggleGroup1.getSelectedToggle() == null) {
                    error = true;
                    label7.setTextFill(Color.RED);
                }
                if (toggleGroup2.getSelectedToggle() == null) {
                    error = true;
                    label8.setTextFill(Color.RED);
                }
                if (datePicker.getValue() == null) {
                    error = true;
                    label9.setTextFill(Color.RED);
                }
                if (error) return;
                Person person = new Person();
                person.setName(textField1.getText());
                person.setCoordinates(new Coordinates(Long.parseLong(textField2.getText()), Integer.parseInt(textField3.getText())));
                person.setLocation(new Location(Long.parseLong(textField4.getText()), Float.parseFloat(textField5.getText())));
                person.setHeight(Long.parseLong(textField6.getText()));
                person.setEyeColor(EyeColor.valueOf(toggleGroup2.getSelectedToggle().getUserData().toString()));
                person.setNationality(Country.valueOf(toggleGroup1.getSelectedToggle().getUserData().toString()));
                person.setBirthday(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                if (controller.addPersonToCollection(person)) {
                    displayPerson(person);
//                    double x = person.getCoordinates().getX();
//                    double y = person.getCoordinates().getY();
//                    createStars(pane_map, x, y, parseHeightToStars(person.getHeight()));
                }
            }
            button.getScene().getWindow().hide();
        });
        Scene scene = new Scene(gridPane, 670, 300);
        scene.getStylesheets().add("/css/style.css");
        button.getStyleClass().add("submit-button");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @FXML
    private Label label_info;
    @FXML
    private Label label_text_type;
    @FXML
    private Label label_text_creation;
    @FXML
    private Label label_text_number;
    @FXML
    private Label label_type;
    @FXML
    private Label label_creation;
    @FXML
    private Label label_number;
    @FXML
    private Button close_button_info;
    private void showInfo(){
        pane_map.setVisible(false);
        Stage primaryStage = new Stage();
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        Scene scene;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/info.fxml"));
        fxmlLoader.setController(this);
        try {
            scene = new Scene(fxmlLoader.load(), this.width, this.height);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        label_text_type.setText(Launcher.getAppLanguage().getString("type"));
        label_type.setText(LinkedHashSet.class.getTypeName().split("\\.")[2]);
        label_text_number.setText(Launcher.getAppLanguage().getString("amount"));
        label_number.setText(String.valueOf(controller.getPersonList().size()));
        label_text_creation.setText(Launcher.getAppLanguage().getString("init"));
        label_creation.setText(controller.getSource().getCreationDate().format(DateTimeFormatter.ofPattern(Launcher.getAppLanguage().getString("date_format"))));
        label_info.setText(Launcher.getAppLanguage().getString("collection_info"));
        primaryStage.setScene(scene);
        primaryStage.show();
        close_button_info.setOnAction(event -> {
            primaryStage.hide();
            pane_map.setVisible(true);
        });
    }
    @FXML
    private Button close_button_profile;
    @FXML
    private Label label_your_profile;
    @FXML
    private TableView<Person> table_profile;
    @FXML
    private TableColumn<Person, String> column_profile_name;
    @FXML
    private TableColumn<Person, Integer> column_profile_id;
    @FXML
    private Label label_nickname;
    @FXML
    private MenuButton mb_language;
    @FXML
    private ImageView iv_avatar;
    @FXML
    private javafx.scene.control.MenuItem mi_ru;
    @FXML
    private javafx.scene.control.MenuItem mi_en;
    @FXML
    private javafx.scene.control.MenuItem mi_dk;
    @FXML
    private javafx.scene.control.MenuItem mi_fi;
    private void openProfile(){
        pane_map.setVisible(false);
        Stage primaryStage = new Stage();
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.setWidth(729);
        primaryStage.setHeight(537);

        Scene scene;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/profile_inside.fxml"));
        fxmlLoader.setController(this);
        try {
            scene = new Scene(fxmlLoader.load(), this.width, this.height);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        label_your_profile.setText(Launcher.getAppLanguage().getString("profile"));
        label_nickname.setText(userManager.getUserName());
        iv_avatar.setImage(new Image(parseNickNameToImage(userManager.getUserName())));

        setTableColumnsInProfile();
        setDataToTableInProfile();


        primaryStage.setScene(scene);
        primaryStage.show();
        close_button_profile.setOnAction(event -> {
            primaryStage.hide();
            pane_map.setVisible(true);
        });
        mi_ru.setOnAction(event -> {
            Launcher.setAppLanguage(Languages.ru);
        });
        mi_dk.setOnAction(event -> {
            Launcher.setAppLanguage(Languages.dk);
        });
        mi_fi.setOnAction(event -> {
            Launcher.setAppLanguage(Languages.fi);
        });
        mi_en.setOnAction(event -> {
            Launcher.setAppLanguage(Languages.en);
        });
    }
    private void setTableColumnsInProfile() {
        column_profile_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_profile_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    }
    private void setDataToTableInProfile(){
        table_profile.setItems(FXCollections.observableList(controller.getPersonList()
                .stream().filter(p -> p.getCreator().equals(userManager.getUserName()))
                .toList()));
    }
    private void createStars(Pane root, double centerX, double centerY, int starsNum) {
        for (int i = 0; i < starsNum; i++) {
            double angle = i * 2 * Math.PI / starsNum;
            double starX = centerX + RADIUS * Math.cos(angle);
            double starY = centerY + RADIUS * Math.sin(angle);

            Polygon star = createStar(starX, starY, STAR_RADIUS);
            root.getChildren().add(star);

            animateStar(star);
        }
    }

    private Polygon createStar(double x, double y, double radius) {
        Polygon star = new Polygon();
        double angleIncrement = 2 * Math.PI / 5;
        double angle = -Math.PI / 2;
        for (int i = 0; i < 5; i++) {
            double outerX = x + radius * Math.cos(angle);
            double outerY = y + radius * Math.sin(angle);
            star.getPoints().add(outerX);
            star.getPoints().add(outerY);

            double innerX = x + radius / 2 * Math.cos(angle + angleIncrement / 2);
            double innerY = y + radius / 2 * Math.sin(angle + angleIncrement / 2);
            star.getPoints().add(innerX);
            star.getPoints().add(innerY);

            angle += angleIncrement;
        }
        star.setFill(Color.YELLOW);
        star.setStroke(Color.ORANGE);
        return star;
    }


    private void animateStar(Polygon star) {
        double startX = star.getTranslateX();
        double startY = star.getTranslateY();
        double endAngle = 360.0;

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(5),
                new KeyValue(star.rotateProperty(), endAngle));
        timeline.getKeyFrames().add(keyFrame);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), star);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        timeline.setOnFinished(event -> {
            fadeOut.play();
            fadeOut.setOnFinished(e -> {
                ((Pane) star.getParent()).getChildren().remove(star);
            });
        });

        timeline.play();
    }

}
