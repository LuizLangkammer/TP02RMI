
import rmi.ClientRMI;
import rmi.ServerRMI;
import rmi.ServerMethods;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {


    public static void main(String args[]) throws InterruptedException {


        String[] options2 = {"Servidor", "Cliente"};
        int selectedType = JOptionPane.showOptionDialog(null, "Qual o serviço que deseja subir",
                "Tipo de serviço", 0, 1, null, options2, 0);


        if(selectedType==0){
            try{
                LocateRegistry.createRegistry(3080);

                ServerMethods server = new ServerRMI();

                Naming.rebind("rmi://localhost:3080/Server", server);

            } catch (RemoteException e) {
                System.out.println("Falha na criação do servidor");
                e.printStackTrace();
                System.exit(0);
            }
            catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                new ClientRMI();
            } catch (RemoteException e) {
                System.out.println("Falha na criação do cliente");
                e.printStackTrace();
                System.exit(0);
            }
        }


    }

}
