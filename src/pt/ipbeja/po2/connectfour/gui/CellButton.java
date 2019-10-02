package pt.ipbeja.po2.connectfour.gui;

/*
 *  Gonçalo Candeias Amaro - 17440
 *  Connect Four - PO2
 *  CellButton
 */

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//Playable Tiles

public class CellButton extends Button {

    private static final Image EMPTY =
            new Image("/resources/Empty.png");
    private static final Image PLAYER1 =
            new Image("/resources/Player1.png");
    private static final Image PLAYER2 =
            new Image("/resources/Player2.png");

    private final ImageView imageView;

    public CellButton() {
        this.imageView = new ImageView(EMPTY);
        this.imageView.setFitWidth(100);
        this.imageView.setFitHeight(100);
        this.setGraphic(imageView);
    }

    /**
     *  setPlayer1
     *  sets the cell image to p21
     */

    public void setPlayer1() {
        this.imageView.setImage(PLAYER1);
        this.imageView.setFitWidth(100);
        this.imageView.setFitHeight(100);
    }

    /**
     *  setPlayer2
     *  sets the cell image to p2
     */

    public void setPlayer2() {
        this.imageView.setImage(PLAYER2);
        this.imageView.setFitWidth(100);
        this.imageView.setFitHeight(100);
    }

    /**
     *  clear
     *  clears the cell image
     */

    public void clear() {
        this.imageView.setImage(EMPTY);
        this.imageView.setFitWidth(100);
        this.imageView.setFitHeight(100);
    }


}
