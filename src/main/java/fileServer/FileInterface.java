package fileServer;

import object.ConnectionInfo;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface FileInterface extends Remote {
    void insert(String line, ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException;
    void delete(String line, ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException;
    String read(ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException, InterruptedException;
}
