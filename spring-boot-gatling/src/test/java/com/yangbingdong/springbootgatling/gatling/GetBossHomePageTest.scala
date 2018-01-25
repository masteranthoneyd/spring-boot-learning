package com.yangbingdong.springbootgatling.gatling

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration.FiniteDuration

class GetBossHomePageTest extends Simulation {

  private val builder = http("GetBossHomePageAPI").get("https://mobile.ibalife.com/app/base/homepage").check(status.is(200))

  val scn: ScenarioBuilder = scenario("GetBossHomePage").repeat(10) {exec(builder)}

  val scn2: ScenarioBuilder = scenario("GetBossHomePage").exec(builder)

  setUp(scn.inject(atOnceUsers(50))).maxDuration(FiniteDuration.apply(10, TimeUnit.MINUTES))
  //    setUp(scn2.inject(constantUsersPerSec(50) during 10)).maxDuration(FiniteDuration.apply(10, TimeUnit.MINUTES))

}