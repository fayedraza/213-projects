package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements  Question.NotifyResult {

    private TextView turn;
    private ImageView chessBoardInterface[][];
    private Button draw;
    private Button resign;
    private Button undo;
    private Button aiMove;
    private Button popupSave;
    private EditText popupName;
    private Button next;
    private Button load;
    private TextView textView;
    private Button loadDate;
    private TextView winner;

    ArrayList<int[]> pointsToPick = new ArrayList<int[]>();

    boolean[][] locationCaptured = new boolean[8][8];
    int clicks = 0;
    int turnValue = 0;
    boolean canCastle = false;
    String lastMove = null;
    int lastCordinates[][] = null;
    boolean kinginCheck = false;
    boolean pieceCaptured = false;
    int firstPosX = 0;
    int firstPosY = 0;
    int secondPosX = 0;
    int secondPosY = 0;
    int lastX = 0;
    int lastY = 0;
    int globalCords[][];
    boolean canDoEnPassant = false;
    Piece[][] grid;
    Piece[][] previousGrid;
    SharedPreferences mPrefs;
    FileOutputStream fos;
    ObjectOutputStream os;
    boolean isAi = false;
    int randomX = 0;
    int randomY = 0;
    String lastString = "";
    boolean justDidEnpassant = false;
    ArrayList<Piece[][]> saveGrid;
    String chosenSave = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        grid = new Piece[8][8];
        boolean[][] locationCaptured = new boolean[8][8];
        boolean canCastle = false;
        boolean pieceCaptured = false;

        grid = populateBoard();
        turn = findViewById(R.id.turn);
        draw = findViewById(R.id.draw);
        resign = findViewById(R.id.resign);
        undo = findViewById(R.id.undo);
        aiMove = findViewById(R.id.aiMove);
        chessBoardInterface = new ImageView[8][8];
        previousGrid = new Piece[8][8];
        saveGrid = new ArrayList<Piece[][]>();
        next = (Button) findViewById(R.id.next);
        load = (Button) findViewById(R.id.loadName);
        loadDate = (Button) findViewById(R.id.loadDate);
        next.setVisibility(View.GONE);
        textView = (TextView) findViewById(R.id.textView2);


        drawBoard(chessBoardInterface);

        chessBoardInterface[0][0].setImageResource(R.drawable.br);
        chessBoardInterface[0][1].setImageResource(R.drawable.bk);
        chessBoardInterface[0][2].setImageResource(R.drawable.bb);
        chessBoardInterface[0][3].setImageResource(R.drawable.bq);
        chessBoardInterface[0][4].setImageResource(R.drawable.bking);
        chessBoardInterface[0][5].setImageResource(R.drawable.bb);
        chessBoardInterface[0][6].setImageResource(R.drawable.bk);
        chessBoardInterface[0][7].setImageResource(R.drawable.br);

        for (int i = 0; i < 8; i++) {
            chessBoardInterface[1][i].setImageResource(R.drawable.bp);
        }
        //PONG
        chessBoardInterface[7][0].setImageResource(R.drawable.wr);
        chessBoardInterface[7][1].setImageResource(R.drawable.wk);
        chessBoardInterface[7][2].setImageResource(R.drawable.wb);
        chessBoardInterface[7][3].setImageResource(R.drawable.wq);
        chessBoardInterface[7][4].setImageResource(R.drawable.wking);
        chessBoardInterface[7][5].setImageResource(R.drawable.wb);
        chessBoardInterface[7][6].setImageResource(R.drawable.wk);
        chessBoardInterface[7][7].setImageResource(R.drawable.wr);

        for (int i = 0; i < 8; i++) {
            chessBoardInterface[6][i].setImageResource(R.drawable.wp);
        }

        undo.setClickable(false);

        turn.setText("White's Turn");


        draw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveGame(v);
                //emptyBoard();
                //grid=populateBoard();
                turn.setText("Draw");
                draw.setClickable(false);
                undo.setClickable(false);
                aiMove.setClickable(false);
                resign.setClickable(false);
            }
        });

        resign.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          //emptyBoard();
                                          //grid=populateBoard();
                                          if (turnValue % 2 == 0) {
                                              turn.setText("Black Wins");
                                          } else {
                                              turn.setText("White Wins");
                                          }

                                          saveGame(v);

                                          draw.setClickable(false);
                                          undo.setClickable(false);
                                          aiMove.setClickable(false);
                                          resign.setClickable(false);
                                      }
                                  }


        );

        undo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                saveGrid.remove(saveGrid.size() - 1);

                if (turnValue % 2 == 0) {
                    turn.setText("Black's Turn" + lastString);
                } else {
                    turn.setText("White's Turn" + lastString);
                }
                turnValue--;

                if (lastString.equals(" (Check)")) {
                    kinginCheck = true;
                }

                if (justDidEnpassant) {
                    canDoEnPassant = true;
                    justDidEnpassant = false;
                }

                if (clicks % 2 == 1) {
                    clicks--;
                }

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (previousGrid[i][j] == null) {
                            grid[i][j] = null;

                            if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                                chessBoardInterface[i][j].setImageResource(R.drawable.lightbrown);
                            } else {
                                chessBoardInterface[i][j].setImageResource(R.drawable.darkbrown);
                            }

                        } else if (previousGrid[i][j] instanceof Pawn) {
                            grid[i][j] = new Pawn(previousGrid[i][j].location, previousGrid[i][j].isWhite);
                            if (previousGrid[i][j].isWhite()) {
                                chessBoardInterface[i][j].setImageResource(R.drawable.wp);
                            } else {
                                chessBoardInterface[i][j].setImageResource(R.drawable.bp);
                            }
                        } else if (previousGrid[i][j] instanceof Queen) {
                            grid[i][j] = new Queen(previousGrid[i][j].location, previousGrid[i][j].isWhite);
                            if (previousGrid[i][j].isWhite()) {
                                chessBoardInterface[i][j].setImageResource(R.drawable.wq);
                            } else {
                                chessBoardInterface[i][j].setImageResource(R.drawable.bq);
                            }
                        } else if (previousGrid[i][j] instanceof Rook) {
                            grid[i][j] = new Rook(previousGrid[i][j].location, previousGrid[i][j].isWhite);
                            if (previousGrid[i][j].isWhite()) {
                                chessBoardInterface[i][j].setImageResource(R.drawable.wr);
                            } else {
                                chessBoardInterface[i][j].setImageResource(R.drawable.br);
                            }
                        } else if (previousGrid[i][j] instanceof Knight) {
                            grid[i][j] = new Knight(previousGrid[i][j].location, previousGrid[i][j].isWhite);
                            if (previousGrid[i][j].isWhite()) {
                                chessBoardInterface[i][j].setImageResource(R.drawable.wk);
                            } else {
                                chessBoardInterface[i][j].setImageResource(R.drawable.bk);
                            }
                        } else if (previousGrid[i][j] instanceof King) {
                            grid[i][j] = new King(previousGrid[i][j].location, previousGrid[i][j].isWhite);
                            if (previousGrid[i][j].isWhite()) {
                                chessBoardInterface[i][j].setImageResource(R.drawable.wking);
                            } else {
                                chessBoardInterface[i][j].setImageResource(R.drawable.bking);
                            }
                        } else if (previousGrid[i][j] instanceof Bishop) {
                            grid[i][j] = new Bishop(previousGrid[i][j].location, previousGrid[i][j].isWhite);
                            if (previousGrid[i][j].isWhite()) {
                                chessBoardInterface[i][j].setImageResource(R.drawable.wb);
                            } else {
                                chessBoardInterface[i][j].setImageResource(R.drawable.bb);
                            }
                        }

                        if (grid[i][j] != null && previousGrid[i][j].hasMoved()) {
                            grid[i][j].setMoved();
                        }
                    }
                }

                addValidClickablePoints(true);

                previousGrid = new Piece[8][8];
                undo.setClickable(false);

                printBoard(grid);

            }
        });

        aiMove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                isAi = true;

                while (true) {

                    randomX = (int) (Math.random() * 8) + 0;
                    randomY = (int) (Math.random() * 8) + 0;

                    System.out.println(randomX + " " + randomY);

                    if (grid[randomX][randomY] == null || (grid[randomX][randomY].isWhite() && turnValue % 2 == 1)
                            || (!grid[randomX][randomY].isWhite() && turnValue % 2 == 0)) {
                        continue;
                    }

                    chessBoardInterface[randomX][randomY].performClick();

                    System.out.println(pointsToPick.size());
                    if (pointsToPick.size() == 0) {
                        pointsToPick = new ArrayList<int[]>();
                        chessBoardInterface[randomX][randomY].performClick();
                        continue;
                    }

                    break;
                }


                int i = (int) (Math.random() * (pointsToPick.size()));
                chessBoardInterface[pointsToPick.get(i)[0]][pointsToPick.get(i)[1]].performClick();

                System.out.println(i);
                System.out.println(pointsToPick.get(i)[0] + " " + pointsToPick.get(i)[1]);

                pointsToPick = new ArrayList<int[]>();

                isAi = false;

            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File dir = getFilesDir();
                File[] files = dir.listFiles();
                PopupMenu popup = new PopupMenu(getApplicationContext(),view);

                Arrays.sort(files, new Comparator()
                {
                    @Override
                    public int compare(Object f1, Object f2) {
                        return ((File) f1).getName().compareTo(((File) f2).getName());
                    }
                });
               Outer: for(File file:files){

                    if(file.getName().contains(".chess")) {
                        //System.out.println(file.getName());
                        popup.getMenu().add(file.getName().replaceAll(".chess",""))
                                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                chosenSave=menuItem.getTitle().toString();
                                //System.out.println(chosenSave);

                                try {
                                    //System.out.println("I AM HERE");
                                    FileInputStream fis = new FileInputStream(file);
                                    InputStreamReader isr = new InputStreamReader(fis);
                                    BufferedReader br = new BufferedReader(isr);
                                    saveGrid=new ArrayList<Piece[][]>();
                                    String line="";
                                    int row=0;
                                    //Log.d("LOAD","HERE");
                                    while((line=br.readLine())!=null){
                                        if(row%8==0) {
                                            saveGrid.add(new Piece[8][8]);
                                        }
                                        Log.d("LOAD",line);
                                        //TO DO: load each piece
                                        String[] pieces = line.split(" ");
                                        for(int i=0; i<8; i++){
                                            //saveGrid.get(saveGrid.size()-1)[row%8][i]=null;
                                            if(pieces[i].equals("null")){
                                                saveGrid.get(saveGrid.size()-1)[row%8][i]=null;
                                            }//end null check
                                            else{
                                                boolean isWhite = pieces[i].charAt(0)=='w';
                                                switch(pieces[i].charAt(1)){
                                                    case 'R':
                                                        saveGrid.get(saveGrid.size()-1)[row%8][i] = new Rook(new int[]{(row % 8), i},isWhite);
                                                        break;
                                                    case 'N':
                                                        saveGrid.get(saveGrid.size()-1)[row%8][i] = new Knight(new int[]{(row % 8), i},isWhite);
                                                        break;
                                                    case 'B':
                                                        saveGrid.get(saveGrid.size()-1)[row%8][i] = new Bishop(new int[]{(row % 8), i},isWhite);
                                                        break;
                                                    case 'Q':
                                                        saveGrid.get(saveGrid.size()-1)[row%8][i] = new Queen(new int[]{(row % 8), i},isWhite);
                                                        break;
                                                    case 'K':
                                                        saveGrid.get(saveGrid.size()-1)[row%8][i] = new King(new int[]{(row % 8), i},isWhite);
                                                        break;
                                                    case 'p':
                                                        saveGrid.get(saveGrid.size()-1)[row%8][i] = new Pawn(new int[]{(row % 8), i},isWhite);
                                                        break;
                                                }//end switch
                                            }//end actual piece check

                                        }//end pieces loop
                                        row++;
                                    }//end read loop
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                    Log.d("SIZE",saveGrid.size()+"");
                                for(Piece[][] grid:saveGrid)
                                    printBoard(grid);

                                draw.setClickable(false);
                                undo.setClickable(false);
                                aiMove.setClickable(false);
                                resign.setClickable(false);
                                draw.setVisibility(View.GONE);
                                undo.setVisibility(View.GONE);
                                aiMove.setVisibility(View.GONE);
                                resign.setVisibility(View.GONE);
                                load.setVisibility(View.GONE);
                                next.setVisibility(View.VISIBLE);
                                loadDate.setVisibility(View.GONE);
                                turn.setText("");
                                textView.setText("");

                                //PING
                                drawBoard(chessBoardInterface);
                                Piece[][] grid = saveGrid.remove(0);

                                for(int i=0; i<8; i++){
                                    for(int j=0; j<8; j++){
                                        if(grid[i][j]!=null) {
                                            if (grid[i][j].isWhite) {
                                                if (grid[i][j] instanceof Rook) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.wr);
                                                } else if (grid[i][j] instanceof Knight) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.wk);
                                                } else if (grid[i][j] instanceof Bishop) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.wb);
                                                } else if (grid[i][j] instanceof Queen) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.wq);
                                                } else if (grid[i][j] instanceof King) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.wking);
                                                } else if (grid[i][j] instanceof Pawn) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.wp);
                                                }
                                            }//end white pieces
                                            else {
                                                if (grid[i][j] instanceof Rook) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.br);
                                                } else if (grid[i][j] instanceof Knight) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.bk);
                                                } else if (grid[i][j] instanceof Bishop) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.bb);
                                                } else if (grid[i][j] instanceof Queen) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.bq);
                                                } else if (grid[i][j] instanceof King) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.bking);
                                                } else if (grid[i][j] instanceof Pawn) {
                                                    chessBoardInterface[i][j].setImageResource(R.drawable.bp);
                                                }
                                            }//end black pieces
                                        }
                                        else{
                                            if (  (i+j)%2==0 ){
                                                chessBoardInterface[i][j].setImageResource(R.drawable.lightbrown);
                                            }else{
                                                chessBoardInterface[i][j].setImageResource(R.drawable.darkbrown);
                                            }
                                        }
                                    }//end col
                                }//end row
                                Toast.makeText(getApplicationContext(), "Loaded Saved Game", Toast.LENGTH_LONG).show();
                                return true;
                            }
                        });
                    }
                }
                popup.show();


            }
        });

        loadDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File dir = getFilesDir();
                File[] files = dir.listFiles();
                PopupMenu popup = new PopupMenu(getApplicationContext(),view);

                Arrays.sort(files, Comparator.comparingLong(File::lastModified));
                Outer: for(File file:files){

                    if(file.getName().contains(".chess")) {
                        //System.out.println(file.getName());
                        popup.getMenu().add(file.getName().replaceAll(".chess",""))
                                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        chosenSave=menuItem.getTitle().toString();
                                        //System.out.println(chosenSave);

                                        try {
                                            //System.out.println("I AM HERE");
                                            FileInputStream fis = new FileInputStream(file);
                                            InputStreamReader isr = new InputStreamReader(fis);
                                            BufferedReader br = new BufferedReader(isr);
                                            saveGrid=new ArrayList<Piece[][]>();
                                            String line="";
                                            int row=0;
                                            //Log.d("LOAD","HERE");
                                            while((line=br.readLine())!=null){
                                                if(row%8==0) {
                                                    saveGrid.add(new Piece[8][8]);
                                                }
                                                Log.d("LOAD",line);
                                                //TO DO: load each piece
                                                String[] pieces = line.split(" ");
                                                for(int i=0; i<8; i++){
                                                    //saveGrid.get(saveGrid.size()-1)[row%8][i]=null;
                                                    if(pieces[i].equals("null")){
                                                        saveGrid.get(saveGrid.size()-1)[row%8][i]=null;
                                                    }//end null check
                                                    else{
                                                        boolean isWhite = pieces[i].charAt(0)=='w';
                                                        switch(pieces[i].charAt(1)){
                                                            case 'R':
                                                                saveGrid.get(saveGrid.size()-1)[row%8][i] = new Rook(new int[]{(row % 8), i},isWhite);
                                                                break;
                                                            case 'N':
                                                                saveGrid.get(saveGrid.size()-1)[row%8][i] = new Knight(new int[]{(row % 8), i},isWhite);
                                                                break;
                                                            case 'B':
                                                                saveGrid.get(saveGrid.size()-1)[row%8][i] = new Bishop(new int[]{(row % 8), i},isWhite);
                                                                break;
                                                            case 'Q':
                                                                saveGrid.get(saveGrid.size()-1)[row%8][i] = new Queen(new int[]{(row % 8), i},isWhite);
                                                                break;
                                                            case 'K':
                                                                saveGrid.get(saveGrid.size()-1)[row%8][i] = new King(new int[]{(row % 8), i},isWhite);
                                                                break;
                                                            case 'p':
                                                                saveGrid.get(saveGrid.size()-1)[row%8][i] = new Pawn(new int[]{(row % 8), i},isWhite);
                                                                break;
                                                        }//end switch
                                                    }//end actual piece check

                                                }//end pieces loop
                                                row++;
                                            }//end read loop
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("SIZE",saveGrid.size()+"");
                                        for(Piece[][] grid:saveGrid)
                                            printBoard(grid);

                                        draw.setClickable(false);
                                        undo.setClickable(false);
                                        aiMove.setClickable(false);
                                        resign.setClickable(false);
                                        draw.setVisibility(View.GONE);
                                        undo.setVisibility(View.GONE);
                                        aiMove.setVisibility(View.GONE);
                                        resign.setVisibility(View.GONE);
                                        load.setVisibility(View.GONE);
                                        next.setVisibility(View.VISIBLE);
                                        loadDate.setVisibility(View.GONE);
                                        turn.setText("");
                                        textView.setText("");

                                        //PING
                                        drawBoard(chessBoardInterface);
                                        Piece[][] grid = saveGrid.remove(0);

                                        for(int i=0; i<8; i++){
                                            for(int j=0; j<8; j++){
                                                if(grid[i][j]!=null) {
                                                    if (grid[i][j].isWhite) {
                                                        if (grid[i][j] instanceof Rook) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.wr);
                                                        } else if (grid[i][j] instanceof Knight) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.wk);
                                                        } else if (grid[i][j] instanceof Bishop) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.wb);
                                                        } else if (grid[i][j] instanceof Queen) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.wq);
                                                        } else if (grid[i][j] instanceof King) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.wking);
                                                        } else if (grid[i][j] instanceof Pawn) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.wp);
                                                        }
                                                    }//end white pieces
                                                    else {
                                                        if (grid[i][j] instanceof Rook) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.br);
                                                        } else if (grid[i][j] instanceof Knight) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.bk);
                                                        } else if (grid[i][j] instanceof Bishop) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.bb);
                                                        } else if (grid[i][j] instanceof Queen) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.bq);
                                                        } else if (grid[i][j] instanceof King) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.bking);
                                                        } else if (grid[i][j] instanceof Pawn) {
                                                            chessBoardInterface[i][j].setImageResource(R.drawable.bp);
                                                        }
                                                    }//end black pieces
                                                }
                                                else{
                                                    if (  (i+j)%2==0 ){
                                                        chessBoardInterface[i][j].setImageResource(R.drawable.lightbrown);
                                                    }else{
                                                        chessBoardInterface[i][j].setImageResource(R.drawable.darkbrown);
                                                    }
                                                }
                                            }//end col
                                        }//end row
                                        Toast.makeText(getApplicationContext(), "Loaded Saved Game", Toast.LENGTH_LONG).show();
                                        return true;
                                    }
                                });
                    }
                }
                popup.show();


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveGrid.size()==0) {
                    next.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Replay complete.",Toast.LENGTH_LONG).show();
                    return;
                }

                drawBoard(chessBoardInterface);
                Piece[][] grid = saveGrid.remove(0);

                for(int i=0; i<8; i++){
                    for(int j=0; j<8; j++){
                        if(grid[i][j]!=null) {
                            if (grid[i][j].isWhite) {
                                if (grid[i][j] instanceof Rook) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.wr);
                                } else if (grid[i][j] instanceof Knight) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.wk);
                                } else if (grid[i][j] instanceof Bishop) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.wb);
                                } else if (grid[i][j] instanceof Queen) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.wq);
                                } else if (grid[i][j] instanceof King) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.wking);
                                } else if (grid[i][j] instanceof Pawn) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.wp);
                                }
                            }//end white pieces
                            else {
                                if (grid[i][j] instanceof Rook) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.br);
                                } else if (grid[i][j] instanceof Knight) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.bk);
                                } else if (grid[i][j] instanceof Bishop) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.bb);
                                } else if (grid[i][j] instanceof Queen) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.bq);
                                } else if (grid[i][j] instanceof King) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.bking);
                                } else if (grid[i][j] instanceof Pawn) {
                                    chessBoardInterface[i][j].setImageResource(R.drawable.bp);
                                }
                            }//end black pieces
                        }
                        else{
                            if (  (i+j)%2==0 ){
                                chessBoardInterface[i][j].setImageResource(R.drawable.lightbrown);
                            }else{
                                chessBoardInterface[i][j].setImageResource(R.drawable.darkbrown);
                            }
                        }
                    }//end col
                }//end row

                if(saveGrid.size()==0) {
                    next.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Replay complete.",Toast.LENGTH_LONG).show();
                }
            }
        });

        undo.setClickable(false);

        mPrefs = getPreferences(MODE_PRIVATE);
        addValidClickablePoints(true);


    }


    public void selectSquare(View view) throws IOException {

        clicks++;

        ImageView img = (ImageView) view;
        String currentMove = getLocation(view);

            if(clicks % 2 == 0){
                int[][] coords = new int[2][2];
                coords[0]=toCoord(lastMove); //SOURCE
                coords[1]=toCoord(currentMove); //DESTINATION

                if(coords[0][0] == coords[1][0] &&  coords[0][1] == coords[1][1]){
                    turnValue+=2;

                    addValidClickablePoints(true);
                    return;
                }


                for(int i=0; i <8; i++){
                    for(int j=0; j <8; j++){

                        boolean tempEmpassant = false;

                        if(grid[i][j] == null) {
                            previousGrid[i][j] = null;
                        }else if(grid[i][j] instanceof  Pawn){
                          previousGrid[i][j] = new Pawn(grid[i][j].location,grid[i][j].isWhite);
                      }else if(grid[i][j] instanceof  Queen){
                          previousGrid[i][j] = new Queen(grid[i][j].location,grid[i][j].isWhite);
                      }else if(grid[i][j] instanceof  Rook){
                          previousGrid[i][j] = new Rook(grid[i][j].location,grid[i][j].isWhite);
                      }else if(grid[i][j] instanceof  Knight){
                          previousGrid[i][j] = new Knight(grid[i][j].location,grid[i][j].isWhite);
                      }else if(grid[i][j] instanceof  King){
                          previousGrid[i][j] = new King(grid[i][j].location,grid[i][j].isWhite);
                      }else if(grid[i][j] instanceof  Bishop){
                          previousGrid[i][j] = new Bishop(grid[i][j].location,grid[i][j].isWhite);
                      }

                        if (previousGrid[i][j] != null && grid[i][j].hasMoved()) {
                            previousGrid[i][j].setMoved();
                        }
                    }
                }

                Piece[][] tempGrid=new Piece[8][8];
                for(int i=0; i<8; i++){
                    for(int j=0; j<8; j++){
                        tempGrid[i][j]=previousGrid[i][j];
                    }
                }

                saveGrid.add(tempGrid);
              /*  for(Piece[][] grid:saveGrid){
                    System.out.println("GRID:");
                    for(int i=0; i<8; i++){
                        for(int j=0; j<8; j++){
                            System.out.print(grid[i][j]+" ");
                        }
                        System.out.println();
                    }

                }*/
                undo.setClickable(true);


                if( grid[ coords[0][0] ][ coords[0][1] ] instanceof King && Math.abs(coords[0][1] - coords[1][1]) ==2  ) {
                        canCastle = true;
                }

                    if(canDoEnPassant) {

                        if ((grid[coords[0][0]][coords[0][1]] instanceof Pawn) && lastY == coords[0][0] && (coords[1][0] == firstPosX && coords[1][1] == firstPosY) || (coords[1][0] == secondPosX && coords[1][1] == secondPosY)) {

                            grid[lastY][lastX] = null;

                            if (  (lastY % 2 ==0 &&  lastX % 2 == 0)  || (lastY % 2 ==1 &&  lastX % 2 == 1) ){
                                chessBoardInterface[lastY][lastX].setImageResource(R.drawable.lightbrown);
                            }else{
                                chessBoardInterface[lastY][lastX].setImageResource(R.drawable.darkbrown);
                            }

                        }
                        justDidEnpassant = true;
                        canDoEnPassant = false;
                    }

                if( (grid[coords[0][0]][coords[0][1]] instanceof Pawn) &&  Math.abs( grid[ coords[0][0] ][ coords[0][1] ].location[0] - coords[1][0])== 2 &&  Math.abs( grid[ coords[0][0] ][ coords[0][1] ].location[1] - coords[1][1])== 0) {

                    if( grid[ coords[0][0] ][ coords[0][1] ].location[0] - coords[1][0] == 2) {
                        firstPosY = grid[coords[0][0]][coords[0][1]].location[1];
                        firstPosX = grid[coords[0][0]][coords[0][1]].location[0]-1 >= 0 ? grid[coords[0][0]][coords[0][1]].location[0]-1 : grid[coords[0][0]][coords[0][1]].location[0];
                        secondPosY = grid[coords[0][0]][coords[0][1]].location[1];
                        secondPosX  = grid[coords[0][0]][coords[0][1]].location[0]-2 >= 0 ?  grid[coords[0][0]][coords[0][1]].location[0]-2 :  grid[coords[0][0]][coords[0][1]].location[0];
                    }else {
                        firstPosY = grid[coords[0][0]][coords[0][1]].location[1];
                        firstPosX = grid[coords[0][0]][coords[0][1]].location[0]+1 <= 7 ? grid[coords[0][0]][coords[0][1]].location[0]+1 :  grid[coords[0][0]][coords[0][1]].location[0]; ;
                        secondPosY = grid[coords[0][0]][coords[0][1]].location[1];
                        secondPosX = grid[coords[0][0]][coords[0][1]].location[0]+2 <= 7 ? grid[coords[0][0]][coords[0][1]].location[0]+2 : grid[coords[0][0]][coords[0][1]].location[0];
                    }
                    lastY = coords[1][0];
                    lastX = coords[1][1];
                    canDoEnPassant = true;

                }

                if(grid[ coords[1][0] ][ coords[1][1] ] != null) {
                    pieceCaptured = true;
                }

                if(grid[ coords[1][0] ][ coords[1][1] ] != null ) {
                    locationCaptured[ coords[1][0] ][ coords[1][1] ] = true;
                }

                grid[ coords[0][0] ][ coords[0][1] ].location=coords[1];
                grid[ coords[1][0] ][ coords[1][1] ] = grid[ coords[0][0] ][ coords[0][1] ];
                grid[ coords[0][0] ][ coords[0][1] ] = null;
                if(grid[ coords[1][0] ][ coords[1][1] ] != null &&  !grid[ coords[1][0] ][ coords[1][1] ].hasMoved())
                    grid[ coords[1][0] ][ coords[1][1] ].setMoved();

                chessBoardInterface[coords[1][0]][coords[1][1]].setImageDrawable(chessBoardInterface[coords[0][0]][coords[0][1]].getDrawable());


                if (  (coords[0][0] % 2 ==0 &&  coords[0][1] % 2 == 0)  || (coords[0][0] % 2 ==1 &&  coords[0][1] % 2 == 1) ){
                    chessBoardInterface[coords[0][0]][coords[0][1]].setImageResource(R.drawable.lightbrown);
                }else{
                    chessBoardInterface[coords[0][0]][coords[0][1]].setImageResource(R.drawable.darkbrown);
                }



               if( (grid[coords[1][0]][coords[1][1]] instanceof Pawn) && isEightRank(grid[ coords[1][0] ][ coords[1][1] ]) ) {


                   if(isAi) {
                       boolean isWhite = grid[coords[1][0]][coords[1][1]].isWhite();
                       grid[coords[1][0]][coords[1][1]]
                               = new Queen(coords[1], isWhite);
                       if (isWhite)
                           chessBoardInterface[coords[1][0]][coords[1][1]].setImageResource(R.drawable.wq);
                       else
                           chessBoardInterface[coords[1][0]][coords[1][1]].setImageResource(R.drawable.bq);
                   }else{

                       globalCords = coords;
                       openDialog();


                   }

                }

                if(kinginCheck && !King.inCheck(grid, turnValue, lastCordinates) && (

                        pieceCaptured

                                ||

                                (grid[ coords[1][0] ][ coords[1][1] ] instanceof King)

                                ||

                                (   grid[ coords[1][0] ][ coords[1][1] ] instanceof Queen || grid[ coords[1][0] ][ coords[1][1] ] instanceof Rook || grid[ coords[1][0] ][ coords[1][1] ] instanceof Bishop) &&


                                        !isNextToKing(grid,coords[1][0], coords[1][1])

                )

                ) {

                    kinginCheck = false;
                    lastString = " (Check)";
                }else{
                    lastString = "";
                }

                String message = "";

                if(canCastle) {
                    if (coords[0][1] - coords[1][1] < 0) {
                        if ((turnValue) % 2 == 1) {
                            boolean isWhite = grid[0][7].isWhite();
                            grid[coords[1][0]][coords[1][1] - 1] = new Rook(new int[]{coords[1][0], (coords[1][1] - 1)}, grid[0][7].isWhite());
                            grid[0][7] = null;

                            if(isWhite){
                                chessBoardInterface[coords[1][0]][coords[1][1] - 1].setImageResource(R.drawable.wr);
                            }else{
                                chessBoardInterface[coords[1][0]][coords[1][1] - 1].setImageResource(R.drawable.br);
                            }

                            chessBoardInterface[0][7].setImageResource(R.drawable.darkbrown);

                        } else {

                            boolean isWhite = grid[7][7].isWhite();

                            grid[coords[1][0]][coords[1][1] - 1] = new Rook(new int[]{coords[1][0], (coords[1][1] - 1)}, isWhite);
                            grid[7][7] = null;

                            if(isWhite){
                                chessBoardInterface[coords[1][0]][coords[1][1] - 1].setImageResource(R.drawable.wr);
                            }else{
                                chessBoardInterface[coords[1][0]][coords[1][1] - 1].setImageResource(R.drawable.br);
                            }

                            chessBoardInterface[7][7].setImageResource(R.drawable.lightbrown);
                        }
                    } else {
                        if ((turnValue) % 2 == 1) {

                            boolean isWhite = grid[0][0].isWhite();

                            grid[coords[1][0]][coords[1][1] + 1] = new Rook(new int[]{coords[1][0], (coords[1][1] + 1)}, isWhite);
                            grid[0][0] = null;

                            if(isWhite){
                                chessBoardInterface[coords[1][0]][coords[1][1] + 1].setImageResource(R.drawable.wr);
                            }else{
                                chessBoardInterface[coords[1][0]][coords[1][1] + 1].setImageResource(R.drawable.br);
                            }

                            chessBoardInterface[0][0].setImageResource(R.drawable.lightbrown);
                        } else {
                            boolean isWhite = grid[7][0].isWhite();

                            grid[coords[1][0]][coords[1][1] + 1] = new Rook(new int[]{coords[1][0], (coords[1][1] + 1)}, isWhite);
                            grid[7][0] = null;

                            if(isWhite){
                                chessBoardInterface[coords[1][0]][coords[1][1] + 1].setImageResource(R.drawable.wr);
                            }else{
                                chessBoardInterface[coords[1][0]][coords[1][1] + 1].setImageResource(R.drawable.br);
                            }

                            chessBoardInterface[7][0].setImageResource(R.drawable.darkbrown);
                        }
                    }
                    canCastle = false;
                }

                if(King.inCheck(grid, turnValue, coords)) {
                    lastCordinates = coords;
                    kinginCheck = true;

                    if(!King.canEscape(grid, turnValue-1)) {
                        emptyBoard();
                        turnValue+=1;
                        if((turnValue)%2==0) turn.setText("Checkmate, White wins");
                        else turn.setText("Checkmate, Black wins");
                        draw.setClickable(false);
                        undo.setClickable(false);
                        aiMove.setClickable(false);
                        resign.setClickable(false);
                        saveGame(view);
                        return;
                    }else {
                        message=" (Check)";
                    }
                }

                if(turnValue % 2 ==0 ){
                    turn.setText("Black's Turn"+message);
                }else{
                    turn.setText("White's Turn"+message);
                }

                turnValue++;

                addValidClickablePoints(true);

                printBoard(grid);

            }else{
                lastMove =currentMove;

                addValidClickablePoints(false);
            }
    }

    public void saveGame(View view){

        saveGrid.add(grid);

        //LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //View popupView = inflater.inflate(R.layout.popout_menu, null);
        setContentView(R.layout.popout_menu);

        winner = findViewById(R.id.whoWins);
        popupName = (EditText) findViewById(R.id.popup_name);
        popupSave = (Button) findViewById(R.id.popup_save);

        if (turnValue % 2 == 0) {
            winner.setText("Black Wins");
        } else {
            winner.setText("White Wins");
        }

        popupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = popupName.getText().toString();
                //SAVE FILE
                try {
                    FileOutputStream fos = openFileOutput(name+".chess", MODE_PRIVATE);
                    String line="";
                    for(Piece[][] grid:saveGrid) {

                        for(int i=0; i<8; i++){
                            line="";
                            for(int j=0; j<8; j++){
                                line+=grid[i][j]+" ";
                            }//end j
                            fos.write(line.getBytes(StandardCharsets.UTF_8));
                            fos.write("\r\n".getBytes());

                        }//end i
                    }//end grid
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("SAVE","ERROR");
                }
                Toast.makeText(getApplicationContext(),"Game Saved",Toast.LENGTH_LONG).show();
                //setContentView(R.layout.activity_main);
            }

        });


        //final PopupWindow window = new PopupWindow(popupView, 500,500,true);
        //window.showAtLocation(view, Gravity.CENTER, 0,0);
    }


    private String getLocation(View view){
        switch(view.getId()) {

            case R.id.a1:
                return "a1";

            case R.id.a2:
                return "a2";

            case R.id.a3:
                return "a3";

            case R.id.a4:
                return "a4";

            case R.id.a5:
                return "a5";

            case R.id.a6:
                return "a6";

            case R.id.a7:
                return "a7";

            case R.id.a8:
                return "a8";

            case R.id.b1:
                return "b1";

            case R.id.b2:
                return "b2";

            case R.id.b3:
                return "b3";

            case R.id.b4:
                return "b4";

            case R.id.b5:
                return "b5";

            case R.id.b6:
                return "b6";

            case R.id.b7:
                return "b7";

            case R.id.b8:
                return "b8";

            case R.id.c1:
                return "c1";

            case R.id.c2:
                return "c2";

            case R.id.c3:
                return "c3";

            case R.id.c4:
                return "c4";

            case R.id.c5:
                return "c5";

            case R.id.c6:
                return "c6";

            case R.id.c7:
                return "c7";

            case R.id.c8:
                return "c8";

            case R.id.d1:
                return "d1";

            case R.id.d2:
                return "d2";

            case R.id.d3:
                return "d3";

            case R.id.d4:
                return "d4";

            case R.id.d5:
                return "d5";

            case R.id.d6:
                return "d6";

            case R.id.d7:
                return "d7";

            case R.id.d8:
                return "d8";

            case R.id.e1:
                return "e1";

            case R.id.e2:
                return "e2";

            case R.id.e3:
                return "e3";

            case R.id.e4:
                return "e4";

            case R.id.e5:
                return "e5";

            case R.id.e6:
                return "e6";

            case R.id.e7:
                return "e7";

            case R.id.e8:
                return "e8";

            case R.id.f1:
                return "f1";

            case R.id.f2:
                return "f2";

            case R.id.f3:
                return "f3";

            case R.id.f4:
                return "f4";

            case R.id.f5:
                return "f5";

            case R.id.f6:
                return "f6";

            case R.id.f7:
                return "f7";

            case R.id.f8:
                return "f8";

            case R.id.g1:
                return "g1";

            case R.id.g2:
                return "g2";

            case R.id.g3:
                return "g3";

            case R.id.g4:
                return "g4";

            case R.id.g5:
                return "g5";

            case R.id.g6:
                return "g6";

            case R.id.g7:
                return "g7";

            case R.id.g8:
                return "g8";

            case R.id.h1:
                return "h1";

            case R.id.h2:
                return "h2";

            case R.id.h3:
                return "h3";

            case R.id.h4:
                return "h4";

            case R.id.h5:
                return "h5";

            case R.id.h6:
                return "h6";

            case R.id.h7:
                return "h7";

            case R.id.h8:
                return "h8";

        }

        return null;
    }

    public static  Piece[][] populateBoard() {

        Piece[][] board = new Piece[8][8];

        board[0][0] = new Rook(new int[] {0,0},false);
        board[0][1] = new Knight(new int[] {0,1},false);
        board[0][2] = new Bishop(new int[] {0,2},false);
        board[0][3] = new Queen(new int[] {0,3},false);
        board[0][4] = new King(new int[] {0,4},false);
        board[0][5] = new Bishop(new int[] {0,5},false);
        board[0][6] = new Knight(new int[] {0,6},false);
        board[0][7] = new Rook(new int[] {0,7},false);

        for(int i=0; i<8; i++) {
            board[1][i] = new Pawn(new int[] {1,i},false);
        }

        board[7][0] = new Rook(new int[] {7,0},true);
        board[7][1] = new Knight(new int[] {7,1},true);
        board[7][2] = new Bishop(new int[] {7,2},true);
        board[7][3] = new Queen(new int[] {7,3},true);
        board[7][4] = new King(new int[] {7,4},true);
        board[7][5] = new Bishop(new int[] {7,5},true);
        board[7][6] = new Knight(new int[] {7,6},true);
        board[7][7] = new Rook(new int[] {7,7},true);

        for(int i=0; i<8; i++) {
            board[6][i] = new Pawn(new int[] {6,i},true);
        }

        return board;
    }//end populateBoar

    private static int[] toCoord(String string) {
        int[] out = new int[2];
        out[0]=8-Integer.parseInt(""+string.charAt(1));
        out[1]=(int)(string.charAt(0))-97;
        return out;
    }

    private static boolean isNextToKing(Piece[][] board, int posx, int posy) {
        if(board[posx][posy].isWhite() ) {
            return (posx - 1>= 0 && board[posx-1][posy] != null && board[posx-1][posy].isWhite() && board[posx-1][posy] instanceof King)
                    || (posx + 1 <= 7 && board[posx+1][posy] != null && board[posx+1][posy].isWhite() && board[posx+1][posy] instanceof King)
                    || (posy + 1 <= 7 &&  board[posx][posy+1] != null && board[posx][posy+1].isWhite() && board[posx][posy+1] instanceof King)
                    || (posy - 1>=0 && board[posx][posy-1] != null && board[posx][posy-1].isWhite() && board[posx][posy-1] instanceof King);
        }else {
            return (posx - 1>= 0 && board[posx-1][posy] != null && !board[posx-1][posy].isWhite() && board[posx-1][posy] instanceof King)
                    || (posx + 1 <= 7 && board[posx+1][posy] != null && !board[posx+1][posy].isWhite() && board[posx+1][posy] instanceof King)
                    || (posy + 1 <= 7 && board[posx][posy+1] != null && !board[posx][posy+1].isWhite() && board[posx][posy+1] instanceof King)
                    || (posy - 1>=0 && board[posx][posy-1] != null && !board[posx][posy-1].isWhite() && board[posx][posy-1] instanceof King);
        }
    }

    private static boolean  isEightRank(Piece piece) {

        if(piece == null) {
            return false;
        }

        return (piece.isWhite() && piece.location[0] == 0) ||  (!piece.isWhite() && piece.location[0] == 7);
    }

    public static void printBoard(Piece[][] board) {
        System.out.println();
        System.out.println();
        for(int i=0; i<8; i++) {
            for(int k=0; k<8; k++) {
                if(board[i][k]!=null)
                    System.out.print(board[i][k]+" ");
                else {
                    if((i+k)%2==0)
                        System.out.print("  "+" ");
                    else System.out.print("##"+" ");
                }
            }//end cols
            System.out.println(8-i);
        }//end rows
        System.out.println(" a  b  c  d  e  f  g  h \n");
    }

    private void addValidClickablePoints(boolean isFirstTry){



        if(isFirstTry && turnValue % 2 ==0 ){

            for(int i=0; i<8; i++) {
                for(int k=0; k<8; k++) {
                   if(grid[i][k] != null && grid[i][k].isWhite() ){
                       chessBoardInterface[i][k].setClickable(true);
                   }else{
                        chessBoardInterface[i][k].setClickable(false);
                   }
                }

            }

        }else if (isFirstTry && turnValue % 2 ==1){

            for(int i=0; i<8; i++) {
                for(int k=0; k<8; k++) {
                    if(grid[i][k] != null && !grid[i][k].isWhite()){

                        chessBoardInterface[i][k].setClickable(true);
                    }else{
                        chessBoardInterface[i][k].setClickable(false);
                    }
                }

            }


        }else {

            int lastCordinates[] = toCoord(lastMove);

            boolean originallyMoved = grid[ lastCordinates[0] ][ lastCordinates[1] ].hasMoved();

            for(int i=0; i<8; i++) {
                for(int k=0; k<8; k++) {

                    if (grid[i][k] != null && ( (grid[i][k].isWhite() && turnValue % 2 ==0) ||  (!grid[i][k].isWhite() && turnValue % 2 ==1) )) {
                        chessBoardInterface[i][k].setClickable(false);
                        continue;
                    }

                    boolean tempEmpassant = false;
                    boolean testPieceMoved = false;

                    if(grid[i][k] != null){
                        testPieceMoved = grid[i][k].hasMoved();
                    }

                    int coords[][] = {lastCordinates, {i, k}};
                    boolean couldCastle = false;

                    if( grid[ coords[0][0] ][ coords[0][1] ] instanceof King && Math.abs(coords[0][1] - coords[1][1]) ==2 ) {

                        if( ((grid[ coords[0][0] ][ coords[0][1] ] instanceof King)  && (kinginCheck || coords[0][0] !=  coords[1][0] || !King.canCastle(grid, coords, locationCaptured)))  ) {
                            chessBoardInterface[i][k].setClickable(false);
                            continue;
                        }else{
                            couldCastle=true;
                        }
                    }

                    if( (couldCastle && grid[ coords[0][0] ][ coords[0][1] ] instanceof King && Math.abs(coords[0][1] - coords[1][1]) ==2) || grid[ coords[0][0] ][ coords[0][1] ].moveLegal(grid, coords[1])) {

                        if ((grid[ coords[0][0] ][ coords[0][1] ] instanceof Pawn) &&  !canDoEnPassant && Math.abs( grid[ coords[0][0] ][ coords[0][1] ].location[1] - coords[1][1]) != 0 && grid[ coords[1][0] ][ coords[1][1] ] == null ) {

                            chessBoardInterface[i][k].setClickable(false);
                            continue;
                        }

                        if(canDoEnPassant) {
                            if((grid[ coords[0][0] ][ coords[0][1] ] instanceof Pawn) && Math.abs( grid[ coords[0][0] ][ coords[0][1] ].location[1] - coords[1][1]) != 0 &&  !((grid[ coords[0][0] ][ coords[0][1] ] instanceof Pawn) && lastY == coords[0][0] &&(coords[1][0] == firstPosX &&  coords[1][1] == firstPosY) ||  (coords[1][0] == secondPosX &&  coords[1][1] == secondPosY))){
                                chessBoardInterface[i][k].setClickable(false);
                                continue;
                            }
                        }

                        Piece temp =   null;

                        if( grid[ coords[1][0] ][ coords[1][1] ] instanceof  Bishop){
                            temp = new Bishop(grid[ coords[1][0] ][ coords[1][1] ].location, grid[ coords[1][0] ][ coords[1][1] ].isWhite() );
                        }else if( grid[ coords[1][0] ][ coords[1][1] ] instanceof  King){
                            temp = new King(grid[ coords[1][0] ][ coords[1][1] ].location, grid[ coords[1][0] ][ coords[1][1] ].isWhite() );
                        }else if( grid[ coords[1][0] ][ coords[1][1] ] instanceof  Knight){
                            temp = new Knight(grid[ coords[1][0] ][ coords[1][1] ].location, grid[ coords[1][0] ][ coords[1][1] ].isWhite() );
                        }else if( grid[ coords[1][0] ][ coords[1][1] ] instanceof  Pawn){
                            temp = new Pawn(grid[ coords[1][0] ][ coords[1][1] ].location, grid[ coords[1][0] ][ coords[1][1] ].isWhite() );
                        }else if( grid[ coords[1][0] ][ coords[1][1] ] instanceof  Queen){
                            temp = new Queen(grid[ coords[1][0] ][ coords[1][1] ].location, grid[ coords[1][0] ][ coords[1][1] ].isWhite() );
                        }else if( grid[ coords[1][0] ][ coords[1][1] ] instanceof  Rook){
                            temp = new Rook(grid[ coords[1][0] ][ coords[1][1] ].location, grid[ coords[1][0] ][ coords[1][1] ].isWhite() );
                        }

                        grid[ coords[0][0] ][ coords[0][1] ].location=coords[1];
                        grid[ coords[1][0] ][ coords[1][1] ] = grid[ coords[0][0] ][ coords[0][1] ];
                        grid[ coords[0][0] ][ coords[0][1] ] = null;

                        if(King.isPieceInCheckWithTheirKing(grid, turnValue)) {

                            grid[ coords[1][0] ][ coords[1][1] ].location=coords[0];
                            grid[ coords[0][0] ][ coords[0][1] ] = grid[ coords[1][0] ][ coords[1][1] ];
                            grid[ coords[1][0] ][ coords[1][1] ] = temp;
                            chessBoardInterface[i][k].setClickable(false);
                            continue;
                        }

                        if(kinginCheck && !King.isPieceInCheckWithTheirKing(grid, turnValue)) {

                            grid[ coords[1][0] ][ coords[1][1] ].location=coords[0];
                            grid[ coords[0][0] ][ coords[0][1] ] = grid[ coords[1][0] ][ coords[1][1] ];
                            grid[ coords[1][0] ][ coords[1][1] ] = temp;
                            chessBoardInterface[i][k].setClickable(true);
                            pointsToPick.add(new int[] {i,k});
                            continue;
                        }else if(kinginCheck) {

                            grid[ coords[1][0] ][ coords[1][1] ].location=coords[0];
                            grid[ coords[0][0] ][ coords[0][1] ] = grid[ coords[1][0] ][ coords[1][1] ];
                            grid[ coords[1][0] ][ coords[1][1] ] = temp;
                            chessBoardInterface[i][k].setClickable(false);
                            continue;
                        }

                        grid[ coords[1][0] ][ coords[1][1] ].location=coords[0];
                        grid[ coords[0][0] ][ coords[0][1] ] = grid[ coords[1][0] ][ coords[1][1] ];
                        grid[ coords[1][0] ][ coords[1][1] ] = temp;


                        chessBoardInterface[i][k].setClickable(true);
                        pointsToPick.add(new int[] {i,k});

                        if(originallyMoved != grid[ lastCordinates[0] ][ lastCordinates[1] ].hasMoved() )
                            grid[ lastCordinates[0] ][ lastCordinates[1] ].setMoved();
                    }

                    }
            }


                chessBoardInterface[lastCordinates[0]][lastCordinates[1]].setClickable(true);



            printBoard(grid);

        }

    }

    private void drawBoard( ImageView a[][]){
        for(int i=0; i< 8; i++){
            for(int j=0; j< 8; j++){
                if(i == 7 && j ==0) {
                    a[i][j] = findViewById(R.id.a1);
                }else if(i == 7 && j ==1) {
                    a[i][j] = findViewById(R.id.b1);
                }else  if(i == 7 && j ==2) {
                    a[i][j] = findViewById(R.id.c1);
                }else if(i == 7 && j ==3) {
                    a[i][j] = findViewById(R.id.d1);
                }else if(i == 7 && j ==4) {
                    a[i][j] = findViewById(R.id.e1);
                }else if(i == 7 && j ==5) {
                    a[i][j] = findViewById(R.id.f1);
                }else  if(i == 7 && j ==6) {
                    a[i][j] = findViewById(R.id.g1);
                }else if(i == 7 && j ==7) {
                    a[i][j] = findViewById(R.id.h1);
                }else  if(i == 6 && j ==0) {
                    a[i][j] = findViewById(R.id.a2);
                }else if(i == 6 && j ==1) {
                    a[i][j] = findViewById(R.id.b2);
                }else  if(i == 6 && j ==2) {
                    a[i][j] = findViewById(R.id.c2);
                }else if(i == 6 && j ==3) {
                    a[i][j] = findViewById(R.id.d2);
                }else if(i == 6 && j ==4) {
                    a[i][j] = findViewById(R.id.e2);
                }else if(i == 6 && j ==5) {
                    a[i][j] = findViewById(R.id.f2);
                }else  if(i == 6 && j ==6) {
                    a[i][j] = findViewById(R.id.g2);
                }else if(i == 6 && j ==7) {
                    a[i][j] = findViewById(R.id.h2);
                }else  if(i == 5 && j ==0) {
                    a[i][j] = findViewById(R.id.a3);
                }else if(i == 5 && j ==1) {
                    a[i][j] = findViewById(R.id.b3);
                }else  if(i == 5 && j ==2) {
                    a[i][j] = findViewById(R.id.c3);
                }else if(i == 5 && j ==3) {
                    a[i][j] = findViewById(R.id.d3);
                }else if(i == 5 && j ==4) {
                    a[i][j] = findViewById(R.id.e3);
                }else if(i == 5 && j ==5) {
                    a[i][j] = findViewById(R.id.f3);
                }else  if(i == 5 && j ==6) {
                    a[i][j] = findViewById(R.id.g3);
                }else if(i == 5 && j ==7) {
                    a[i][j] = findViewById(R.id.h3);
                } if(i == 4 && j ==0) {
                    a[i][j] = findViewById(R.id.a4);
                }else if(i == 4 && j ==1) {
                    a[i][j] = findViewById(R.id.b4);
                }else  if(i == 4 && j ==2) {
                    a[i][j] = findViewById(R.id.c4);
                }else if(i == 4 && j ==3) {
                    a[i][j] = findViewById(R.id.d4);
                }else if(i == 4 && j ==4) {
                    a[i][j] = findViewById(R.id.e4);
                }else if(i == 4 && j ==5) {
                    a[i][j] = findViewById(R.id.f4);
                }else  if(i == 4 && j ==6) {
                    a[i][j] = findViewById(R.id.g4);
                }else if(i == 4 && j ==7) {
                    a[i][j] = findViewById(R.id.h4);
                }else  if(i == 3 && j ==0) {
                    a[i][j] = findViewById(R.id.a5);
                }else if(i == 3 && j ==1) {
                    a[i][j] = findViewById(R.id.b5);
                }else  if(i == 3 && j ==2) {
                    a[i][j] = findViewById(R.id.c5);
                }else if(i == 3 && j ==3) {
                    a[i][j] = findViewById(R.id.d5);
                }else if(i == 3 && j ==4) {
                    a[i][j] = findViewById(R.id.e5);
                }else if(i == 3 && j ==5) {
                    a[i][j] = findViewById(R.id.f5);
                }else  if(i == 3 && j ==6) {
                    a[i][j] = findViewById(R.id.g5);
                }else if(i == 3 && j ==7) {
                    a[i][j] = findViewById(R.id.h5);
                }else  if(i == 2 && j ==0) {
                    a[i][j] = findViewById(R.id.a6);
                }else if(i == 2 && j ==1) {
                    a[i][j] = findViewById(R.id.b6);
                }else  if(i == 2 && j ==2) {
                    a[i][j] = findViewById(R.id.c6);
                }else if(i == 2 && j ==3) {
                    a[i][j] = findViewById(R.id.d6);
                }else if(i == 2 && j ==4) {
                    a[i][j] = findViewById(R.id.e6);
                }else if(i == 2 && j ==5) {
                    a[i][j] = findViewById(R.id.f6);
                }else  if(i == 2 && j ==6) {
                    a[i][j] = findViewById(R.id.g6);
                }else if(i == 2 && j ==7) {
                    a[i][j] = findViewById(R.id.h6);
                }else  if(i == 1 && j ==0) {
                    a[i][j] = findViewById(R.id.a7);
                }else if(i == 1 && j ==1) {
                    a[i][j] = findViewById(R.id.b7);
                }else  if(i == 1 && j ==2) {
                    a[i][j] = findViewById(R.id.c7);
                }else if(i == 1 && j ==3) {
                    a[i][j] = findViewById(R.id.d7);
                }else if(i == 1 && j ==4) {
                    a[i][j] = findViewById(R.id.e7);
                }else if(i == 1 && j ==5) {
                    a[i][j] = findViewById(R.id.f7);
                }else  if(i == 1 && j == 6) {
                    a[i][j] = findViewById(R.id.g7);
                }else if(i == 1 && j ==7) {
                    a[i][j] = findViewById(R.id.h7);
                }else  if(i == 0 && j ==0) {
                    a[i][j] = findViewById(R.id.a8);
                }else if(i == 0 && j == 1) {
                    a[i][j] = findViewById(R.id.b8);
                }else  if(i == 0 && j == 2) {
                    a[i][j] = findViewById(R.id.c8);
                }else if(i == 0 && j ==3) {
                    a[i][j] = findViewById(R.id.d8);
                }else if(i == 0 && j ==4) {
                    a[i][j] = findViewById(R.id.e8);
                }else if(i == 0 && j ==5) {
                    a[i][j] = findViewById(R.id.f8);
                }else  if(i == 0 && j ==6) {
                    a[i][j] = findViewById(R.id.g8);
                }else if(i == 0 && j ==7) {
                    a[i][j] = findViewById(R.id.h8);
                }


            }
        }
    }

    private void emptyBoard() {
        for(int i=0; i< 8; i++){
            for(int j=0; j< 8; j++) {
                if( (i%2==0 && j%2==0) ||  (i%2==1 && j%2==1) ){
                    chessBoardInterface[i][j].setImageResource(R.drawable.darkbrown);
                }else{
                    chessBoardInterface[i][j].setImageResource(R.drawable.lightbrown);
                }

                chessBoardInterface[i][j].setClickable(false);
            }
            }
    }

    public void openDialog(){

        Question q = new Question();
        q.setCancelable(false);
        q.show(getSupportFragmentManager(), "Question");

    }


    @Override
    public void onDialogPositiveClick(int index) {

        boolean isWhite = grid[globalCords[1][0]][globalCords[1][1]].isWhite();

       if(index == 0){

           grid[globalCords[1][0]][globalCords[1][1]]
                   = new Knight(globalCords[1], isWhite);
           if (isWhite)
               chessBoardInterface[globalCords[1][0]][globalCords[1][1]].setImageResource(R.drawable.wk);
           else
               chessBoardInterface[globalCords[1][0]][globalCords[1][1]].setImageResource(R.drawable.bk);

       }else if(index == 1){

           grid[globalCords[1][0]][globalCords[1][1]]
                   = new Bishop(globalCords[1], isWhite);
           if (isWhite)
               chessBoardInterface[globalCords[1][0]][globalCords[1][1]].setImageResource(R.drawable.wb);
           else
               chessBoardInterface[globalCords[1][0]][globalCords[1][1]].setImageResource(R.drawable.bb);

       }else if(index == 2){

           grid[globalCords[1][0]][globalCords[1][1]]
                   = new Rook(globalCords[1], isWhite);
           if (isWhite)
               chessBoardInterface[globalCords[1][0]][globalCords[1][1]].setImageResource(R.drawable.wr);
           else
               chessBoardInterface[globalCords[1][0]][globalCords[1][1]].setImageResource(R.drawable.br);

       }else if(index == 3 || index == -1){

           grid[globalCords[1][0]][globalCords[1][1]]
                   = new Queen(globalCords[1], isWhite);
           if (isWhite)
               chessBoardInterface[globalCords[1][0]][globalCords[1][1]].setImageResource(R.drawable.wq);
           else
               chessBoardInterface[globalCords[1][0]][globalCords[1][1]].setImageResource(R.drawable.bq);

       }
    }
}