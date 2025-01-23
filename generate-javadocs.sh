#!/bin/bash

CLASSPATH=$(cat classpath.txt)
echo "Using classpath: $CLASSPATH"

javadoc -d docs \
        -sourcepath src/main/java \
        -subpackages com.adrain.llm_middleware \
        -classpath "$CLASSPATH"
