package com.mattjoneslondon.stationwanderers.model;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.List;
import javafx.geometry.Point2D;

public class Person extends Circle {
    private final Random random = new Random();
    private double dx;  // velocity in x direction
    private double dy;  // velocity in y direction
    private final double speed;
    private static final double RADIUS = 5.0;
    private Point2D destination;
    private boolean isActive = true;
    private CoffeeShop coffeeShop;
    private static final double COLLISION_BUFFER = 2.0; // Distance to maintain between people

    public Person(double initialX, double initialY, Color color, double speed, Point2D destination, CoffeeShop coffeeShop) {
        super(initialX, initialY, RADIUS);
        this.speed = speed;
        this.destination = destination;
        this.coffeeShop = coffeeShop;
        setFill(color);
        setDirectionToDestination();
    }

    // Add getters for testing
    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    private void setDirectionToDestination() {
        // Calculate direction vector to destination
        double dirX = destination.getX() - getCenterX();
        double dirY = destination.getY() - getCenterY();
        
        // Normalize the direction vector and multiply by speed
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        dx = (dirX / length) * speed;
        dy = (dirY / length) * speed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void move(double width, double height, List<Person> others) {
        if (!isActive) return;

        double newX = getCenterX() + dx;
        double newY = getCenterY() + dy;

        // Check for collision with coffee shop
        if (coffeeShop.intersects(newX, newY, RADIUS)) {
            // Calculate coffee shop bounds
            double shopLeft = coffeeShop.getTranslateX();
            double shopRight = shopLeft + CoffeeShop.WIDTH;
            double shopTop = coffeeShop.getTranslateY();
            double shopBottom = shopTop + CoffeeShop.HEIGHT;
            
            // Calculate closest point on coffee shop perimeter
            double closestX = Math.max(shopLeft, Math.min(newX, shopRight));
            double closestY = Math.max(shopTop, Math.min(newY, shopBottom));
            
            // Calculate vector from closest point to person
            double nx = newX - closestX;
            double ny = newY - closestY;
            double len = Math.sqrt(nx * nx + ny * ny);
            
            if (len > 0) {
                nx /= len;
                ny /= len;
                
                // Move person to edge of coffee shop plus radius
                newX = closestX + nx * (RADIUS + 1);
                newY = closestY + ny * (RADIUS + 1);
                
                // Reflect velocity based on which side was hit
                if (Math.abs(closestX - shopLeft) < 0.1 || Math.abs(closestX - shopRight) < 0.1) {
                    dx = -dx;
                }
                if (Math.abs(closestY - shopTop) < 0.1 || Math.abs(closestY - shopBottom) < 0.1) {
                    dy = -dy;
                }
            }
        }

        // Check for collision with other people
        boolean hadCollision = false;
        for (Person other : others) {
            if (other != this && other.isActive && isColliding(other, newX, newY)) {
                hadCollision = true;
                
                // Calculate vector from other person to current position
                double nx = getCenterX() - other.getCenterX();
                double ny = getCenterY() - other.getCenterY();
                double len = Math.sqrt(nx * nx + ny * ny);
                nx /= len;
                ny /= len;
                
                // Move away from collision while maintaining speed
                double perpX = -ny;
                double perpY = nx;
                double sideMultiplier = Math.signum(dx * perpX + dy * perpY);
                
                // Set new velocity perpendicular to collision while maintaining speed
                dx = perpX * speed * sideMultiplier;
                dy = perpY * speed * sideMultiplier;
                
                // Update position with collision buffer
                newX = other.getCenterX() + (nx * (RADIUS * 2 + COLLISION_BUFFER));
                newY = other.getCenterY() + (ny * (RADIUS * 2 + COLLISION_BUFFER));
                break;
            }
        }

        // Keep within screen bounds
        if (newX - RADIUS <= 0) newX = RADIUS;
        if (newX + RADIUS >= width) newX = width - RADIUS;
        if (newY - RADIUS <= 0) newY = RADIUS;
        if (newY + RADIUS >= height) newY = height - RADIUS;

        setCenterX(newX);
        setCenterY(newY);

        // Check if we've reached the destination (within a small threshold)
        double distToDestination = Math.sqrt(
            Math.pow(getCenterX() - destination.getX(), 2) +
            Math.pow(getCenterY() - destination.getY(), 2)
        );
        
        // Use a larger threshold to ensure we catch the destination
        if (distToDestination < RADIUS * 2) {
            isActive = false;
            return;
        }

        // Only adjust direction if we haven't had a collision
        if (!hadCollision && random.nextDouble() < 0.02) { // 2% chance each frame
            setDirectionToDestination();
        }
    }

    private boolean isColliding(Person other, double newX, double newY) {
        double distance = Math.sqrt(
            Math.pow(newX - other.getCenterX(), 2) +
            Math.pow(newY - other.getCenterY(), 2)
        );
        return distance <= (RADIUS * 2 + COLLISION_BUFFER);
    }
} 