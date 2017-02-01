package com.livelygig.product.keeper.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import com.livelygig.product.keeper.api.models._
import com.livelygig.product.security.keeper.SecurityHeaderFilter

/**
  * Created by shubham.k on 09-01-2017.
  */

/**
  * The keeper service
  *
  * It serves as a module to provide role based access control
  * to the resource services
  */
trait KeeperService extends Service {

  /**
    * Take the auth token and returns the authorization information
    *
    * @return
    */
  def authorize(): ServiceCall[String, AuthorizationInfo]


  /**
    * Take the UserAuth with email and password and return the Auth token
    *
    * @return
    */
  def login(): ServiceCall[UserLoginModel, UserAuthRes]

  /**
    * Take the user object with auth info and profile info
    * and returns success or failure
    *
    * @return
    */
  def createUser(): ServiceCall[User, UserAuthRes]

  def keeperTopicProducer: Topic[KeeperTopics]

  def descriptor = {
    import Service._
    named("authorization").withCalls(
      namedCall("/api/auth/authorize", authorize _),
      namedCall("/api/auth/login", login _)
    )
      .withTopics(topic("keeper-topics", keeperTopicProducer))
      .withHeaderFilter(SecurityHeaderFilter.Composed)
  }
}
