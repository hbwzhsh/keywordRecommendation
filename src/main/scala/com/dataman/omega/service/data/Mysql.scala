package com.dataman.omega.service.data

import slick.driver.MySQLDriver.simple._
import com.dataman.omega.service.utils.{Configs => C}
/**
 * Created by mymac on 15/9/25.
 */
object Mysql {

    case class Message(articleid: Int, title: String, subcontent: String, content: String, appid: Int, keywords: String)

    class MessageTable(tag: Tag) extends Table[Message](tag, "art4test") {
      def articleid = column[Int]("articleid", O.PrimaryKey)
      def title = column[String]("title")
      def subcontent = column[String]("subcontent")
      def content = column[String]("content")
      def appid = column[Int]("appid")
      def keywords = column[String]("keywords")

      def * = (articleid, title, subcontent, content, appid, keywords) <> (Message.tupled, Message.unapply)
    }

    def createTable() = {
      def db = Database.forURL(
        url = s"jdbc:mysql://${C.mHost}:${C.mPort.toString}/${C.mDB}?user=${C.mUser}&password=${C.mPasswd}&useUnicode=true&characterEncoding=utf8",
        driver = "com.mysql.jdbc.Driver"
      )
      implicit val session = db.createSession()
      val messages = TableQuery[MessageTable]
      messages.ddl.create
    }

    def insertMessage(msg: InputMsg) = {
      def db = Database.forURL(
        url = s"jdbc:mysql://${C.mHost}:${C.mPort.toString}/${C.mDB}?user=${C.mUser}&password=${C.mPasswd}&useUnicode=true&characterEncoding=utf8",
        driver = "com.mysql.jdbc.Driver"
      )
      implicit val session = db.createSession()
      val messages = TableQuery[MessageTable]
      val m1 = Message(msg.articleid,
                       msg.title.getOrElse(""),
                       msg.subcontent.getOrElse(""),
                       msg.content.getOrElse(""),
                       msg.appid,
                       msg.keywords.getOrElse("")
      )
      messages += m1
    }

}

