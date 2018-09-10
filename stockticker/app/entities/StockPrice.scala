package entities

case class StockPrice(symbol: String,
                  name: String,
                  price: BigDecimal)

object StockPrice {
  import play.api.libs.json._ 

  implicit val stockWrites = new Writes[StockPrice] {
    def writes(stock: StockPrice) = Json.obj(
      "symbol" -> stock.symbol,
      "name" -> stock.name,
      "price" -> stock.price
    )
  }
}