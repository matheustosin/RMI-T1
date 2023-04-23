package server;

import fileServer.FileImpl;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final List<String> ports = new ArrayList<>();
    private static final int initialPortNormalServer = 1090;
    private static final int fileServerPort = 1100;
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Server <machine> <type>");
            System.exit(1);
        }

        // type 0 == server normal
        // type 1 == server file
        if ("0".equals(args[1])) {
            for (int i = 0; i < 10; i++) {
                startNormalServer(args[0], initialPortNormalServer+i);
            }
            System.out.println("Servidores rodando nas portas: \n" + ports);
        } else if("1".equals(args[1])){
            startFileServer(args[0], fileServerPort);
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
            ports.add(String.valueOf(port));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startFileServer(String host, int port) {
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
