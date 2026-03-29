#!/bin/bash
# add-car.sh
NAME=$1
YEAR=$2
POWER=$3

curl -X POST http://localhost:8080/api/cars \
     -H "Content-Type: application/json" \
     -d "{
           \"name\": \"$NAME\",
           \"year\": $YEAR,
           \"hp\": $POWER,
           \"createdBy\": \"COPILOT_CLI\"
         }"