# Hancha

A classic Snake game written in Java with Swing.

## Features

- Grid-based movement on a 20x20 board
- Wrap-around edges: leave one side of the board and reappear on the opposite side
- Random apple spawning that never lands on the snake
- Self-collision stops the snake
- Moving into the cell the tail is leaving is allowed, as in classic Snake
- Turn buffering that prevents instant 180° reversals

## Controls

| Key | Action |
|-----|--------|
| ↑ | Move up |
| ↓ | Move down |
| ← | Move left |
| → | Move right |

## Requirements

- JDK 27 (the version set in `pom.xml`)
- Maven 3.x

## Build and run

```bash
git clone https://github.com/kevin-297/Hancha.git
cd Hancha
mvn compile
java -cp target/classes com.hancha.Main
```

Or open the project in your IDE (IntelliJ IDEA, Eclipse, etc.) and run `com.hancha.Main`.

## Project structure

```
src/main/java/com/hancha/
├── Main.java   # Application window (JFrame) and entry point
└── Pane.java   # Game loop, rendering (JPanel), input handling and game logic
```

## Configuration

Gameplay constants are defined at the top of `Pane.java`:

| Constant | Description |
|----------|-------------|
| `FPS` | Game ticks per second (controls snake speed) |
| `CELL_SIZE` | Size of each grid cell in pixels |
| `WIDTH`, `HEIGHT` | Board dimensions in cells |

## How it works

- The game logic runs on its own thread at a fixed tick rate, while rendering happens through Swing's `paintComponent`.
- Access to the snake is synchronized between the game thread and the Event Dispatch Thread.
- Each tick adds a new head, then removes the tail unless an apple was eaten.
- Collision is checked before the snake is modified, so a fatal move leaves the snake intact.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
