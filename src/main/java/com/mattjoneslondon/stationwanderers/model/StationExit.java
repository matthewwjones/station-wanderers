package com.mattjoneslondon.stationwanderers.model;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class StationExit extends Group {
    private static final double WIDTH = 40;
    private static final double HEIGHT = 20;

    public StationExit(double x, double y) {
        Rectangle exit = new Rectangle(WIDTH, HEIGHT);
        exit.setTranslateX(x);
        exit.setTranslateY(y);
        
        Text text = new Text("Exit →");
        text.setTranslateX(x + WIDTH/2 - text.getBoundsInLocal().getWidth()/2);
        text.setTranslateY(y + HEIGHT/2 + text.getBoundsInLocal().getHeight()/4);
        
        getChildren().addAll(exit, text);
    }
} 