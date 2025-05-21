import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.geometry.Bounds;

public class CoffeeShop extends Group {
    public static final double WIDTH = 80;
    public static final double HEIGHT = 40;
    private final Rectangle bounds;
    
    public CoffeeShop(double x, double y) {
        // Create the shop building
        bounds = new Rectangle(WIDTH, HEIGHT);
        bounds.setFill(Color.BROWN);
        bounds.setStroke(Color.BLACK);
        
        // Create coffee cup symbol
        Circle cup = new Circle(8);
        cup.setFill(Color.WHITE);
        cup.setStroke(Color.BLACK);
        cup.setTranslateX(WIDTH/4);
        cup.setTranslateY(HEIGHT/2);
        
        // Add steam lines above cup
        for (int i = 0; i < 3; i++) {
            Rectangle steam = new Rectangle(2, 5);
            steam.setFill(Color.WHITE);
            steam.setTranslateX(WIDTH/4 - 5 + (i * 5));
            steam.setTranslateY(HEIGHT/2 - 12);
            steam.setRotate(15 - (i * 15)); // Varying angles for steam
            getChildren().add(steam);
        }
        
        // Create "COFFEE" text
        Text label = new Text("COFFEE");
        label.setFill(Color.WHITE);
        label.setFont(Font.font(12));
        label.setTranslateX(WIDTH/2 + 5);
        label.setTranslateY(HEIGHT/2 + 5);
        
        // Add all elements
        getChildren().addAll(bounds, cup, label);
        
        // Position the entire shop
        setTranslateX(x - WIDTH/2);
        setTranslateY(y - HEIGHT/2);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean intersects(double x, double y, double radius) {
        // Get the bounds in scene coordinates
        Bounds localBounds = bounds.getBoundsInLocal();
        Bounds sceneBounds = bounds.localToScene(localBounds);
        
        // Check if the point (with radius) intersects the rectangle
        return sceneBounds.intersects(x - radius, y - radius, radius * 2, radius * 2);
    }
} 