#!/bin/bash

# Script para compilar todos los clientes Metro (CLI y GUI)
# Uso: ./compile.sh [cli|gui|all]

MODE=${1:-all}

echo "========================================="
echo "  Metro Client - Compilador"
echo "========================================="
echo ""

# Verificar Java
if ! command -v javac &> /dev/null; then
    echo "❌ Error: javac no está disponible"
    exit 1
fi

compile_cli() {
    echo "🔨 Compilando MetroClient.java (CLI)..."
    javac MetroClient.java
    if [ $? -eq 0 ]; then
        echo "✅ CLI compilado exitosamente"
    else
        echo "❌ Error compilando CLI"
        return 1
    fi
}

compile_gui() {
    echo "🔨 Compilando MetroClientGUI.java (GUI)..."
    javac MetroClientGUI.java
    if [ $? -eq 0 ]; then
        echo "✅ GUI compilado exitosamente"
    else
        echo "❌ Error compilando GUI"
        return 1
    fi
}

case $MODE in
    cli)
        compile_cli
        ;;
    gui)
        compile_gui
        ;;
    all)
        compile_cli
        echo ""
        compile_gui
        ;;
    *)
        echo "❌ Modo inválido: $MODE"
        echo "Uso: ./compile.sh [cli|gui|all]"
        exit 1
        ;;
esac

echo ""
echo "✅ Compilación completada"
echo ""
echo "Para ejecutar:"
echo "  CLI: java MetroClient <host> <port>"
echo "  GUI: java MetroClientGUI"
