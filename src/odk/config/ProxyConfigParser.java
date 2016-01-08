package odk.config;

import java.util.*;

import static java.lang.String.format;

/**
 * User: operehod
 * Date: 04.01.2016
 * Time: 12:07
 */
public class ProxyConfigParser {


    private static final String LOCAL_PORT = ".localPort";
    private static final String REMOTE_HOST = ".remoteHost";
    private static final String REMOTE_PORT = ".remotePort";

    public static List<ProxyConfig> parse(Properties properties) {

        ProxyConfigsBuilderByProperties builder = new ProxyConfigsBuilderByProperties();
        for (String key : properties.stringPropertyNames()) {
            builder.addProperty(key, properties.getProperty(key));
        }

        return builder.build();
    }


    private static class ProxyConfigBuilder {
        private String name;
        private int localPort = -1;
        private int remotePort = -1;
        private String remoteHost;

        public ProxyConfigBuilder(String name) {
            this.name = name;
        }

        public void addLocalPort(int localPort) {
            this.localPort = localPort;
        }

        public void addRemoteHost(String remoteHost) {
            this.remoteHost = remoteHost;
        }

        public void addRemotePort(int remotePort) {
            this.remotePort = remotePort;
        }


        public ProxyConfig build() {
            validate();
            return new ProxyConfig(name, localPort, remoteHost, remotePort);
        }

        private void validate() {
            if (localPort == -1)
                throw new IllegalArgumentException(format("Local port for proxy server [%s] is undefined", name));
            if (remoteHost == null)
                throw new IllegalArgumentException(format("Remote host for proxy server [%s] is undefined", name));
            if (remotePort == -1)
                throw new IllegalArgumentException(format("Remote port for proxy server [%s] is undefined", name));
        }

    }

    private static class ProxyConfigsBuilderByProperties {

        private Map<String, ProxyConfigBuilder> map = new HashMap<>();


        public List<ProxyConfig> build() {
            List<ProxyConfig> res = new ArrayList<>();
            for (ProxyConfigBuilder builder : map.values()) {
                res.add(builder.build());
            }
            return res;
        }

        public void addProperty(String propertyName, String propertyValue) {
            //1. localPort
            int idx = propertyName.lastIndexOf(LOCAL_PORT);
            if (idx > 0) {
                ProxyConfigBuilder builder = getBuilder(propertyName, idx);
                builder.addLocalPort(parseInt(propertyName, propertyValue));
                return;
            }

            //2. remoteHost
            idx = propertyName.lastIndexOf(REMOTE_HOST);
            if (idx > 0) {
                ProxyConfigBuilder builder = getBuilder(propertyName, idx);
                builder.addRemoteHost(propertyValue);
                return;
            }

            //3. remotePort
            idx = propertyName.lastIndexOf(REMOTE_PORT);
            if (idx > 0) {
                ProxyConfigBuilder builder = getBuilder(propertyName, idx);
                builder.addRemotePort(parseInt(propertyName, propertyValue));
                return;
            }

            throw new IllegalArgumentException(format("Property name must end with [%s] or [%s] or [%s]", LOCAL_PORT, REMOTE_HOST, REMOTE_PORT));


        }

        private ProxyConfigBuilder getBuilder(String propertyName, int idx) {
            String proxyName = propertyName.substring(0, idx);
            if (!map.containsKey(proxyName))
                map.put(proxyName, new ProxyConfigBuilder(proxyName));
            return map.get(proxyName);
        }
    }

    private static int parseInt(String propertyName, String propertyValue) {
        try {
            return Integer.parseInt(propertyValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(format("Property [%s] must be integer!", propertyName));
        }

    }


}
