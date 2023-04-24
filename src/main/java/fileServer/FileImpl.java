package fileServer;

import object.ConnectionInfo;

import java.io.*;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.util.concurrent.Semaphore;

public class FileImpl extends UnicastRemoteObject implements FileInterface {

    private static final long serialVersionUID = -4655735859349646646L;

    private static final Semaphore deletionSemaphore = new Semaphore(1);
    private static final Semaphore insertionSemaphore = new Semaphore(1);
    private static final Semaphore readSemaphore = new Semaphore(1);
    public static final int MILLIS = 5000;

    public FileImpl() throws IOException {
        createSharedFile();
    }

    private void createSharedFile() throws IOException {
        try {
            File file = new File("sharedFile.txt");
            if (!file.exists()) {
                newFile();
            }
        } catch (Exception ignored) {}
    }

    private void newFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("sharedFile.txt", true));
        writer.write("Get Out");
        writer.newLine();
        writer.write("BlacKkKlansman");
        writer.newLine();
        writer.write("Us");
        writer.newLine();
        writer.flush();
        writer.close();
    }

    @Override
    public void insert(String newLine, ConnectionInfo connectionInfo) throws ServerNotActiveException, IOException {
        try {
            saveLog("validando recurso para inserir", connectionInfo);
            if (insertionSemaphore.availablePermits() < 1 || deletionSemaphore.availablePermits() < 1) {
                saveLog("está aguardando para realizar a inserção", connectionInfo);
            }
            insertionSemaphore.acquire();
            deletionSemaphore.acquire();
            saveLog("recurso alocado para insercao", connectionInfo);
            String fileLine;
            boolean existentName = false;

            BufferedReader reader = new BufferedReader(new FileReader("sharedFile.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("sharedFile.txt", true));
            while ((fileLine = reader.readLine()) != null & (!existentName)) {
                if(fileLine.trim().equalsIgnoreCase(newLine)) {
                    existentName = true;
                }
            }
            if(!existentName) {
                writer.write(newLine);
                writer.newLine();
            }
            writer.flush();
            reader.close();
            writer.close();
            Thread.sleep(MILLIS);
            if (existentName) {
                saveLog("Nao pode inserir o mesmo filme '" + newLine + "' no arquivo com sucesso", connectionInfo);
            } else {
                saveLog("Inseriu a linha '" + newLine + "' no arquivo com sucesso", connectionInfo);
            }
            insertionSemaphore.release();
            deletionSemaphore.release();
        } catch (InterruptedException | IOException | ServerNotActiveException e) {
            saveLog("erro " + e.getMessage(), connectionInfo);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String line, ConnectionInfo connectionInfo) throws ServerNotActiveException, IOException {
        try {
            saveLog("validando recurso para deletar", connectionInfo);
            if (insertionSemaphore.availablePermits() < 1 || deletionSemaphore.availablePermits() < 1 || readSemaphore.availablePermits() < 1) {
                saveLog("está aguardando para realizar a deleção da linha", connectionInfo);
            }
            insertionSemaphore.acquire();
            deletionSemaphore.acquire();
            readSemaphore.acquire();
            saveLog("recurso alocado para deletar", connectionInfo);
            File tempFile = new File("tempFile.txt");
            BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempFile));
            BufferedReader reader = new BufferedReader(new FileReader("sharedFile.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("sharedFile.txt", true));

            String fileLine;

            while((fileLine = reader.readLine()) != null) {
                if(fileLine.trim().equals(line)) {
                    continue;
                }
                tempWriter.write(fileLine);
                tempWriter.newLine();
            }
            tempWriter.flush();
            reader.close();
            writer.close();
            tempWriter.close();
            File oldFile = new File("sharedFile.txt");
            boolean deleted = oldFile.delete();
            boolean lineDeleted = tempFile.renameTo(new File("sharedFile.txt"));

            Thread.sleep(MILLIS);
            saveLog("A linha '" + line + "' foi deletada do arquivo", connectionInfo);
            insertionSemaphore.release();
            deletionSemaphore.release();
            readSemaphore.release();
        } catch (InterruptedException | IOException | ServerNotActiveException e) {
            saveLog("erro: "+ e.getMessage(), connectionInfo);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String read(ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException, InterruptedException {
        return readAllLines(connectionInfo);

    }

    private String readAllLines(ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException, InterruptedException {
        BufferedReader reader = new BufferedReader(new FileReader("sharedFile.txt"));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }
        reader.close();
        saveLog("Realizou uma leitura no arquivo com sucesso", connectionInfo);

        Thread.sleep(MILLIS);
        return content.toString();
    }

    private void saveLog(String message, ConnectionInfo connectionInfo) throws IOException, ServerNotActiveException {
        BufferedWriter outStream = new BufferedWriter(new FileWriter("log.txt", true));

        var localTime = LocalTime.now();

        outStream.write(localTime + " " + connectionInfo.getClientName() +
                            " no server " + connectionInfo.getServerHost() + ":" + connectionInfo.getServerPort() +
                            " " + message + "\n");
        outStream.flush();
        outStream.close();
    }
}
