package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import com.example.tictactoe.AI;

public class MainActivity extends AppCompatActivity {

    private final String PLAYER_1_SYM = "X";
    private final String PLAYER_2_SYM = "O";
    private final ArrayList<ArrayList<Button>> buttonsMatrix = new ArrayList<>();
    private boolean IS_AI_ACTIVE = true;
    private boolean TURN_OF_PLAYER_1 = true;
    private final AI AI = new AI();


    protected void popupGameOver(String winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String msg = "\"" + winner + "\" has won.";
        if (PLAYER_1_SYM.equals(winner) || !IS_AI_ACTIVE) {
            msg += "\n\nCongratulations!";
        }

        builder.setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // ...
                    }
                })
                .setTitle("Game Over");
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonsMatrix.add(new ArrayList<Button>() {{ add(findViewById(R.id.b1)); add(findViewById(R.id.b2)); add(findViewById(R.id.b3)); }});
        buttonsMatrix.add(new ArrayList<Button>() {{ add(findViewById(R.id.b4)); add(findViewById(R.id.b5)); add(findViewById(R.id.b6)); }});
        buttonsMatrix.add(new ArrayList<Button>() {{ add(findViewById(R.id.b7)); add(findViewById(R.id.b8)); add(findViewById(R.id.b9)); }});


        int nthColButton = 0, nthRowButton = 0;
        RelativeLayout.LayoutParams params = null;

        for (ArrayList<Button> buttonsRow : buttonsMatrix) {
            for (Button button : buttonsRow)
            {
                // Set button positions and sizes
                button.setTextSize(80);
                button.setTextColor(ColorStateList.valueOf(0xffffffff));

                params = (RelativeLayout.LayoutParams) button.getLayoutParams();
                params.width = (getResources().getDisplayMetrics().widthPixels - 20) / 3 - 20;
                params.height = (getResources().getDisplayMetrics().heightPixels - 400) / 3;
                nthColButton += 1;

                if (nthColButton == 1) {
                    params.leftMargin = 30;
                }
                else if (nthColButton == 2) {
                    params.leftMargin = params.width + 40;
                }
                else if (nthColButton == 3) {
                    params.leftMargin = 2 * params.width + 50;
                }
                else {
                    nthColButton = 1;
                    params.leftMargin = 30;
                    nthRowButton++;
                }

                params.topMargin = nthRowButton*params.height + 10;
                params.bottomMargin = 0;
                button.setLayoutParams(params);

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if ((String) button.getText() == "") {
                            if (IS_AI_ACTIVE) {
                                if (TURN_OF_PLAYER_1) {
                                    button.setText(PLAYER_1_SYM);
                                    TURN_OF_PLAYER_1 = false;
                                }
                                AI.think(buttonsMatrix, PLAYER_1_SYM, PLAYER_2_SYM);
                                TURN_OF_PLAYER_1 = true;
                            }
                            else {
                                button.setText(TURN_OF_PLAYER_1 ? PLAYER_1_SYM : PLAYER_2_SYM);
                                TURN_OF_PLAYER_1 = !TURN_OF_PLAYER_1;
                            }
                        }

                        String winner = AI.checkWinCondition(buttonsMatrix);
                        if (winner != null) {
                            for (ArrayList<Button> buttons : buttonsMatrix) {
                                for (Button button : buttons) {
                                    button.setEnabled(false);
                                }
                            }

                            popupGameOver(winner);
                        }

                    }
                });
            }
        }

        Button btnBg = findViewById(R.id.btn_bg);
        params = (RelativeLayout.LayoutParams) btnBg.getLayoutParams();
        params.topMargin = 15;
        params.leftMargin = 35;
        params.rightMargin = 35;
        params.width = getResources().getDisplayMetrics().widthPixels - 70;
        params.height = getResources().getDisplayMetrics().heightPixels - 410;
        btnBg.setLayoutParams(params);

        // Place restart button
        Button restartButton = findViewById(R.id.restart_btn);
        params = (RelativeLayout.LayoutParams) restartButton.getLayoutParams();
        params.topMargin = getResources().getDisplayMetrics().heightPixels - 150;
        params.leftMargin = 30;
        restartButton.setLayoutParams(params);

        restartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (ArrayList<Button> buttons : buttonsMatrix) {
                    for (Button button : buttons) {
                        button.setText("");
                        button.setEnabled(true);
                    }
                }
                TURN_OF_PLAYER_1 = true;
            }
        });

        // AI on off button
        Switch aiSwitch = findViewById(R.id.ai_switch);
        params = (RelativeLayout.LayoutParams) aiSwitch.getLayoutParams();
        params.topMargin = getResources().getDisplayMetrics().heightPixels - 130;
        params.leftMargin = getResources().getDisplayMetrics().widthPixels - 300;
        aiSwitch.setLayoutParams(params);

        aiSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IS_AI_ACTIVE = !IS_AI_ACTIVE;
            }
        });

    }
}