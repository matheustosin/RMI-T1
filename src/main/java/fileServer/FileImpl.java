package FileServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class FileImpl extends UnicastRemoteObject implements FileInterface {
    private static final Semaphore deletionSemaphore = new Semaphore(1);
    private static final Semaphore insertionSemaphore = new Semaphore(1);
    private static final Semaphore readSemaphore = new Semaphore(1);

    public FileImpl() throws IOException {
        BufferedWriter file = new BufferedWriter(new FileWriter("src\\main\\java\\file\\sharedFile.txt", true));
    }

    @Override
    public void insert(String line) {
        try {
            insertionSemaphore.acquire();
            // TODO - fazer logica de inserir no arquivo
            // TODO - Adicionar tempo que o sor pediu para simular lock (100ms)
            // TODO - Inser¸c˜oes ocorrem adicionando dados ao final do arquivo, dessa forma estas devem ser mutualmente exclusivas, podendo uma inser¸c˜ao ocorrer em paralelo com um n´umero qualquer de leituras.
            insertionSemaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int lineNumber) {
        try {
            insertionSemaphore.acquire();
            deletionSemaphore.acquire();
            readSemaphore.acquire();
            // TODO - fazer logica de deletar no arquivo
            insertionSemaphore.release();
            deletionSemaphore.release();
            readSemaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String read() {
        // TODO - ler arquivo
        return "";
    }
}
