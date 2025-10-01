# ⚡ INICIO RÁPIDO - Interfaz Gráfica

## 🎯 En 3 pasos (1 minuto)

### Paso 1: Compilar y Ejecutar (30 segundos)

**Linux/macOS:**
```bash
./run_gui.sh
```

**Windows:**
```batch
run_gui.bat
```

**Manual:**
```bash
javac MetroClientGUI.java
java MetroClientGUI
```

### Paso 2: Conectar (15 segundos)

En la ventana que se abre:

1. **Panel de Conexión** (arriba izquierda):
   ```
   Host: 3.81.235.32
   Puerto: 5000
   ```
   Click en → **[Conectar]**

2. Espera el mensaje: 🟢 **Conectado**

### Paso 3: Autenticar (15 segundos)

3. **Panel de Autenticación** (arriba derecha):
   ```
   Usuario: admin
   Contraseña: metro123
   ```
   Click en → **[Autenticar]**

4. Espera el mensaje: 🟢 **Autenticado como admin**

---

## 🎮 ¡Listo para usar!

Ahora puedes:

### 📊 Ver Telemetría (Automático)
Se actualiza cada segundo:
- 🚄 **Velocidad**: km/h con barra de progreso
- 🔋 **Batería**: % con barra de colores
- 🧭 **Dirección**: FORWARD/BACKWARD
- 🏢 **Estación**: 1-5

### 🎮 Controlar el Metro
Click en los botones:

| Botón | Acción |
|-------|--------|
| **🚀 SPEED UP** | +10 km/h |
| **🐌 SLOW DOWN** | -10 km/h |
| **🛑 STOP NOW** | Detener |
| **▶️ START NOW** | Reanudar |

### 📝 Ver Log
Todas las operaciones se registran con timestamp en el panel inferior.

---

## 🖼️ Distribución de la Interfaz

```
┌─────────────────────────────────────────────────┐
│  🔗 Conexión          │  🔐 Autenticación        │
│  Host: [        ]     │  Usuario: [       ]      │
│  Puerto: [      ]     │  Contraseña: [    ]      │
│  [Conectar]           │  [Autenticar]            │
├─────────────────────────────────────────────────┤
│  📊 Telemetría        │  🎮 Panel de Control     │
│                       │                          │
│  🚄 Velocidad: 60 km/h│  [🚀 SPEED UP]           │
│  ████████░░ 80%       │  [🐌 SLOW DOWN]          │
│                       │  [🛑 STOP NOW]           │
│  🔋 Batería: 85%      │  [▶️ START NOW]          │
│  ████████░░ 85%       │                          │
│                       │  [👋 Desconectar]        │
│  🧭 Dirección: FORWARD│                          │
│  🏢 Estación: 3       │                          │
├─────────────────────────────────────────────────┤
│  Estado: 🟢 Autenticado como admin               │
├─────────────────────────────────────────────────┤
│  📝 Log de Mensajes                              │
│  [14:23:45] 📤 TYPE:CMD;TOKEN:...;ACTION:SPEED_UP│
│  [14:23:45] 📥 TYPE:ACK;ACTION:SPEED_UP          │
│  [14:23:45] ✅ Comando SPEED_UP ejecutado        │
└─────────────────────────────────────────────────┘
```

---

## 🚨 Solución de Problemas Rápida

### ❌ "Connection refused"
→ Servidor no está corriendo. Verifica el servidor C.

### ❌ "Authentication failed"
→ Usa `admin`/`metro123` o `observer`/`metro123`

### ❌ Comando rechazado
→ Revisa el log para ver la razón (batería baja, límite de velocidad, etc.)

### ⚠️ Telemetría no se actualiza
→ Verifica que estés autenticado (debe decir 🟢)

---

## 💡 Tips Rápidos

1. **Colores de Batería**:
   - 🟢 Verde: > 50% (OK)
   - 🟠 Naranja: 20-50% (Atención)
   - 🔴 Rojo: < 20% (Crítico)

2. **Estado de Conexión**:
   - ⚪ Desconectado
   - 🟡 Conectando
   - 🟢 Conectado/Autenticado
   - 🔴 Error

3. **Siempre desconecta correctamente**:
   - Usa el botón **[👋 Desconectar]**
   - No cierres la ventana directamente

4. **Múltiples usuarios**:
   - `admin`: Puede enviar comandos
   - `observer`: Solo puede ver telemetría

---

## 📁 Archivos del Proyecto

```
java_cli/
├── MetroClientGUI.java      ← Código fuente de la GUI
├── MetroClient.java          ← Código fuente del CLI
├── README_GUI.md             ← Documentación completa GUI
├── README.md                 ← Documentación completa CLI
├── QUICK_START_GUI.md        ← Este archivo
├── GUIA_USO_RAPIDA.md        ← Guía rápida CLI
├── run_gui.sh                ← Script Linux/macOS
├── run_gui.bat               ← Script Windows
├── compile.sh                ← Compilador universal
└── .gitignore                ← Archivos ignorados
```

---

## 🔄 Opciones Alternativas

### Compilar manualmente
```bash
javac MetroClientGUI.java
```

### Ejecutar manualmente
```bash
java MetroClientGUI
```

### Compilar ambos (CLI y GUI)
```bash
./compile.sh all
```

### Usar solo el CLI
```bash
javac MetroClient.java
java MetroClient 3.81.235.32 5000
```

---

## 📚 Más Información

- **Documentación completa GUI**: Ver `README_GUI.md`
- **Documentación CLI**: Ver `README.md`
- **Protocolo de comunicación**: Ver documentación técnica
- **Servidor**: Ver `server.c`

---

## ✅ Checklist de Verificación

Antes de comenzar, verifica:

- [ ] Java instalado: `java -version`
- [ ] Compilador disponible: `javac -version`
- [ ] Archivos descargados: `MetroClientGUI.java`
- [ ] Servidor corriendo (opcional para testing)

---

## 🎉 ¡Listo!

Ahora tienes una interfaz gráfica completa para controlar el Metro.

**Comando más rápido:**
```bash
./run_gui.sh
```

**¡Disfruta la experiencia visual del Metro Telemetry Client! 🚄**
