package graphql.bankrecords

import com.typesafe.config.ConfigFactory
import org.json4s.jackson.Serialization
import com.typesafe.config.Config

import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{ read }
import org.json4s.NoTypeHints

class RestDAO {

  private val conf: Config = ConfigFactory.load

  private val transactionsService = conf.getString("rest.transactions.endpoint")
  private val customersService = conf.getString("rest.customers.endpoint")
  private val transactionsForAccountPath = conf.getString("rest.transactions.transactionsForAccount")
  private val accountsForCustomerPath = conf.getString("rest.customers.accountsForCustomer")
  private val allCustomersPath = conf.getString("rest.customers.allCustomers")
  private val allTransactionsPath = conf.getString("rest.transactions.allTransactions")
  private val customerByIdPath = conf.getString("rest.customers.customerById")
  private val transactionByIdPath = conf.getString("rest.transactions.transactionsById")

  private val timeout = conf.getLong("reset.timeout.millis")

  implicit val formats = Serialization.formats(NoTypeHints)

  def getAccountTransactions(accountId : String ) : List[Transaction] = {
    val urlPath = transactionsService + transactionsForAccountPath
    println(urlPath, accountId)
    val queryProperties = Map("accountId" -> accountId)

    val result = resultBuilder[List[Transaction]](urlPath, queryProperties, read[List[Transaction]])
    queryHelperLists[Transaction](result)
  }

  def getCustomerAccounts(customerId : String) : List[String] = {
    val urlPath = customersService + accountsForCustomerPath
    println(urlPath, customerId)
    val queryProperties = Map("customerId" -> customerId)

    val result = resultBuilder[List[String]](urlPath, queryProperties, read[List[String]])
    queryHelperLists[String](result)
  }

  def getTransactionsForCustomer(customerId : String) : List[Transaction] = {
    val accounts = getCustomerAccounts(customerId)
    val transactions = for (account <- accounts) yield {
      getAccountTransactions(account.trim)
    }
    transactions.flatten[Transaction]
  }

  def getAllCustomers() : List[Customer] = {
    val urlPath = customersService + allCustomersPath
    println(urlPath)
    val queryProperties = Map.empty[String, String]

    val result = resultBuilder[List[Customer]](urlPath, queryProperties, read[List[Customer]])
    queryHelperLists[Customer](result)
  }

  def getAllTransactions() : List[Transaction] = {
    val urlPath = transactionsService + allTransactionsPath
    println(urlPath)
    val queryProperties = Map.empty[String, String]

    val result = resultBuilder[List[Transaction]](urlPath, queryProperties, read[List[Transaction]])
    queryHelperLists[Transaction](result)
  }

  def getCustomerById(customerId : String) : Option[Customer] = {
    val urlPath = customersService + customerByIdPath
    println(urlPath, customerId)
    val queryProperties = Map("customerId" -> customerId)

    resultBuilder[Customer](urlPath, queryProperties, read[Customer])
  }

  def getTransactionById(transactionId : String) : Option[Transaction] = {
    val urlPath = customersService + transactionByIdPath
    println(urlPath, transactionId)
    val queryProperties = Map("transactionId" -> transactionId)

    resultBuilder[Transaction](urlPath, queryProperties, read[Transaction])
  }

  private def resultBuilder[T](urlPath: String, queryProperties: Map[String, String], mapper: (String) => T) : Option[T] = {
    val req = new HTTPGetRequest(urlPath, queryProperties)
    val stringResponseOptional = req.getSync(timeout)
    stringResponseOptional match {
      case None => None
      case Some(s) => Some(mapper(s))
    }
  }

  private def queryHelperLists[T](maybeResult : Option[List[T]]) : List[T] = {
    maybeResult match {
      case None => Nil
      case Some(l) => l
    }
  }

}

object RestDAO {
  val dao = new RestDAO
}