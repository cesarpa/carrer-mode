#!/bin/bash
# race-control.sh - Skill para gestionar inscripciones por categoría

COMMAND=$1
PARAM=$2

case $COMMAND in
  "list-category")
    # Lista todos los carros de una categoría específica (Ej: GT3, F1)
    echo "🏎️ Buscando vehículos en la categoría: $PARAM..."
    curl -s -X GET "http://localhost:8080/api/cars/search?category=$PARAM" | jq .
    ;;

  "add-car")
    # Genera un JSON rápido para insertar un carro nuevo
    # Uso: ./race-control.sh add-car "McLaren F1"
    echo "🛠️ Generando entrada para $PARAM..."
    curl -X POST http://localhost:8080/api/cars \
         -H "Content-Type: application/json" \
         -d "{\"model\": \"$PARAM\", \"year\": 2026, \"category\": {\"id\": 1}}"
    ;;

  *)
    echo "Uso: race-control {list-category|add-car} {valor}"
    ;;
esac