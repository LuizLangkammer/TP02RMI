package rmi;

import classes.FieldInfo;
import enums.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerMethods extends Remote {

    String sayHello() throws RemoteException;

    String getName() throws RemoteException;

    FieldInfo[][] connectClient(String name) throws RemoteException;

    Result receivePlay(String name, byte line, byte column) throws  RemoteException;

}
