# 🖥️ Metro Telemetry Client - Interfaz Gráfica (GUI)

Cliente con interfaz gráfica en Java Swing para el Sistema de Telemetría de Metro Autónomo.

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![GUI](https://img.shields.io/badge/GUI-Swing-blue.svg)]()
[![Status](https://img.shields.io/badge/Status-Stable-green.svg)]()

## 📋 Descripción

Interfaz gráfica moderna y fácil de usar que implementa el protocolo completo del sistema de telemetría del metro. Incluye visualización en tiempo real, panel de control y monitoreo de estado.

**Características principales:**
- ✅ Interfaz visual moderna con Java Swing
- ✅ Conexión TCP directa al servidor C
- ✅ Panel de autenticación integrado
- ✅ Visualización de telemetría en tiempo real con barras de progreso
- ✅ Botones intuitivos para todos los comandos
- ✅ Log de mensajes con timestamps
- ✅ Indicadores visuales de estado (conectado/desconectado/autenticado)
- ✅ Sin dependencias externas (solo JDK)

## 🎨 Capturas de Pantalla

### Interfaz Principal
La interfaz incluye:
- **Panel de Conexión** (superior izquierda): Host y puerto del servidor
- **Panel de Autenticación** (superior derecha): Usuario y contraseña
- **Panel de Telemetría** (centro izquierda): Velocidad, batería, dirección, estación
- **Panel de Control** (centro derecha): Botones de comandos
- **Log de Mensajes** (inferior): Historial de todas las operaciones

## 🔧 Requisitos

- **Java JDK 8 o superior**

Verificar instalación:
```bash
java -version
javac -version
```

## 📦 Instalación y Compilación

### Paso 1: Compilar
```bash
javac MetroClientGUI.java
```

Esto genera `MetroClientGUI.class`

## 🚀 Uso

### Ejecutar la interfaz gráfica:
```bash
java MetroClientGUI
```

La ventana se abrirá automáticamente.

## 📖 Guía de Uso Paso a Paso

### 1️⃣ Conectar al Servidor

1. En el panel **"🔗 Conexión"**:
   - **Host**: Ingrese la IP del servidor (ej: `3.81.235.32`)
   - **Puerto**: Ingrese el puerto (ej: `5000`)
2. Click en el botón **"Conectar"**
3. El estado cambiará a 🟢 **Conectado**

### 2️⃣ Autenticarse

1. En el panel **"🔐 Autenticación"**:
   - **Usuario**: Ingrese el usuario (ej: `admin`)
   - **Contraseña**: Ingrese la contraseña (ej: `metro123`)
2. Click en el botón **"Autenticar"**
3. El estado cambiará a 🟢 **Autenticado como admin**

**Usuarios disponibles:**
- `admin` / `metro123` → Rol ADMIN (todos los permisos)
- `observer` / `metro123` → Rol OBSERVER (solo lectura)

### 3️⃣ Ver Telemetría en Tiempo Real

Una vez autenticado, el panel **"📊 Telemetría en Tiempo Real"** se actualiza automáticamente cada segundo:

- **🚄 Velocidad**: 
  - Muestra velocidad actual en km/h
  - Barra de progreso visual (0-100 km/h)
  
- **🔋 Batería**:
  - Muestra porcentaje de batería
  - Barra de progreso con colores:
    - 🟢 Verde: > 50%
    - 🟠 Naranja: 20-50%
    - 🔴 Rojo: < 20%
  
- **🧭 Dirección**: FORWARD o BACKWARD

- **🏢 Estación**: Número de estación actual (1-5)

### 4️⃣ Enviar Comandos

En el panel **"🎮 Panel de Control"**, use los botones para controlar el metro:

#### 🚀 SPEED UP
- Aumenta velocidad +10 km/h
- Máximo: 100 km/h
- Requiere batería > 10%
- Solo usuarios ADMIN

#### 🐌 SLOW DOWN
- Disminuye velocidad -10 km/h
- Mínimo: 0 km/h
- Solo usuarios ADMIN

#### 🛑 STOP NOW
- Detiene el metro inmediatamente (velocidad = 0)
- Solo usuarios ADMIN

#### ▶️ START NOW
- Reanuda el movimiento del metro
- Solo funciona si fue detenido con STOP NOW
- Solo usuarios ADMIN

### 5️⃣ Monitorear Log de Mensajes

El panel **"📝 Log de Mensajes"** muestra:
- 📤 Mensajes enviados al servidor
- 📥 Mensajes recibidos del servidor
- ✅ Confirmaciones de comandos
- ❌ Errores y advertencias
- Timestamp de cada evento

### 6️⃣ Desconectar

Click en el botón **"👋 Desconectar"** para:
- Cerrar sesión (logout)
- Desconectar del servidor
- Resetear la interfaz

## 🎯 Flujo Típico de Uso

```
1. Ejecutar → java MetroClientGUI
2. Conectar → Ingresar host y puerto → Click "Conectar"
3. Autenticar → Ingresar usuario y contraseña → Click "Autenticar"
4. Monitorear → Ver telemetría en tiempo real
5. Controlar → Click en botones de comandos (SPEED UP, SLOW DOWN, etc.)
6. Desconectar → Click en "Desconectar"
```

## 🎨 Características de la Interfaz

### Colores y Estados

| Estado | Color | Símbolo |
|--------|-------|---------|
| Desconectado | Gris | ⚪ |
| Conectando | Naranja | 🟡 |
| Conectado | Verde | 🟢 |
| Autenticado | Verde | 🟢 |
| Error | Rojo | 🔴 |

### Barras de Progreso

- **Velocidad**: Azul (0-100 km/h)
- **Batería**: 
  - Verde (> 50%)
  - Naranja (20-50%)
  - Rojo (< 20%)

### Iconos Visuales

- 🔗 Conexión
- 🔐 Autenticación
- 📊 Telemetría
- 🎮 Control
- 📝 Log
- 🚄 Velocidad
- 🔋 Batería
- 🧭 Dirección
- 🏢 Estación

## 🏗️ Arquitectura del Código

### Componentes Principales

```java
public class MetroClientGUI extends JFrame {
    // Paneles
    - createTopPanel()        // Conexión y autenticación
    - createCenterPanel()     // Telemetría y comandos
    - createBottomPanel()     // Log de mensajes
    
    // Funcionalidad
    - connect()               // Conectar al servidor
    - authenticate()          // Autenticar usuario
    - sendCommand()           // Enviar comandos
    - startTelemetryListener() // Listener de telemetría
    - updateTelemetry()       // Actualizar visualización
    - disconnect()            // Desconectar
}
```

### Tecnologías

| Componente | Tecnología |
|-----------|------------|
| **GUI Framework** | Java Swing |
| **Layout Managers** | GridBagLayout, BorderLayout, BoxLayout |
| **Threading** | ScheduledExecutorService |
| **Networking** | java.net.Socket |
| **I/O** | BufferedReader + PrintWriter |

## 🔄 Comparación: GUI vs CLI

| Característica | GUI | CLI |
|---------------|-----|-----|
| **Interfaz** | Visual moderna | Texto en terminal |
| **Uso** | Click en botones | Escribir opciones |
| **Telemetría** | Actualización automática | Modo especial (opción 5) |
| **Visualización** | Barras de progreso, colores | Texto con emojis |
| **Accesibilidad** | Más intuitivo | Más técnico |
| **Dependencias** | Solo JDK | Solo JDK |

## 🐞 Troubleshooting

### Error: "Connection refused"
**Causa:** Servidor no está ejecutándose  
**Solución:** 
1. Verificar que el servidor C esté corriendo
2. Verificar host y puerto en el panel de conexión

### Error: "Authentication failed"
**Causa:** Credenciales incorrectas  
**Solución:** 
- Usar `admin/metro123` o `observer/metro123`
- Verificar mayúsculas/minúsculas

### Error: "Permission denied"
**Causa:** Usuario OBSERVER intenta enviar comandos  
**Solución:** 
- Desconectar
- Re-autenticarse como `admin`

### Comando rechazado (❌ en log)
**Posibles razones:**
- Batería baja (< 10%) → Esperar recarga
- Velocidad en límite (100 km/h) → Usar SLOW DOWN
- Metro ya detenido → No usar STOP NOW
- Metro no detenido por comando → START NOW solo funciona después de STOP NOW

### Telemetría no se actualiza
**Solución:**
1. Verificar estado: Debe estar "Autenticado"
2. Revisar log por errores
3. Re-conectar si es necesario

## 📦 Empaquetado como JAR Ejecutable

### 1. Crear archivo `Manifest-GUI.txt`:
```
Main-Class: MetroClientGUI

```
*(Nota: debe haber una línea vacía al final)*

### 2. Compilar y empaquetar:
```bash
javac MetroClientGUI.java
jar cfm MetroClientGUI.jar Manifest-GUI.txt MetroClientGUI.class
```

### 3. Ejecutar JAR:
```bash
java -jar MetroClientGUI.jar
```

### 4. (Opcional) Crear ejecutable doble-click:

**En Windows:**
```batch
javaw -jar MetroClientGUI.jar
```

**En macOS/Linux:**
```bash
#!/bin/bash
java -jar MetroClientGUI.jar
```

## 🔒 Seguridad

- ✅ Autenticación basada en tokens
- ✅ Roles diferenciados (ADMIN/OBSERVER)
- ✅ Timeout de conexión (5 segundos)
- ✅ Validación de comandos en servidor
- ✅ Log completo de operaciones
- ✅ Desconexión limpia con logout

## 💡 Tips y Mejores Prácticas

### Tip 1: Monitoreo Continuo
Deje la interfaz abierta para monitoreo continuo de telemetría en tiempo real.

### Tip 2: Múltiples Instancias
Puede abrir varias instancias del GUI para conectar con diferentes usuarios:
```bash
# Terminal 1: Admin
java MetroClientGUI

# Terminal 2: Observer
java MetroClientGUI
```

### Tip 3: Log como Histórico
El log mantiene todo el historial de la sesión. Scroll hacia arriba para ver eventos anteriores.

### Tip 4: Códigos de Color
Aprenda los códigos de color de la batería para detectar rápidamente niveles críticos.

### Tip 5: Desconexión Limpia
Siempre use el botón "Desconectar" en vez de cerrar la ventana directamente.

## 🎓 Casos de Uso

### Operador de Control
```
1. Abrir GUI
2. Conectar como admin
3. Monitorear telemetría
4. Ajustar velocidad según necesidad
5. Detener en emergencia (STOP NOW)
```

### Supervisor de Monitoreo
```
1. Abrir GUI
2. Conectar como observer
3. Monitorear telemetría sin intervenir
4. Revisar log de eventos
```

### Testing y Debugging
```
1. Conectar a servidor local
2. Observar mensajes en log (📤 📥)
3. Validar protocolo de comunicación
```

## 🚀 Ventajas de la GUI

1. **Intuitiva**: No requiere memorizar comandos
2. **Visual**: Barras de progreso y colores informativos
3. **Tiempo Real**: Actualización automática de telemetría
4. **Accesible**: Ideal para usuarios no técnicos
5. **Completa**: Todas las funcionalidades del CLI
6. **Profesional**: Interfaz moderna y pulida

## 📚 Recursos Relacionados

- **CLI Version**: `MetroClient.java` - Cliente de línea de comandos
- **Servidor C**: `server.c` - Servidor de telemetría
- **Web UI**: `web-ui-main/` - Interfaz web TypeScript/React
- **Protocolo**: Ver documentación técnica

## 🤝 Contribuciones

Para contribuir:
1. Fork el proyecto
2. Mejore la interfaz gráfica
3. Agregue nuevas funcionalidades
4. Envíe Pull Request

## 📝 Licencia

Este proyecto es parte del Sistema de Telemetría Metro Autónomo.  
Desarrollado para el curso de Arquitectura y Protocolos de Red 2025.

## 👤 Autor

**Sistema de Telemetría Metro Autónomo**  
Interfaz Gráfica - Java Swing  
Año: 2025

---

## 🎯 Inicio Rápido (30 segundos)

```bash
# Compilar
javac MetroClientGUI.java

# Ejecutar
java MetroClientGUI

# En la GUI:
# 1. Host: 3.81.235.32, Puerto: 5000 → Click "Conectar"
# 2. Usuario: admin, Contraseña: metro123 → Click "Autenticar"
# 3. ¡Listo para usar!
```

**⭐ ¡Disfruta de la interfaz gráfica del Metro Telemetry Client!**
