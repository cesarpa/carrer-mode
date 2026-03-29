#!/bin/bash
# sim-race-skill.sh

COMMAND=$1
PILOT_NAME=$2
CAT_NAME=$3

case $COMMAND in
  "check-eligibility")
    # Lógica: F1 requiere licencia 'S', GT3 requiere 'A' o superior
    echo "🔍 Verificando elegibilidad para $PILOT_NAME en categoría $CAT_NAME..."
    # Aquí podrías hacer un curl a tu API de Spring Boot
    curl -s "http://localhost:8080/api/pilots/search?name=$PILOT_NAME" | jq -r '.licence' | grep -q "S"

    if [ $? -eq 0 ]; then
      echo "✅ ¡Autorizado! Licencia válida para alta competición."
    else
      echo "❌ Denegado. Se requiere licencia superior para $CAT_NAME."
    fi
    ;;
esac