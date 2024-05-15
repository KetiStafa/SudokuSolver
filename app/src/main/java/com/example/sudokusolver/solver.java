package com.example.sudokusolver;

import java.util.ArrayList;

public class solver {
    int[][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndex;
     int selectedRow;
     int selectedColumn;
    solver(){
        selectedRow = -1;
        selectedColumn = -1;

        board = new int[9][9];
        for(int row=0;row<9;row++){
            for(int column=0;column<9;column++){
                board[row][column] = 0;
            }
        }
        emptyBoxIndex = new ArrayList<>();
    }
    public void getEmptyIndexes(){
        for(int row=0;row<9;row++){
            for(int column=0;column<9;column++){
                if(this.board[row][column] == 0){
                    this.emptyBoxIndex.add(new ArrayList<>());
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(row);
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(column);
                }
            }
        }
    }

    // so how this is going to work is that after the button solve is pressed when the first cell is
    // filled this function is going to check if the number placed is correct
    // meaning that there are no same numbers in the same row, column, or in the 3x3 square
    // it keeps doing this and if it has gone in a certain spot where in the last cell there are no combinations
    // as the correct number, here will come to play the backtracking method. It will go one cell behind
    // and for example if the number placed is 3 it will be incremented checked and if it is correct it will move on
    // with the solution if not it will again go one cell behind and also change the number by incrementing then checking and so on.
    private boolean check(int row, int column){
        if (this.board[row][column] > 0){ // in here 0 is the empty cell
            for(int i = 0;i<9; i++){
                if (this.board[i][column] == this.board[row][column] && row != i){
                    // this is checking the cells in the same row
                    return false;
                }

                if (this.board[row][i] == this.board[row][column] && column != i){
                    // this is checking the cells in the same column
                    return false;
                }
            }
            // after the row and column check we have to check inside the
            // 3x3 boxes
            int boxRow = row/3;
            int boxCol = column/3;

            for(int r=boxRow*3;r<boxRow*3+3;r++){
                for(int c=boxCol*3;c<boxCol*3+3;c++){
                    if(this.board[r][c] == this.board[row][column] && row != r && column != c){
                        return false;
                    }
                }
            }
        }
        return true; // if it return true the number place means that it could be a potential number
    }
    // I am going to use recursion to solve the Sudoku Board
    public boolean solve(sudokuBoard display){
        int row = -1;
        int column = -1;

        for (int r=0;r<9;r++){
            for (int c=0;c<9;c++){
                if (this.board[r][c] == 0){
                    row = r;
                    column = c;
                    break;
                }
            }
        }
        if (row==-1 || column == -1){
            return true;
        }
        for (int i =1;i<10;i++){
            this.board[row][column] = i;
            display.invalidate();
            // now the check function that I created is going to be put to work,
            // meaning that is going to check if the number placed is valid or not
            if(check(row,column)){
                if(solve(display)){
                    return true;
                }
            }
            this.board[row][column] = 0;
        }
        return false;
    }

    public void resetBoard(){
        for (int r=0;r<9;r++){
            for (int c=0;c<9;c++){
                board[r][c] = 0;
            }
        }
        this.emptyBoxIndex = new ArrayList<>();
    }

    public void setNumberPos(int num){
        if (this.selectedRow != -1 && this.selectedColumn != -1){
            if(this.board[this.selectedRow-1][this.selectedColumn-1] == num){
                this.board[this.selectedRow-1][this.selectedColumn-1] = 0;
            }
            else {
                this.board[this.selectedRow-1][this.selectedColumn-1] = num;
            }
        }

    }
    public  int[][] getBoard(){
        return this.board;
    }
    public ArrayList<ArrayList<Object>> getEmptyBoxIndex(){
        return this.emptyBoxIndex;
    }
    public int getSelectedRow(){
        return selectedRow;
    }
    public int getSelectedColumn(){
        return selectedColumn;
    }
    public void setSelectedRow(int r){
        selectedRow = r;
    }
    public void setSelectedColumn(int c){
        selectedColumn = c;
    }
}
