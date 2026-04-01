import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class leo extends JFrame {

    private JTextField buscador;
    private JButton botonBuscar;

    public leo() {
        // Configuración básica del JFrame
        setUndecorated(true);
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ====================== BARRA DE TÍTULO PERSONALIZADA =======
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(30, 30, 30));
        titleBar.setPreferredSize(new Dimension(0, 40));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        JButton btnMinimize = createTitleButton("−", new Color(255, 180, 0));
        JButton btnMaximize = createTitleButton("□", new Color(0, 200, 80));
        JButton btnClose = createTitleButton("×", new Color(220, 50, 50));

        buttonsPanel.add(btnMinimize);
        buttonsPanel.add(btnMaximize);
        buttonsPanel.add(btnClose);
        titleBar.add(buttonsPanel, BorderLayout.EAST);
        add(titleBar, BorderLayout.NORTH);

        // ====================== BARRA DE MENÚ ======================
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // ====================== PANEL PRINCIPAL ======================
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // Campo de texto
        buscador = new JTextField();
        buscador.setBounds(224, 251, 297, 25);
        mainPanel.add(buscador);

        // Botón Ir
        botonBuscar = new JButton("Ir");
        botonBuscar.setBounds(528, 251, 94, 23);
        mainPanel.add(botonBuscar);

        // Listener del botón "Ir"
        botonBuscar.addActionListener(e -> procesarURL());

        // Listener para habilitar/deshabilitar el botón según el texto
        buscador.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarBoton(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarBoton(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarBoton(); }
        });

        add(mainPanel, BorderLayout.CENTER);

        // Acciones botones barra de título
        btnMinimize.addActionListener(e -> setState(JFrame.ICONIFIED));
        btnMaximize.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        btnClose.addActionListener(e -> System.exit(0));

        // Estado inicial del botón
        actualizarBoton();
    }

    // ====================== PROCESAR URL LOCAL ======================
    private void procesarURL() {
        String texto = buscador.getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        try {
            // Validar que sea una URL file:///
            URI uri = new URI(texto);
            if (!"file".equalsIgnoreCase(uri.getScheme())) {
                JOptionPane.showMessageDialog(this, 
                    "La URL debe ser un archivo local (debe comenzar con file:///)", 
                    "URL inválida", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convertir URI a File
            File archivo = new File(uri);

            if (archivo.exists()) {
                // El archivo existe → intentar abrirlo
                Desktop.getDesktop().open(archivo);
            } else {
                // El archivo NO existe
                JOptionPane.showMessageDialog(this, 
                    "El archivo no existe:\n" + archivo.getAbsolutePath(), 
                    "Archivo no encontrado", 
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "URL inválida o error al procesar el archivo:\n" + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====================== HABILITAR/DESABILITAR BOTÓN ======================
    private void actualizarBoton() {
        botonBuscar.setEnabled(!buscador.getText().trim().isEmpty());
    }

    private JButton createTitleButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(45, 40));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new leo().setVisible(true);
        });
    }
}