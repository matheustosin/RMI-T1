package server;

import java.io.IOException;
import java.rmi.Remote;

public interface RequestInterface extends Remote {
    void insert(String line);
    void delete(String line);
    String read() throws IOException;
}
