package com.cinema.http.model

import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag
import com.cinema.http.DBHandler

case class Order(
  id: Option[Int] = None,
  userid: Int,
  screeningid: Int,
  status: Option[String] = Some("BOOKED"),
  ticketscount: Int,
  totalprice: Option[BigDecimal] = None
)

object OrdersDB extends DBHandler{

  import scala.concurrent.ExecutionContext.Implicits.global

  class Orders(tag: Tag) extends Table[Order](tag, "orders"){
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("userId")
    def screeningId = column[Int]("screeningId")
    def status = column[String]("status")
    def ticketsCount = column[Int]("ticketsCount")
    def totalPrice = column[BigDecimal]("totalPrice")

    def * = (id.?, userId, screeningId, status.?, ticketsCount, totalPrice.?) <> (Order.tupled, Order.unapply)
  }

  val orders = TableQuery[Orders]

  def addOrder(orderEntity: Order) = awaitQuery[Order](
    db.run(orders returning orders.map(_.id) += orderEntity)
      .map(id => orderEntity.copy(id = Some(id)))
  )

  def getOrder(orderId: Int) = awaitQuery[Option[Order]](
    db.run(orders.filter(_.id === orderId).result.headOption)
  )

  def getAllOrders = awaitQuery[Seq[Order]](
    db.run(orders.result)
  )

  def getAllOrdersPage(page: Int = 1) = awaitQuery[Seq[Order]](
    db.run(orders.drop((page -1) *10).take(10).result)
  )

  def updateOrder(orderEntity: Order) = awaitQuery[Int](
    db.run(orders.insertOrUpdate(orderEntity))
  )

  def getUserOrders(userId: Int) = awaitQuery[Seq[Order]](
    db.run(orders.filter(_.userId === userId).result)
  )

  def getUserOrdersPage(userId: Int, page: Int) = awaitQuery[Seq[Order]](
    db.run(orders.filter(_.userId === userId).drop((page -1) *10).take(10).result)
  )

  def getScreeningOrders(screeningId: Int) = awaitQuery[Seq[Order]](
    db.run(orders.filter(_.screeningId === screeningId).result)
  )

  def getScreeningOrdersPage(screeningId: Int, page: Int) = awaitQuery[Seq[Order]](
    db.run(orders.filter(_.screeningId === screeningId).drop((page -1) *10).take(10).result)
  )

}