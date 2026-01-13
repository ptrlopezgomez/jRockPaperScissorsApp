package com.motivus.jdeskapp.rps;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RockPaperScissorsController {
    @FXML
    private Label applicationTitle;

    @FXML
    protected void onRockButtonClick() {
        applicationTitle.setText("Rock Paper Scissors Application!");
    }

    @FXML
    protected void onPaperButtonClick() {
        applicationTitle.setText("Rock Paper Scissors Application!");
    }

    @FXML
    protected void onScissorsButtonClick() {
        applicationTitle.setText("Rock Paper Scissors Application!");
    }
}
