package fileServer;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class FileImpl extends UnicastRemoteObject implements FileInterface {
    private static final Semaphore deletionSemaphore = new Semaphore(1);
    private static final Semaphore insertionSemaphore = new Semaphore(1);
    private static final Semaphore readSemaphore = new Semaphore(1);
    private BufferedWriter writer;
    private BufferedReader reader;

    public FileImpl() throws IOException {
        writer = new BufferedWriter(new FileWriter("src\\main\\java\\file\\sharedFile.txt", true));
        reader = new BufferedReader(new FileReader("src\\main\\java\\file\\sharedFile.txt"));
    }

    @Override
    public void insert(String newLine) {
        try {
            //TODO Verificar se deve lockar para não pode deletar ao estar inserindo também
            insertionSemaphore.acquire();
            //Verificar se já não existe o valor no arquivo
            String fileLine;
            Boolean existentName = false;

            while ((fileLine = reader.readLine()) != null & (existentName == false)) {
                if(fileLine.trim().equalsIgnoreCase(newLine)) {
                    existentName = true;
                }
            }
            //logica de inserir no arquivo
            if(existentName == false) {
                writer.write(newLine+ "\r\n");
            }
            //Tempo para simular lock (100ms)
            Thread.sleep(100);
            insertionSemaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String line) {
        try {
            insertionSemaphore.acquire();
            deletionSemaphore.acquire();
            readSemaphore.acquire();
            // TODO - fazer logica de deletar no arquivo
            File tempFile = new File("tempFile.txt");
            BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempFile));
            String fileLine;

            while((fileLine = reader.readLine()) != null) {
                if(fileLine.trim().equals(line)) {
                    continue;
                }
                writer.write(fileLine + System.getProperty("line.separator"));
            }
            boolean lineDeleted = tempFile.renameTo(new File("sharedFile.txt"));
            tempWriter.close();
            //TODO Tratamento de erro com o lineDeleted (se der algum problema, vira falso)
            insertionSemaphore.release();
            deletionSemaphore.release();
            readSemaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String read() throws IOException {
        return readAllLines(reader);
    }

    private String readAllLines(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }

        return content.toString();
    }
}
