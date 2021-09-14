#!/bin/sh
read -sp 'Please Enter the JL API_KEY: ' apiKey
echo
API_KEY=$apiKey ./gradlew bootRun
