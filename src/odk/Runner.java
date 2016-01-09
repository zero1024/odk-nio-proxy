package odk;

import odk.config.ProxyConfig;
import odk.config.ProxyConfigParser;
import odk.task.AcceptTask;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * User: operehod
 * Date: 28.12.2015
 * Time: 19:53
 */
public class Runner {

    private static final Logger logger = Logger.getLogger(Runner.class.getName());
    private static final String DEFAULT_PROPERTIES_PATH = "/odk/config/proxy.properties";


    public static void main(String[] args) {

        Properties properties;

        try (InputStream propertiesStream = getPropertiesAsStream(args)) {
            properties = new Properties();
            properties.load(propertiesStream);
        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Proxy server isn't starting.", e);
            }
            return;
        }


        List<ProxyConfig> configList = ProxyConfigParser.parse(properties);
        if (configList.isEmpty()) {
            throw new IllegalArgumentException("Configuration file for proxy server is empty!");
        }

        List<AcceptTask> tasks = new ArrayList<>();
        for (ProxyConfig config : configList) {

            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, format("Proxy from [localhost:%s] to [%s:%s] will be started.", config.getLocalPort(), config.getRemoteHost(), config.getRemotePort()));
            }

            tasks.add(new AcceptTask(config));
        }
        WorkBoard.init(tasks);

        int processors = Runtime.getRuntime().availableProcessors();

        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, format("Detected %s available processors. %s thread workers will be started.", processors, processors));
        }

        for (int i = 0; i < processors; i++) {
            new Thread(new Worker()).start();
        }

    }


    public static InputStream getPropertiesAsStream(String[] args) throws IOException {
        if (args.length > 0) {

            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, format("Reading properties from file system by path - [%s]", args[0]));
            }

            return new FileInputStream(args[0]);
        }

        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, format("Reading properties from classpath - [%s]", DEFAULT_PROPERTIES_PATH));
        }

        return Runner.class.getResourceAsStream(DEFAULT_PROPERTIES_PATH);
    }


}
