#!/bin/bash

./gradlew processor:jar
./gradlew jar
java -jar build/libs/09-comparator-generator-1.0.jar
