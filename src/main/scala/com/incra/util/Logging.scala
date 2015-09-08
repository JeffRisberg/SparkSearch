package com.incra.util

import org.slf4j.LoggerFactory

/**
 * Logging Utility code
 */
trait Logging {
  lazy val log = LoggerFactory.getLogger(getClass)
  lazy val sqlLog = LoggerFactory.getLogger("sql." + getClass.getName)

  def logSQL(sql: String) {
    sqlLog.info("-----------------------------------------------------------------------------------------\n {}", sql)
  }
}


