package object;

import java.io.Serializable;

public class ConnectionInfo implements Serializable {

    private static final long serialVersionUID = 7842289981627372113L;

    private String serverHost;
    private String serverPort;
    private String clientName;

    public ConnectionInfo(String serverHost, String serverPort, String clientName) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.clientName = clientName;
    }

    public String getServerHost() {
        return serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getClientName() {
        return clientName;
    }
}
