package server;

import fileServer.FileInterface;
import object.ConnectionInfo;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class RequestImpl extends UnicastRemoteObject implements RequestInterface {

    protected RequestImpl() throws RemoteException {
    }

    @Override
    public void insert(String line, ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException {
        String connectLocation = "rmi://" + "localhost" + ":1100/file";
        FileInterface fileInterface = getFileInterface(connectLocation);
        fileInterface.insert(line, connectionInfo);
    }

    @Override
    public void delete(String line, ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException {
        String connectLocation = "rmi://" + "localhost" + ":1100/file";
        FileInterface fileInterface = getFileInterface(connectLocation);
        fileInterface.delete(line, connectionInfo);
    }

    @Override
    public String read(ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException, InterruptedException {
        String connectLocation = "rmi://" + "localhost" + ":1100/file";
        FileInterface fileInterface = getFileInterface(connectLocation);
        return fileInterface.read(connectionInfo);
    }

    private static FileInterface getFileInterface(String connectLocation) {
        FileInterface fileInterface = null;
        try {
            fileInterface = (FileInterface) Naming.lookup(connectLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInterface;
    }
}
