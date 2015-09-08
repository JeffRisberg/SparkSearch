package com.incra.init

import org.fusesource.scalate.util.Logging

/**
 * Config Support.
 */
object ConfigSupport extends Logging {
  val overrideConfigFile = System.getProperty("main.config.location", "/etc/override.conf")

  val environment = System.getProperty("org.scalatra.environment", "development")

  val configEnvironment = System.getProperty("environment", environment)

  val loadConfig = {
    log.info("Scalatra environment = " + environment)
    log.info("Config environment = " + configEnvironment)
    //ConfigLoader.loadConfig(overrideConfigFile, configEnvironment, Map.empty)
  }
}
