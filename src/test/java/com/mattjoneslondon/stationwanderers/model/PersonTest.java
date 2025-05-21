package com.mattjoneslondon.stationwanderers.model;

import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class PersonTest {
    private Person person;
    private static final double INITIAL_X = 100.0;
    private static final double INITIAL_Y = 100.0;
    private static final double SPEED = 1.0;
    private static final double DELTA = 0.001;
    private Point2D destination;
    private CoffeeShop coffeeShop;

    @BeforeEach
    void setUp() {
        destination = new Point2D(500, 500);  // Test destination
        coffeeShop = new CoffeeShop(300, 300);  // Test coffee shop
        person = new Person(INITIAL_X, INITIAL_Y, Color.BLUE, SPEED, destination, coffeeShop);
    }

    @Test
    void shouldInitializeWithCorrectValues() {
        assertAll(
            () -> assertThat(person.getCenterX(), closeTo(INITIAL_X, DELTA)),
            () -> assertThat(person.getCenterY(), closeTo(INITIAL_Y, DELTA)),
            () -> assertThat(person.getFill(), is(Color.BLUE)),
            () -> assertThat(person.getRadius(), closeTo(5.0, DELTA))  // Updated to new radius
        );
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0",      // Top-left corner
        "800, 0",    // Top-right corner
        "0, 600",    // Bottom-left corner
        "800, 600"   // Bottom-right corner
    })
    void shouldBounceOffBoundaries(double width, double height) {
        List<Person> others = new ArrayList<>();
        // Move multiple times to ensure we hit boundaries
        for (int i = 0; i < 1000; i++) {
            person.move(width, height, others);
            assertAll(
                () -> assertThat(person.getCenterX(), allOf(
                    greaterThanOrEqualTo(person.getRadius()),
                    lessThanOrEqualTo(width - person.getRadius())
                )),
                () -> assertThat(person.getCenterY(), allOf(
                    greaterThanOrEqualTo(person.getRadius()),
                    lessThanOrEqualTo(height - person.getRadius())
                ))
            );
        }
    }

    @Test
    void shouldMaintainConstantSpeedDuringCollision() {
        Person other = new Person(INITIAL_X + 15, INITIAL_Y, Color.RED, SPEED, destination, coffeeShop);
        List<Person> others = List.of(other);

        // Record initial velocities
        double initialSpeed = Math.sqrt(
            Math.pow(person.getDx(), 2) + Math.pow(person.getDy(), 2)
        );

        // Move until collision occurs
        for (int i = 0; i < 10; i++) {
            person.move(800, 600, others);
        }

        // Check speed after collision
        double finalSpeed = Math.sqrt(
            Math.pow(person.getDx(), 2) + Math.pow(person.getDy(), 2)
        );

        assertThat(finalSpeed, closeTo(initialSpeed, DELTA));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 2.0, 3.0})
    void shouldMaintainConstantSpeedDuringMovement(double testSpeed) {
        person = new Person(INITIAL_X, INITIAL_Y, Color.BLUE, testSpeed, destination, coffeeShop);
        List<Person> others = new ArrayList<>();

        // Move multiple times
        for (int i = 0; i < 100; i++) {
            person.move(800, 600, others);
            double currentSpeed = Math.sqrt(
                Math.pow(person.getDx(), 2) + Math.pow(person.getDy(), 2)
            );
            assertThat(currentSpeed, closeTo(testSpeed, DELTA));
        }
    }

    @Test
    void shouldChangeDirectionOnCollision() {
        Person other = new Person(INITIAL_X + 15, INITIAL_Y, Color.RED, SPEED, destination, coffeeShop);
        List<Person> others = List.of(other);

        // Record initial direction
        double initialDx = person.getDx();
        double initialDy = person.getDy();

        // Move until collision occurs
        for (int i = 0; i < 10; i++) {
            person.move(800, 600, others);
        }

        // Direction should change after collision
        assertAll(
            () -> assertThat(person.getDx(), not(closeTo(initialDx, DELTA))),
            () -> assertThat(person.getDy(), not(closeTo(initialDy, DELTA)))
        );
    }

    @Test
    void shouldNotOverlapDuringCollision() {
        Person other = new Person(INITIAL_X + 15, INITIAL_Y, Color.RED, SPEED, destination, coffeeShop);
        List<Person> others = List.of(other);

        // Move multiple times
        for (int i = 0; i < 100; i++) {
            person.move(800, 600, others);
            other.move(800, 600, List.of(person));

            double distance = Math.sqrt(
                Math.pow(person.getCenterX() - other.getCenterX(), 2) +
                Math.pow(person.getCenterY() - other.getCenterY(), 2)
            );

            assertThat(distance, greaterThanOrEqualTo(person.getRadius() + other.getRadius() - DELTA));
        }
    }
} 