package client;

import server.RequestInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class Client {
    private static final List<String> ports =  asList("1090", "1091", "1092", "1093", "1094", "1095", "1096", "1097", "1098", "1099");


    public static void main(String[] args) throws IOException {
        //TODO Criar servers, definir portas e hostnames
        String remoteHostName = args[0];
        Random random = new Random();
        int randomPort = random.nextInt(10);
        String connectLocation = "rmi://" + remoteHostName + ":"+randomPort+"/request";
        RequestInterface requestInterface = getRequestInterface(connectLocation);

        //TODO Pegar ação do cliente e chamar método correto
        //requestInterface.insert("teste");
        requestInterface.delete("teste");
        //requestInterface.read();
        //TODO Gerenciar retorno para o cliente
    }

    private static RequestInterface getRequestInterface(String connectLocation) {
        RequestInterface requestInterface = null;
        try {
            requestInterface = (RequestInterface) Naming.lookup(connectLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestInterface;
    }
}
