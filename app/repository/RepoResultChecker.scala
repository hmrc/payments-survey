/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package repository

//import reactivemongo.api.commands.WriteResult
//
//import scala.concurrent.{ExecutionContext, Future}
//
//object RepoResultChecker {
//  implicit class WriteResultChecker(future: Future[WriteResult]) {
//    def checkResult[T](ifOkay: => T)(implicit ec: ExecutionContext): Future[T] = future.map {
//      case writeResult: WriteResult if isNotReallyOk(writeResult) => throw new RuntimeException(writeResult.toString)
//      case _ => ifOkay
//    }
//
//    def checkResult(implicit ec: ExecutionContext): Future[Unit] = checkResult(())
//  }
//
//  private def isNotReallyOk(writeResult: WriteResult) = !writeResult.ok || writeResult.writeErrors.nonEmpty || writeResult.writeConcernError.isDefined
//
//}
