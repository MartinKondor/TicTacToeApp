package com.example.tictactoe;

import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

public class AI {

    private final ArrayList<Integer> mM = new ArrayList<>();
    private ArrayList<Integer> mMN = new ArrayList<>();

    /***
     * @param buttonsMatrix
     * @param PLAYER_1_SYM
     * @param PLAYER_2_SYM
     */
    public void think(ArrayList<ArrayList<Button>> buttonsMatrix, String PLAYER_1_SYM, String PLAYER_2_SYM) {

        // Make a moves array
        mM.clear();
        for (int i = 0; i < buttonsMatrix.size(); i++) {
            for (int j = 0; j < buttonsMatrix.get(i).size(); j++) {
                String move = buttonsMatrix.get(i).get(j).getText().toString();
                int currentCell = move.equals(PLAYER_1_SYM) ? -1 : (move.equals(PLAYER_2_SYM) ? 1 : 0);
                mM.add(currentCell);
            }
        }

        // Copy moves
        mMN.clear();
        boolean noAIMove = true;
        for (int i = 0; i < mM.size(); i++) {
            mMN.add(mM.get(i));

            if (mM.get(i) == 1) {
                noAIMove = false;
            }
        }

        if (noAIMove) {
            Random r = new Random();
            int step = 4;

            do {
                step = r.nextInt(9);
            } while (mMN.get(step) != 0);

            mMN.set(step, 1);
        }
        else {
            minimax(mMN);
        }

        for (int i = 0; i < mMN.size(); i++) {
            if (!mM.get(i).equals(mMN.get(i)) && mMN.get(i).equals(1)) {
                int row = Math.round(i / 3);
                buttonsMatrix.get(row).get(i - 3*row).setText(PLAYER_2_SYM);
                break;
            }
        }

    }

    /***
     * Find the optimal move with MiniMax algorithm
     *
     * @param moves
     * @return score for the last move
     */
    private int minimax(ArrayList<Integer> moves) {

        // Score block or win moves
        int winner = Integer.parseInt(checkWinnerFromMoves(moves).toString());
        if (winner == -1) {  // Player
            return -10;
        }
        else if (winner == 1) {  // AI
            return 10;
        }

        int move = -1;
        int score = -2;

        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i) == 0) {
                ArrayList<Integer> newMoves = new ArrayList<>();
                for (int j = 0; j < moves.size(); j++) {
                    newMoves.add(moves.get(j));
                }

                newMoves.set(i, 1);
                int scoreFromMove = minimax(newMoves);

                // Plus point if has a neighbor
                if (i >= 1 && moves.get(i - 1) == 1) scoreFromMove++;
                if (i <= 7 && moves.get(i + 1) == 1) scoreFromMove++;

                // Center is generaly a good point
                if (i == 4) scoreFromMove += 2;

                for (int k = 0; k < 3; k++) {

                    // Two enemy in one row
                    if ((moves.get(0 + k) == -1 && moves.get(1 + k) == -1) ||
                            (moves.get(0 + k) == -1 && moves.get(2 + k) == -1) ||
                            (moves.get(1 + k) == -1 && moves.get(2 + k) == -1)) {
                        scoreFromMove += 5;
                    }

                    // Two enemy in one col
                    if ((moves.get(0 + k) == -1 && moves.get(3 + k) == -1) ||
                            (moves.get(0 + k) == -1 && moves.get(6 + k) == -1) ||
                            (moves.get(3 + k) == -1 && moves.get(6 + k) == -1)) {
                        scoreFromMove += 5;
                    }
                }

                // Two enemy on diagonal
                if ((moves.get(0) == -1 && moves.get(4) == -1) ||
                        (moves.get(0) == -1 && moves.get(8) == -1) ||
                        (moves.get(4) == -1 && moves.get(8) == -1)) {
                    scoreFromMove += 5;
                }

                // Two enemy on anti-diagonal
                if ((moves.get(2) == -1 && moves.get(4) == -1) ||
                        (moves.get(2) == -1 && moves.get(6) == -1) ||
                        (moves.get(4) == -1 && moves.get(6) == -1)) {
                    scoreFromMove += 5;
                }

                if (scoreFromMove > score) {
                    move = i;
                    score = scoreFromMove;
                }

            }
        }
        if (move == -1) {
            return 0;
        } else {
            mMN.set(move, 1);
        }
        return score;
    }

    private Integer checkWinnerFromMoves(ArrayList<Integer> moves) {

        //  Check diagonal
        if (!moves.get(0).equals(0) &&
            moves.get(0).equals(moves.get(4)) && moves.get(4).equals(moves.get(8)))
        {
            return moves.get(0);
        }

        if (!moves.get(2).equals(0) &&
                moves.get(2).equals(moves.get(4)) && moves.get(4).equals(moves.get(6)))
        {
            return moves.get(2);
        }

        for (int i = 0; i < 3; i++) {

            // Check rows
            if (!moves.get(0 + 3*i).equals(0) &&
                    moves.get(0 + 3*i).equals(moves.get(1 + 3*i)) && moves.get(1 + 3*i).equals(moves.get(2 + 3*i)))
            {
                return moves.get(0 + 3*i);
            }

            // Check cols
            if (!moves.get(0 + i).equals(0) &&
                    moves.get(0 + i).equals(moves.get(3 + i)) && moves.get(3 + i).equals(moves.get(6 + i)))
            {
                return moves.get(0 + i);
            }

        }

        return 0;
    }

    protected String checkWinCondition(ArrayList<ArrayList<Button>> M) {
        // Diagonal
        if (M.get(0).get(0).getText().toString().equals(M.get(1).get(1).getText().toString()) &&
                M.get(1).get(1).getText().toString().equals(M.get(2).get(2).getText().toString()) &&
                !M.get(0).get(0).getText().toString().equals(""))
        {
            return M.get(0).get(0).getText().toString();
        }

        // Anti-Diagonal
        if (M.get(0).get(2).getText().toString().equals(M.get(1).get(1).getText().toString()) &&
                M.get(1).get(1).getText().toString().equals(M.get(2).get(0).getText().toString()) &&
                !M.get(0).get(2).getText().toString().equals(""))
        {
            return M.get(0).get(2).getText().toString();
        }

        for (int i = 0; i < M.size(); i++) {

            // Check rows
            if (M.get(i).get(0).getText().toString().equals(M.get(i).get(1).getText().toString()) &&
                    M.get(i).get(1).getText().toString().equals(M.get(i).get(2).getText().toString()) &&
                    !M.get(i).get(0).getText().toString().equals(""))
            {
                return M.get(i).get(0).getText().toString();
            }

            // Check colums
            if (M.get(0).get(i).getText().toString().equals(M.get(1).get(i).getText().toString()) &&
                    M.get(1).get(i).getText().toString().equals(M.get(2).get(i).getText().toString()) &&
                    !M.get(0).get(i).getText().toString().equals(""))
            {
                return M.get(0).get(i).getText().toString();
            }
        }

        return null;
    }

}
