package graphql.bankrecords

import sangria.execution.deferred.Relation
import sangria.schema.{Field, ListType, ObjectType}
import sangria.schema._
import sangria.macros.derive._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.language.postfixOps
import sangria.macros.derive._


object SchemaDef {

  val TransactionType = ObjectType[Unit, Transaction](
    "Transaction",
    fields[Unit, Transaction](
      Field("transactionId", StringType, resolve = _.value.transactionId),
      Field("accountId", StringType, resolve = _.value.accountId),
      Field("transactionDay", IntType, resolve = _.value.transactionDay),
      Field("category", StringType, resolve = _.value.category),
      Field("transactionAmount", BigDecimalType, resolve = _.value.transactionAmount)
    )
  )

  val CustomerType = ObjectType[Unit, Customer](
    "Customer",
    fields[Unit, Customer](
      Field("customerId", StringType, resolve = _.value.customerId),
      Field("forename", StringType, resolve = _.value.forename),
      Field("surname", StringType, resolve = _.value.surname),
      Field("accounts", ListType(StringType), resolve = _.value.accounts)
    )
  )

  val QueryType = ObjectType(
    "Query",
    fields[RestDAO, Unit](
      Field("accountsOfCustomer", ListType(StringType),
        description = Some("Returns accounts for a given  `customerId`."),
        arguments = Argument("customerId", StringType) :: Nil,
        resolve = c => c.ctx.getCustomerAccounts(c.arg[String]("customerId"))),
      Field("transactionsOfAccount", ListType(TransactionType),
        description = Some("get transactions for a given account `accountId`."),
        arguments = Argument("accountId", StringType) :: Nil,
        resolve = c => c.ctx.getAccountTransactions(c.arg[String]("accountId"))),
      Field("transactionsOfCustomer", ListType(TransactionType),
        description = Some("get transactions for a given customer `customerId`."),
        arguments = Argument("customerId", StringType) :: Nil,
        resolve = c => c.ctx.getTransactionsForCustomer(c.arg[String]("customerId"))),
      Field("allTransactions", ListType(TransactionType),
        description = Some("get all transactions"),
        arguments = Nil,
        resolve = c => c.ctx.getAllTransactions()),
      Field("allCustomers", ListType(CustomerType),
        description = Some("get all customers"),
        arguments = Nil,
        resolve = c => c.ctx.getAllCustomers()),
      Field("transactionById", OptionType(TransactionType),
        description = Some("get transactions by an id `transactionId`."),
        arguments = Argument("transactionId", StringType) :: Nil,
        resolve = c => c.ctx.getTransactionById(c.arg[String]("transactionId"))),
      Field("customerById", OptionType(CustomerType),
        description = Some("get customer by an id `customerId`."),
        arguments = Argument("customerId", StringType) :: Nil,
        resolve = c => c.ctx.getCustomerById(c.arg[String]("customerId")))
    )
  )

  // 3
  val SchemaDefinition = Schema(QueryType)

}
