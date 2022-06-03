import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.time.Duration
import java.util._
import scala.jdk.CollectionConverters._
object KafkaConsumerApp extends App {

  val props:Properties = new Properties()
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "test")
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringDeserializer")
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer = new KafkaConsumer[String, String](props)
  val topics = Seq("t1")
  try {
    consumer.subscribe(topics.asJava)
    while (true) {
      val records = consumer.poll(Duration.ofMillis(100))
      for (record <- records.asScala) {
        println("Topic: " + record.topic() +
          ",Key: " + record.key() +
          ",Value: " + record.value() +
          ", Offset: " + record.offset() +
          ", Partition: " + record.partition())
      }
    }
  }catch{
    case e:Exception => e.printStackTrace()
  }finally {
    consumer.close()
  }
}
