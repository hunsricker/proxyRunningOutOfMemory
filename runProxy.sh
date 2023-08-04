#!/bin/sh

# output calling 'java -version'
# openjdk version "20.0.1" 2023-04-18
# OpenJDK Runtime Environment GraalVM CE 20.0.1+9.1 (build 20.0.1+9-jvmci-23.0-b12)
# OpenJDK 64-Bit Server VM GraalVM CE 20.0.1+9.1 (build 20.0.1+9-jvmci-23.0-b12, mixed mode, sharing)
JAVA_HOME='/Users/akreutze/.sdkman/candidates/java/20.0.1-graalce'
JAVA_OPTS='--enable-preview -XX:+UseZGC -Xms128m -Xmx128m -XX:MaxMetaspaceSize=64m -XX:MetaspaceSize=64m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./sampleData'
MICRONAUT_OPTS='-Dio.netty.leakDetection.level=paranoid -Dio.netty.leakDetection.targetRecords=30'
JAVA_APP_OPTS='--micronaut.server.port=7580'
LOG4J_OPTS='-Dlog4j.configurationFile=src/main/resources/log4j2.xml'
$JAVA_HOME/bin/java $LOG4J_OPTS $JAVA_OPTS $MICRONAUT_OPTS -jar target/proxyrunningoutofmemory-0.1.jar $JAVA_APP_OPTS >> ./app.log 2>&1