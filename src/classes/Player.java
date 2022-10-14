package classes;

import rmi.ClientMethods;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Player {


    public FieldInfo[][] board;

    public ClientMethods client;

    public String name;

    public int point;

    public Player(FieldInfo[][] board, String name, ClientMethods client){
        this.client = client;
        this.name = name;
        this.board = board;
    }

}
