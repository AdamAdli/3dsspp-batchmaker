package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import javax.swing.*;
import javafx.scene.image.Image;

public class LoadingJFXButton extends Button {
    private static Image cachedLoadingImage;
    static {
        ClassLoader cldr = LoadingJButton.class.getClassLoader();
        java.net.URL imageURL = cldr.getResource("loading_blue.gif");
        try {
            cachedLoadingImage = new Image(imageURL.toString());
        } catch (Exception ex) {

        }

    }
    private ImageView loadingIcon;

    public LoadingJFXButton() {
        super();
        heightProperty().addListener((observable, oldValue, newValue) -> {
            loadIcon(newValue.intValue());
        });
    }

    public LoadingJFXButton(String text) {
        super(text);
        heightProperty().addListener((observable, oldValue, newValue) -> {
            loadIcon(newValue.intValue());
        });
    }

    public LoadingJFXButton(String text, Node graphic) {
        super(text, graphic);
        heightProperty().addListener((observable, oldValue, newValue) -> {
            loadIcon(newValue.intValue());
        });
    }

    private void loadIcon(int size) {
        if (size > 0) {
            if (loadingIcon == null) {
                loadingIcon = new ImageView(cachedLoadingImage);
            }
            loadingIcon.setFitWidth(size - 8);
            loadingIcon.setFitHeight(size - 8);
            //loadingIcon = cachedLoadingImage.getScaledInstance(size - 8, size- 8, Image.SCALE_DEFAULT);
        }
    }

    String oldText;

    public void showProgress() {
        oldText = getText();
        setMinWidth(getWidth());
        setGraphic(loadingIcon);
        setText("");
    }

    public void hideProgress() {
        setGraphic(null);
        setText(oldText);
        setMinWidth(USE_COMPUTED_SIZE);
    }
}
