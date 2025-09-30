import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Metro Telemetry Client - Java Implementation
 * 
 * Cliente para el sistema de telemetría de metro autónomo.
 * Se conecta directamente al servidor C mediante sockets TCP.
 * 
 * Implementa el protocolo completo:
 * - Autenticación (TYPE:AUTH)
 * - Comandos (TYPE:CMD)
 * - Telemetría (TYPE:TELEMETRY)
 * - Logout (TYPE:LOGOUT)
 * 
 * @author Sistema de Telemetría Metro Autónomo
 * @version 1.0
 */
public class MetroClient {
    
    // Constantes del protocolo
    private static final int BUFFER_SIZE = 1024;
    private static final int TIMEOUT = 5000; // 5 segundos
    
    // Variables de conexión
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Scanner scanner;
    
    // Variables de sesión
    private String token;
    private String username;
    private boolean authenticated;
    private boolean running;
    
    // Thread para telemetría
    private Thread telemetryThread;
    
    /**
     * Constructor del cliente Metro
     */
    public MetroClient() {
        this.scanner = new Scanner(System.in);
        this.authenticated = false;
        this.running = true;
    }
    
    /**
     * Conectar al servidor Metro
     * @param host Dirección IP o hostname del servidor
     * @param port Puerto del servidor
     * @return true si la conexión fue exitosa
     */
    public boolean connect(String host, int port) {
        try {
            System.out.println("\n🔗 Conectando a " + host + ":" + port + "...");
            
            socket = new Socket(host, port);
            socket.setSoTimeout(TIMEOUT);
            
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println("✅ Conectado al servidor Metro exitosamente!");
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error conectando al servidor: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Enviar mensaje al servidor
     * @param message Mensaje a enviar
     */
    private void sendMessage(String message) {
        output.println(message);
        System.out.println("📤 Enviado: " + message);
    }
    
    /**
     * Recibir mensaje del servidor
     * @return Mensaje recibido o null si hay error
     */
    private String receiveMessage() {
        try {
            String message = input.readLine();
            if (message != null) {
                System.out.println("📥 Recibido: " + message);
            }
            return message;
        } catch (SocketTimeoutException e) {
            System.err.println("⏱️  Timeout esperando respuesta del servidor");
            return null;
        } catch (IOException e) {
            System.err.println("❌ Error recibiendo mensaje: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Autenticación con el servidor
     * @return true si la autenticación fue exitosa
     */
    public boolean authenticate() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           AUTENTICACIÓN");
        System.out.println("=".repeat(50));
        
        System.out.print("Usuario: ");
        username = scanner.nextLine().trim();
        
        System.out.print("Contraseña: ");
        String password = scanner.nextLine().trim();
        
        // Enviar mensaje de autenticación
        String authMessage = String.format("TYPE:AUTH;USER:%s;PASS:%s", username, password);
        sendMessage(authMessage);
        
        // Recibir respuesta (ignorar telemetría si llega antes)
        int maxAttempts = 5;
        String response = null;
        
        for (int i = 0; i < maxAttempts; i++) {
            response = receiveMessage();
            
            if (response == null) {
                return false;
            }
            
            // Si es telemetría, ignorarla y seguir esperando
            if (response.startsWith("TYPE:TELEMETRY")) {
                System.out.println("⏭️  Ignorando telemetría, esperando respuesta de autenticación...");
                continue;
            }
            
            // Si es respuesta de autenticación, procesarla
            if (response.startsWith("TYPE:AUTH_OK") || response.startsWith("TYPE:ERR")) {
                break;
            }
        }
        
        if (response == null) {
            System.err.println("❌ No se recibió respuesta de autenticación");
            return false;
        }
        
        // Validar respuesta
        if (response.startsWith("TYPE:AUTH_OK")) {
            // Extraer token
            String[] parts = response.split(";");
            for (String part : parts) {
                if (part.startsWith("TOKEN:")) {
                    token = part.substring(6).trim();
                    break;
                }
            }
            
            if (token != null && !token.isEmpty()) {
                authenticated = true;
                System.out.println("\n✅ Autenticación exitosa!");
                System.out.println("🔑 Token: " + token);
                System.out.println("👤 Usuario: " + username);
                return true;
            }
        } else if (response.startsWith("TYPE:ERR")) {
            System.err.println("❌ Autenticación fallida");
            String reason = extractReason(response);
            if (reason != null) {
                System.err.println("   Razón: " + reason);
            }
        }
        
        return false;
    }
    
    /**
     * Enviar comando al servidor
     * @param action Acción a ejecutar (SPEED_UP, SLOW_DOWN, STOPNOW, STARTNOW)
     * @return true si el comando fue exitoso
     */
    public boolean sendCommand(String action) {
        if (!authenticated) {
            System.err.println("❌ Debe autenticarse primero");
            return false;
        }
        
        // Enviar comando
        String cmdMessage = String.format("TYPE:CMD;TOKEN:%s;ACTION:%s", token, action);
        sendMessage(cmdMessage);
        
        // Recibir respuesta (ignorar telemetría si llega antes)
        int maxAttempts = 5;
        String response = null;
        
        for (int i = 0; i < maxAttempts; i++) {
            response = receiveMessage();
            
            if (response == null) {
                return false;
            }
            
            // Si es telemetría, ignorarla y seguir esperando
            if (response.startsWith("TYPE:TELEMETRY")) {
                System.out.println("⏭️  Ignorando telemetría, esperando respuesta de comando...");
                continue;
            }
            
            // Si es respuesta de comando, procesarla
            if (response.startsWith("TYPE:ACK") || response.startsWith("TYPE:ERR")) {
                break;
            }
        }
        
        if (response == null) {
            System.err.println("❌ No se recibió respuesta del comando");
            return false;
        }
        
        // Validar respuesta
        if (response.startsWith("TYPE:ACK")) {
            System.out.println("✅ Comando " + action + " ejecutado exitosamente");
            return true;
        } else if (response.startsWith("TYPE:ERR")) {
            System.err.println("❌ Error ejecutando comando " + action);
            String reason = extractReason(response);
            if (reason != null) {
                System.err.println("   Razón: " + reason);
            }
            return false;
        }
        
        return false;
    }
    
    /**
     * Extraer razón de un mensaje de error
     * @param errorMessage Mensaje de error
     * @return Razón del error
     */
    private String extractReason(String errorMessage) {
        String[] parts = errorMessage.split(";");
        for (String part : parts) {
            if (part.startsWith("REASON:")) {
                return part.substring(7).trim();
            }
        }
        return null;
    }
    
    /**
     * Parsear y mostrar mensaje de telemetría
     * @param message Mensaje de telemetría
     */
    private void parseTelemetry(String message) {
        try {
            // Parsear: TYPE:TELEMETRY;SPEED:50.0;BATTERY:85;DIRECTION:FORWARD;STATION:3
            String[] parts = message.split(";");
            
            String speed = "N/A";
            String battery = "N/A";
            String direction = "N/A";
            String station = "N/A";
            
            for (String part : parts) {
                if (part.startsWith("SPEED:")) {
                    speed = part.substring(6).trim();
                } else if (part.startsWith("BATTERY:")) {
                    battery = part.substring(8).trim();
                } else if (part.startsWith("DIRECTION:")) {
                    direction = part.substring(10).trim();
                } else if (part.startsWith("STATION:")) {
                    station = part.substring(8).trim();
                }
            }
            
            // Crear barra de batería visual
            int batteryInt = Integer.parseInt(battery);
            int bars = batteryInt / 10;
            String batteryBar = "█".repeat(bars) + "░".repeat(10 - bars);
            
            // Mostrar telemetría formateada
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
            );
            
            System.out.println("\n[" + timestamp + "] 📊 TELEMETRÍA");
            System.out.println("  🚄 Velocidad: " + speed + " km/h");
            System.out.println("  🔋 Batería:   " + battery + "% [" + batteryBar + "]");
            System.out.println("  🧭 Dirección: " + direction);
            System.out.println("  🏢 Estación:  " + station);
            System.out.println("-".repeat(50));
            
        } catch (Exception e) {
            System.err.println("❌ Error parseando telemetría: " + e.getMessage());
        }
    }
    
    /**
     * Escuchar telemetría en tiempo real (thread separado)
     */
    public void startTelemetryListener() {
        telemetryThread = new Thread(() -> {
            System.out.println("\n📡 Iniciando escucha de telemetría...");
            System.out.println("   (Presione Enter para volver al menú)\n");
            
            try {
                socket.setSoTimeout(1000); // 1 segundo timeout para permitir cancelación
                
                while (running) {
                    try {
                        String message = input.readLine();
                        if (message != null && message.startsWith("TYPE:TELEMETRY")) {
                            parseTelemetry(message);
                        }
                    } catch (SocketTimeoutException e) {
                        // Timeout normal, continuar
                        continue;
                    }
                }
                
                // Restaurar timeout original
                socket.setSoTimeout(TIMEOUT);
                
            } catch (IOException e) {
                if (running) {
                    System.err.println("❌ Error en telemetría: " + e.getMessage());
                }
            }
        });
        
        telemetryThread.start();
    }
    
    /**
     * Detener escucha de telemetría
     */
    public void stopTelemetryListener() {
        if (telemetryThread != null && telemetryThread.isAlive()) {
            running = false;
            try {
                telemetryThread.join(2000); // Esperar máximo 2 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            running = true;
        }
    }
    
    /**
     * Cerrar sesión (logout)
     */
    public void logout() {
        if (authenticated && token != null) {
            String logoutMessage = String.format("TYPE:LOGOUT;TOKEN:%s", token);
            sendMessage(logoutMessage);
            
            String response = receiveMessage();
            if (response != null) {
                System.out.println("✅ Sesión cerrada correctamente");
            }
        }
        
        authenticated = false;
        token = null;
    }
    
    /**
     * Cerrar conexión
     */
    public void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            if (scanner != null) scanner.close();
            
            System.out.println("\n🔌 Conexión cerrada");
            
        } catch (IOException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
    
    /**
     * Mostrar menú principal
     */
    private void showMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    METRO TELEMETRY CLIENT - MENÚ PRINCIPAL");
        System.out.println("=".repeat(50));
        System.out.println("1. 🚀 SPEED_UP    - Incrementar velocidad +10 km/h");
        System.out.println("2. 🐌 SLOW_DOWN   - Decrementar velocidad -10 km/h");
        System.out.println("3. 🛑 STOPNOW     - Detener metro inmediatamente");
        System.out.println("4. ▶️  STARTNOW    - Reanudar movimiento");
        System.out.println("5. 📡 Escuchar telemetría en tiempo real");
        System.out.println("6. 👋 Salir");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Ejecutar bucle principal del cliente
     */
    public void run() {
        if (!authenticated) {
            System.err.println("❌ No autenticado. Cerrando...");
            return;
        }
        
        while (running) {
            showMenu();
            System.out.print("\nSeleccione una opción: ");
            String option = scanner.nextLine().trim();
            
            switch (option) {
                case "1":
                    sendCommand("SPEED_UP");
                    break;
                    
                case "2":
                    sendCommand("SLOW_DOWN");
                    break;
                    
                case "3":
                    sendCommand("STOPNOW");
                    break;
                    
                case "4":
                    sendCommand("STARTNOW");
                    break;
                    
                case "5":
                    System.out.println("\n📡 Modo telemetría activo");
                    System.out.println("   Presione Enter para volver al menú...\n");
                    
                    startTelemetryListener();
                    
                    // Esperar a que el usuario presione Enter
                    scanner.nextLine();
                    
                    stopTelemetryListener();
                    System.out.println("\n⏸️  Telemetría detenida");
                    break;
                    
                case "6":
                    System.out.println("\n👋 Cerrando sesión...");
                    running = false;
                    break;
                    
                default:
                    System.err.println("❌ Opción inválida. Intente nuevamente.");
            }
        }
        
        logout();
    }
    
    /**
     * Mostrar banner de bienvenida
     */
    private static void showBanner() {
        System.out.println("\n" + "╔" + "═".repeat(60) + "╗");
        System.out.println("║" + " ".repeat(10) + "METRO TELEMETRY CLIENT - Java CLI" + " ".repeat(16) + "║");
        System.out.println("║" + " ".repeat(10) + "Sistema de Control de Metro Autónomo" + " ".repeat(12) + "║");
        System.out.println("╚" + "═".repeat(60) + "╝");
    }
    
    /**
     * Mostrar información de uso
     */
    private static void showUsage() {
        System.err.println("\n❌ Uso: java MetroClient [<host> <port>]");
        System.err.println("\nOpciones:");
        System.err.println("  1. Con argumentos:");
        System.err.println("     java MetroClient <host> <port>");
        System.err.println("\n  2. Con variables de entorno:");
        System.err.println("     export METRO_SERVER_HOST=3.81.235.32");
        System.err.println("     export METRO_SERVER_PORT=5000");
        System.err.println("     java MetroClient");
        System.err.println("\nEjemplos:");
        System.err.println("  java MetroClient 3.81.235.32 5000    # Servidor AWS");
        System.err.println("  java MetroClient localhost 5000       # Servidor local");
    }
    
    /**
     * Punto de entrada principal
     * @param args Argumentos de línea de comandos: [host] [port] (opcionales si hay variables de entorno)
     */
    public static void main(String[] args) {
        showBanner();
        
        String host = null;
        int port = 0;
        
        // Intentar leer de argumentos de línea de comandos
        if (args.length == 2) {
            host = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("❌ Puerto inválido: " + args[1]);
                System.exit(1);
            }
        }
        // Si no hay argumentos, intentar leer variables de entorno
        else if (args.length == 0) {
            host = System.getenv("METRO_SERVER_HOST");
            String portEnv = System.getenv("METRO_SERVER_PORT");
            
            if (host != null && portEnv != null) {
                try {
                    port = Integer.parseInt(portEnv);
                    System.out.println("🔧 Usando variables de entorno:");
                    System.out.println("   METRO_SERVER_HOST=" + host);
                    System.out.println("   METRO_SERVER_PORT=" + port);
                } catch (NumberFormatException e) {
                    System.err.println("❌ Variable METRO_SERVER_PORT inválida: " + portEnv);
                    System.exit(1);
                }
            } else {
                System.err.println("\n❌ No se encontraron argumentos ni variables de entorno");
                showUsage();
                System.exit(1);
            }
        }
        // Número incorrecto de argumentos
        else {
            showUsage();
            System.exit(1);
        }
        
        // Crear y ejecutar cliente
        MetroClient client = new MetroClient();
        
        try {
            // Conectar al servidor
            if (!client.connect(host, port)) {
                System.exit(1);
            }
            
            // Autenticar
            if (!client.authenticate()) {
                System.err.println("\n❌ Autenticación fallida. Cerrando...");
                client.close();
                System.exit(1);
            }
            
            // Ejecutar bucle principal
            client.run();
            
        } catch (Exception e) {
            System.err.println("\n❌ Error fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
        
        System.out.println("\n✅ Cliente finalizado correctamente");
    }
}
