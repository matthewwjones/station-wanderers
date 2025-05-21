import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(ApplicationExtension.class)
class StationWanderersAppTest {
    private Scene scene;
    private Stage stage;

    @Start
    private void start(Stage stage) {
        this.stage = stage;
        StationWanderersApp app = new StationWanderersApp();
        app.start(stage);
        scene = stage.getScene();
    }

    @Test
    void shouldCreateInitialPeople(FxRobot robot) {
        var people = scene.getRoot().getChildrenUnmodifiable().filtered(node -> node instanceof Person);
        assertThat(people.size(), is(10)); // Initial people count
    }

    @Test
    void shouldPositionPeopleAtGates(FxRobot robot) {
        var people = scene.getRoot().getChildrenUnmodifiable().filtered(node -> node instanceof Person)
            .stream()
            .map(node -> (Person) node)
            .collect(Collectors.toList());

        // All people should start at y = GATE_MARGIN
        assertAll(
            people.stream()
                .map(person -> () -> assertThat(person.getCenterY(), is(20.0))) // GATE_MARGIN
        );

        // X positions should be within the valid range for gates
        double minX = 20.0; // First gate x position
        double maxX = scene.getWidth() - 20.0; // Last gate x position
        
        assertAll(
            people.stream()
                .map(person -> () -> assertThat(person.getCenterX(), 
                    both(greaterThanOrEqualTo(minX)).and(lessThanOrEqualTo(maxX))))
        );
    }

    @Test
    void shouldCreateStationExit(FxRobot robot) {
        var exit = scene.getRoot().getChildrenUnmodifiable().filtered(node -> 
            node instanceof StationExit)
            .stream()
            .findFirst()
            .orElse(null);
        
        assertThat(exit, is(notNullValue()));
        
        // Check position is in bottom right
        double expectedX = scene.getWidth() - 20 - 40; // GATE_MARGIN + half width
        double expectedY = scene.getHeight() - 20 - 20; // GATE_MARGIN + half height
        
        assertAll(
            () -> assertThat(exit.getTranslateX() + 40, closeTo(expectedX, 0.1)), // 40 is half width
            () -> assertThat(exit.getTranslateY() + 20, closeTo(expectedY, 0.1)) // 20 is half height
        );
    }

    @Test
    void shouldCreateCoffeeShop(FxRobot robot) {
        var coffeeShop = scene.getRoot().getChildrenUnmodifiable().filtered(node -> 
            node instanceof CoffeeShop)
            .stream()
            .findFirst()
            .orElse(null);
        
        assertThat(coffeeShop, is(notNullValue()));
        
        // Check position is in center
        assertAll(
            () -> assertThat(coffeeShop.getTranslateX() + 30, closeTo(scene.getWidth() / 2, 0.1)), // 30 is half width
            () -> assertThat(coffeeShop.getTranslateY() + 20, closeTo(scene.getHeight() / 2, 0.1)) // 20 is half height
        );
    }

    @Test
    void shouldCreateTwentyFourGates(FxRobot robot) {
        var gates = scene.getRoot().getChildrenUnmodifiable().filtered(node -> 
            node instanceof Gate && ((Gate) node).getNumber() != -1);
        assertThat(gates.size(), is(24));
    }

    @Test
    void shouldCreateJubileeLineGate(FxRobot robot) {
        var jubileeGate = scene.getRoot().getChildrenUnmodifiable().filtered(node -> 
            node instanceof Gate && ((Gate) node).getLabel().equals("Jubilee Line"))
            .stream()
            .findFirst()
            .orElse(null);
        
        assertThat(jubileeGate, is(notNullValue()));
        Gate gate = (Gate) jubileeGate;
        assertThat(gate.getLabel(), is("Jubilee Line"));
        assertThat(gate.getNumber(), is(-1));
    }

    @Test
    void shouldNumberGatesCorrectly(FxRobot robot) {
        var gates = scene.getRoot().getChildrenUnmodifiable().filtered(node -> 
            node instanceof Gate && ((Gate) node).getNumber() != -1)
            .stream()
            .map(node -> (Gate) node)
            .collect(Collectors.toList());

        // Verify gate numbers are 1 through 24
        assertAll(
            gates.stream()
                .map(gate -> () -> assertThat(gate.getNumber(), 
                    both(greaterThanOrEqualTo(1)).and(lessThanOrEqualTo(24))))
        );

        // Verify each number is used exactly once
        List<Integer> numbers = gates.stream()
            .map(Gate::getNumber)
            .sorted()
            .collect(Collectors.toList());
        
        assertThat(numbers, is(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 
            13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)));
    }

    @Test
    void shouldHaveWhiteBackground(FxRobot robot) {
        String backgroundColor = scene.getRoot().getStyle();
        assertThat(backgroundColor, containsString("-fx-background-color: white"));
    }

    @Test
    void shouldHaveCorrectWindowTitle(FxRobot robot) {
        assertThat(stage.getTitle(), is("Station Wanderers"));
    }

    @Test
    void shouldHaveCorrectWindowDimensions(FxRobot robot) {
        assertAll(
            () -> assertThat(scene.getWidth(), is(800.0)),
            () -> assertThat(scene.getHeight(), is(600.0))
        );
    }
} 