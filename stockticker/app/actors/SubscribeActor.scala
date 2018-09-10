package actors

import javax.inject._
import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import play.api.libs.json._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

object SubscribeActor {
  def props = Props[SubscribeActor]

  case class WatchStock(symbol: String)
}

class SubscribeActor  extends Actor {
	import SubscribeActor._
  def receive = {
	case WatchStock(symbol: String) =>
      // addStocks(symbol)
      sender() ! symbol

  }
}