/*
 *  Gonçalo Candeias Amaro - 17440
 *  Connect Four - PO2
 *  tests
 */

package pt.ipbeja.po2.connectfour.model;

import pt.ipbeja.po2.connectfour.View;
import org.junit.jupiter.api.*;

// i wonder if i can export the project with the required name without having to recreate it and copy recursively

class modelTest {

    private ConnectFourModel model;
    private View view;

    @Test
    void teste01() {
        /*
         *  Colocação de uma peça numa coluna vazia;
         *  a peça deve ficar na linha de baixo (linhacinco);
         */

        model = new ConnectFourModel(view);

        int randCol = 4; //[0, 5]
        int randRow = 4; //[0, 6]

        model.testMethod(randRow, randCol, Cell.PLAYER1);
        Assertions.assertEquals(Cell.PLAYER1, model.getCell(5, randCol));
    }

    @Test
    void teste02() {
        /*
         *  Colocação de uma peça numa coluna contendo já uma peça na linha quatro,
         *  a segunda linha a contar de baixo; sendo a linha de baixo a linha cinco,
         *  a nova peça deve ficar por cima da que lá estava, portanto na linha três;
         *
         *  Did you mean: Coluna?
         */

        model = new ConnectFourModel(view);

        int randCol = 3;

        model.testMethod(5, randCol, Cell.PLAYER2);
        model.testMethod(4, randCol, Cell.PLAYER2);

        model.testMethod(4, randCol, Cell.PLAYER1);

        Assertions.assertEquals(Cell.PLAYER2, model.getCell(4, randCol));
        Assertions.assertEquals(Cell.PLAYER1, model.getCell(3, randCol));

    }

    @Test
    void teste03() {
        /*
         *  Tentativa de colocação de uma peça numa coluna cheia;
         *  o modelo deve ficar igual;
         */

        model = new ConnectFourModel(view);

        int randRow = 2;
        int randCol = 4;

        for (int i = 6; i >= 0; i--) {
            model.testMethod(randRow, i, Cell.PLAYER2);
        } // does not generate a Win because it doesn't trigger the play/turncounter

        model.testMethod(randRow, randCol, Cell.PLAYER1);

        for (int j = 6; j >= 0; j--) {
            Assertions.assertNotEquals(Cell.PLAYER1, model.getCell(randRow, randCol));
        } // if it didn't play then no arrayoutofbounds neither a single P1 play

    }

    @Test
    void teste04() {
        /*
         *  Uma jogada em que o jogador não ganha; esta é a situação mais frequente;
         */

        model = new ConnectFourModel(view);

        int randCol = 3;

        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                model.testMethod(i, randCol, Cell.PLAYER1);
            } else {
                model.testMethod(i, randCol, Cell.PLAYER2);
            }

        }

        Assertions.assertFalse(model.isWin(0, randCol));

    }

    @Test
    void teste05() {
        /*
         *  Uma jogada em que o jogador ganha com uma linha de quatro na horizontal;
         */

        model = new ConnectFourModel(view);

        for (int i = 0; i <= 4; i++) {
            model.testMethod(i, 5, Cell.PLAYER1); //turn 8 to be pair and minimum
        }

        Assertions.assertTrue(model.isWin(4, 5));
    }

    @Test
    void teste06() {
        /*
         *  Uma jogada em que o jogador ganha com uma linha de quatro na vertical;
         */

        model = new ConnectFourModel(view);

        for (int i = 0; i <= 4; i++) {
            model.testMethod(i, 0, Cell.PLAYER1); //turn 8 to be pair and minimum
        }

        Assertions.assertTrue(model.isWin(4, 0));
    }

    @Test
    void teste07() {
        /*
         *  Uma jogada em que o jogador ganha com uma linha de quatro na diagonal;
         */

        model = new ConnectFourModel(view);

        model.testMethod(5, 0, Cell.PLAYER2);
        model.testMethod(4, 0, Cell.PLAYER2);
        model.testMethod(3, 0, Cell.PLAYER2);
        model.testMethod(2, 0, Cell.PLAYER1);

        model.testMethod(5, 1, Cell.PLAYER2);
        model.testMethod(4, 1, Cell.PLAYER2);
        model.testMethod(3, 1, Cell.PLAYER1);

        model.testMethod(5, 2, Cell.PLAYER2);
        model.testMethod(4, 2, Cell.PLAYER1);

        model.testMethod(5, 3, Cell.PLAYER1);

        model.isWin(5, 3);              //big function but still under 30 semicolons, cheers

    }

    @Test
    void teste08() {
        /*
         *  Uma jogada em que o jogo termina sem vitória;
         *  note que neste caso o modelo do teste deve ser iniciado
         *  só com uma posição EMPTY na linha de cima (linha zero) e
         *  depois realizar uma jogada que resulte em empate.
         */

        model = new ConnectFourModel(view);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col += 2) {
                if (col == 4 || col == 5) {
                    model.testMethod(row, col, Cell.PLAYER1);
                } else {
                    model.testMethod(row, col, Cell.PLAYER2);
                }
            }
        }

        Assertions.assertTrue(model.isDraw());
    }

}