# 🚄 Cliente Metro Telemetría - Java

## 📋 Descripción

Clientes Java para el Sistema de Telemetría de Metro Autónomo. Disponibles en **dos versiones**:

1. **CLI (Command Line Interface)** - Interfaz de línea de comandos
2. **GUI (Graphical User Interface)** - Interfaz gráfica visual

Ambas implementan el protocolo completo y se conectan directamente al servidor C mediante sockets TCP.

---

## 🎯 ¿Qué versión usar?

### 🖥️ Interfaz Gráfica (GUI) - **RECOMENDADO**

✅ **Usa la GUI si...**
- Prefieres interfaz visual moderna
- Quieres ver la telemetría con barras de progreso y colores
- Necesitas una experiencia más intuitiva
- No estás familiarizado con la terminal
- Quieres control visual con botones

```bash
# Inicio rápido GUI
./run_gui.sh          # Linux/macOS
run_gui.bat           # Windows
```

**📖 Ver:** `README_GUI.md` | `QUICK_START_GUI.md`

---

### 💻 Línea de Comandos (CLI)

✅ **Usa el CLI si...**
- Prefieres trabajar en terminal
- Necesitas automatizar con scripts
- Estás en un servidor sin interfaz gráfica
- Quieres menor consumo de recursos
- Te gusta la experiencia de terminal

```bash
# Inicio rápido CLI
javac MetroClient.java
java MetroClient 3.81.235.32 5000
```

**📖 Ver:** `README.md` | `GUIA_USO_RAPIDA.md`

---

## 🚀 Inicio Rápido

### Opción 1: Interfaz Gráfica (Recomendado)

```bash
# 1. Compilar y ejecutar
./run_gui.sh

# 2. En la ventana:
#    - Host: 3.81.235.32, Puerto: 5000 → [Conectar]
#    - Usuario: admin, Contraseña: metro123 → [Autenticar]

# 3. ¡Listo! Usa los botones para controlar el metro
```

### Opción 2: Línea de Comandos

```bash
# 1. Compilar
javac MetroClient.java

# 2. Ejecutar
java MetroClient 3.81.235.32 5000

# 3. Autenticar
#    Usuario: admin
#    Contraseña: metro123

# 4. Seleccionar opciones del menú (1-6)
```

---

## 📊 Comparación CLI vs GUI

| Característica | CLI | GUI |
|---------------|-----|-----|
| **Interfaz** | Texto en terminal | Ventana gráfica |
| **Facilidad de uso** | Media | Alta |
| **Visualización** | Texto + emojis | Barras de progreso + colores |
| **Telemetría** | Modo especial (opción 5) | Automática en tiempo real |
| **Comandos** | Menú numerado (1-6) | Botones visuales |
| **Log** | En terminal | Panel dedicado |
| **Recursos** | Muy bajo | Bajo-Medio |
| **Automatización** | Fácil (scripts) | Difícil |
| **SSH/Remoto** | ✅ Sí | ❌ No |
| **Aprendizaje** | Curva media | Intuitivo |

---

## 🔧 Requisitos

- **Java JDK 8 o superior**

Verificar:
```bash
java -version
javac -version
```

Si no tienes Java:
- **Ubuntu/Debian**: `sudo apt install default-jdk`
- **macOS**: `brew install openjdk`
- **Windows**: Descargar desde [oracle.com/java](https://www.oracle.com/java/technologies/downloads/)

---

## 📁 Estructura de Archivos

```
java_cli/
│
├── 🖥️ INTERFAZ GRÁFICA (GUI)
│   ├── MetroClientGUI.java       ← Código fuente GUI
│   ├── README_GUI.md              ← Documentación completa GUI
│   ├── QUICK_START_GUI.md         ← Guía rápida GUI
│   ├── run_gui.sh                 ← Launcher Linux/macOS
│   └── run_gui.bat                ← Launcher Windows
│
├── 💻 LÍNEA DE COMANDOS (CLI)
│   ├── MetroClient.java           ← Código fuente CLI
│   ├── README.md                  ← Documentación completa CLI
│   └── GUIA_USO_RAPIDA.md         ← Guía rápida CLI
│
├── 🛠️ UTILIDADES
│   ├── compile.sh                 ← Compilador universal
│   ├── .gitignore                 ← Archivos ignorados
│   └── LEEME.md                   ← Este archivo
│
└── 📦 COMPILADOS (generados)
    ├── MetroClientGUI.class       ← GUI compilado
    └── MetroClient.class          ← CLI compilado
```

---

## 🎮 Funcionalidades Disponibles

### Ambas versiones incluyen:

✅ **Conexión TCP**
- Conectar a servidor C directamente
- Host y puerto configurables
- Timeout de 5 segundos

✅ **Autenticación**
- Usuario y contraseña
- Token de sesión
- Roles: ADMIN / OBSERVER

✅ **Comandos de Control** (solo ADMIN)
- 🚀 **SPEED_UP**: +10 km/h
- 🐌 **SLOW_DOWN**: -10 km/h
- 🛑 **STOPNOW**: Detener inmediatamente
- ▶️ **STARTNOW**: Reanudar movimiento

✅ **Telemetría en Tiempo Real**
- 🚄 Velocidad (0-100 km/h)
- 🔋 Batería (0-100%)
- 🧭 Dirección (FORWARD/BACKWARD)
- 🏢 Estación (1-5)

✅ **Manejo de Errores**
- Timeout de conexión
- Credenciales inválidas
- Permisos insuficientes
- Comandos rechazados

✅ **Logout Seguro**
- Cierre de sesión correcto
- Invalidación de token

---

## 📖 Documentación

### Interfaz Gráfica (GUI)
- **Completa**: `README_GUI.md`
- **Rápida**: `QUICK_START_GUI.md`

### Línea de Comandos (CLI)
- **Completa**: `README.md`
- **Rápida**: `GUIA_USO_RAPIDA.md`

### General
- **Comparación**: Este archivo (`LEEME.md`)

---

## 🔐 Usuarios Disponibles

| Usuario | Contraseña | Rol | Permisos |
|---------|-----------|-----|----------|
| `admin` | `metro123` | ADMIN | ✅ Todos los comandos |
| `observer` | `metro123` | OBSERVER | 📊 Solo ver telemetría |

---

## 🌐 Servidores

### Servidor AWS (Producción)
```
Host: 3.81.235.32
Puerto: 5000
```

### Servidor Local (Testing)
```bash
# Terminal 1: Iniciar servidor
./server 5000 logs.txt

# Terminal 2: Conectar cliente
Host: localhost
Puerto: 5000
```

---

## 🛠️ Scripts Disponibles

### Compilar

**Ambos (CLI + GUI):**
```bash
./compile.sh all
```

**Solo CLI:**
```bash
./compile.sh cli
```

**Solo GUI:**
```bash
./compile.sh gui
```

### Ejecutar

**GUI:**
```bash
./run_gui.sh          # Linux/macOS
run_gui.bat           # Windows
```

**CLI:**
```bash
java MetroClient <host> <port>
```

---

## 💡 Casos de Uso

### 👨‍💼 Administrador del Sistema
**Usar: GUI**
- Interfaz visual para control directo
- Monitoreo en tiempo real con gráficos
- Ejecución rápida de comandos con botones

### 👀 Supervisor de Monitoreo
**Usar: GUI o CLI**
- GUI: Visualización clara de estado
- CLI: Monitoreo desde terminal

### 🔧 Testing y Debugging
**Usar: CLI**
- Ver mensajes del protocolo
- Automatizar pruebas con scripts
- Análisis de comportamiento

### 🖥️ Servidor Remoto (SSH)
**Usar: CLI**
- Conexión sin interfaz gráfica
- Menor consumo de recursos
- Automatización con scripts

---

## 🐞 Solución de Problemas

### Error: "Connection refused"
**Causa:** Servidor no corriendo  
**Solución:** Verificar que `./server 5000` esté activo

### Error: "Authentication failed"
**Causa:** Credenciales incorrectas  
**Solución:** Usar `admin/metro123` o `observer/metro123`

### Error: "Permission denied"
**Causa:** Usuario OBSERVER enviando comandos  
**Solución:** Autenticar como `admin`

### GUI: Botones deshabilitados
**Causa:** No autenticado  
**Solución:** Conectar y autenticar primero

### CLI: Timeout esperando respuesta
**Causa:** Servidor no responde  
**Solución:** Verificar conexión de red

---

## 📦 Empaquetado JAR

### GUI Ejecutable
```bash
# Crear JAR de GUI
echo "Main-Class: MetroClientGUI" > Manifest-GUI.txt
echo "" >> Manifest-GUI.txt
javac MetroClientGUI.java
jar cfm MetroClientGUI.jar Manifest-GUI.txt MetroClientGUI.class

# Ejecutar
java -jar MetroClientGUI.jar
```

### CLI Ejecutable
```bash
# Crear JAR de CLI
echo "Main-Class: MetroClient" > Manifest-CLI.txt
echo "" >> Manifest-CLI.txt
javac MetroClient.java
jar cfm MetroClient.jar Manifest-CLI.txt MetroClient.class

# Ejecutar
java -jar MetroClient.jar 3.81.235.32 5000
```

---

## 🔄 Flujo de Trabajo Típico

### Con GUI
```
1. ./run_gui.sh
2. [Conectar] → Host + Puerto
3. [Autenticar] → Usuario + Contraseña
4. Ver telemetría automática
5. Click en botones para comandos
6. [Desconectar] al terminar
```

### Con CLI
```
1. java MetroClient <host> <port>
2. Ingresar usuario y contraseña
3. Seleccionar opción del menú (1-6)
4. Opción 5 para telemetría
5. Opción 6 para salir
```

---

## 🎯 Recomendaciones

### Para usuarios nuevos
→ **Usar GUI** (`./run_gui.sh`)
- Más fácil de aprender
- Visual e intuitivo
- No requiere memorizar comandos

### Para usuarios avanzados
→ **Usar CLI** (`java MetroClient`)
- Más control y flexibilidad
- Automatizable con scripts
- Menor overhead

### Para servidores remotos
→ **Solo CLI disponible**
- Funciona por SSH
- Sin requerimientos gráficos

---

## 📚 Recursos Adicionales

- **Servidor C**: `server.c` en directorio raíz
- **Web UI**: `web-ui-main/` - Interfaz TypeScript/React
- **API Python**: `METROAPP_PYHTONAPI-main/`
- **Protocolo**: Ver documentación técnica del proyecto

---

## 🤝 Contribuciones

Para contribuir:
1. Fork el repositorio
2. Crea una rama: `git checkout -b feature/mejora`
3. Haz commits: `git commit -m 'Mejora X'`
4. Push: `git push origin feature/mejora`
5. Abre Pull Request

---

## 📝 Licencia

Proyecto desarrollado para el curso de **Arquitectura y Protocolos de Red 2025**.  
Sistema de Telemetría Metro Autónomo.

---

## ✨ Inicio Rápido Final

**La forma más fácil de empezar:**

```bash
# Opción 1: GUI (Recomendado para la mayoría)
./run_gui.sh

# Opción 2: CLI (Para usuarios avanzados)
./compile.sh cli
java MetroClient 3.81.235.32 5000
```

---

**🎉 ¡Disfruta controlando el Metro Autónomo!**

Para más ayuda:
- GUI: Ver `README_GUI.md` o `QUICK_START_GUI.md`
- CLI: Ver `README.md` o `GUIA_USO_RAPIDA.md`
