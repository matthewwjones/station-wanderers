import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StationExit extends Group {
    private static final double WIDTH = 80;
    private static final double HEIGHT = 40;
    
    public StationExit(double x, double y) {
        // Create the exit sign background
        Rectangle background = new Rectangle(WIDTH, HEIGHT);
        background.setFill(Color.GREEN);
        background.setStroke(Color.BLACK);
        
        // Create "EXIT" text
        Text exitText = new Text("EXIT");
        exitText.setFill(Color.WHITE);
        exitText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        exitText.setTranslateX(10);
        exitText.setTranslateY(HEIGHT/2 + 6); // Adjust for text baseline
        
        // Create arrow pointing right
        Polygon arrow = new Polygon(
            0, 0,          // Point 1
            20, 0,         // Point 2
            20, -5,        // Point 3
            30, 5,         // Point 4 (arrow tip)
            20, 15,        // Point 5
            20, 10,        // Point 6
            0, 10          // Point 7
        );
        arrow.setFill(Color.WHITE);
        arrow.setTranslateX(45);
        arrow.setTranslateY(HEIGHT/2);
        
        // Add all elements
        getChildren().addAll(background, exitText, arrow);
        
        // Position the entire exit sign
        setTranslateX(x - WIDTH/2);
        setTranslateY(y - HEIGHT/2);
    }
} 