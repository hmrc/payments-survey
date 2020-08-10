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

package journeylogger

import play.api.Logger
import play.api.mvc.Request
import requests.JourneyRequest
import traceid.TraceIdExt

object JourneyLogger {

  private val log: Logger = Logger("journey")

  def debug(message: => String)(implicit request: Request[_]): Unit = logMessage(message, Debug)

  def info(message: => String)(implicit request: Request[_]): Unit = logMessage(message, Info)

  def warn(message: => String)(implicit request: Request[_]): Unit = logMessage(message, Warn)

  def error(message: => String)(implicit request: Request[_]): Unit = logMessage(message, Error)

  def debug(message: => String, ex: Throwable)(implicit request: Request[_]): Unit = logMessage(message, ex, Debug)

  def info(message: => String, ex: Throwable)(implicit request: Request[_]): Unit = logMessage(message, ex, Info)

  def warn(message: => String, ex: Throwable)(implicit request: Request[_]): Unit = logMessage(message, ex, Warn)

  def error(message: => String, ex: Throwable)(implicit request: Request[_]): Unit = logMessage(message, ex, Error)

  private def journeyId(implicit request: JourneyRequest[_]) = s"[journeyId: ${request.journey._id}]"

  private def traceId(implicit request: JourneyRequest[_]) = s"[traceId: ${request.journey.traceId}]"

  private def origin(implicit request: JourneyRequest[_]) = s"[origin: ${request.journey.origin}]"

  private def context(implicit request: Request[_]) = s"[context: ${request.method} ${request.path}]"

  private def transactionReference(implicit request: JourneyRequest[_]) =
    request
      .journey
      .order
      .map(_.transactionReference.value)
      .map(t => s"[txnRef: ${t}]")
      .getOrElse("")

  private def traceIdsFromUrlIfDifferentThanInJourney(implicit request: JourneyRequest[_]): String = {
    TraceIdExt
      .traceIdStringsFromQueryParameter()
      .map(_.filterNot(_ == request.journey.traceId.value))
      .map(_.mkString("[traceId different:", "", "]"))
      .getOrElse("")
  }

  private def traceIdFromQueryParameter(implicit request: Request[_]): String =
    TraceIdExt
      .traceIdStringsFromQueryParameter()
      .map(_.mkString("[traceId:", "", "]"))
      .getOrElse("[NoTraceId]")

  def makeRichMessage(message: String)(implicit request: Request[_]): String = request match {
    case r: JourneyRequest[_] =>
      implicit val journeyRequest: JourneyRequest[_] = r
      s"$message $origin $traceId $journeyId $traceIdsFromUrlIfDifferentThanInJourney $transactionReference $context"
    case r =>
      s"$message $traceIdFromQueryParameter $context"

  }

  private sealed trait LogLevel

  private case object Debug extends LogLevel

  private case object Info extends LogLevel

  private case object Warn extends LogLevel

  private case object Error extends LogLevel

  private def logMessage(message: => String, level: LogLevel)(implicit request: Request[_]): Unit = {
    lazy val richMessage = makeRichMessage(message)
    level match {
      case Debug => log.debug(richMessage)
      case Info => log.info(richMessage)
      case Warn => log.warn(richMessage)
      case Error => log.error(richMessage)
    }
  }

  private def logMessage(message: => String, ex: Throwable, level: LogLevel)(implicit request: Request[_]): Unit = {
    lazy val richMessage = makeRichMessage(message)
    level match {
      case Debug => log.debug(richMessage, ex)
      case Info => log.info(richMessage, ex)
      case Warn => log.warn(richMessage, ex)
      case Error => log.error(richMessage, ex)
    }
  }

}

