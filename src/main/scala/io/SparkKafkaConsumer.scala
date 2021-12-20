package de.htwg.rs.chatbot
package io

import de.htwg.rs.chatbot.model.TwitchInput
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

object SparkKafkaConsumer extends App{

  val sparkConfig = SparkSession.builder.appName("SparkKafkaConsumer").config("spark.master", "local[*]").getOrCreate()
  val sparkStreamingContext = new StreamingContext(sparkConfig.sparkContext, Seconds(1))

  sparkStreamingContext.sparkContext.setLogLevel("ERROR")

  val kafkaConnectionConfig = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "something"
  )

  val kafkaTopics = Array("twitch")

  val stream = KafkaUtils.createDirectStream[String, String](
    sparkStreamingContext,
    PreferConsistent,
    Subscribe[String, String](kafkaTopics, kafkaConnectionConfig)
  )

  val records = stream.map(record => record.value().split(".*Emote.*?,")(1).split(".*?,")(0).split(" "))

  sparkStreamingContext.start() // start the computation
  sparkStreamingContext.awaitTermination() // await termination

}
