package client;

import server.RequestInterface;

import java.io.IOException;
import java.rmi.Naming;

public class Client {
    public static void main(String[] args) throws IOException {
        //TODO Criar servers, definir portas e hostnames
        //TODO Sortear server para requisição do client
        String remoteHostName = args[0];
        //TODO Tirar a porta fixa do server
        String connectLocation = "rmi://" + remoteHostName + ":1099/request";
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
