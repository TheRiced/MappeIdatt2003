package ntnu.idatt2003.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;
import ntnu.idatt2003.controller.HomeController;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.factory.BoardGameFactory;
import ntnu.idatt2003.file.BoardFileReader;
import ntnu.idatt2003.file.BoardFileReaderGson;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.SnakeAndLadderGame;
import ntnu.idatt2003.model.Tile;
import ntnu.idatt2003.view.HomePage;

/**
 * Application to run the Snakes and Ladders board game.
 * Handles user input for setting up the game (players, dice) and starts the game.
 */
public class BoardGameApp extends Application {
    @Override
    public void start(Stage primaryStage) {

        HomePage homePage = new HomePage();

        HomeController homeController = new HomeController(primaryStage, homePage);

        homeController.showHome();

    }
    public static void main(String[] args) {
        launch(args);
    }
}