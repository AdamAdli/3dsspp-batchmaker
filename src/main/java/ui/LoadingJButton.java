package ui;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoadingJButton extends JButton {
    private static Image cachedLoadingImage;
    static {
        ClassLoader cldr = LoadingJButton.class.getClassLoader();
        java.net.URL imageURL = cldr.getResource("loading_blue.gif");
        cachedLoadingImage = new ImageIcon(imageURL).getImage();
    }
    private ImageIcon loadingIcon;

    public LoadingJButton() {
        super();
    }

    public LoadingJButton(Icon icon) {
        super(icon);
    }

    public LoadingJButton(String text) {
        super(text);
    }

    public LoadingJButton(Action a) {
        super(a);
    }

    public LoadingJButton(String text, Icon icon) {
        super(text, icon);
    }

    @Override
    protected void init(String text, Icon icon) {
        super.init(text, icon);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loadIcon(e.getComponent().getHeight());
                super.componentResized(e);
            }
        });
    }

    private void loadIcon(int size) {
        if (size > 0) loadingIcon = new ImageIcon(cachedLoadingImage.getScaledInstance(size - 8, size- 8, Image.SCALE_DEFAULT));
    }

    String oldText;

    public void showProgress() {
        oldText = getText();
        setIcon(loadingIcon);
        setText("");
    }

    public void hideProgress() {
        setIcon(null);
        setText(oldText);
    }
}
