package de.htwg.rs.chatbot
package control

import io.TwitchOutput
import model.{Channel, Command, Message, TwitchInput}

import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

import scala.collection.mutable.ListBuffer

class SubscribableTest extends AnyWordSpec with Matchers {
  class MySubscribable extends Subscribable[Int] {
    def subscriberList: List[Subscriber[Int]] = subscribers.toList
  }
  val subscribable = new MySubscribable
  val subscriber = new Subscriber[Int] {
    override def onMessage(message: Int): Unit = ???
  }

  "A subscribable" should {
    "add something to a list of subscribers" in {
      subscribable.subscribe(subscriber)
      subscribable.subscriberList(0) shouldBe a[Subscriber[Int]]
    }
    "be able to unsubscribe" in {
      subscribable.unsubscribe(subscriber)
      subscribable.subscriberList shouldBe ListBuffer.empty[Subscriber[Int]]
    }
  }
}
