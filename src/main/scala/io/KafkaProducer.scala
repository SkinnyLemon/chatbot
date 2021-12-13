package de.htwg.rs.chatbot
package io

import org.apache.kafka.clients.producer._

import java.util.Properties

object KafkaProducer {
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val TOPIC = "twitch"
  val KEY = "message"

  def send(message: String): Unit = {
    val record = new ProducerRecord(TOPIC, KEY, message)
    val result = producer.send(record)
    result.get()
  }

  def kill(): Unit = producer.close()
}
