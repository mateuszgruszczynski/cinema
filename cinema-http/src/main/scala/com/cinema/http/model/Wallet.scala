package com.cinema.http.model

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import com.cinema.http.DBHandler

case class Wallet(
  id: Option[Int] = None,
  userid: Int,
  value: BigDecimal
)

object WalletsDB extends DBHandler {

  import scala.concurrent.ExecutionContext.Implicits.global

  class Wallets(tag: Tag) extends Table[Wallet](tag, "wallets"){
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("userid")
    def value = column[BigDecimal]("value")

    def * = (id.?, userId, value) <> (Wallet.tupled, Wallet.unapply)
  }

  val wallets = TableQuery[Wallets]

  def addWallet(walletEntity: Wallet) = awaitQuery[Wallet](
    db.run(wallets returning wallets.map(_.id) += walletEntity)
      .map(id => walletEntity.copy(id = Some(id)))
  )

  def getWallet(userId: Int) = awaitQuery[Option[Wallet]](
    db.run(wallets.filter(_.userId === userId).result.headOption)
  )

  def updateWallet(walletEntity: Wallet) = awaitQuery[Int](
    db.run(wallets.insertOrUpdate(walletEntity))
  )
}