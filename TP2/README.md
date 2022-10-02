# Fixer le classpath pour inclure les libraries nécessaires à la compilation et l'exécution des programmes

export CP=.:amqp-client-5.7.1.jar:slf4j-api-1.7.26.jar:slf4j-simple-1.7.26.jar

# Compiler les programmes

javac -cp $CP *.java

# Exécuter un producteur et un consommateur pour une queue

java -cp $CP QueueSender
java -cp $CP QueueReceiver

# Exécuter un consommateur et un producteur pour un topic

java -cp $CP TopicReceiver
java -cp $CP TopicSender
