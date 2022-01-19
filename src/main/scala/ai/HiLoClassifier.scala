package de.htwg.rs.chatbot
package ai

import iris.Evaluator

import de.htwg.rs.chatbot.model.{HiLoGame, Iris}
import org.datavec.api.records.reader.impl.csv.CSVRecordReader
import org.datavec.api.split.FileSplit
import org.datavec.api.transform.schema.Schema
import org.datavec.api.util.ndarray.RecordConverter
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, NeuralNetConfiguration}
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.evaluation.classification.Evaluation
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.{DataSet, SplitTestAndTrain}
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer
import org.nd4j.linalg.dataset.api.preprocessor.{NormalizerMinMaxScaler, NormalizerStandardize}
import org.nd4j.linalg.learning.config.Sgd
import org.nd4j.linalg.lossfunctions.LossFunctions

import java.io.File
import java.util

class HiLoClassifier extends Evaluator[HiLoGame]{


  var model: MultiLayerNetwork = null
  var labels: INDArray = null
  var normalizer = new NormalizerStandardize() // Neural nets all about numbers. Lets normalize our data

  def initialize(): Evaluator[HiLoGame] = {

    val numLinesToSkip = 0;
    val delimiter = ',';
    var recordReader = new CSVRecordReader(numLinesToSkip,delimiter);
    recordReader.initialize(new FileSplit(new File("C:\\Projects\\chatbot\\src\\main\\scala\\ai\\higher-lower-no-duplicates.txt")));

    val labelIndex = 5
    val numClasses = 2
    val batchSize = 1045

    val iterator: DataSetIterator = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, numClasses);
    var allData: DataSet = iterator.next();
    allData.shuffle();
    val testAndTrain: SplitTestAndTrain = allData.splitTestAndTrain(0.65); //Use 65% of data for training

    val trainingData: DataSet = testAndTrain.getTrain();
    val testData: DataSet = testAndTrain.getTest();

    //val normalizer = new NormalizerStandardize() // Neural nets all about numbers. Lets normalize our data
    normalizer.fit(trainingData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
    normalizer.transform(trainingData);     //Apply normalization to the training data
    normalizer.transform(testData);         //Apply normalization to the test data. This is using statistics calculated from the *training* set

    val numInputs = 5
    val outputNum = 2
    val nodesInHiddenLayers = 8
    val seed = 123

    var conf: MultiLayerConfiguration = new NeuralNetConfiguration.Builder()
      .seed(seed)
      .activation(Activation.TANH)
      .weightInit(WeightInit.XAVIER)
      .updater(new Sgd(0.1))
      .l2(1e-4)
      .list()
      .layer(new DenseLayer.Builder().nIn(numInputs).nOut(nodesInHiddenLayers)
        .build())
      .layer(new DenseLayer.Builder().nIn(nodesInHiddenLayers).nOut(nodesInHiddenLayers)
        .build())
      .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
        .activation(Activation.SOFTMAX) //Override the global TANH activation with softmax for this layer
        .nIn(nodesInHiddenLayers).nOut(outputNum).build())
      .build();

    model = new MultiLayerNetwork(conf)
    model.init();
    model.setListeners(new ScoreIterationListener(100)) //record score once every 100 iterations


    for (i <- 0 until 2000) {
      model.fit(trainingData)
    }

    val output = model.output(testData.getFeatures)

    val eval = new Evaluation(2) //evaluate the model on the test set
    eval.eval(testData.getLabels, output)
    println(eval.stats())

    labels = testData.getLabels

    this
  }

  def evaluate(game: HiLoGame): HiLoGame = {

    var schema = new Schema.Builder()
      .addColumnsInteger("first", "second", "third", "fourth", "fifth")
      .build()


    val record = RecordConverter.toRecord(schema, util.Arrays.asList(game.firstNumber, game.secondNumber, game.thirdNumber, game.fourthNumber, game.fifthNumber ))
    var data = RecordConverter.toArray(record)
    //model.fit(data, Array(0,1))
    val labelIndices0 = model.predict(data)
    normalizer.transform(data)
    val labelIndices = model.predict(data)
    print(s"LabelIndic not normalized: " + labelIndices0(0))
    print(s"LabelIndice: " + labelIndices(0))

    HiLoGame(game.firstNumber, game.secondNumber, game.thirdNumber, game.fourthNumber, game.fifthNumber, labelIndices(0))
  }

}
