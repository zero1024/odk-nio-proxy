package odk.config;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:41
 */
public class ProxyConfig {


    private int localPort;
    private int remotePort;
    private String remoteHost;

    public ProxyConfig(int localPort, int remotePort, String remoteHost) {
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
    }

    public int getLocalPort() {
        return localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }
}
