import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * Metro Telemetry Client - GUI Implementation
 * 
 * Interfaz gráfica para el sistema de telemetría de metro autónomo.
 * Implementa el protocolo completo con una interfaz visual moderna.
 * 
 * @author Sistema de Telemetría Metro Autónomo
 * @version 1.0
 */
public class MetroClientGUI extends JFrame {
    
    // Constantes del protocolo
    private static final int BUFFER_SIZE = 1024;
    private static final int TIMEOUT = 5000;
    
    // Variables de conexión
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String token;
    private String username;
    private boolean authenticated = false;
    private boolean running = true;
    
    // Thread para telemetría
    private ScheduledExecutorService telemetryExecutor;
    
    // Componentes de UI
    private JTextField hostField;
    private JTextField portField;
    private JTextField userField;
    private JPasswordField passField;
    private JButton connectButton;
    private JButton authButton;
    private JButton disconnectButton;
    
    // Paneles de comandos
    private JButton speedUpButton;
    private JButton slowDownButton;
    private JButton stopButton;
    private JButton startButton;
    
    // Visualización de telemetría
    private JLabel speedLabel;
    private JLabel batteryLabel;
    private JLabel directionLabel;
    private JLabel stationLabel;
    private JProgressBar batteryBar;
    private JProgressBar speedBar;
    
    // Log de mensajes
    private JTextArea logArea;
    private JLabel statusLabel;
    
    // Colores
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color ERROR_COLOR = new Color(244, 67, 54);
    private final Color WARNING_COLOR = new Color(255, 152, 0);
    private final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    
    /**
     * Constructor del GUI
     */
    public MetroClientGUI() {
        setTitle("🚄 Metro Telemetry Client - GUI");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        initComponents();
        
        // Centrar ventana
        setLocationRelativeTo(null);
    }
    
    /**
     * Inicializar componentes de la UI
     */
    private void initComponents() {
        // Panel superior - Conexión y Autenticación
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Panel central - Telemetría y Comandos
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Panel inferior - Log y Estado
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Crear panel superior (conexión y auth)
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Panel de conexión
        JPanel connectionPanel = new JPanel(new GridBagLayout());
        connectionPanel.setBorder(createTitledBorder("🔗 Conexión"));
        connectionPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Host
        gbc.gridx = 0; gbc.gridy = 0;
        connectionPanel.add(new JLabel("Host:"), gbc);
        
        hostField = new JTextField("3.81.235.32", 15);
        gbc.gridx = 1;
        connectionPanel.add(hostField, gbc);
        
        // Puerto
        gbc.gridx = 0; gbc.gridy = 1;
        connectionPanel.add(new JLabel("Puerto:"), gbc);
        
        portField = new JTextField("5000", 15);
        gbc.gridx = 1;
        connectionPanel.add(portField, gbc);
        
        // Botón conectar
        connectButton = createStyledButton("Conectar", PRIMARY_COLOR);
        connectButton.addActionListener(e -> connect());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        connectionPanel.add(connectButton, gbc);
        
        // Panel de autenticación
        JPanel authPanel = new JPanel(new GridBagLayout());
        authPanel.setBorder(createTitledBorder("🔐 Autenticación"));
        authPanel.setBackground(Color.WHITE);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Usuario
        gbc.gridx = 0; gbc.gridy = 0;
        authPanel.add(new JLabel("Usuario:"), gbc);
        
        userField = new JTextField("admin", 15);
        userField.setEnabled(false);
        gbc.gridx = 1;
        authPanel.add(userField, gbc);
        
        // Contraseña
        gbc.gridx = 0; gbc.gridy = 1;
        authPanel.add(new JLabel("Contraseña:"), gbc);
        
        passField = new JPasswordField("metro123", 15);
        passField.setEnabled(false);
        gbc.gridx = 1;
        authPanel.add(passField, gbc);
        
        // Botón autenticar
        authButton = createStyledButton("Autenticar", SUCCESS_COLOR);
        authButton.setEnabled(false);
        authButton.addActionListener(e -> authenticate());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        authPanel.add(authButton, gbc);
        
        panel.add(connectionPanel);
        panel.add(authPanel);
        
        return panel;
    }
    
    /**
     * Crear panel central (telemetría y comandos)
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Panel de telemetría
        JPanel telemetryPanel = createTelemetryPanel();
        
        // Panel de comandos
        JPanel commandPanel = createCommandPanel();
        
        panel.add(telemetryPanel);
        panel.add(commandPanel);
        
        return panel;
    }
    
    /**
     * Crear panel de telemetría
     */
    private JPanel createTelemetryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(createTitledBorder("📊 Telemetría en Tiempo Real"));
        panel.setBackground(Color.WHITE);
        
        // Velocidad
        JPanel speedPanel = new JPanel(new BorderLayout(10, 5));
        speedPanel.setBackground(Color.WHITE);
        speedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        speedLabel = new JLabel("🚄 Velocidad: -- km/h");
        speedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        speedPanel.add(speedLabel, BorderLayout.NORTH);
        speedBar = new JProgressBar(0, 100);
        speedBar.setStringPainted(true);
        speedBar.setString("0 km/h");
        speedBar.setForeground(PRIMARY_COLOR);
        speedPanel.add(speedBar, BorderLayout.CENTER);
        panel.add(speedPanel);
        
        // Batería
        JPanel batteryPanel = new JPanel(new BorderLayout(10, 5));
        batteryPanel.setBackground(Color.WHITE);
        batteryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        batteryLabel = new JLabel("🔋 Batería: -- %");
        batteryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        batteryPanel.add(batteryLabel, BorderLayout.NORTH);
        batteryBar = new JProgressBar(0, 100);
        batteryBar.setStringPainted(true);
        batteryBar.setString("0%");
        batteryBar.setForeground(SUCCESS_COLOR);
        batteryPanel.add(batteryBar, BorderLayout.CENTER);
        panel.add(batteryPanel);
        
        // Dirección
        JPanel directionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        directionPanel.setBackground(Color.WHITE);
        directionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        directionLabel = new JLabel("🧭 Dirección: --");
        directionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        directionPanel.add(directionLabel);
        panel.add(directionPanel);
        
        // Estación
        JPanel stationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stationPanel.setBackground(Color.WHITE);
        stationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        stationLabel = new JLabel("🏢 Estación: --");
        stationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        stationPanel.add(stationLabel);
        panel.add(stationPanel);
        
        return panel;
    }
    
    /**
     * Crear panel de comandos
     */
    private JPanel createCommandPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorder("🎮 Panel de Control"));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // SPEED_UP
        speedUpButton = createCommandButton("🚀 SPEED UP", "Aumentar velocidad +10 km/h");
        speedUpButton.addActionListener(e -> sendCommand("SPEED_UP"));
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(speedUpButton, gbc);
        
        // SLOW_DOWN
        slowDownButton = createCommandButton("🐌 SLOW DOWN", "Disminuir velocidad -10 km/h");
        slowDownButton.addActionListener(e -> sendCommand("SLOW_DOWN"));
        gbc.gridy = 1;
        panel.add(slowDownButton, gbc);
        
        // STOP NOW
        stopButton = createCommandButton("🛑 STOP NOW", "Detener metro inmediatamente");
        stopButton.addActionListener(e -> sendCommand("STOPNOW"));
        gbc.gridy = 2;
        panel.add(stopButton, gbc);
        
        // START NOW
        startButton = createCommandButton("▶️ START NOW", "Reanudar movimiento");
        startButton.addActionListener(e -> sendCommand("STARTNOW"));
        gbc.gridy = 3;
        panel.add(startButton, gbc);
        
        // Botón desconectar
        disconnectButton = createStyledButton("👋 Desconectar", ERROR_COLOR);
        disconnectButton.setEnabled(false);
        disconnectButton.addActionListener(e -> disconnect());
        gbc.gridy = 4;
        panel.add(disconnectButton, gbc);
        
        // Deshabilitar comandos inicialmente
        setCommandsEnabled(false);
        
        return panel;
    }
    
    /**
     * Crear panel inferior (log)
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(createTitledBorder("📝 Log de Mensajes"));
        logPanel.setBackground(Color.WHITE);
        
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        
        statusLabel = new JLabel("⚪ Desconectado");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(logPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crear botón estilizado
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Crear botón de comando
     */
    private JButton createCommandButton(String text, String tooltip) {
        JButton button = createStyledButton(text, PRIMARY_COLOR);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(200, 60));
        return button;
    }
    
    /**
     * Crear borde con título
     */
    private Border createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            PRIMARY_COLOR
        );
        return border;
    }
    
    /**
     * Conectar al servidor
     */
    private void connect() {
        String host = hostField.getText().trim();
        String portText = portField.getText().trim();
        
        if (host.isEmpty() || portText.isEmpty()) {
            showError("Host y puerto son requeridos");
            return;
        }
        
        try {
            int port = Integer.parseInt(portText);
            
            log("🔗 Conectando a " + host + ":" + port + "...");
            updateStatus("🟡 Conectando...", WARNING_COLOR);
            
            socket = new Socket(host, port);
            socket.setSoTimeout(TIMEOUT);
            
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            
            log("✅ Conectado al servidor exitosamente");
            updateStatus("🟢 Conectado", SUCCESS_COLOR);
            
            // Habilitar autenticación
            connectButton.setEnabled(false);
            hostField.setEnabled(false);
            portField.setEnabled(false);
            userField.setEnabled(true);
            passField.setEnabled(true);
            authButton.setEnabled(true);
            
        } catch (NumberFormatException e) {
            showError("Puerto inválido: " + portText);
        } catch (IOException e) {
            showError("Error conectando: " + e.getMessage());
            updateStatus("🔴 Error de conexión", ERROR_COLOR);
        }
    }
    
    /**
     * Autenticar con el servidor
     */
    private void authenticate() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        
        if (user.isEmpty() || pass.isEmpty()) {
            showError("Usuario y contraseña son requeridos");
            return;
        }
        
        try {
            // Enviar mensaje de autenticación
            String authMessage = String.format("TYPE:AUTH;USER:%s;PASS:%s", user, pass);
            sendMessage(authMessage);
            
            // Recibir respuesta (ignorar telemetría)
            String response = null;
            for (int i = 0; i < 5; i++) {
                response = receiveMessage();
                if (response != null && !response.startsWith("TYPE:TELEMETRY")) {
                    break;
                }
            }
            
            if (response != null && response.startsWith("TYPE:AUTH_OK")) {
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
                    username = user;
                    
                    log("✅ Autenticación exitosa");
                    log("🔑 Token: " + token);
                    log("👤 Usuario: " + username);
                    updateStatus("🟢 Autenticado como " + username, SUCCESS_COLOR);
                    
                    // Habilitar comandos
                    authButton.setEnabled(false);
                    userField.setEnabled(false);
                    passField.setEnabled(false);
                    setCommandsEnabled(true);
                    disconnectButton.setEnabled(true);
                    
                    // Iniciar listener de telemetría
                    startTelemetryListener();
                }
            } else if (response != null && response.startsWith("TYPE:ERR")) {
                showError("Autenticación fallida");
                updateStatus("🔴 Autenticación fallida", ERROR_COLOR);
            }
            
        } catch (Exception e) {
            showError("Error en autenticación: " + e.getMessage());
        }
    }
    
    /**
     * Enviar comando al servidor
     */
    private void sendCommand(String action) {
        if (!authenticated) {
            showError("Debe autenticarse primero");
            return;
        }
        
        try {
            String cmdMessage = String.format("TYPE:CMD;TOKEN:%s;ACTION:%s", token, action);
            sendMessage(cmdMessage);
            
            // Recibir respuesta
            String response = null;
            for (int i = 0; i < 5; i++) {
                response = receiveMessage();
                if (response != null && !response.startsWith("TYPE:TELEMETRY")) {
                    break;
                }
            }
            
            if (response != null && response.startsWith("TYPE:ACK")) {
                log("✅ Comando " + action + " ejecutado exitosamente");
            } else if (response != null && response.startsWith("TYPE:ERR")) {
                String reason = extractReason(response);
                showError("Error en " + action + ": " + reason);
            }
            
        } catch (Exception e) {
            showError("Error enviando comando: " + e.getMessage());
        }
    }
    
    /**
     * Desconectar del servidor
     */
    private void disconnect() {
        try {
            running = false;
            
            if (telemetryExecutor != null) {
                telemetryExecutor.shutdown();
            }
            
            if (authenticated && token != null) {
                String logoutMessage = String.format("TYPE:LOGOUT;TOKEN:%s", token);
                sendMessage(logoutMessage);
                receiveMessage();
                log("✅ Sesión cerrada");
            }
            
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            
            log("🔌 Desconectado del servidor");
            updateStatus("⚪ Desconectado", Color.GRAY);
            
            // Reset UI
            resetUI();
            
        } catch (IOException e) {
            showError("Error desconectando: " + e.getMessage());
        }
    }
    
    /**
     * Iniciar listener de telemetría
     */
    private void startTelemetryListener() {
        telemetryExecutor = Executors.newSingleThreadScheduledExecutor();
        telemetryExecutor.scheduleAtFixedRate(() -> {
            try {
                if (input.ready()) {
                    String message = input.readLine();
                    if (message != null && message.startsWith("TYPE:TELEMETRY")) {
                        SwingUtilities.invokeLater(() -> updateTelemetry(message));
                    }
                }
            } catch (IOException e) {
                if (running) {
                    SwingUtilities.invokeLater(() -> 
                        showError("Error en telemetría: " + e.getMessage())
                    );
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Actualizar visualización de telemetría
     */
    private void updateTelemetry(String message) {
        try {
            String[] parts = message.split(";");
            
            String speed = "0";
            String battery = "0";
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
            
            // Actualizar UI
            double speedValue = Double.parseDouble(speed);
            int batteryValue = Integer.parseInt(battery);
            
            speedLabel.setText(String.format("🚄 Velocidad: %.1f km/h", speedValue));
            speedBar.setValue((int) speedValue);
            speedBar.setString(String.format("%.0f km/h", speedValue));
            
            batteryLabel.setText(String.format("🔋 Batería: %d%%", batteryValue));
            batteryBar.setValue(batteryValue);
            batteryBar.setString(batteryValue + "%");
            
            // Color de batería
            if (batteryValue < 20) {
                batteryBar.setForeground(ERROR_COLOR);
            } else if (batteryValue < 50) {
                batteryBar.setForeground(WARNING_COLOR);
            } else {
                batteryBar.setForeground(SUCCESS_COLOR);
            }
            
            directionLabel.setText("🧭 Dirección: " + direction);
            stationLabel.setText("🏢 Estación: " + station);
            
        } catch (Exception e) {
            log("⚠️ Error parseando telemetría: " + e.getMessage());
        }
    }
    
    /**
     * Enviar mensaje al servidor
     */
    private void sendMessage(String message) {
        output.println(message);
        log("📤 " + message);
    }
    
    /**
     * Recibir mensaje del servidor
     */
    private String receiveMessage() {
        try {
            String message = input.readLine();
            if (message != null) {
                log("📥 " + message);
            }
            return message;
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Extraer razón de error
     */
    private String extractReason(String errorMessage) {
        String[] parts = errorMessage.split(";");
        for (String part : parts) {
            if (part.startsWith("REASON:")) {
                return part.substring(7).trim();
            }
        }
        return "Error desconocido";
    }
    
    /**
     * Habilitar/deshabilitar comandos
     */
    private void setCommandsEnabled(boolean enabled) {
        speedUpButton.setEnabled(enabled);
        slowDownButton.setEnabled(enabled);
        stopButton.setEnabled(enabled);
        startButton.setEnabled(enabled);
    }
    
    /**
     * Resetear UI
     */
    private void resetUI() {
        authenticated = false;
        token = null;
        running = true;
        
        connectButton.setEnabled(true);
        hostField.setEnabled(true);
        portField.setEnabled(true);
        userField.setEnabled(false);
        passField.setEnabled(false);
        authButton.setEnabled(false);
        disconnectButton.setEnabled(false);
        setCommandsEnabled(false);
        
        speedLabel.setText("🚄 Velocidad: -- km/h");
        batteryLabel.setText("🔋 Batería: -- %");
        directionLabel.setText("🧭 Dirección: --");
        stationLabel.setText("🏢 Estación: --");
        speedBar.setValue(0);
        batteryBar.setValue(0);
    }
    
    /**
     * Mostrar error
     */
    private void showError(String message) {
        log("❌ " + message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Actualizar estado
     */
    private void updateStatus(String status, Color color) {
        statusLabel.setText(status);
        statusLabel.setForeground(color);
    }
    
    /**
     * Log de mensajes
     */
    private void log(String message) {
        String timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("HH:mm:ss")
        );
        logArea.append("[" + timestamp + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    /**
     * Punto de entrada principal
     */
    public static void main(String[] args) {
        // Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear y mostrar GUI
        SwingUtilities.invokeLater(() -> {
            MetroClientGUI gui = new MetroClientGUI();
            gui.setVisible(true);
            gui.log("🚄 Metro Telemetry Client GUI iniciado");
            gui.log("📝 Conecte al servidor para comenzar");
        });
    }
}
