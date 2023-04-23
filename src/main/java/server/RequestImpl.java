package server;

import fileServer.FileInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RequestImpl extends UnicastRemoteObject implements RequestInterface {

    protected RequestImpl() throws RemoteException {
    }

    @Override
    public void insert(String line) {
        String connectLocation = "rmi://" + "localhost" + ":1100/file";
        FileInterface fileInterface = getFileInterface(connectLocation);
        fileInterface.insert(line);
    }

    @Override
    public void delete(int lineNumber) {
        String connectLocation = "rmi://" + "localhost" + ":1100/file";
        FileInterface fileInterface = getFileInterface(connectLocation);
        fileInterface.delete(lineNumber);
    }

    @Override
    public String read() {
        String connectLocation = "rmi://" + "localhost" + ":1100/file";
        FileInterface fileInterface = getFileInterface(connectLocation);
        return fileInterface.read();
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
