import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class leo extends JFrame {
    public String url1 = "";

    private JTextField buscador;
    private JButton botonBuscar;

    public leo() {
        initializeFrame();
        
        // --- BARRA DE TÍTULO ---
        add(createTitleBar(), BorderLayout.NORTH);
        
        // --- PANEL PRINCIPAL ---
        add(createMainPanel(), BorderLayout.CENTER);
        
        actualizarBoton();
    }

    private void initializeFrame() {
        setUndecorated(true);
        setSize(820, 620); 
        
        // TAMAÑO MÍNIMO SOLICITADO
        setMinimumSize(new Dimension(400, 300)); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // =========================================================================
    // BARRA DE TÍTULO
    // =========================================================================
    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        
        Color colorBarra = new Color(220, 220, 220); 
        titleBar.setBackground(colorBarra); 
        titleBar.setPreferredSize(new Dimension(0, 40));

        moverVentana(titleBar);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        JButton btnmin = CreadorBotones("−", colorBarra);
        JButton btnmax = CreadorBotones("□", colorBarra);
        JButton btncerrar = CreadorBotones("×", colorBarra);

        aplicarEfectoHover(btnmin, new Color(200, 200, 200), new Color(80, 80, 80)); 
        aplicarEfectoHover(btnmax, new Color(200, 200, 200), new Color(80, 80, 80));
        aplicarEfectoHover(btncerrar, new Color(232, 17, 35), Color.WHITE); 

        btnmin.addActionListener(e -> setState(JFrame.ICONIFIED)); 
        
        btnmax.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setSize(820, 620);
                setLocationRelativeTo(null); 
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        
        btncerrar.addActionListener(e -> System.exit(0));

        buttonsPanel.add(btnmin);
        buttonsPanel.add(btnmax);
        buttonsPanel.add(btncerrar);

        titleBar.add(buttonsPanel, BorderLayout.EAST);
        return titleBar;
    }

    private void aplicarEfectoHover(JButton boton, Color hoverBg, Color hoverFg) {
        Color bgColorOriginal = boton.getBackground();
        Color fgColorOriginal = boton.getForeground();

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                boton.setBackground(hoverBg);
                boton.setForeground(hoverFg);
            }
            public void mouseExited(MouseEvent evt) {
                boton.setBackground(bgColorOriginal);
                boton.setForeground(fgColorOriginal);
            }
        });
    }

    private JButton CreadorBotones(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(new Color(80, 80, 80)); 
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(45, 40));
        return btn;
    }

    // =========================================================================
    // PANEL PRINCIPAL Y COMPONENTES
    // =========================================================================
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        
        mainPanel.setBackground(new Color(235, 235, 235));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(12, 12, 12, 12), 
            BorderFactory.createLineBorder(new Color(190, 190, 190), 2)
        ));

        // APLICAMOS LA FUNCIÓN DE REDIMENSIONAMIENTO CORREGIDA AQUÍ
        hacerRedimensionable(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        buscador = new JTextField(25); 
        buscador.setBackground(Color.WHITE);
        buscador.setForeground(new Color(60, 60, 60));
        buscador.setCaretColor(new Color(100, 100, 100));
        buscador.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10) 
        ));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0; 
        mainPanel.add(buscador, gbc);

        botonBuscar = new JButton("Ir"); 
        botonBuscar.setFont(new Font("Arial", Font.BOLD, 13));
        botonBuscar.setFocusPainted(false);
        botonBuscar.setBorderPainted(false);
        botonBuscar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        botonBuscar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if(!buscador.getText().trim().isEmpty()) {
                    botonBuscar.setBackground(new Color(41, 128, 185)); 
                }
            }
            public void mouseExited(MouseEvent evt) {
                if(!buscador.getText().trim().isEmpty()) {
                    botonBuscar.setBackground(new Color(52, 73, 94)); 
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0; 
        mainPanel.add(botonBuscar, gbc);

        botonBuscar.addActionListener(e -> {
            if(!buscador.getText().trim().isEmpty()){
                procesarURL();
            }
        });
        
        buscador.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarBoton(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarBoton(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarBoton(); }
        });

        return mainPanel;
    }

    // =========================================================================
    // REDIMENSIONAR VENTANA DESDE LA ESQUINA (VERSIÓN FLUIDA)
    // =========================================================================
    private void hacerRedimensionable(JPanel panel) {
        MouseAdapter resizer = new MouseAdapter() {
            boolean resizing = false;

            @Override
            public void mousePressed(MouseEvent e) {
                // Si hace clic en la esquina inferior derecha (área de 15x15 píxeles)
                if (e.getX() >= panel.getWidth() - 15 && e.getY() >= panel.getHeight() - 15) {
                    resizing = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                resizing = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing) {
                    // Calculamos el tamaño midiendo la distancia del ratón a la esquina superior de la ventana
                    int newWidth = e.getXOnScreen() - getX();
                    int newHeight = e.getYOnScreen() - getY();

                    // Aplicamos el límite mínimo (400x300) sin bloquear la posición del ratón
                    newWidth = Math.max(400, newWidth);
                    newHeight = Math.max(300, newHeight);

                    setSize(newWidth, newHeight);
                    revalidate();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Cambiar el cursor a flechas de redimensión al acercarse a la esquina
                if (e.getX() >= panel.getWidth() - 15 && e.getY() >= panel.getHeight() - 15) {
                    panel.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                } else {
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };

        panel.addMouseListener(resizer);
        panel.addMouseMotionListener(resizer);
    }

    // ====================== LÓGICA DE LA APLICACIÓN ======================
    private void procesarURL() {
        String texto = buscador.getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        try {
            URI uri = new URI(texto);
            if (!"file".equalsIgnoreCase(uri.getScheme())) {
                JOptionPane.showMessageDialog(this, 
                    "La URL debe ser un archivo local (debe comenzar con file:///)", 
                    "URL inválida", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            File archivo = new File(uri);

            if (archivo.exists()) {
                Desktop.getDesktop().open(archivo);
            } else {
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

    private void actualizarBoton() {
        boolean tieneTexto = !buscador.getText().trim().isEmpty();
        
        if(!tieneTexto) {
            botonBuscar.setBackground(new Color(180, 180, 180)); 
            botonBuscar.setForeground(Color.WHITE); 
            botonBuscar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
        } else {
            botonBuscar.setBackground(new Color(52, 73, 94)); 
            botonBuscar.setForeground(Color.WHITE); 
            botonBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        }
    }

    private void moverVentana(JPanel titleBar) {
        final int[] mouseX= new int[1];
        final int[] mouseY= new int[1];

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX[0]= e.getX();
                mouseY[0]= e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int newX= e.getXOnScreen()-mouseX[0];
                int newY= e.getYOnScreen()-mouseY[0];
                setLocation(newX, newY);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new leo().setVisible(true);
        });
    }
}