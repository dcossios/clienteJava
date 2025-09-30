# 🚀 GUÍA DE USO RÁPIDA - Cliente Java Metro

## ⚡ INICIO RÁPIDO (3 MINUTOS)

### Paso 1: Compilar (30 segundos)
```bash
javac MetroClient.java
```

### Paso 2: Ejecutar (30 segundos)
```bash
java MetroClient 3.81.235.32 5000
```

### Paso 3: Autenticarse (1 minuto)
```
Usuario: admin
Contraseña: metro123
```

### Paso 4: ¡Usar! (1 minuto)
```
Opción: 1  → SPEED_UP
Opción: 5  → Ver telemetría
Opción: 6  → Salir
```

---

## 📋 MENÚ DE COMANDOS

```
1. 🚀 SPEED_UP     → Aumentar velocidad +10 km/h
2. 🐌 SLOW_DOWN    → Disminuir velocidad -10 km/h
3. 🛑 STOPNOW      → Detener metro (velocidad = 0)
4. ▶️  STARTNOW     → Reanudar movimiento
5. 📡 Telemetría   → Ver datos en tiempo real
6. 👋 Salir        → Cerrar sesión
```

---

## 🎮 CASOS DE USO COMUNES

### Aumentar la Velocidad
```
Menu → Opción: 1
✅ Velocidad aumenta +10 km/h
```

### Ver Estado del Metro
```
Menu → Opción: 5
📊 Muestra velocidad, batería, dirección, estación
[Presionar Enter para volver]
```

### Detener el Metro
```
Menu → Opción: 3
✅ Metro se detiene inmediatamente
```

### Reanudar Movimiento
```
Menu → Opción: 4
✅ Metro reanuda (solo si fue detenido con STOPNOW)
```

---

## 🔐 USUARIOS Y PERMISOS

| Usuario | Contraseña | Rol | Permisos |
|---------|------------|-----|----------|
| `admin` | `metro123` | ADMIN | ✅ Todos los comandos |
| `observer` | `metro123` | OBSERVER | ❌ Solo ver telemetría |

---

## 📊 TELEMETRÍA - QUÉ SIGNIFICA

```
[14:23:45] 📊 TELEMETRÍA
  🚄 Velocidad: 60.0 km/h     ← Velocidad actual
  🔋 Batería:   85% [████]    ← Nivel de batería
  🧭 Dirección: FORWARD        ← FORWARD o BACKWARD
  🏢 Estación:  3              ← Estación actual (1-5)
```

### Interpretación:

- **Velocidad:** 0-100 km/h
- **Batería:** 0-100%, ⚠️ si <10% no permite comandos
- **Dirección:** 
  - `FORWARD` → Estación 1 → 2 → 3 → 4 → 5
  - `BACKWARD` → Estación 5 → 4 → 3 → 2 → 1
- **Estación:** 1 a 5, cambia dirección en 1 y 5

---

## ⚠️ ERRORES COMUNES Y SOLUCIONES

### ❌ "Connection refused"
**Problema:** Servidor no está corriendo  
**Solución:**
```bash
# Verificar servidor
telnet 3.81.235.32 5000
# Si falla, el servidor está apagado
```

### ❌ "Authentication failed"
**Problema:** Usuario/contraseña incorrectos  
**Solución:** Usar `admin/metro123` o `observer/metro123`

### ❌ "Permission denied"
**Problema:** Usuario OBSERVER intenta comandos  
**Solución:** Autenticarse como `admin`

### ❌ "Speed limit reached"
**Problema:** Ya estás a 100 km/h  
**Solución:** Usar SLOW_DOWN primero

### ❌ "Battery too low"
**Problema:** Batería < 10%  
**Solución:** Esperar a que se recargue

### ❌ "Already at minimum speed"
**Problema:** Ya estás a 0 km/h  
**Solución:** Usar SPEED_UP

### ❌ "Metro not stopped by command"
**Problema:** Intentas STARTNOW pero metro no fue detenido con STOPNOW  
**Solución:** Solo funciona si usaste STOPNOW antes

---

## 🌐 SERVIDORES DISPONIBLES

### Servidor AWS (Producción)
```bash
java MetroClient 3.81.235.32 5000
```

### Servidor Local (Testing)
```bash
# Terminal 1: Iniciar servidor
./server 5000 logs.txt

# Terminal 2: Conectar cliente
java MetroClient localhost 5000
```

---

## 💡 TIPS Y TRUCOS

### Tip 1: Ver Mensajes del Protocolo
```
📤 Enviado: TYPE:CMD;...    ← Lo que envías
📥 Recibido: TYPE:ACK;...   ← Lo que recibes
```
Útil para debugging!

### Tip 2: Usar Variables de Entorno
```bash
export METRO_SERVER_HOST=3.81.235.32
export METRO_SERVER_PORT=5000
java MetroClient
```

### Tip 3: Telemetría en Background
Opción 5 corre en thread separado, puedes seguir usando comandos.

### Tip 4: Logout Limpio
Siempre usa opción 6 para cerrar sesión correctamente.

---

## 🎯 FLUJO TÍPICO DE USO

```
1. Compilar → javac MetroClient.java
2. Ejecutar → java MetroClient <host> <port>
3. Autenticar → admin / metro123
4. Ver estado → Opción 5 (telemetría)
5. Controlar → Opción 1, 2, 3, o 4
6. Salir → Opción 6
```

---

## 📱 EJEMPLOS VISUALES

### Ejemplo 1: Aumentar Velocidad
```
Opción: 1
📤 Enviado: TYPE:CMD;TOKEN:abc123;ACTION:SPEED_UP
📥 Recibido: TYPE:ACK;ACTION:SPEED_UP;RESULT:OK
✅ Comando SPEED_UP ejecutado exitosamente
```

### Ejemplo 2: Ver Telemetría
```
Opción: 5

[14:07:47] 📊 TELEMETRÍA
  🚄 Velocidad: 70.0 km/h
  🔋 Batería:   88% [████████░░]
  🧭 Dirección: FORWARD
  🏢 Estación:  1
--------------------------------------------------

[Presionar Enter]
⏸️  Telemetría detenida
```

### Ejemplo 3: Error Manejado
```
Opción: 2
📤 Enviado: TYPE:CMD;TOKEN:abc123;ACTION:SLOW_DOWN
📥 Recibido: TYPE:ERR;ACTION:SLOW_DOWN;REASON:Already at minimum speed
❌ Error ejecutando comando SLOW_DOWN
   Razón: Already at minimum speed
```

---

## 🔄 CICLO DE VIDA DE UNA SESIÓN

```
1. Conexión    → Socket TCP establecido
2. Auth        → Usuario/contraseña → Token
3. Comandos    → Envío con token válido
4. Telemetría  → Recepción cada 10s
5. Logout      → Invalidar token
6. Desconexión → Cerrar socket
```

---

## 📝 CHECKLIST ANTES DE USAR

- [ ] Java instalado (`java -version`)
- [ ] Cliente compilado (`MetroClient.class` existe)
- [ ] Servidor corriendo (probar con `telnet`)
- [ ] Credenciales a mano (`admin/metro123`)
- [ ] Entender comandos básicos
- [ ] Saber cómo salir (opción 6)

---

## 🆘 AYUDA RÁPIDA

### ¿Cómo compilar?
```bash
javac MetroClient.java
```

### ¿Cómo ejecutar?
```bash
java MetroClient <host> <port>
```

### ¿Qué usuarios hay?
- `admin/metro123` (control total)
- `observer/metro123` (solo lectura)

### ¿Cómo salir?
```
Opción: 6
```

### ¿Cómo ver telemetría?
```
Opción: 5
```

### ¿Cómo aumentar velocidad?
```
Opción: 1
```

---

## 📞 CONTACTO Y SOPORTE

- **README completo:** Ver `README.md`
- **Protocolo:** Ver documentación técnica
- **Issues:** GitHub Issues
- **Email:** [tu@email.com]

---

**¡Listo para usar el cliente Java!** 🎉

**Comando más común:**
```bash
java MetroClient 3.81.235.32 5000
# Usuario: admin
# Contraseña: metro123
```
