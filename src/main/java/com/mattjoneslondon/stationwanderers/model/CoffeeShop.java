package com.mattjoneslondon.stationwanderers.model;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CoffeeShop extends Group {
    public static final double WIDTH = 60;
    public static final double HEIGHT = 40;

    public CoffeeShop(double x, double y) {
        Rectangle shop = new Rectangle(WIDTH, HEIGHT);
        shop.setTranslateX(x);
        shop.setTranslateY(y);
        
        Text symbol = new Text("☕");
        symbol.setTranslateX(x + WIDTH/2 - 5);
        symbol.setTranslateY(y + HEIGHT/2 + 5);
        
        Text steam = new Text("~");
        steam.setTranslateX(x + WIDTH/2 - 2);
        steam.setTranslateY(y + HEIGHT/2 - 5);
        
        getChildren().addAll(shop, symbol, steam);
    }

    public boolean intersects(double x, double y, double radius) {
        double shopLeft = getTranslateX();
        double shopRight = shopLeft + WIDTH;
        double shopTop = getTranslateY();
        double shopBottom = shopTop + HEIGHT;

        // Expand the shop bounds by the radius for collision detection
        return x + radius > shopLeft && x - radius < shopRight &&
               y + radius > shopTop && y - radius < shopBottom;
    }
} 