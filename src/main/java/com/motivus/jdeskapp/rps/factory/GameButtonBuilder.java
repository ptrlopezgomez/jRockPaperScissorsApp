package com.motivus.jdeskapp.rps.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class GameButtonBuilder {
    private GameButton gameButton;
    private Button button;

    public GameButtonBuilder() {
        gameButton = new GameButton(null, null, null);
    }

    public GameButtonBuilder addText(String text) {
        gameButton.setButtonText(text);
        return this;
    }

    public GameButtonBuilder addIcon(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setPreserveRatio(true);
        gameButton.setImageView(imageView);
        return this;
    }

    public GameButtonBuilder setPrefSize(double width, double heigh) {
        gameButton.setPrefSize(width, heigh);
        return this;
    }

    public Button build() {
        if (button!=null) {
            return button;
        } else {
            if (gameButton.getButtonText() !=null
                    && gameButton.getImageView() != null){
                button = gameButton.getGameButton();
                return button;
            }
            return null;
        }
    }

    public void setOnClickAction(EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }
}
