package app.tictactoe.mmc.com.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmc.tiktaktoe.CellPosition;
import com.mmc.tiktaktoe.TicTacToeBoard;
import com.mmc.tiktaktoe.TicTacToeMover;
import com.mmc.tiktaktoe.TicTacToeRefeere;
import com.mmc.tiktaktoe.TicTacToeType;
import com.mmc.tiktaktoe.abstraction.Board;
import com.mmc.tiktaktoe.abstraction.Cell;
import com.mmc.tiktaktoe.abstraction.Mover;
import com.mmc.tiktaktoe.abstraction.Position;
import com.mmc.tiktaktoe.abstraction.Printer;
import com.mmc.tiktaktoe.abstraction.Refeere;
import com.mmc.tiktaktoe.rules.DiagonalRule;
import com.mmc.tiktaktoe.rules.HorizontalRule;
import com.mmc.tiktaktoe.rules.VerticalRule;

public class MainActivity extends AppCompatActivity implements Printer {
    Board board;
    Mover mover;
    private Position position;
    Refeere refeere;

    TextView tvReultX;
    TextView tvReultO;
    Button btnReset;
    TextView tvTurn;
    TextView tvAmoutnOfGames;

    int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictactoe);

        tvReultX = (TextView) findViewById(R.id.tv_x_count);
        tvReultO = (TextView) findViewById(R.id.tv_o_count);
        tvTurn = (TextView) findViewById(R.id.tv_turn);
        tvAmoutnOfGames = (TextView) findViewById(R.id.tv_game_amount);

        btnReset = (Button) findViewById(R.id.btn_reset);

        board = new TicTacToeBoard();

        refeere = new TicTacToeRefeere(board);
        refeere.addRule(new HorizontalRule());
        refeere.addRule(new VerticalRule());
        refeere.addRule(new DiagonalRule());

        mover = new TicTacToeMover(board);
        mover.startFrom(TicTacToeType.O);

        refeere.setOnWinListener(new Refeere.OnWinListener() {
            @Override
            public void onWin(Cell cell) {

                Toast.makeText(MainActivity.this, "Wygrywa " + cell.getType() + " !!!", Toast.LENGTH_SHORT).show();

                tvReultO.setText("O: "+refeere.resultO());
                tvReultX.setText("X: "+refeere.resultX());


            }
        });

        mover.setOnMoveListener(new Mover.OnMoveListener() {
            @Override
            public void onMove() {
                refeere.checkIfSomeoneWon();

//                if (board.isFull()){
//                    board.resetFields();
//                }

                setTurnTitle();

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board.resetFields();
                mover.reset();
                refeere.reset();



                amount++;
                setAmoutOfGames();
                resetFieldsColor();
                setTurnTitle();
                print();
            }
        });

        setTurnTitle();

        print();


    }

    private void setAmoutOfGames() {
        tvAmoutnOfGames.setText(""+amount);
    }

    private void setTurnTitle() {
        if (mover.getTurn() == TicTacToeType.X){
            tvTurn.setText("Teraz jest kolej -> X");
        }else {
            tvTurn.setText("Teraz jest kolej -> O");
        }
    }

    ImageView[][] list;
    @Override
    public void print() {
        ImageView cell11 = (ImageView) findViewById(R.id.iv_ttt11);
        ImageView cell12 = (ImageView) findViewById(R.id.iv_ttt12);
        ImageView cell13 = (ImageView) findViewById(R.id.iv_ttt13);

        ImageView cell21 = (ImageView) findViewById(R.id.iv_ttt21);
        ImageView cell22 = (ImageView) findViewById(R.id.iv_ttt22);
        ImageView cell23 = (ImageView) findViewById(R.id.iv_ttt23);

        ImageView cell31 = (ImageView) findViewById(R.id.iv_ttt31);
        ImageView cell32 = (ImageView) findViewById(R.id.iv_ttt32);
        ImageView cell33 = (ImageView) findViewById(R.id.iv_ttt33);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Position position = getPosition(view);

                if (!refeere.isWon()){
                    mover.move(position);
                }

                print();
            }
        };



        list = new ImageView[3][];

        ImageView[] row1 = new ImageView[3];

        row1[0] = cell11;
        row1[1] = cell12;
        row1[2] = cell13;

        ImageView[] row2 = new ImageView[3];

        row2[0] = cell21;
        row2[1] = cell22;
        row2[2] = cell23;

        ImageView[] row3 = new ImageView[3];

        row3[0] = cell31;
        row3[1] = cell32;
        row3[2] = cell33;

        list[0] = row1;
        list[1] = row2;
        list[2] = row3;


        int i = 0;
        for (Cell[] ta : board.getCells()) {

            int k = 0;

            for (Cell tic : ta ) {
                list[i][k].setOnClickListener(onClickListener);

                if (tic.isUsed()) {

                    if (tic.isX()){
                        list[i][k].setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                    }else{
                        list[i][k].setImageDrawable(getResources().getDrawable(R.drawable.ic_panorama_fish_eye_black_24dp));
                    }

                    if (tic.isTakePartOfWon()){
                        list[i][k].setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        list[i][k].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }


                }else {
                    Log.d("LOGI", i+ " " + k);
                    list[i][k].setImageDrawable(null);
                }

                k++;
            }

            i++;
        }
    }

    public Position getPosition(View view) {
        switch(view.getId()){
            case R.id.iv_ttt11:
                return new CellPosition(0,0);
            case R.id.iv_ttt12:
                return new CellPosition(0,1);
            case R.id.iv_ttt13:
                return new CellPosition(0,2);
            case R.id.iv_ttt21:
                return new CellPosition(1,0);
            case R.id.iv_ttt22:
                return new CellPosition(1,1);
            case R.id.iv_ttt23:
                return new CellPosition(1,2);
            case R.id.iv_ttt31:
                return new CellPosition(2,0);
            case R.id.iv_ttt32:
                return new CellPosition(2,1);
            case R.id.iv_ttt33:
                return new CellPosition(2,2);
        }
        return null;
    }


    public void resetFieldsColor() {
        int i = 0;
        for (Cell[] ta : board.getCells()) {

            int k = 0;

            for (Cell tic : ta) {

                list[i][k].setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                k++;
            }

            i++;
        }
    }

}
