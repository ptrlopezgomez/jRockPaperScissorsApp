package com.motivus.jdeskapp.rps;

import com.motivus.jdeskapp.rps.factory.GameButtonBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class RockPaperScissorsApp extends Application {

    public static final String MESSAGE_WAIT = """
            Wait!. Click <START> button and wait  
            until countdown reaches to <1 seconds>
            """;
    public static final int WAITING_TIME_PLAYER_THRESHOLD = 2;
    public static final String TIE_MESSAGE = "It's a TIE!!, try Again!!";
    public static final String TOO_SLOW_TRY_AGAIN = "Too slow!!, try again!!";
    public static final String DIALOG_TITLE_HOLD_YOUR_HORSES = "Hold your horses!";
    private Timeline timeline;
    private Integer secondsLeft = 3;
    private Text labelSeconds;
    private Label countDownCounter;
    private Text labelPlayer;
    private Label scorePlayer;
    private Text labelComputer;
    private Label scoreComputer;
    private Label defaultLabelChoice = new Label("Choice: ?");

    private Integer playerChoice;
    private Integer computerChoice;
    private final static String[] handSigns = new String[] { "ROCK", "PAPER", "SCISSORS"};

    /*
    ------------------------------------------------------
    Win combinations, or rules ...
          if R (0) and P (1) then P (1) --> concatenation in handsigns = (01) -> winner = 1
          if R (0) and S (2) then R (0) --> concatenation in handsigns = (02) -> winner = 0
          if P (1) and S (2) then S (2) --> concatenation in handsigns = (12) -> winner = 2
    ------------------------------------------------------
    This is a graphical illustration:
     R  P  S    W
     0  1  -    1
     0  -  2    0
     -  1  2    2
    ------------------------------------------------------
    Repeated values means TIE (same handsign both players)
    In a map, keys will be the concatenations, and the value is the winner:
     */
    Map<String, Integer> gameWinnerRules = Map.of(
            "01", 1,
            "02", 0,
            "12", 2
    );

    public Label getDefaultLabelChoice() {
        Label label = new Label(defaultLabelChoice.getText());
        label.setFont(defaultFont);
        return label;
    }

    private StackPane computerScorePanel;
    private StackPane playerScorePanel;
    private StackPane countDownScorePanel;

    private final Font defaultFont = Font.font("Verdana", FontWeight.BOLD, 16);

    GameButtonBuilder bStart;
    GameButtonBuilder bRock;
    GameButtonBuilder bPaper;
    GameButtonBuilder bScissors;

    @Override
    public void start(Stage stage) throws IOException {
        // BorderPane as root to position buttons at bottom
        BorderPane root = new BorderPane();
        initTimeline();

        // Create the labels
        createScoreLabelsAndPanels(root);

        // Create the buttons
        createButtons(root);

        Scene scene = new Scene(root, 400, 480);
        stage.setTitle("Rock-Paper-Scissors App");
        stage.setScene(scene);
        stage.show();
    }

    private void updateCountDown() {
        labelSeconds.setText("Seconds. Remaining: " + String.valueOf(secondsLeft));
    }


    private void createScorePanels() {
        Rectangle computerScoreRect = new Rectangle(120, 80);
        computerScoreRect.setFill(null);
        computerScoreRect.setStroke(Color.BLACK);
        Rectangle playerScoreRect = new Rectangle(120, 80);
        playerScoreRect.setFill(null);
        playerScoreRect.setStroke(Color.BLACK);
        Rectangle countDownTimer = new Rectangle(120, 80);
        countDownTimer.setFill(null);
        countDownTimer.setStroke(Color.RED);

        scoreComputer = getDefaultLabelChoice();
        scoreComputer.setFont(defaultFont);
        scorePlayer = getDefaultLabelChoice();
        scorePlayer.setFont(defaultFont);
        countDownCounter = new Label("Winner: ?");
        countDownCounter.setFont(defaultFont);

        computerScorePanel = new StackPane(computerScoreRect, scoreComputer);
        playerScorePanel = new StackPane(playerScoreRect, scorePlayer);
        countDownScorePanel = new StackPane(countDownTimer, countDownCounter);
    }

    private void createScoreLabelsAndPanels(BorderPane rootPanel) {
        labelComputer = new Text("Computer: 0");
        labelComputer.setFont(defaultFont);
        labelPlayer = new Text("Player: 0");
        labelPlayer.setFont(defaultFont);
        labelSeconds = new Text("Seconds. Remaining: 3");
        labelSeconds.setFont(defaultFont);

        createScorePanels();

        VBox labelsBox = new VBox(15,
                labelComputer, computerScorePanel, labelPlayer,
                playerScorePanel, labelSeconds, countDownScorePanel);
        labelsBox.setAlignment(Pos.CENTER);
        rootPanel.setTop(labelsBox);
    }

    private void displayMessageDialog(String message) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(DIALOG_TITLE_HOLD_YOUR_HORSES);
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.show();
    }

    private void createButtons(BorderPane rootPanel) {
        bStart = new GameButtonBuilder()
                .addText("Start")
                .addIcon(getImageByType("START"))
                .setPrefSize(90, 50);
        bRock = new GameButtonBuilder()
                .addText(" ")
                .addIcon(getImageByType("ROCK"))
                .setPrefSize(90, 50);
        bPaper = new GameButtonBuilder()
                .addText(" ")
                .addIcon(getImageByType("PAPER"))
                .setPrefSize(90, 50);
        bScissors = new GameButtonBuilder()
                .addText(" ")
                .addIcon(getImageByType("SCISSORS"))
                .setPrefSize(90, 50);

        // HBox for horizontal alignment of buttons
        HBox buttonBox = new HBox(10, bStart.build(), bRock.build(), bPaper.build(), bScissors.build()); // 10px spacing
        buttonBox.setStyle("-fx-alignment: center; -fx-padding: 10;");
        rootPanel.setBottom(buttonBox);

        createButtonActions();
    }

    private void initTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            this.secondsLeft--;
            updateCountDown();
            if (this.secondsLeft<=0) {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.statusProperty()
                .addListener((obs, old, newStatus) -> {
                    System.out.println("Status: " + newStatus);
                    if (newStatus == Animation.Status.STOPPED) {
                        triggerComputerChoice();
                    }
                });
    }

    private void triggerComputerChoice() {
        if (this.playerChoice == null) {
            displayMessageDialog(TOO_SLOW_TRY_AGAIN);
            clearChoicesForRoles();
        } else {
            this.computerChoice = new Random().nextInt(0, handSigns.length);
            updateChoiceByRol("COMPUTER", this.computerChoice);
            if (this.playerChoice == this.computerChoice) {
                displayMessageDialog(TIE_MESSAGE);
            } else {
                compareHands();
            }
        }
    }

    private void clearChoicesForRoles() {
        StackPane rolPanel = getStackPaneForRol("PLAYER");
        rolPanel.getChildren().removeLast();
        rolPanel.getChildren().add(1, getDefaultLabelChoice());
    }

    private void compareHands() {
        // At this point we can create the map of roles, so we can decide which rol WINS
        Map<Integer, String> roles = Map.of(
                this.playerChoice, "PLAYER",
                this.computerChoice, "COMPUTER"
        );
        // List to be concatenated to validate against the rules
        List<String> lst = new ArrayList<>(List.of(String.valueOf(this.playerChoice), String.valueOf(this.computerChoice)));
        // Must be sorted ascending as we saw in the rule mapper
        lst.sort(Comparator.naturalOrder());
        // Using reduce function to contatenate the list after sorting the values
        String result = lst.stream().reduce("", (val1, val2) -> val1 + val2);

        Integer winner = gameWinnerRules.get(result);
        System.out.println("concatenation result: " + result);
        System.out.println("winner: " + winner);
        if (winner == null) {
            displayMessageDialog(TIE_MESSAGE);
        } else {
            String winnerRol = roles.get(winner);
            Text winnerLabel = getTextforRol(winnerRol);
            updateLabelScore(winnerLabel);
            updateWinnerStackPane(winnerRol);
            displayMessageDialog("<"+winnerRol+"> WINS!!");
        }
    }

    private void updateWinnerStackPane(String winnerRol) {
        StackPane displayPanel = countDownScorePanel;
        displayPanel.getChildren().removeLast();
        ImageView imageView = getImageViewByRol(winnerRol);
        displayPanel.getChildren().add(1, imageView);
    }

    private ImageView getImageViewByRol(String winnerRol) {
        ImageView imageView = new ImageView(getImageByType(winnerRol));
        imageView.setFitWidth(55);
        imageView.setFitHeight(55);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private Image getImageByType(String type){
        return switch(type.toUpperCase()) {
            case "PLAYER" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/player.png")));
            case "COMPUTER" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/computer.png")));
            case "TROPHY" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/trophy.png")));
            case "ROCK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hand-rock.png")));
            case "PAPER" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hand-paper.png")));
            case "SCISSORS" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/hand-scissors.png")));
            case "START" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/rock-paper-scissors.png")));
            default -> throw new IllegalStateException("Unexpected value: " + type.toUpperCase());
        };
    }

    private StackPane getStackPaneForRol(String rol) {
        return switch (rol.toUpperCase()) {
            case "PLAYER" -> playerScorePanel;
            case "COMPUTER" -> computerScorePanel;
            default -> throw new IllegalStateException("Unexpected value: " + rol);
        };
    }

    private void updateLabelScore(Text rol) {
        Integer score = getLastScoreFromText(rol.getText());
        String text = rol.getText();
        String replacedText = text.replaceAll("\\d", "");
        rol.setText(replacedText + (++score));
    }

    public Text getTextforRol(String rol) {
        return switch (rol) {
            case "PLAYER" ->  labelPlayer;
            case "COMPUTER" -> labelComputer;
            default -> throw new IllegalStateException("Unexpected value: " + rol);
        };
    }

    public Integer getLastScoreFromText(String text) {
        String digitStr = text.replaceAll("[^0-9]", "");
        return Integer.parseInt(digitStr);
    }

    private void createButtonActions() {
        bStart.setOnClickAction(e-> {

            this.computerChoice = null;
            this.playerChoice = null;
            clearChoicesForRoles();

            if (timeline!=null) {
                timeline.stop();
            }

            secondsLeft = 3;
            updateCountDown();
            timeline.play();
        });

        bRock.setOnClickAction(e -> {
            if (secondsLeft < WAITING_TIME_PLAYER_THRESHOLD && timeline!=null && timeline.getStatus() == Animation.Status.RUNNING) {
                this.playerChoice = 0;
                updateChoiceByRol("PLAYER", this.playerChoice);
            } else {
                displayMessageDialog(MESSAGE_WAIT);
            }
        });

        bPaper.setOnClickAction(e -> {
            if (secondsLeft < WAITING_TIME_PLAYER_THRESHOLD && timeline!=null && timeline.getStatus() == Animation.Status.RUNNING) {
                this.playerChoice = 1;
                updateChoiceByRol("PLAYER", this.playerChoice);
            } else {
                displayMessageDialog(MESSAGE_WAIT);
            }
        });

        bScissors.setOnClickAction(e -> {
            if (secondsLeft < WAITING_TIME_PLAYER_THRESHOLD && timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
                this.playerChoice = 2;
                updateChoiceByRol("PLAYER", this.playerChoice);
            } else {
                displayMessageDialog(MESSAGE_WAIT);
            }
        });
    }

    private void updateChoiceByRol(String rol, Integer choice) {
        StackPane rolPanel = getStackPaneForRol(rol);
        ImageView imageView = new ImageView(getImageByType(handSigns[choice]));
        imageView.setFitWidth(55);
        imageView.setFitHeight(55);
        imageView.setPreserveRatio(true);
        rolPanel.getChildren().removeLast();
        rolPanel.getChildren().add(1, imageView);

    }
}
