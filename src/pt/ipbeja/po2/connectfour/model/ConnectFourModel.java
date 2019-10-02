/*
 *  Gonçalo Candeias Amaro - 17440
 *  Connect Four - PO2
 *  ConnectFourModel
 */

package pt.ipbeja.po2.connectfour.model;

import javafx.util.Pair;
import pt.ipbeja.po2.connectfour.View;

import java.util.ArrayList;

//The game (or the logic of it)

public class ConnectFourModel {

    // all names should be self-explanatory

    public final int width = 6;
    public final int height = 7;
    private final int bottom = 5;

    private static int p1Plays = 0, p2Plays = 0;

    private final View view;

    private final Cell[][] boardData;
    private static int turnCounter;

    private ArrayList<Pair<Integer, Integer>> playsMade = new ArrayList<>();
    private ArrayList<Cell> playType = new ArrayList<>();

    private ArrayList<Pair<Integer, Integer>> undosMade = new ArrayList<>();
    private ArrayList<Cell> undoType = new ArrayList<>();

    private int undos = 0;
    private boolean gameMode = false;   //defaults to regular game mode, like it should have ever been... sigh...

    public ConnectFourModel(View view) {
        this.view = view;
        boardData = new Cell[width][height];
        this.fillBoard();
    }

    /**
     * fillBoard
     * creates empty virtual board
     */

    private void fillBoard() {
        for (int row = 0; row < this.width; row++) {
            for (int col = 0; col < this.height; col++) {
                this.boardData[row][col] = Cell.EMPTY;
            }
        }
    }

    /**
     * selectedCell
     *
     * @param row
     * @param col plays the intended play if it is permitted
     */


    public void selectedCell(int row, int col) {        //activates the play
        if (popable(row, col)) {
            popIt(col);
            turnCounter++;
            return;
        }
        if (this.isPlayable(row, col)) {
            this.updateState(row, col);
            Cell cell = this.getCell(row, col);
            this.view.update(cell, row, col);
            turnCounter++;
            this.checkState(row, col);
        } else if (isEmpty(row, col)) {
            selectedCell(row + 1, col);
        } else if (row != 0) {
            selectedCell(row - 1, col);         //big function but still under 30 semicolons, cheers
        }
    }

    /**
     * popIt
     *
     * @param col pops the cell
     */

    private void popIt(int col) {
        Cell[] plays = new Cell[6];

        plays[0] = Cell.EMPTY;

        for (int row = 1; row <= 5; row++) {
            plays[row] = getCell(row - 1, col);
        }

        for (int fall = 0; fall <= 5; fall++) {
            this.boardData[fall][col] = plays[fall];
            this.view.update(plays[fall], fall, col);
        }

        playsMade.add(new Pair<>(5, col));
        playType.add(Cell.EMPTY);   // temporary fix for undo & redo when popped
    }

    /**
     * unPop
     *
     * @param col undo the popped play
     */

    private void unPop(int col) {
        Cell[] plays = new Cell[6];

        for (int row = 5; row >= 1; row--) {
            plays[row - 1] = getCell(row, col);
        }

        if ((this.turnCounter - 1) % 2 == 0) {
            this.boardData[bottom][col] = Cell.PLAYER1;
            this.view.update(Cell.PLAYER1, bottom, col);
        } else {
            this.boardData[bottom][col] = Cell.PLAYER2;
            this.view.update(Cell.PLAYER2, bottom, col);
        }

        for (int jump = 4; jump >= 0; jump--) {
            this.boardData[jump][col] = plays[jump];
            this.view.update(plays[jump], jump, col);
        }
    }

    /**
     * popable
     *
     * @param row
     * @param col
     * @return if it is, duh
     * is it a pop-able situation, if its box is checked and if its bottom
     */

    private boolean popable(int row, int col) {   // is it popable
        boolean playerOwned;

        if (turnCounter % 2 == 0) {
            playerOwned = (boardData[5][col].equals(Cell.PLAYER1));
        } else {
            playerOwned = (boardData[5][col].equals(Cell.PLAYER2));
        }
        return this.gameMode && playerOwned && (row == this.bottom);
    }

    /**
     * getCell
     *
     * @param row
     * @param col
     * @return cell type
     * returns the cell type
     */

    public Cell getCell(int row, int col) { //ONLY PUBLIC BECAUSE TESTS (those damned tests)
        return boardData[row][col];
    }

    /**
     * checkState
     *
     * @param row
     * @param col checks the game state at that point,if it is a win or a draw
     */

    private void checkState(int row, int col) {   // checks if is a win or a draw
        //you need 7 turns to win (7-1 because starts from 0)
        if (turnCounter > 6) {
            if (isWin(row, col)) {
                this.view.playerWon((turnCounter - 1) % 2);
            } else if (isDraw()) {
                this.view.draw();
            }
        }
    }

    /**
     * isWin
     *
     * @param row
     * @param col
     * @return win?
     * checks if it is a win
     */

    public boolean isWin(int row, int col) { //ONLY PUBLIC BECAUSE TESTS
        return rowCheck(row) || colCheck(col) || diagCheck(row, col) || otherDiagCheck(row, col);
    }

    /**
     * isDraw
     *
     * @return draw?
     * checks if it is a draw
     */

    public boolean isDraw() { //ONLY PUBLIC BECAUSE TESTS
        return ((this.turnCounter) >= (width * height));
    }

    /**
     * updateState
     *
     * @param row
     * @param col updates the virtual board
     */

    private void updateState(int row, int col) {
        this.playsMade.add(new Pair<>(row, col));
        if (this.turnCounter % 2 == 0) {
            this.boardData[row][col] = Cell.PLAYER1;
            this.playType.add(Cell.PLAYER1);
            this.p1Plays++;
        } else {
            this.boardData[row][col] = Cell.PLAYER2;
            this.playType.add(Cell.PLAYER2);
            this.p2Plays++;
        }
        this.undos = 0;
        this.undoType.clear();
        this.undosMade.clear();
    }

    /**
     * isPlayable
     *
     * @param row
     * @param col
     * @return legal play?
     * checks availability
     */

    private boolean isPlayable(int row, int col) {
        return (isBottom(row, col) || isStack(row, col));
    }

    /**
     * isStack
     *
     * @param row
     * @param col
     * @return stack?
     * checks if it is in a stack
     */

    private boolean isStack(int row, int col) {
        return (isEmpty(row, col)) && (this.boardData[row + 1][col] != Cell.EMPTY);
    }

    /**
     * isBottom
     *
     * @param row
     * @param col
     * @return bottom pos?
     * checks if it is on the bottom of the board
     */

    private boolean isBottom(int row, int col) {
        return (isEmpty(row, col)) && (row == 5);
    }

    /**
     * isEmpty
     *
     * @param row
     * @param col
     * @return checks if the cell is empty
     */

    private boolean isEmpty(int row, int col) {
        return (this.boardData[row][col] == Cell.EMPTY);
    }

    /**
     * rowCheck
     *
     * @param row
     * @return win?
     * checks row for win
     */

    private boolean rowCheck(int row) {
        int counter = 0;
        for (int col = 0; col < height - 1; col++) {
            if (this.boardData[row][col] != Cell.EMPTY) {
                if (this.boardData[row][col] == this.boardData[row][col + 1]) {
                    counter++;
                    if (counter == 3) return true;
                } else {
                    counter = 0;
                }
            }
        }
        return false;
    }

    /**
     * colCheck
     *
     * @param col
     * @return win?
     * checks col for win
     */

    private boolean colCheck(int col) {
        int counter = 0;
        for (int row = width - 1; row > 0; row--) {
            if (this.boardData[row][col] != Cell.EMPTY) {
                if (this.boardData[row][col] == this.boardData[row - 1][col]) {
                    counter++;
                    if (counter == 3) return true;
                } else {
                    counter = 0;
                }
            }
        }
        return false;
    }

    /**
     * diagCheck
     *
     * @param row
     * @param col
     * @return win?
     * checks the diagonal for win
     */

    private boolean diagCheck(int row, int col) {
        int counter = 0;
        if (row > col) {
            row -= col;
            col = 0;
        } else if (row < col) {
            col -= row;
            row = 0;
        } else {
            row = 0;
            col = 0;
        }
        for (int a = 0; a < height - 1; a++) {
            if (row == 5 || col == 6) break;
            if (this.boardData[row][col] != Cell.EMPTY) {
                if (this.boardData[row][col] == this.boardData[row + 1][col + 1]) {
                    counter++;
                    if (counter == 3) return true;
                } else {
                    counter = 0;
                }
            }
            row++;
            col++;
        }
        return false;           //big function but still under 30 semicolons, cheers
    }

    /**
     * otherDiagCheck
     *
     * @param oldRow
     * @param oldCol
     * @return win?
     * checks the mirrored diagonal
     */

    private boolean otherDiagCheck(int oldRow, int oldCol) {
        int counter = 0;
        int col = oldRow + oldCol;
        int row = 0;
        if (col > 6) {
            row = col - 6;
            col = 6;
        }
        for (int a = 0; a < height - 1; a++) {
            if (row == 5 || col == 0) break;
            if (this.boardData[row][col] != Cell.EMPTY) {
                if (this.boardData[row][col] == this.boardData[row + 1][col - 1]) {
                    counter++;
                    if (counter == 3) return true;
                } else {
                    counter = 0;
                }
            }
            row++;
            col--;
        }
        return false;           //big function but still under 30 semicolons, cheers
    }

    /**
     * getP1Plays
     *
     * @return n_plays from p1
     * returns the numbers of plays by p1
     */

    public int getP1Plays() {
        return p1Plays;
    }

    /**
     * getP2Plays
     *
     * @return n_plays from p2
     * returns the numbers of plays by p2
     */

    public int getP2Plays() {
        return p2Plays;
    }

    /**
     * undo
     * undo the last play
     */

    public void undo() {
        Pair<Integer, Integer> getter = playsMade.get(this.turnCounter - 1);
        int row = getter.getKey();
        int col = getter.getValue();
        Cell type = playType.get(this.turnCounter - 1);

        if (type == Cell.EMPTY) { //it was a pop, there is no empty play
            unPop(col);
        } else {
            this.boardData[row][col] = Cell.EMPTY;

            this.view.clear(row, col);
        }

        this.undosMade.add(getter);
        this.undoType.add(type);

        this.playsMade.remove(turnCounter - 1);
        this.playType.remove(turnCounter - 1);

        this.undos++;
        this.turnCounter--;                     //big function but still under 30 semicolons, cheers
    }

    /**
     * redo
     * redo the last undo
     */

    public void redo() {
        if (undos > 0) {
            Pair<Integer, Integer> getter = undosMade.get(undos - 1);
            int row = getter.getKey();
            int col = getter.getValue();
            Cell type = undoType.get(undos - 1);

            if (type == Cell.EMPTY) { //it was a pop, there is no empty play
                popIt(col);
            } else {
                this.boardData[row][col] = type;

                Cell cell = this.getCell(row, col);
                this.view.update(cell, row, col);
            }

            this.playsMade.add(getter);
            this.playType.add(type);

            this.undosMade.remove(undos - 1);
            this.undoType.remove(undos - 1);

            this.undos--;
            this.turnCounter++;
        }             //big function but still under 30 semicolons, cheers
    }

    /**
     * setGameMode
     *
     * @param mode pop or regular
     *             switches between the game modes
     */

    public void setGameMode(boolean mode) {
        this.gameMode = mode;
    }

    /**
     * tesMethod
     *
     * @param row
     * @param col
     * @param type of cell
     *             tests the game in the junit thing
     */

    public void testMethod(int row, int col, Cell type) {     // for the game it is completely useless, but needed for tests
        if (this.isPlayable(row, col)) {                      // behold! it has the same logic as the game function itself
            this.boardData[row][col] = type;                  // so it's just a test function like you asked
            this.turnCounter++;
        } else if (isEmpty(row, col)) {
            testMethod(row + 1, col, type);
        } else if (row != 0) {
            testMethod(row - 1, col, type);
        }
    }
}
