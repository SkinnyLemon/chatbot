package de.htwg.rs.chatbot
package ai.hiLo

import ai.{Classifier, Evaluator}
import model.*

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class HiLoClassifierTest extends AnyWordSpec with Matchers {
  "A HiLoClassifier object" should {
    "define a multilayernetwork" in {
      val multiLayerNetwork = HiLoClassifier().defineNetwork()
      multiLayerNetwork shouldBe a[MultiLayerNetwork]
    }
    "initialize a HiLoGame evaluator object" in {
      val initializedHiLoClassifier = HiLoClassifier().initialize()
      initializedHiLoClassifier shouldBe a[Evaluator[HiLoGame]]
    }
    "evaluate a deck" in {
      val hiLoDeck = HiLoGame(firstNumber = 7, secondNumber = 10, thirdNumber = 11, fourthNumber = 9, fifthNumber = 12)
      val evaluatedHiLoDeck = HiLoClassifier().initialize().evaluate(hiLoDeck)
      evaluatedHiLoDeck.bestChoice should be(0)
    }
  }
}
