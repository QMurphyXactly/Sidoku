package com.SelTemplate;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException{

        System.setProperty("webdriver.chrome.driver", "/Users/qmurphy/Documents/Chrome Driver/chromedriver");
        System.out.println("Chrome Driver Connected");
        WebDriver driver = new ChromeDriver();
        Actions action = new Actions(driver);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        BoardSpace board [][] = new BoardSpace [9][9];
        BoardSpace square [][] = new BoardSpace[3][3];

        driver.get("https://nine.websudoku.com/?");

        WebElement piece;
        String value;

        for (int i = 0; i < 9; i++){  //internal  board creation
            for (int j = 0; j < 9; j++){
                String id = "f" + j + i;
                BoardSpace temp;

                piece = driver.findElement(By.id(id));
                value = piece.getAttribute("value");

                if(value.equals("")) {
                    temp = new BoardSpace(piece, 0, true, i, j);
                    board[i][j] = temp;
                } else {
                    temp = new BoardSpace(piece, Integer.parseInt(value), false, i, j);
                    board[i][j] = temp;
                }
                System.out.print(temp.getValue() + " ");
            }
            System.out.print("\n");
        }

        boolean loops = true;
        int ind = 0;
        //    while (loops) {
        for (int i = 0; i < 9; i++){  //printing the board (for checking/delete later)
            for (int j = 0; j < 9; j++){
                System.out.print(board[i][j].getValue() + " ");
            }
            System.out.print("\n");
        }
        for (int i = 0; i < 9; i++) {  // getting possibles of squares
            for (int j = 0; j < 9; j++) {
                int row = i;
                int column = j;
                if (board[row][column].getType()) {
                    for (int k = 1; k <= 9; k++) {
                        if (!numberInColumn(k, board[row][column], board)) {
                            if (!numberInRow(k, board[row][column], board)) {
                                if (!numberInSquare(k, board[row][column], board)) {
                                    (board[row][column].possibles)[k - 1] = true;
                                }
                            }
                        }
                    }
                }
            }
        }
/*
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int row = i;
                    int column = j;
                    if (board[row][column].getType()) {
                        int index = 0;
                        for (int k = 0; k < 9; k++) {
                            if ((board[row][column].possibles)[k]) {
                                index++;
                            }
                        }
                        if (index == 1) {
                            for (int k = 0; k < 9; k++) {
                                if ((board[row][column].possibles)[k]) {
                                    board[row][column].getElement().sendKeys("" + (k + 1) + Keys.RETURN);
                                    board[row][column].value = (k + 1);
                                    board[row][column].type = false;
                                    break;
                                }
                            }
                        }
                        System.out.println("Row: " + row + "\t" + "Column: " + column);
                        board[row][column].getPossiblesValues();
                    }
                }
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if(board[i][j].getType()){
                        break;
                    }
                    else {
                        if(i == 8 && j == 8) {
                            loops = false;
                        }
                        continue;
                    }
                }
                break;
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    board[i][j].resetPossibles();
                    System.out.println("Reset -- Row: " + i + "\t" + "Column: " + j);
                    board[i][j].getPossiblesValues();
                }
            }
            if(ind++ == 2)
                break;
        }
//        board[0][0].getElement().sendKeys("1" + Keys.RETURN);
*/
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 9; j++) {
                int row = i;
                int column = j;
                for (int k = 0; k < 9; k++) {

                    if (board[i][j].getType()) {
                        System.out.println("Row: " + row + "\t" + "Column: " + column + "\tNumber: " + (k + 1) + "\tT/F: " + onlySpotColumn(k, board[row][column], board));
                    }
                }
            }
        }


        System.out.println("Done");
    }

    private static class BoardSpace {
        WebElement piece;
        int value;
        boolean type;
        boolean possibles [];
        int row;
        int column;

        private BoardSpace(WebElement iPiece, int iValue, boolean iType, int ix, int iy) {

            piece = iPiece;
            value = iValue;
            possibles = new boolean[9];
            type = iType;           //--------------------if true the piece is a blank space for input
            row = ix;
            column = iy;

            for (boolean x: possibles) {
                x = false;
            }

        }

        private WebElement getElement(){
            return piece;
        }

        private int getValue(){
            return value;
        }

        private boolean[] getPossibles(){
            return possibles;
        }

        private void resetPossibles(){
            for (int i = 0; i < 9; i++) {
                possibles[i] = false;
            }
        }

        private  boolean getType(){
            return type;
        }

        private int getRow(){
            return row;
        }

        private int getColumn(){
            return column;
        }

        private void getPossiblesValues() {
            for (boolean x : possibles ) {
                System.out.println(x);
            }
        }

        public  String toString(){
            return ("Web Element: " + piece.toString() + "\n"
                    + "Value: " + getValue() + "\n"
                    + "Type: " + getType() + "\n"
                    + "X: " + getRow() + "\n"
                    + "Y: " + getColumn());
        }
    }

    private static boolean numberInColumn(int number, BoardSpace bs, BoardSpace[][] board){      //returns true if the number is in the same row
        int column = bs.getColumn();

        for (int i = 0; i < 9; i++){

            int value = board[i][column].getValue();

            if (value == number){
                return true;
            }
        }
        return false;
    }

    private static boolean numberInRow(int number, BoardSpace bs, BoardSpace[][] board){      //returns true if the number is in the same row
        int row = bs.getRow();

        for (int i = 0; i < 9; i++){

            int value = board[row][i].getValue();

            if (value == number){
                return true;
            }
        }
        return false;
    }

    private static boolean numberInSquare(int number, BoardSpace bs, BoardSpace[][] board){
        int row = bs.getRow();
        int column = bs.getColumn();

        if(row % 3 == 0){
            if(column % 3 == 0) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row + i][column + j].getValue())
                            return true;
                    }
                }
            }
            if(column % 3 == 1) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row + i][column - 1 + j].getValue())
                            return true;
                    }
                }
            }
            if(column % 3 == 3) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row + i][column - 2 + j].getValue())
                            return true;
                    }
                }
            }
        }
        if(row % 3 == 1){
            if(column % 3 == 0) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row -1 + i][column + j].getValue())
                            return true;
                    }
                }
            }
            if(column % 3 == 1) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row - 1 + i][column - 1 + j].getValue())
                            return true;
                    }
                }
            }
            if(column % 3 == 3) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row -1 + i][column - 2 + j].getValue())
                            return true;
                    }
                }
            }
        }
        if(row % 3 == 2){
            if(column % 3 == 0) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row - 2 + i][column + j].getValue())
                            return true;
                    }
                }
            }
            if(column % 3 == 1) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row - 2+ i][column - 1 + j].getValue())
                            return true;
                    }
                }
            }
            if(column % 3 == 3) {
                for(int i = 0;i < 3; i++ ){
                    for (int j = 0; j < 3; j++){
                        if(number == board[row - 2 + i][column - 2 + j].getValue())
                            return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean onlySpotColumn(int number, BoardSpace bs, BoardSpace[][] board){
        int column = bs.getColumn();
        int index = 0;

        for(int row = 0; row < 9; row++){
            if(board[row][column].getType()){
                if(board[row][column].possibles[0]){
                    index++;
                }
            }
        }
        if(index == 1){
            return true;
        }
        return false;
    }

    private static void onlySpotSquare(int number, BoardSpace bs, BoardSpace[][] board, BoardSpace [][] square){
        int row = bs.getRow();
        int column = bs.getColumn();

        if(row % 3 == 0) {
            if (column % 3 == 0) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(board[row + i][column + j].getType()){

                        }
                    }
                }
            }
        }



    }

}


/*








 */






