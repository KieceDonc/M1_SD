package com.perfect.bank.helpers;

import java.util.Properties;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigOverride {

  public static Config ipAndPort(Config config, String newIp, String newPort) {
    Properties properties = new Properties();
    properties.setProperty("akka.remote.artery.canonical.hostname", newIp);
    properties.setProperty("akka.remote.artery.canonical.port", newPort);
    Config overrides = ConfigFactory.parseProperties(properties);
    Config overrided = overrides.withFallback(ConfigFactory.load());

    return overrided;
  }
}
