package com.mattjoneslondon.stationwanderers.model;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Gate extends Group {
    private final double width;
    private final double height = 20;

    public Gate(String label, double x, double y, double width) {
        this.width = width;
        
        Rectangle gate = new Rectangle(width, height);
        gate.setTranslateX(x);
        gate.setTranslateY(y);
        
        Text text = new Text(label);
        text.setTranslateX(x + width/2 - text.getBoundsInLocal().getWidth()/2);
        text.setTranslateY(y + height/2 + text.getBoundsInLocal().getHeight()/4);
        
        getChildren().addAll(gate, text);
    }

    public Gate(int number, double x, double y) {
        this(String.valueOf(number), x, y, 40);
    }
} 