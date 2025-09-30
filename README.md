# 🚄 Metro Telemetry Client - Java CLI

Cliente de línea de comandos en Java para el Sistema de Telemetría de Metro Autónomo.

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Stable-green.svg)]()

## 📋 Descripción

Cliente Java CLI que se conecta **directamente al servidor C** mediante sockets TCP e implementa el protocolo completo de telemetría del metro autónomo. Permite autenticación, envío de comandos de control y visualización de telemetría en tiempo real.

**Características principales:**
- ✅ Conexión TCP directa al servidor C
- ✅ Autenticación basada en tokens
- ✅ Control completo del metro (SPEED_UP, SLOW_DOWN, STOP, START)
- ✅ Telemetría en tiempo real con threading
- ✅ Interfaz CLI interactiva y amigable
- ✅ Manejo robusto de errores
- ✅ Sin dependencias externas (solo JDK)

## 🔧 Requisitos

- **Java JDK 8 o superior**

Verificar instalación:
```bash
java -version
```

## 📦 Instalación

### Opción 1: Clonar el repositorio
```bash
git clone https://github.com/TU_USUARIO/metro-java-client.git
cd metro-java-client
```

### Opción 2: Descargar archivo
Descarga `MetroClient.java` directamente desde [Releases](https://github.com/TU_USUARIO/metro-java-client/releases)

## 🚀 Compilación

```bash
javac MetroClient.java
```

Esto genera `MetroClient.class`

## 💻 Uso

### Sintaxis básica:
```bash
java MetroClient <host> <port>
```

### Ejemplos:

**Conectar a servidor AWS:**
```bash
java MetroClient 3.81.235.32 5000
```

**Conectar a servidor local:**
```bash
java MetroClient localhost 5000
```

**Conectar a IP específica:**
```bash
java MetroClient 192.168.1.100 5000
```

### Usando variables de entorno:
```bash
export METRO_SERVER_HOST=3.81.235.32
export METRO_SERVER_PORT=5000
java MetroClient
```

## 🔐 Autenticación

Al ejecutar el cliente, solicita credenciales:

```
==================================================
           AUTENTICACIÓN
==================================================
Usuario: admin
Contraseña: metro123
```

**Usuarios disponibles:**
- `admin` / `metro123` → Rol ADMIN (todos los permisos)
- `observer` / `metro123` → Rol OBSERVER (solo lectura)

## 🎮 Comandos Disponibles

Una vez autenticado, verás el menú principal:

```
==================================================
    METRO TELEMETRY CLIENT - MENÚ PRINCIPAL
==================================================
1. 🚀 SPEED_UP    - Incrementar velocidad +10 km/h
2. 🐌 SLOW_DOWN   - Decrementar velocidad -10 km/h
3. 🛑 STOPNOW     - Detener metro inmediatamente
4. ▶️  STARTNOW    - Reanudar movimiento
5. 📡 Escuchar telemetría en tiempo real
6. 👋 Salir
==================================================

Seleccione una opción: _
```

### 1. SPEED_UP - Incrementar Velocidad
```
Opción: 1
```
- Aumenta velocidad +10 km/h
- Máximo: 100 km/h
- Requiere batería > 10%
- Solo usuarios ADMIN

### 2. SLOW_DOWN - Decrementar Velocidad
```
Opción: 2
```
- Disminuye velocidad -10 km/h
- Mínimo: 0 km/h
- Solo usuarios ADMIN

### 3. STOPNOW - Detener Metro
```
Opción: 3
```
- Detiene el metro inmediatamente (velocidad = 0)
- Solo usuarios ADMIN
- Requiere batería > 10%

### 4. STARTNOW - Reanudar Movimiento
```
Opción: 4
```
- Reanuda el movimiento del metro
- Solo funciona si fue detenido con STOPNOW
- Solo usuarios ADMIN

### 5. Ver Telemetría en Tiempo Real
```
Opción: 5
```
- Muestra telemetría cada 10 segundos
- Formato visualizado con barras de progreso
- Presionar Enter para volver al menú

Ejemplo de salida:
```
[14:23:45] 📊 TELEMETRÍA
  🚄 Velocidad: 60.0 km/h
  🔋 Batería:   85% [████████░░]
  🧭 Dirección: FORWARD
  🏢 Estación:  3
--------------------------------------------------
```

### 6. Salir
```
Opción: 6
```
- Cierra sesión (logout)
- Desconecta del servidor
- Termina el cliente

## 📡 Protocolo Implementado

### Mensajes de Autenticación
```
Cliente → Servidor: TYPE:AUTH;USER:admin;PASS:metro123
Servidor → Cliente: TYPE:AUTH_OK;TOKEN:abc123xyz;ROLE:ADMIN
```

### Mensajes de Comando
```
Cliente → Servidor: TYPE:CMD;TOKEN:abc123xyz;ACTION:SPEED_UP
Servidor → Cliente: TYPE:ACK;ACTION:SPEED_UP;RESULT:OK
```

### Mensajes de Telemetría
```
Servidor → Cliente: TYPE:TELEMETRY;SPEED:50.0;BATTERY:85;DIRECTION:FORWARD;STATION:3
```

### Mensajes de Error
```
Servidor → Cliente: TYPE:ERR;ACTION:SPEED_UP;REASON:Speed limit reached
```

### Logout
```
Cliente → Servidor: TYPE:LOGOUT;TOKEN:abc123xyz
Servidor → Cliente: TYPE:LOGOUT_OK
```

## 📊 Ejemplo de Uso Completo

```bash
$ javac MetroClient.java
$ java MetroClient 3.81.235.32 5000

╔════════════════════════════════════════════════════════════╗
║          METRO TELEMETRY CLIENT - Java CLI                ║
║          Sistema de Control de Metro Autónomo             ║
╚════════════════════════════════════════════════════════════╝

🔗 Conectando a 3.81.235.32:5000...
✅ Conectado al servidor Metro exitosamente!

==================================================
           AUTENTICACIÓN
==================================================
Usuario: admin
Contraseña: metro123

📤 Enviado: TYPE:AUTH;USER:admin;PASS:metro123
📥 Recibido: TYPE:AUTH_OK;TOKEN:KXhdIY2BVN1IhJaN

✅ Autenticación exitosa!
🔑 Token: KXhdIY2BVN1IhJaN
👤 Usuario: admin

==================================================
    METRO TELEMETRY CLIENT - MENÚ PRINCIPAL
==================================================
1. 🚀 SPEED_UP    - Incrementar velocidad +10 km/h
2. 🐌 SLOW_DOWN   - Decrementar velocidad -10 km/h
3. 🛑 STOPNOW     - Detener metro inmediatamente
4. ▶️  STARTNOW    - Reanudar movimiento
5. 📡 Escuchar telemetría en tiempo real
6. 👋 Salir
==================================================

Seleccione una opción: 1

📤 Enviado: TYPE:CMD;TOKEN:KXhdIY2BVN1IhJaN;ACTION:SPEED_UP
📥 Recibido: TYPE:ACK;ACTION:SPEED_UP;RESULT:OK
✅ Comando SPEED_UP ejecutado exitosamente

Seleccione una opción: 5

📡 Modo telemetría activo
   Presione Enter para volver al menú...

📡 Iniciando escucha de telemetría...

[14:07:47] 📊 TELEMETRÍA
  🚄 Velocidad: 70.0 km/h
  🔋 Batería:   88% [████████░░]
  🧭 Dirección: FORWARD
  🏢 Estación:  1
--------------------------------------------------

[Enter presionado]

⏸️  Telemetría detenida

Seleccione una opción: 6

👋 Cerrando sesión...
📤 Enviado: TYPE:LOGOUT;TOKEN:KXhdIY2BVN1IhJaN
📥 Recibido: TYPE:LOGOUT_OK
✅ Sesión cerrada correctamente

🔌 Conexión cerrada

✅ Cliente finalizado correctamente
```

## 🏗️ Arquitectura del Código

### Clase Principal: `MetroClient`

```java
public class MetroClient {
    // Métodos públicos
    boolean connect(String host, int port)     // Conectar al servidor
    boolean authenticate()                     // Autenticación
    boolean sendCommand(String action)         // Enviar comandos
    void startTelemetryListener()              // Iniciar thread telemetría
    void stopTelemetryListener()               // Detener thread telemetría
    void logout()                              // Cerrar sesión
    void close()                               // Cerrar conexión
    void run()                                 // Bucle principal
}
```

### Características Técnicas

| Característica | Implementación |
|---------------|----------------|
| **Socket** | `java.net.Socket` |
| **I/O** | `BufferedReader` + `PrintWriter` |
| **Threading** | `java.lang.Thread` |
| **Timeout** | 5000ms configurable |
| **Buffer** | 1024 bytes |
| **Encoding** | UTF-8 |
| **Timestamp** | `java.time.LocalDateTime` |

## 🛡️ Manejo de Errores

El cliente maneja automáticamente:

- ✅ Error de conexión (host/puerto inválidos)
- ✅ Timeout de respuesta (5 segundos)
- ✅ Credenciales inválidas
- ✅ Token expirado o inválido
- ✅ Permisos insuficientes
- ✅ Comandos rechazados (batería baja, límites)
- ✅ Desconexión abrupta del servidor
- ✅ Mensajes malformados
- ✅ Telemetría asíncrona (ignora broadcasts durante auth/comandos)

## 🐞 Troubleshooting

### Error: "Connection refused"
**Causa:** Servidor no está ejecutándose  
**Solución:** Verificar que el servidor C esté corriendo en el puerto especificado

### Error: "Operation timed out"
**Causa:** IP/puerto incorrectos o servidor no accesible  
**Solución:** Verificar IP, puerto y conectividad de red

### Error: "Authentication failed"
**Causa:** Credenciales incorrectas  
**Solución:** Usar `admin/metro123` o `observer/metro123`

### Error: "Permission denied"
**Causa:** Usuario OBSERVER intenta enviar comandos  
**Solución:** Autenticarse como admin

### Comando rechazado (TYPE:ERR)
**Posibles razones:**
- Batería baja (<10%)
- Velocidad en límite máximo (100 km/h)
- Metro ya detenido (STOPNOW)
- Metro no detenido por comando (STARTNOW)

## 📦 Empaquetado como JAR (Opcional)

### 1. Crear archivo `Manifest.txt`:
```
Main-Class: MetroClient

```
*(Nota: debe haber una línea vacía al final)*

### 2. Compilar y empaquetar:
```bash
javac MetroClient.java
jar cfm MetroClient.jar Manifest.txt MetroClient.class
```

### 3. Ejecutar JAR:
```bash
java -jar MetroClient.jar 3.81.235.32 5000
```

## 🔒 Seguridad

- **Autenticación:** Basada en tokens temporales
- **Roles:** ADMIN (control total) / OBSERVER (solo lectura)
- **Timeout:** Evita conexiones colgadas
- **Validación:** Todos los comandos son validados por el servidor
- **Logging:** Todas las acciones son registradas en el servidor

## 🎓 Casos de Uso

### Administrador de Sistema
```bash
# Conectar y aumentar velocidad
java MetroClient 3.81.235.32 5000
# Usuario: admin / Contraseña: metro123
# Opción: 1 (SPEED_UP)
```

### Operador de Monitoreo
```bash
# Conectar y ver telemetría
java MetroClient 3.81.235.32 5000
# Usuario: observer / Contraseña: metro123
# Opción: 5 (Ver telemetría)
```

### Testing/Debugging
```bash
# Probar en servidor local
java MetroClient localhost 5001
```

## 📚 Recursos Adicionales

- **Servidor C:** [metro-server](https://github.com/TU_USUARIO/metro-server)
- **Documentación del Protocolo:** Ver `informe_tecnico.pdf`
- **Web UI (TypeScript):** [metro-web-ui](https://github.com/TU_USUARIO/metro-web-ui)

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto es parte del Sistema de Telemetría Metro Autónomo.  
Desarrollado para el curso de Arquitectura y Protocolos de Red 2025.

## 👤 Autor

**Sistema de Telemetría Metro Autónomo**  
Curso: Arquitectura y Protocolos de Red  
Año: 2025

## 🙏 Agradecimientos

- Profesor del curso
- Comunidad Java
- Contribuidores del proyecto

---

**⭐ Si este proyecto te fue útil, dale una estrella en GitHub!**