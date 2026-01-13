package com.motivus.jdeskapp.rps.factory;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameButton {
    protected String buttonText;
    protected Image iconImage;
    protected ImageView imageView;
    protected Button gameButton;
    protected Double prefWidth;
    protected Double prefHeigh;

    public GameButton(String buttonText, Image iconImage, ImageView imageView) {
        this.buttonText = buttonText;
        this.iconImage = iconImage;
        this.imageView = imageView;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public Image getIconImage() {
        return iconImage;
    }

    public void setIconImage(Image iconImage) {
        this.iconImage = iconImage;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Button getGameButton() {
        this.gameButton = new Button(buttonText, imageView);
        this.gameButton.setPrefSize(prefWidth, prefHeigh);
        return this.gameButton;
    }

    public void setGameButton(Button gameButton) {
        this.gameButton = gameButton;
    }

    public void setPrefSize(double width, double heigh) {
        this.prefHeigh = heigh;
        this.prefWidth = width;
    }
}