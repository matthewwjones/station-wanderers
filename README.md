# Station Wanderers

A JavaFX application that simulates people moving through a station, demonstrating crowd movement and collision avoidance.

## Description

Station Wanderers is a visual simulation that shows people (represented as blue circles) moving from entry gates to either the station exit or the Jubilee Line. The simulation includes:

- 24 numbered entry gates at the top of the screen
- A Jubilee Line gate at the bottom left
- A station exit at the bottom right
- A coffee shop in the center that people must navigate around
- Continuous spawning of new people (average 5 per second)
- Realistic collision avoidance between people and obstacles

## Features

- **Realistic Movement**: People move smoothly from their starting gate to their destination
- **Collision Detection**: People avoid colliding with each other and the coffee shop
- **Dynamic Spawning**: New people continuously appear at random intervals
- **Multiple Destinations**: People randomly choose between the station exit and Jubilee Line
- **Variable Speeds**: People move at either normal (0.5) or fast (1.0) speed
- **Visual Elements**:
  - Numbered gates (1-24)
  - Coffee shop with cup symbol and steam
  - Exit sign with arrow
  - Jubilee Line gate

## Requirements

- Java 17 or higher
- JavaFX 17 or higher
- Gradle 7.0 or higher

## Building and Running

1. Clone the repository:
   ```bash
   git clone https://github.com/mattjoneslondon/station-wanderers.git
   cd station-wanderers
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew run
   ```

## Testing

The project includes JUnit tests that verify the movement and collision detection logic. Run the tests with:

```bash
./gradlew test
```

## Project Structure

```
src/
├── main/java/com/mattjoneslondon/stationwanderers/
│   ├── model/
│   │   ├── Person.java         # Person entity with movement logic
│   │   ├── CoffeeShop.java     # Coffee shop obstacle
│   │   ├── Gate.java          # Entry and exit gates
│   │   └── StationExit.java   # Station exit
│   └── ui/
│       └── StationWanderersApp.java  # Main application class
└── test/java/com/mattjoneslondon/stationwanderers/
    └── model/
        └── PersonTest.java     # Unit tests for Person class
```

## How It Works

1. People spawn randomly at one of the 24 top gates
2. Each person is assigned:
   - A random destination (station exit or Jubilee Line)
   - A random speed (normal or fast)
3. People move toward their destination while:
   - Avoiding collisions with other people
   - Navigating around the coffee shop
   - Staying within screen boundaries
4. Upon reaching their destination, people disappear
5. New people continuously spawn to maintain movement in the station

## Contributing

Feel free to submit issues and enhancement requests! 