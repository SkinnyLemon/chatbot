package de.htwg.rs.chatbot
package ai.iris

import ai.{Classifier, Evaluator}
import model.*

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class IrisClassifierTest extends AnyWordSpec with Matchers {
  "A IrisClassifier object" should {
    "define a multilayernetwork" in {
      val multiLayerNetwork = IrisClassifier().defineNetwork()
      multiLayerNetwork shouldBe a[MultiLayerNetwork]
    }
    "initialize a iris evaluator object" in {
      val initializedIrisClassifier = IrisClassifier().initialize()
      initializedIrisClassifier shouldBe a[Evaluator[Iris]]
    }
    "evaluate a iris" in {
      val iris = Iris(sepalLength = 5.1, sepalWidth = 3.5, petalLength = 1.4, petalWidth = 0.2)
      val evaluatedIris = IrisClassifier().initialize().evaluate(iris)
      evaluatedIris.irisClass should startWith("Iris Setosa")
    }
    "get iris names for classes" in {
      val irisSetosa = IrisClassifier().getIrisClass(0)
      irisSetosa should startWith("Iris Setosa")
      val irisVersicolor = IrisClassifier().getIrisClass(1)
      irisVersicolor should startWith("Iris Versicolor")
      val irisVirginica = IrisClassifier().getIrisClass(2)
      irisVirginica should startWith("Iris Virginica")
      val notValidInput = IrisClassifier().getIrisClass(3)
      notValidInput should startWith("not valid")
    }
  }
}
