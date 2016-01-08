package odk.config;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:41
 */
public class ProxyConfig {


    private String name;
    private int localPort;
    private int remotePort;
    private String remoteHost;

    public ProxyConfig(String name, int localPort, String remoteHost, int remotePort) {
        this.name = name;
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
    }

    public String getName() {
        return name;
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
