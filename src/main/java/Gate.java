import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Gate extends StackPane {
    private static final double WIDTH = 30;
    private static final double HEIGHT = 20;
    private final int number;
    private final String label;

    public Gate(int number, double x, double y) {
        this(number, String.valueOf(number), x, y, WIDTH);
    }

    public Gate(String label, double x, double y, double width) {
        this(-1, label, x, y, width);
    }

    private Gate(int number, String label, double x, double y, double width) {
        this.number = number;
        this.label = label;
        
        // Create the platform rectangle
        Rectangle platform = new Rectangle(width, HEIGHT);
        platform.setFill(Color.LIGHTGRAY);
        platform.setStroke(Color.BLACK);
        
        // Create the label text
        Text labelText = new Text(label);
        labelText.setFill(Color.BLACK);
        if (number == -1) { // Special named gate
            labelText.setFont(Font.font(10)); // Smaller font for longer text
        }
        
        // Add both to the StackPane
        getChildren().addAll(platform, labelText);
        
        // Position the gate
        setTranslateX(x - width/2);
        setTranslateY(y);
    }

    public int getNumber() {
        return number;
    }

    public String getLabel() {
        return label;
    }
} 