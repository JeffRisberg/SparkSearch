package com.incra.init

import com.escalatesoft.subcut.inject.NewBindingModule
import com.incra.services.{SiteService, FacilityService, OriginService, ActivityService}

import scala.slick.driver.MySQLDriver.simple._

/**
 * Dependency injection configuration for services.
 */
object ServiceConfiguration extends NewBindingModule(mutableBindingModule => {
  import mutableBindingModule._

  val url = "jdbc:mysql://localhost:3306/sparksearch"
  val driver = "com.mysql.jdbc.Driver"
  val user = "developer"
  val password = "123456"

  bind[Database] toProvider {
    implicit bindingModule => Database.forURL(url, user = user, password = password, driver = driver)
  }

  bind[SiteService] toProvider {
    implicit bindingModule => new SiteService
  }

  bind[ActivityService] toProvider {
    implicit bindingModule => new ActivityService
  }

  bind[OriginService] toProvider {
    implicit bindingModule => new OriginService
  }

  bind[FacilityService] toProvider {
    implicit bindingModule => new FacilityService
  }
})
