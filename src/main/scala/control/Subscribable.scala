package de.htwg.rs.chatbot
package control

import scala.collection.mutable.ListBuffer

trait Subscriber[T]:
  def onMessage(message: T): Unit

trait Subscribable[T]:
  protected val subscribers = ListBuffer.empty[Subscriber[T]]

  def subscribe(subscriber: Subscriber[T]): Unit = subscribers += subscriber

  def unsubscribe(subscriber: Subscriber[T]): Unit = subscribers -= subscriber
