#!/bin/bash

# Script para compilar y ejecutar la interfaz gráfica del Metro Client
# Uso: ./run_gui.sh

echo "========================================="
echo "  Metro Telemetry Client - GUI Launcher"
echo "========================================="
echo ""

# Verificar que Java esté instalado
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java no está instalado"
    echo "   Por favor instale Java JDK 8 o superior"
    exit 1
fi

if ! command -v javac &> /dev/null; then
    echo "❌ Error: javac no está disponible"
    echo "   Por favor instale Java JDK 8 o superior"
    exit 1
fi

echo "✅ Java detectado:"
java -version
echo ""

# Compilar si no existe el .class o si el .java es más nuevo
if [ ! -f "MetroClientGUI.class" ] || [ "MetroClientGUI.java" -nt "MetroClientGUI.class" ]; then
    echo "🔨 Compilando MetroClientGUI.java..."
    javac MetroClientGUI.java
    
    if [ $? -eq 0 ]; then
        echo "✅ Compilación exitosa"
    else
        echo "❌ Error en compilación"
        exit 1
    fi
else
    echo "✅ MetroClientGUI.class está actualizado"
fi

echo ""
echo "🚀 Iniciando interfaz gráfica..."
echo ""

# Ejecutar la GUI
java MetroClientGUI

echo ""
echo "👋 GUI cerrada"
