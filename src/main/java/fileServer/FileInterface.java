package FileServer;

import java.rmi.Remote;

public interface FileInterface extends Remote {
    void insert(String line);
    void delete(int lineNumber);
    String read();
}
