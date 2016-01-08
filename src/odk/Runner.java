package odk;

import odk.config.ProxyConfig;
import odk.config.ProxyConfigParser;
import odk.task.AcceptTask;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static java.util.stream.Collectors.toList;

/**
 * User: operehod
 * Date: 28.12.2015
 * Time: 19:53
 */
public class Runner {

    public static void main(String[] args) {

        Properties properties;

        try (InputStream propertiesStream = getPropertiesStream(args)) {
            properties = new Properties();
            properties.load(propertiesStream);
        } catch (IOException e) {
            return;
        }


        int processors = Runtime.getRuntime().availableProcessors();

        List<ProxyConfig> configList = ProxyConfigParser.parse(properties);

        WorkBoard.init(configList.stream().map(AcceptTask::new).collect(toList()));

        for (int i = 0; i < processors; i++) {
            new Thread(new Worker()).start();
        }

    }


    public static InputStream getPropertiesStream(String[] args) throws IOException {
        if (args.length > 0)
            return new FileInputStream(args[0]);
        return Runner.class.getResourceAsStream("/odk/config/proxy.properties");
    }


}
