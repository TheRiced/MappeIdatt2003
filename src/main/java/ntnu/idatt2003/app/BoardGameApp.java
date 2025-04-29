package ntnu.idatt2003.app;


import javafx.application.Application;
import javafx.stage.Stage;
import ntnu.idatt2003.controller.HomeController;
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