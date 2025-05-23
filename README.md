# MappeIdatt2003

A JavaFX application implementing two classic board games: **Snakes & Ladders** and **Ludo**.  
Designed for educational use in the IDATT2003 course at NTNU. The goal with this project is to develop board games using Java.
So far we have implemented two games:
Snakes and Ladders, and Ludo
Where snakes and ladders is the mvp. 

## Features
- Graphical User interface
- Unit Testing
- Games moving x and y coordinates

## Prerequisties
- Java 23

- Maven 4


## Installation
```bash
# 1. CLone the project
git clone https://github.com/YourUsername/MappeIdatt2003.git
   
# 2. Build and run
mvn clean install
mvn javafx:run

```

----

## User Guide
Select Game:
- Snakes + Ladders
- Ludo

Configure
Snakes & Ladders
- Enter number of players (2–5)
- Enter each player’s name, age, and icon

Ludo
- Enter number of players (2–4)
- Enter each player’s name, age, icon, and token color

Click “Roll Dice”.

Tokens move according to the dice roll and game rules.

The first player to reach the final tile wins!


Autosave after each turn. (Ludo only)

On restart, the game will resume from the last saved state.

## Testing
```bash
mvn test
