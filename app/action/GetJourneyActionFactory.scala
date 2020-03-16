/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package action

import com.google.inject.Inject
import config.AppConfig
import model._
import payapi.cardpaymentjourney.PayApiConnector
import payapi.corcommon.model.{ JourneyId, Origins }
import play.api.Logger
import play.api.mvc._
import requests.{ JourneyRequest, RequestSupport }
import uk.gov.hmrc.auth.core.{ AuthorisationException, AuthorisedFunctions }
import uk.gov.hmrc.http.{ HeaderCarrier, HttpException, SessionKeys }
import uk.gov.hmrc.play.HeaderCarrierConverter
import views.DefaultViews

import scala.concurrent.{ ExecutionContext, Future }

final class GetJourneyActionFactory @Inject() (
  af: AuthorisedFunctions,
  paymentApi: PayApiConnector,
  defaultViews: DefaultViews)(implicit ec: ExecutionContext, config: AppConfig) {

  import RequestSupport._

  def journeyActionRefiner(journeyId: JourneyId): ActionRefiner[Request, JourneyRequest] =
    new ActionRefiner[Request, JourneyRequest] {

      override protected def refine[A](request: Request[A]): Future[Either[Result, JourneyRequest[A]]] = {
        implicit val r: Request[A] = request

        for {
          maybeJourney <- paymentApi.findJourney(journeyId)
        } yield maybeJourney match {
          case Some(journey) => Right(new JourneyRequest(journey, request))
          case None =>
            Logger.warn(s"Trying to access page for non existing journey [$journeyId]")
            Left(Results.NotFound(defaultViews.notFound))
        }
      }

      override protected def executionContext: ExecutionContext = ec
    }

  def validSessionActionRefiner: ActionRefiner[JourneyRequest, JourneyRequest] =
    new ActionRefiner[JourneyRequest, JourneyRequest] {

      override protected def refine[A](request: JourneyRequest[A]): Future[Either[Result, JourneyRequest[A]]] = {

        implicit val r: Request[A] = request

        val checkSession = request.journey.origin match {
          case Origins.PfP800 => false
          case Origins.BcPngr => false
          case Origins.Mib => false
          case _ => true
        }

        val refinement: Either[Result, JourneyRequest[A]] = if (checkSession) {
          if (request.journey.sessionId.isDefined && request.journey.sessionId.map(_.value) == request.session.get(SessionKeys.sessionId)) {
            Right(request)
          } else {
            Logger.warn(s"Session in journey does not match session in cookie. Journey: ${request.journey._id.value}")
            Left(Results.Unauthorized(defaultViews.unauthorised()))
          }
        } else {
          Right(request)
        }

        Future.successful(refinement)
      }

      override protected def executionContext: ExecutionContext = ec
    }

  /**
   * Some origins require that user has to be logged-in in order to continue the journey.
   * This applies when user starts journey being already logged-in (in BTA for example)
   */
  def logInFilter: ActionFilter[JourneyRequest] = new ActionFilter[JourneyRequest] {

    override def filter[A](request: JourneyRequest[A]): Future[Option[Result]] = {
      val authExpected = request.journey.origin.authExpected

      implicit val r: Request[A] = request
      implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

      lazy val unauthorisedResult: Result = Results.Unauthorized(defaultViews.unauthorised)

      if (authExpected)
        af.authorised() {
          Future.successful(None)
        }
          .recover {
            case ae: AuthorisationException =>
              Logger.warn(s"This request is supposed to be authenticated but user was not logged in. Did journey originated in logged in system? [${request.journey._id}] [${request.journey.origin}] [${ae.reason}]")
              Some(unauthorisedResult)

            case e: HttpException =>
              Logger.error("An error occurred while contacting /auth. It's possible the auth microservice was down", e)
              Some(Results.InternalServerError(defaultViews.internalServerError()))
          }
      else Future.successful(None)
    }

    override protected def executionContext: ExecutionContext = ec
  }
}
