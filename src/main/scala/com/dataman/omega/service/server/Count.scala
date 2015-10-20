package com.dataman.omega.service.server

import java.io.BufferedInputStream
import java.net.URL
import java.util.Properties
import java.util.zip.GZIPInputStream

import com.dataman.omega.service.data.WordCountMsg
import com.dataman.webservice.Analyzer
import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.CoreLabel
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.jsoup.Jsoup

/**
 * Created by fchen on 15-10-20.
 */
object Count {
  val sc = new SparkContext()
  def count(msg: WordCountMsg): String = {
    count(sc, msg.table)
  }
  def count(sc: SparkContext, table: String): String = {

    val sqlContext = new SQLContext(sc)
    val HDFS_HOST = "10.3.12.9:9000"
    val STOPWORD_PATH = s"hdfs://$HDFS_HOST/test/stopword.dic"

    val ANALYZER_URL = "http://10.3.12.2:8666/analyzer"
    val DB_HOST = "10.3.12.10"
    val DB = "ldadb"
    val USER = "ldadev"
    val PASSWORD = "ldadev1234"
    val ROW1 = "content"
    val ROW2 = "keywords_char"
    val keyWordWeight = 10

    val rdd = sqlContext.read.format("jdbc").options(
      Map("url" -> s"jdbc:mysql://${DB_HOST}:3306/${DB}?user=${USER}&password=${PASSWORD}",
        "dbtable" -> table,
        "driver" -> "com.mysql.jdbc.Driver")).load()
      .select(ROW1, ROW2)
      .repartition(10)
      .mapPartitions(iter => {
        val props = new Properties
        props.setProperty("sighanCorporaDict", ANALYZER_URL)
        props.setProperty("serDictionary", s"$ANALYZER_URL/dict-chris6.ser.gz")
        props.setProperty("inputEncoding", "UTF-8")
        props.setProperty("sighanPostProcessing", "true")
        val segmenter = new CRFClassifier[CoreLabel](props)
        val url = s"$ANALYZER_URL/ctb.gz"
        val is = new URL(url).openStream
        val inputStream = new GZIPInputStream(new BufferedInputStream(is))
        segmenter.loadClassifierNoExceptions(inputStream, props)
        inputStream.close
        is.close
        iter.map( record => {
          val doc = if (record(0).toString.length > 0 && record(0) != null) {
            val m = """\\r\\n|\\r|\\n|\\"""
            //val text = Jsoup.parse(record(0).toString.replaceAll(m, "")).text()
            val text = Jsoup.parse(record(0).toString.replaceAll(m, "")).text()
            //val text = record.toString.replaceAll(m, "")
            segmenter.segmentString(text).toArray.mkString(" ")
          } else ""
        doc + (" " + record(1).toString) * keyWordWeight
      })
    })

    def clearData(rdd: RDD[String]): RDD[Array[String]] = {
      rdd.map( line => {
        line.replaceAll( """\[ t \]|\[ \\/t \]""", "")
      }).map(_.split(" ")).map(list => {
        // 去掉包含英文字母，以及数值的单词
        list.filter(x => {
          """[1-9,0]""".r.findAllMatchIn(x).isEmpty && """[a-z,A-Z]""".r.findAllMatchIn(x).isEmpty
        })
      })
    }

    def filter(rdd: RDD[Array[String]]): RDD[String] = {
      val stopwords: Set[String] = sc.textFile(STOPWORD_PATH).map(_.trim).filter(_.size > 0).distinct.collect.toSet
      val broadcastsw = sc.broadcast(stopwords)
      rdd.flatMap(x => x).filter(x => x.size > 1 && !broadcastsw.value.contains(x))
    }

    filter(clearData(rdd)).map((_, 1)).reduceByKey(_ + _).sortBy(_._2, false).take(500).map(x => {
      x._1 + ":" + x._2
    }).mkString(",")
  }
}