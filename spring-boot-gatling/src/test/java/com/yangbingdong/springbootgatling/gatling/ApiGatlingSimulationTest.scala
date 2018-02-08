package com.yangbingdong.springbootgatling.gatling

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration.{Duration, FiniteDuration}

class ApiGatlingSimulationTest extends Simulation {

  val scn: ScenarioBuilder = scenario("AddAndFindPersons").repeat(100, "n") {
    exec(
      http("AddPerson-API")
        .post("http://localhost:8080/persons")
        .header("Content-Type", "application/json")
        .body(StringBody("""{"firstName":"John${n}","lastName":"Smith${n}","birthDate":"1980-01-01", "address": {"country":"pl","city":"Warsaw","street":"Test${n}","postalCode":"02-200","houseNo":${n}}}"""))
        .check(status.is(200))
    ).pause(Duration.apply(5, TimeUnit.MILLISECONDS))
  }.repeat(1000, "n") {
    exec(
      http("GetPerson-API")
        .get("http://localhost:8080/persons/${n}")
        .check(status.is(200))
    )
  }

  val scn2: ScenarioBuilder = scenario("AddAndFindPersons").exec(
    http("GetPerson-API")
      .get("http://localhost:8080/persons/1")
      .check(status.is(200))
  )

  setUp(scn.inject(atOnceUsers(30))).maxDuration(FiniteDuration.apply(10, TimeUnit.MINUTES))

//  setUp(scn2.inject(constantUsersPerSec(1500) during 20)).maxDuration(FiniteDuration.apply(10, TimeUnit.MINUTES))

}