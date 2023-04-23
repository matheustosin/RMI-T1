package server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RequestInterface extends Remote {
    void insert(String line) throws RemoteException;
    void delete(String line) throws RemoteException;
    String read() throws IOException;
}
