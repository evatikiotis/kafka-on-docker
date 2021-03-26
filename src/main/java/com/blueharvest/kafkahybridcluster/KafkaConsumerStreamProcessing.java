package com.blueharvest.kafkahybridcluster;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerStreamProcessing {


    public static void main(final String[] args) throws Exception {
        final String TOPIC = "custom-population";
        final String BOOTSTRAP_SERVERS = "localhost:9092";

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Serializers/deserializers (serde) for String and Long types
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();
        StreamsBuilder builder = new StreamsBuilder();
        // Construct a `KStream` from the input topic, where message values
        // represent lines of text (for the sake of this example, we ignore whatever may be stored
        // in the message keys).
        KStream<String, String> textLines = builder.stream(TOPIC, Consumed.with(stringSerde, stringSerde));

        KTable<String, Long> wordCounts = textLines
                // Split each text line, by whitespace, into words.  The text lines are the message
                // values, i.e. we can ignore whatever data is in the message keys and thus invoke
                // `flatMapValues` instead of the more generic `flatMap`.
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
                // We use `groupBy` to ensure the words are available as message keys
                .groupBy((key, value) -> value)
                // Count the occurrences of each word (message key).
                .count();

// Convert the `KTable<String, Long>` into a `KStream<String, Long>` and write to the output topic.
        wordCounts.toStream().to("streams-wordcount-output", Produced.with(stringSerde, longSerde));


        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}
