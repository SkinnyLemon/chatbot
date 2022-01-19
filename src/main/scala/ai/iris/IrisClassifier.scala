package de.htwg.rs.chatbot.ai.iris

import de.htwg.rs.chatbot.ai.{Classifier, Evaluator}
import de.htwg.rs.chatbot.model.Iris
import org.datavec.api.records.reader.RecordReader
import org.datavec.api.records.reader.impl.csv.CSVRecordReader
import org.datavec.api.split.FileSplit
import org.datavec.api.transform.transform.normalize.Normalize
import org.datavec.api.transform.TransformProcess
import org.datavec.api.util.ndarray.RecordConverter
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.evaluation.classification.Evaluation
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.dataset.SplitTestAndTrain
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize
import org.nd4j.linalg.learning.config.Sgd
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.datavec.api.transform.schema.Schema

import java.io.File
import java.util
import scala.collection.mutable.HashMap
import scala.collection.mutable


case class IrisClassifier() extends Classifier[Iris] with Evaluator[Iris] {


  val model: MultiLayerNetwork = defineNetwork()
  val normalizer = new NormalizerStandardize() // Neural nets all about numbers. Lets normalize our data


  def defineNetwork(): MultiLayerNetwork =
    val numInputs = 4
    val outputNum = 3
    val seed = 123

    val conf: MultiLayerConfiguration = new NeuralNetConfiguration.Builder()
      .seed(seed)
      .activation(Activation.TANH)
      .weightInit(WeightInit.XAVIER)
      .updater(new Sgd(0.1))
      .l2(1e-4)
      .list()
      .layer(new DenseLayer.Builder().nIn(numInputs).nOut(3)
        .build())
      .layer(new DenseLayer.Builder().nIn(3).nOut(3)
        .build())
      .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
        .activation(Activation.SOFTMAX) //Override the global TANH activation with softmax for this layer
        .nIn(3).nOut(outputNum).build())
      .build();
    new MultiLayerNetwork(conf)


  def initialize(): Evaluator[Iris] =
    val numLinesToSkip = 0;
    val delimiter = ',';
    val recordReader = new CSVRecordReader(numLinesToSkip, delimiter)
    recordReader.initialize(new FileSplit(new File("C:\\Projects\\chatbot\\src\\main\\scala\\ai\\iris\\iris.txt")));

    val labelIndex = 4
    val numClasses = 3
    val batchSize = 150

    val iterator: DataSetIterator = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, numClasses);
    val allData: DataSet = iterator.next();
    allData.shuffle();
    val testAndTrain: SplitTestAndTrain = allData.splitTestAndTrain(0.65); //Use 65% of data for training

    val trainingData: DataSet = testAndTrain.getTrain();
    val testData: DataSet = testAndTrain.getTest();

    normalizer.fit(trainingData); //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
    normalizer.transform(trainingData); //Apply normalization to the training data
    normalizer.transform(testData); //Apply normalization to the test data. This is using statistics calculated from the *training* set

    model.init();
    model.setListeners(new ScoreIterationListener(100)) //record score once every 100 iterations

    for (i <- 0 until 1000) {
      model.fit(trainingData)
    }

    val output = model.output(testData.getFeatures)

    val eval = new Evaluation(3) //evaluate the model on the test set
    eval.eval(testData.getLabels, output)
    println(eval.stats())

    val labels = testData.getLabels

    this


  def evaluate(iris: Iris): Iris =
    val schema = new Schema.Builder()
      .addColumnsDouble("sepalLength", "sepalWidth", "petalLength", "petalWidth")
      .build()
    val record = RecordConverter.toRecord(schema, util.Arrays.asList(iris.sepalLength, iris.sepalWidth, iris.petalLength, iris.petalWidth))
    val data = RecordConverter.toArray(record)
    normalizer.transform(data)
    val labelIndices = model.predict(data)
    Iris(iris.sepalLength, iris.sepalWidth, iris.petalLength, iris.petalWidth, getIrisClass(labelIndices(0)))


  def getIrisClass(classAsNumber: Int): String =
    classAsNumber match {
      case 0 => "Iris Setosa"
      case 1 => "Iris Versicolor"
      case 2 => "Iris Virginica"
      case _ => "not valid"
    }


}