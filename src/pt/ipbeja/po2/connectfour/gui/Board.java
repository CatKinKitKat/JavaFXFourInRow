/*
 *  Gonçalo Candeias Amaro - 17440
 *  Connect Four - PO2
 *  Board
 */

package pt.ipbeja.po2.connectfour.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import pt.ipbeja.po2.connectfour.View;
import pt.ipbeja.po2.connectfour.model.Cell;
import pt.ipbeja.po2.connectfour.model.ConnectFourModel;

import java.util.Optional;

//Game board (Basically the GUI)

public class Board extends GridPane implements View {

    private Launcher launcher;
    private final ConnectFourModel model;
    private CellButton[][] cells;

    public Board(Launcher launcher) {
        this.model = new ConnectFourModel(this);
        this.generateBoard();
        this.makeMenu();
        this.launcher = launcher;           //we need to point to the launcher if we want to call the reboot method
    }

    /**
     *  generateBoard
     *  generates the game board for user to interact
     */

    private void generateBoard() {
        CellButtonHandler handler = new CellButtonHandler();
        this.cells = new CellButton[this.model.width][this.model.height];

        for (int col = 0; col < this.model.height; col++) {
            for (int row = 0; row < this.model.width; row++) {
                CellButton cell = new CellButton();
                cell.setMaxSize(100, 100);
                cell.setOnAction(handler);
                this.add(cell, col, row);
                this.cells[row][col] = cell;
            }
        }
    }

    /**
     *  draw
     *  alert generator with reboot implementation
     */

    @Override
    public void draw() {
        Alert draw = new Alert(Alert.AlertType.CONFIRMATION,
                "It's a draw, nobody won.");
        draw.setTitle("Draw!");
        draw.setHeaderText(null);
        draw.setGraphic(null);

        ButtonType relaunch = new ButtonType("Play again!");
        ButtonType exit = new ButtonType("Exit game", ButtonBar.ButtonData.CANCEL_CLOSE);

        draw.getButtonTypes().setAll(relaunch, exit);

        Optional<ButtonType> pressed = draw.showAndWait();
        if (pressed.get() == relaunch) {
            this.launcher.relaunch();
        } else {
            System.exit(0);
        }
    }

    /**
     *  playerWon
     *  @param player
     *  whole alert generator with points and game reboot
     */

    @Override
    public void playerWon(int player) {
        int pts = 6 * 7;
        ImageView playerImage = new ImageView();
        playerImage.setFitWidth(100);
        playerImage.setFitHeight(100);

        if (player == 0) {
            pts -= this.model.getP1Plays();
            playerImage.setImage(new Image("/resources/Player1.png"));
        } else {
            pts -= this.model.getP2Plays();
            playerImage.setImage(new Image("/resources/Player2.png"));
        }

        Alert won = new Alert(Alert.AlertType.CONFIRMATION,
                "You won, with " + pts + " pts!\n");
        won.setTitle("Victory!");
        won.setHeaderText(null);
        won.setGraphic(playerImage);

        ButtonType relaunch = new ButtonType("Play again!");
        ButtonType exit = new ButtonType("Exit game", ButtonBar.ButtonData.CANCEL_CLOSE);

        won.getButtonTypes().setAll(relaunch, exit);

        Optional<ButtonType> pressed = won.showAndWait();
        if (pressed.get() == relaunch) {
            this.launcher.relaunch();
        } else {
            System.exit(0);                 //big function but still under 30 semicolons, cheers
        }
    }


    /**
     * update
     * @param cell type
     * @param row
     * @param col
     * updates user board, might clear if needed
     */

    @Override
    public void update(Cell cell, int row, int col) {
        CellButton cellButton = cells[row][col];
        if (cell == Cell.PLAYER1) {
            cellButton.setPlayer1();
        } else if (cell == Cell.PLAYER2) {
            cellButton.setPlayer2();
        } else {
            cellButton.clear();
        }
    }

    /**
     *  makeMenu
     *  generates the menu, above it is called after board creation
     */

    private void makeMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu();
        menu.setText("Game");                                //this menu bar and menu are in the bottom left,
        //since there was no restriction or rule about it
        MenuItem undoBtn = new MenuItem();
        undoBtn.setText("Undo");
        undoBtn.setOnAction(e -> {
            this.model.undo();
        });

        MenuItem redoBtn = new MenuItem();
        redoBtn.setText("Redo");
        redoBtn.setId("redo");
        redoBtn.setOnAction(e -> {
            this.model.redo();
        });

        CheckMenuItem gameModeSelect = new CheckMenuItem("PopOut?");
        gameModeSelect.setOnAction(event -> {
            if (gameModeSelect.isSelected()) {
                this.model.setGameMode(true);                           //pop out variant
            } else {
                this.model.setGameMode(false);                         //falls back to normal
            }
        });

        menu.getItems().addAll(undoBtn, redoBtn, gameModeSelect);
        menuBar.getMenus().add(menu);

        this.add(menuBar, 0, 7);        //big function but still under 30 semicolons, cheers
    }

    /**
     *
     * @param row
     * @param col
     */

    @Override
    public void clear(int row, int col) {                       //clears the board when called
        CellButton cellButton = cells[row][col];
        cellButton.clear();
    }

    private class CellButtonHandler implements EventHandler<ActionEvent> {      //it's the button handler
        @Override
        public void handle(ActionEvent event) {
            CellButton cell = (CellButton) event.getSource();

            int row = getRowIndex(cell);
            int col = getColumnIndex(cell);

            Board.this.model.selectedCell(row, col);
        }
    }
}
