package server;

import object.ConnectionInfo;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface RequestInterface extends Remote {
    void insert(String line, String serverHost, String serverPort, String clientName) throws IOException, ServerNotActiveException;
    void delete(String line, String serverHost, String serverPort, String clientName) throws IOException, ServerNotActiveException;
    String read(String serverHost, String serverPort, String clientName) throws IOException, ServerNotActiveException, InterruptedException;
}
