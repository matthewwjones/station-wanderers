package com.mattjoneslondon.stationwanderers.ui;

import com.mattjoneslondon.stationwanderers.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StationWanderersApp extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final double NORMAL_SPEED = 0.5;
    private static final double FAST_SPEED = 1.0;
    private static final int GATE_COUNT = 24;
    private static final double GATE_MARGIN = 20.0;
    private static final double STANDARD_GATE_WIDTH = 80.0;
    private static final Color PERSON_COLOR = Color.BLUE;
    private static final double SPAWN_RATE = 5.0; // people per second
    
    private final Random random = new Random();
    private List<Person> people;
    private Point2D exitPoint;
    private Point2D jubileePoint;
    private List<Point2D> startingPoints;
    private Pane root;
    private CoffeeShop coffeeShop;
    private long lastSpawnTime = 0;

    @Override
    public void start(Stage stage) {
        // Create root pane
        root = new Pane();
        root.setStyle("-fx-background-color: white;");

        // Initialize collections
        people = new ArrayList<>();
        startingPoints = new ArrayList<>();

        // Create gates and collect starting points
        createGates(root);
        
        // Create Jubilee Line gate and store its position
        Gate jubileeGate = new Gate("Jubilee Line", GATE_MARGIN, HEIGHT - GATE_MARGIN - 20, STANDARD_GATE_WIDTH);
        root.getChildren().add(jubileeGate);
        jubileePoint = new Point2D(GATE_MARGIN, HEIGHT - GATE_MARGIN - 20);

        // Create coffee shop in the center
        coffeeShop = new CoffeeShop(WIDTH/2, HEIGHT/2);
        root.getChildren().add(coffeeShop);

        // Create station exit and store its position
        StationExit exit = new StationExit(WIDTH - GATE_MARGIN - 40, HEIGHT - GATE_MARGIN - 20);
        root.getChildren().add(exit);
        exitPoint = new Point2D(WIDTH - GATE_MARGIN - 40, HEIGHT - GATE_MARGIN - 20);

        // Create scene
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Station Wanderers");

        // Set up animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePeople(now);
                checkSpawnNewPerson(now);
            }
        };
        timer.start();

        stage.show();
    }

    private void checkSpawnNewPerson(long now) {
        // Convert nanoseconds to seconds
        double currentTime = now / 1_000_000_000.0;
        double lastTime = lastSpawnTime / 1_000_000_000.0;
        
        // Calculate time since last spawn
        double timeSinceLastSpawn = currentTime - lastTime;
        
        // Calculate probability of spawning this frame
        // Using exponential distribution for random intervals
        double spawnProbability = 1 - Math.exp(-SPAWN_RATE * timeSinceLastSpawn);
        
        if (random.nextDouble() < spawnProbability) {
            createNewPerson(true);
            lastSpawnTime = now;
        }
    }

    private void createGates(Pane root) {
        double gateSpacing = (WIDTH - 40) / (GATE_COUNT - 1);
        
        for (int i = 0; i < GATE_COUNT; i++) {
            double x = 20 + (i * gateSpacing);
            Gate gate = new Gate(i + 1, x, GATE_MARGIN);
            root.getChildren().add(gate);
            startingPoints.add(new Point2D(x, GATE_MARGIN));
        }
    }

    private void updatePeople(long now) {
        // Lists to track changes
        List<Person> peopleToRemove = new ArrayList<>();

        // Update existing people
        for (Person person : people) {
            person.move(WIDTH, HEIGHT, people);
            
            // Mark inactive people for removal
            if (!person.isActive()) {
                peopleToRemove.add(person);
            }
        }

        // Apply changes after iteration
        for (Person person : peopleToRemove) {
            root.getChildren().remove(person);
            people.remove(person);
        }
    }

    private Person createNewPerson(boolean addToLists) {
        // Choose random starting point
        Point2D start = startingPoints.get(random.nextInt(startingPoints.size()));
        
        // Choose random destination (exit or Jubilee Line)
        Point2D destination = random.nextBoolean() ? exitPoint : jubileePoint;
        
        // Choose random speed
        double speed = random.nextBoolean() ? NORMAL_SPEED : FAST_SPEED;
        
        // Create new person
        Person person = new Person(start.getX(), start.getY(), PERSON_COLOR, speed, destination, coffeeShop);
        
        // Add to lists if requested
        if (addToLists) {
            people.add(person);
            root.getChildren().add(person);
        }
        
        return person;
    }

    public static void main(String[] args) {
        launch();
    }
} 