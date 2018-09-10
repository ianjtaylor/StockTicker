package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import yahoofinance.YahooFinance
import play.api.libs.streams.ActorFlow
import akka.actor._
import akka.stream.Materializer
import actors._
import scala.concurrent.Future
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (system: ActorSystem,
                             cc:ControllerComponents)extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    var stock = YahooFinance.get("INTC");
    stock.print();
    Ok(views.html.index())
  }

    implicit val timeout: Timeout = 5.seconds
  def subscribe = WebSocket.accept[String, String] { request =>
   Flow[String].map { symbol =>
    val subscribedStock = system.actorOf(SubscribeActor.props, request.id.toString())
    subscribedStock ? SubscribeActor.WatchStock(symbol.toString())
    symbol
  }
  }

  def hello(symbol: String) = Action.async {
    val subscribedStock = system.actorOf(SubscribeActor.props, symbol)
    (subscribedStock ? SubscribeActor.WatchStock(symbol)).mapTo[String].map { symbol =>
        Ok(symbol)
      }
    }
}
