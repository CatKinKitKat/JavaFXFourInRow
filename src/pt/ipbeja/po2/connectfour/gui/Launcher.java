/*
 *  Gonçalo Candeias Amaro - 17440
 *  Connect Four - PO2
 *  Launcher
 */

package pt.ipbeja.po2.connectfour.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.Stage;

import java.util.Optional;

//Game Launcher

public class Launcher extends Application {

    private Stage stage = new Stage();

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        greeter(stage);
    }

    /**
     * launcher
     * @param stage
     * boots the game
     */

    private void launcher(Stage stage) {
        Board board = new Board(this);
        Scene scene = new Scene(board);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Connect Four (a.k.a Four in a Row)"); //My name is Jeff
        stage.show();
    }

    /**
     * greeter
     * @param stage
     * creates a alert box as a greeter for the user
     */

    private void greeter(Stage stage) {
        Alert greeting = new Alert(Alert.AlertType.CONFIRMATION,
                "Welcome to Connect Four");
        greeting.setTitle("Connect Four (a.k.a Four in a Row)");
        greeting.setHeaderText(null);
        greeting.setGraphic(null);

        ButtonType play = new ButtonType("Play!");
        ButtonType exit = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

        greeting.getButtonTypes().setAll(play, exit);

        Optional<ButtonType> pressed = greeting.showAndWait();
        if (pressed.get() == play) {
            launcher(stage);
        } else {
            System.exit(0);         //big function but still under 30 semicolons, cheers
        }
    }

    /**
     * relaunch
     * reboots the game
     */

    public void relaunch() {
        stage.close();
        stage = new Stage();
        launcher(stage);
    }

}
