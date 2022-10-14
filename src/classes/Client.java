package classes;

import windows.Window;

public abstract class Client {

    protected FieldInfo[][] board;
    protected boolean myTurn = false;

    protected Window window;

    public abstract void waitForTurn();

    public abstract void play(byte i, byte j);

    protected FieldInfo[][] buildBoard(byte[] input){

        FieldInfo[][] board = new FieldInfo[input[1]][input[2]];

        int count = 3;
        for(int i=0; i<input[1]; i++){
            for(int j=0; j<input[2]; j++){
                board[i][j] = new FieldInfo();
                if(input[count]==1){
                    board[i][j].setShip(true);
                }
                count++;
            }
        }

        return board;
    }

}
