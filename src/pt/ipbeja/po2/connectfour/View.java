/*
 *  Gonçalo Candeias Amaro - 17440
 *  Connect Four - PO2
 *  View
 */

package pt.ipbeja.po2.connectfour;

import pt.ipbeja.po2.connectfour.model.Cell;

// Communication Interface

public interface View {

    void draw(); //Rare AF

    void playerWon(int player);

    void update(Cell cell, int row, int col);

    void clear(int row, int col);

}
