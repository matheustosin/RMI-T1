package Server;

import java.rmi.Remote;

public interface RequestInterface extends Remote {
    void insert(String line);
    void delete(int lineNumber);
    String read();
}
