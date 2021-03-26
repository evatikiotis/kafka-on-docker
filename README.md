# Kafka Hybrid Cluster

## How to RUN
#### Run kafka and related services. Please keep in mind that all services should be UP to continue.
$ docker-compose up -d

#### Check which services are UP with the following command. 
$ docker-compose ps

If one of the services is not up rerun the previous command and check again.

When everything is UP, wait some time and navigate to the control-center http://localhost:9021/, select the available cluster and create topic named "custom-population". 
Once you created the topic you can run the producer main class. For some reasons you may need to run producer more than one times to send records in the topic.
If you want you can try running consumer as well, but it doesn't work properly yet

#### pom.xml dependencies
- kafka-clients to use producer and consumer Java APIs
- kafka-streams stream processing library. In comparison to other stream processing mechanisms kafka-streams is just a library and no further configuration is required
- log4j implementation


#### Source code

KafkaPopulationProducer: Simple Producer based on csv input file

KafkaPopulationConsumer: Work in Progress / simple consumer with word count job.
