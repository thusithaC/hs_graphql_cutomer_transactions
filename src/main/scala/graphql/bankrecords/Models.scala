package graphql.bankrecords

/**
  * Created by thusitha on 7/26/18.
  */


case class Transaction(transactionId : String,
                       accountId : String,
                       transactionDay : Int,
                       category: String,
                       transactionAmount:BigDecimal)

case class Customer(customerId : String,
                    forename : String,
                    surname : String,
                    accounts: List[String])


