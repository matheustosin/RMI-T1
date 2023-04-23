package Server;

import FileServer.FileImpl;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Server <machine> <port> <type>");
            System.exit(1);
        }

        // type 0 == server normal
        // type 1 == server file
        if ("0".equals(args[2])) {
            startNormalServer(args[0], Integer.parseInt(args[1]));
        } else {
            startFileServer(args[0], Integer.parseInt(args[1]));
        }

    }

    private static void startNormalServer(String host, int port) {
        try {
            LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            System.out.println("RMI registry already running.");
        }

        try {
            String server = "rmi://" + host + ":" + port + "/request";
            Naming.rebind(server, new RequestImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startFileServer(String host, int port) {
        //TODO Criar arquivo inicial com alguns exemplos
        try {
            LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            System.out.println("RMI registry already running.");
        }

        try {
            String server = "rmi://" + host + ":" + port + "/file";
            Naming.rebind(server, new FileImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
