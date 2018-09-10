package actors

import akka.actor._
import scala.concurrent.duration._
import entities._
import play.api.libs.json._
import yahoofinance.YahooFinance

object SubscribeActor {
	def props(out: ActorRef) = Props(new SubscribeActor(out))
}

class SubscribeActor(out: ActorRef) extends Actor {
	def receive = {
		case symbol: String =>
			val system = akka.actor.ActorSystem("system")
			import system.dispatcher
			system.scheduler.schedule(0 seconds, 15 seconds, out, getStockInfo(symbol))
		}
		def getStockInfo(symbol: String) = {
			// Somehow the following two lines only get called once
			var stock = YahooFinance.get(symbol)
			println(stock)

			// But the line below is called on schedule
			Json.toJson(StockPrice(stock.getSymbol(), stock.getName(), stock.getQuote().getPrice())).toString()
		}
	}