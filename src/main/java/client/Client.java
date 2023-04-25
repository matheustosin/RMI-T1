package client;

import object.ConnectionInfo;
import server.RequestInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.server.ServerNotActiveException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class Client {
    private static final List<String> ports = asList("1090", "1091", "1092", "1093", "1094", "1095", "1096", "1097", "1098", "1099");
    private static final List<String> valuesInsert = asList("IT", "Star Wars", "Star Trek");
    private static final List<String> valuesDelete = asList("Get Out", "BlacKkKlansman", "Us");

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.scheduleAtFixedRate(startClient(args[0], "client"+(i+1)), 1, 1, TimeUnit.SECONDS);
//            startClient(args[0], "client"+(i+1)).run();
        }
    }

    private static RequestInterface getRequestInterface(String connectLocation) {
        RequestInterface requestInterface = null;
        try {
            requestInterface = (RequestInterface) Naming.lookup(connectLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestInterface;
    }

    private static Runnable startClient(String remoteHostName, String clientname) {
        return () -> {
            Random random = new Random();
            int randomPort = random.nextInt(10);
            String connectLocation = "rmi://" + remoteHostName + ":"+ports.get(randomPort)+"/request";
            System.out.println("Client criado "+ connectLocation);
            RequestInterface requestInterface = getRequestInterface(connectLocation);
            try {
                int randomMethod = random.nextInt(10);
                int randomvalues = random.nextInt(3);
                if (randomMethod < 7) {
                    System.out.println("read");
                    requestInterface.read(remoteHostName, ports.get(randomPort), clientname);
//                    System.out.println(content);
                } else if (randomMethod < 9) {
                    System.out.println("insert");
                    requestInterface.insert(valuesInsert.get(randomvalues), remoteHostName, ports.get(randomPort), clientname);
                } else {
                    System.out.println("delete");
                    requestInterface.delete(valuesDelete.get(randomvalues),remoteHostName, ports.get(randomPort), clientname);
                }

            } catch (IOException | ServerNotActiveException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
