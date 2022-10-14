package rmi;

import classes.FieldInfo;
import enums.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientMethods extends Remote {

    String sayHello() throws RemoteException;

    void receiveTurn() throws RemoteException;

    void receivePlayAndTurn(Result result, byte line, byte column) throws RemoteException;

}
