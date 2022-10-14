package rmi;

import classes.FieldInfo;
import classes.Player;
import enums.Result;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class ServerRMI extends UnicastRemoteObject implements ServerMethods {

    private ArrayList<Player> players;

    private ArrayList<FieldInfo[][]> boards;

    private ArrayList<String> names;

    private Player playerInTurn;

    private Player playerOutTurn;

    private boolean won = false;

    public ServerRMI() throws RemoteException {
        super();

        boards = initializeBoard();
        players = new ArrayList<Player>();
        names = new ArrayList<String>();

    }

    @Override
    public String getName(){

        Random random = new Random();
        String name;
        do{
            name = "Client" + random.nextInt(100);
        }while(names.contains(name));

        names.add(name);

        return name;
    }


    @Override
    public String sayHello() throws RemoteException {
        return "Olá Cliente, vamos jogar?";
    }

    @Override
    public FieldInfo[][] connectClient(String name) {
        if(players.size()>=2){
            return null;
        }

        if(!names.contains(name)){
            return null;
        }
        ClientMethods client;
        try {
            client = (ClientMethods) Naming.lookup("rmi://localhost:3080/"+name);
        } catch (Exception e ) {
            return null;
        }

        FieldInfo[][] board = boards.get(players.size());
        Player newPlayer = new Player(board, name, client);
        players.add(newPlayer);

        new Thread(){
            @Override
            public void run(){
                if(players.size()==2){
                    startGame();
                }
            }
        }.start();

        System.out.println("Player conectado");
        return board;
    }

    @Override
    public Result receivePlay(String name, byte line, byte column) throws RemoteException {

        Result currentPlayerFeedback;
        Result enemyPlayerFeedback = null;
        if(name.equals(playerInTurn.name) && !won){

            if(playerOutTurn.board[line][column].isOpen()){
                return Result.ALREADYOPEN;
            }

            playerOutTurn.board[line][column].setOpen(true);

            if(playerOutTurn.board[line][column].ship){
                playerInTurn.point++;
                currentPlayerFeedback = Result.HIT;
            }else{
                currentPlayerFeedback = Result.NOTHIT;
            }

            if(playerInTurn.point == 9){
                currentPlayerFeedback = Result.WON;
                enemyPlayerFeedback = Result.LOST;
                won = true;
            }

            playerOutTurn.client.receivePlayAndTurn(enemyPlayerFeedback, line, column);

        }else{
            return Result.NOTYOURTURN;
        }

        switchTurn();

        return currentPlayerFeedback;
    }

    private void startGame(){
        try {
            System.out.println(players.get(0).client.sayHello());
            System.out.println(players.get(1).client.sayHello());
            playerInTurn = players.get(0);
            playerOutTurn = players.get(1);
            players.get(0).client.receiveTurn();
        } catch (RemoteException e) {
            System.out.println("Falha ao iniciar a partida");
        }
    }

    private void switchTurn (){
        Player auxiliar = playerInTurn;
        playerInTurn = playerOutTurn;
        playerOutTurn = auxiliar;
    }



    private ArrayList<FieldInfo[][]> initializeBoard (){

        FieldInfo[][] player1Fields = new FieldInfo[8][10];
        FieldInfo[][] player2Fields = new FieldInfo[8][10];

        //Initialize array
        loop1:for(int i=0; i<player1Fields.length; i++) {
            for (int j = 0; j < player1Fields[0].length; j++) {
                player1Fields[i][j] = new FieldInfo();
                player2Fields[i][j] = new FieldInfo();
            }
        }

        Random random = new Random();

        int column, line, direction;
        boolean invertDirection;
        boolean cantSet=false;
        for(int i=0; i<3; i++){


            do{
                column = random.nextInt(10);
                line = random.nextInt(8);
                direction = random.nextInt(2);
                invertDirection = !checkDirectionLength(player1Fields.length,player1Fields[0].length, line, column, direction, i+2);
                if(hasNearShip(i+2, invertDirection, direction, line, column, player1Fields)){
                    cantSet=true;
                }else{
                    cantSet=false;
                    setShip(i+2, invertDirection, direction, line, column, player1Fields);
                }
            }while(cantSet);

            do{
                column = random.nextInt(10);
                line = random.nextInt(1);
                direction = random.nextInt(2);
                invertDirection = !checkDirectionLength(player2Fields.length,player2Fields[0].length, line, column, direction, i+2);
                if(hasNearShip(i+2, invertDirection, direction, line, column, player2Fields)){
                    cantSet=true;
                }else{
                    cantSet=false;
                    setShip(i+2, invertDirection, direction, line, column, player2Fields);
                }
            }while(cantSet);

        }

        ArrayList<FieldInfo[][]> boards = new ArrayList<FieldInfo[][]>(2);
        boards.add(player1Fields);
        boards.add(player2Fields);

        return boards;
    }

    private boolean checkDirectionLength(int maxI,int maxJ, int i, int j, int direction, int length){
        switch(direction){
            case 0: {
                if(j + length - 1 < maxJ) {
                    return true;
                }
                return false;
            }
            case 1: {
                if (i + length - 1 < maxI) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean hasNearShip(int length, boolean invertDirection, int direction, int line, int column, FieldInfo[][] playerFields){
        for(int c=0; c<length+1; c++){
            int sum = c;
            if(invertDirection) sum = -c;
            switch (direction) {
                case 0: {
                    if((column+sum >= playerFields[0].length) ||  playerFields[line][column + sum].isShip()){
                        return true;
                    }
                    break;
                }
                case 1: {
                    if((line+sum >= playerFields.length) || playerFields[line+sum][column].isShip()){
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    private void setShip(int length, boolean invertDirection, int direction, int line, int column, FieldInfo[][] playerFields){
        for(int c=0; c<length; c++){
            int sum = c;
            if(invertDirection) sum = -c;
            switch (direction) {
                case 0: {
                    playerFields[line][column + sum].setShip(true);
                    break;
                }
                case 1: {
                    playerFields[line+sum][column].setShip(true);
                    break;
                }
            }
        }
    }


}
