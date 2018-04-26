package com.yangbingdong.springbootgatling.gatling

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration.FiniteDuration

class DockerTest extends Simulation {

  private val builder = http("DockerTest").get("http://192.168.6.113:8080/aop/1/bbb?name=ybd&age=24").check(status.is(200))

  val scn: ScenarioBuilder = scenario("DockerTest").repeat(100) {exec(builder)}

  val scn2: ScenarioBuilder = scenario("DockerTest").exec(builder)

  setUp(scn.inject(atOnceUsers(100))).maxDuration(FiniteDuration.apply(10, TimeUnit.MINUTES))
  //    setUp(scn2.inject(constantUsersPerSec(50) during 10)).maxDuration(FiniteDuration.apply(10, TimeUnit.MINUTES))

}