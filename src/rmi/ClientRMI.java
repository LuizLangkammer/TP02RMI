package rmi;

import classes.FieldInfo;
import enums.Result;
import windows.Window;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI extends UnicastRemoteObject implements ClientMethods{

    ServerMethods server;

    Window window;

    boolean myTurn = false;

    String name;
    public ClientRMI() throws RemoteException {
        super();

        try{

            server = (ServerMethods) Naming.lookup("rmi://localhost:3080/Server");
            System.out.println("O servidor está conectado e diz: "+server.sayHello());
            name = server.getName();

            Naming.rebind("rmi://localhost:3080/" + name, (ClientMethods)this);
            System.out.println("Cliente compartilhado");

            FieldInfo[][] board = server.connectClient(name);
            if(board!=null){
                System.out.println("Tabuleiro em mãos");
                window = new Window(board, this);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void play(byte i, byte j){
        System.out.println(myTurn);
        if(myTurn){
            try {
                System.out.println("enviando jogada");
                Result result = server.receivePlay(name, i, j);

                if(result == Result.HIT || result == Result.NOTHIT){
                    window.setField(i,j,result == Result.HIT);
                    window.setMessage("Aguardando turno");
                }else{
                    if(result == Result.ALREADYOPEN){
                        return;
                    }
                    if(result == Result.WON){
                        window.setField(i,j,true);
                        window.setMessage("Você Venceu!!!!!!!");
                    }
                }

                myTurn = false;

            } catch (RemoteException e) {
                System.out.println("Falha ao enviar jogada");
            }
        }
    }

    @Override
    public void receiveTurn(){
        window.setMessage("Sua vez!");
        myTurn = true;
    }

    @Override
    public void receivePlayAndTurn(Result result, byte line, byte column){
        if(result == Result.LOST){
            window.setField(line, column);
            window.setMessage("Perdemos");
            myTurn = false;
        }else{
            window.setField(line, column);
            window.setMessage("Sua vez!");
            myTurn = true;
        }
    }


    @Override
    public String sayHello() throws RemoteException {
        return name+" está pronto para jogar!";
    }
}
