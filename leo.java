import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class leo extends JFrame {
    
    private JTabbedPane sistemaPestanas;
    private JLabel etiquetaEstado;
    private int contadorPestanas = 1;

    public leo() {
        initializeFrame();
        
        // --- BARRA DE TÍTULO PRINCIPAL ---
        add(createTitleBar(), BorderLayout.NORTH);
        
        // --- SISTEMA DE PESTAÑAS ---
        sistemaPestanas = new JTabbedPane();
        sistemaPestanas.setBackground(new Color(235, 235, 235));
        hacerRedimensionable(sistemaPestanas); 
        add(sistemaPestanas, BorderLayout.CENTER);
        
        // --- BARRA DE ESTADO GLOBAL ---
        add(createStatusBar(), BorderLayout.SOUTH);
        
        // 1. CREAMOS LA "PESTAÑA FALSA" (CON EL "+" GRANDE Y ELEGANTE)
        sistemaPestanas.addTab("", new JPanel()); 
        
        JLabel btnSumar = new JLabel("+"); 
        btnSumar.setFont(new Font("Arial", Font.PLAIN, 24)); 
        btnSumar.setForeground(new Color(100, 100, 100));
        btnSumar.setHorizontalAlignment(SwingConstants.CENTER);
        btnSumar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSumar.setPreferredSize(new Dimension(35, 20));

        // LA SOLUCIÓN: Agregamos "mousePressed" directamente al "+" para que detecte el clic
        btnSumar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnSumar.setForeground(new Color(46, 204, 113)); }
            public void mouseExited(MouseEvent e) { btnSumar.setForeground(new Color(100, 100, 100)); }
            public void mousePressed(MouseEvent e) { abrirNuevaPestana(); } // <--- ¡AQUÍ ESTÁ LA MAGIA!
        });

        sistemaPestanas.setTabComponentAt(0, btnSumar); 

        // Por si hacen clic en el borde de la pestaña y no exactamente en el símbolo "+"
        sistemaPestanas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int indiceClic = sistemaPestanas.indexAtLocation(e.getX(), e.getY());
                if (indiceClic == sistemaPestanas.getTabCount() - 1) {
                    abrirNuevaPestana();
                }
            }
        });
        
        abrirNuevaPestana();
    }

    private void initializeFrame() {
        setUndecorated(true);
        setSize(1000, 700); 
        setMinimumSize(new Dimension(500, 400)); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // =========================================================================
    // BARRA DE TÍTULO (CON LOS ICONOS ANTIGUOS RESTAURADOS)
    // =========================================================================
    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        Color colorBarra = new Color(220, 220, 220); 
        titleBar.setBackground(colorBarra); 
        titleBar.setPreferredSize(new Dimension(0, 40));
        moverVentana(titleBar);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        // Iconos originales
        JButton btnmin = CreadorBotones("−", colorBarra);
        JButton btnmax = CreadorBotones("□", colorBarra);
        JButton btncerrar = CreadorBotones("×", colorBarra);

        aplicarEfectoHover(btnmin, new Color(200, 200, 200), new Color(80, 80, 80)); 
        aplicarEfectoHover(btnmax, new Color(200, 200, 200), new Color(80, 80, 80));
        aplicarEfectoHover(btncerrar, new Color(232, 17, 35), Color.WHITE); 

        btnmin.addActionListener(e -> setState(JFrame.ICONIFIED)); 
        btnmax.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setSize(1000, 700);
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

    // =========================================================================
    // LÓGICA DE PESTAÑAS Y CABECERAS
    // =========================================================================
    private void abrirNuevaPestana() {
        JPanel panelContenido = crearPanelBuscador();
        String titulo = "Buscador " + contadorPestanas++;
        
        int posicion = sistemaPestanas.getTabCount() - 1;
        if(posicion < 0) posicion = 0;

        sistemaPestanas.insertTab(null, null, panelContenido, null, posicion);
        sistemaPestanas.setTabComponentAt(posicion, crearCabeceraPestana(titulo, panelContenido));
        
        sistemaPestanas.setSelectedIndex(posicion);
    }

    private JPanel crearCabeceraPestana(String titulo, JPanel panelContenido) {
        JPanel cabecera = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cabecera.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8)); 
        
        JLabel btnCerrar = new JLabel(" × ");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.setForeground(new Color(150, 150, 150));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCerrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnCerrar.setForeground(Color.RED); }
            public void mouseExited(MouseEvent e) { btnCerrar.setForeground(new Color(150, 150, 150)); }
            public void mousePressed(MouseEvent e) {
                int i = sistemaPestanas.indexOfComponent(panelContenido);
                if (i != -1) {
                    sistemaPestanas.remove(i);
                }
            }
        });

        cabecera.add(lblTitulo);
        cabecera.add(btnCerrar);
        
        return cabecera;
    }

    // =========================================================================
    // CREACIÓN DEL PANEL INDEPENDIENTE (El interior de cada pestaña)
    // =========================================================================
    private JPanel crearPanelBuscador() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245)); 
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        JTextField localBuscador = new JTextField(25); 
        localBuscador.setBackground(Color.WHITE);
        localBuscador.setForeground(new Color(60, 60, 60));
        localBuscador.setCaretColor(new Color(100, 100, 100));
        localBuscador.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10) 
        ));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0; 
        panel.add(localBuscador, gbc);

        JButton localBoton = new JButton("Ir"); 
        localBoton.setFont(new Font("Arial", Font.BOLD, 13));
        localBoton.setFocusPainted(false);
        localBoton.setBorderPainted(false);
        localBoton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        localBoton.setBackground(new Color(180, 180, 180)); 
        localBoton.setForeground(Color.WHITE); 
        localBoton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        localBoton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if(!localBuscador.getText().trim().isEmpty()) {
                    localBoton.setBackground(new Color(41, 128, 185)); 
                }
            }
            public void mouseExited(MouseEvent evt) {
                if(!localBuscador.getText().trim().isEmpty()) {
                    localBoton.setBackground(new Color(52, 73, 94)); 
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0; 
        panel.add(localBoton, gbc);

        localBoton.addActionListener(e -> {
            if(!localBuscador.getText().trim().isEmpty()){
                procesarURLLocal(localBuscador.getText());
            }
        });
        
        localBuscador.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarBotonLocal(localBuscador, localBoton); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarBotonLocal(localBuscador, localBoton); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarBotonLocal(localBuscador, localBoton); }
        });

        return panel;
    }

    private void actualizarBotonLocal(JTextField buscador, JButton boton) {
        boolean tieneTexto = !buscador.getText().trim().isEmpty();
        if(!tieneTexto) {
            boton.setBackground(new Color(180, 180, 180)); 
            boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
        } else {
            boton.setBackground(new Color(52, 73, 94)); 
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        }
    }

    private void procesarURLLocal(String texto) {
        etiquetaEstado.setText("Cargando...");
        etiquetaEstado.setForeground(new Color(41, 128, 185)); 

        javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
            try {
                URI uri = new URI(texto.trim());
                if (!"file".equalsIgnoreCase(uri.getScheme())) {
                    JOptionPane.showMessageDialog(this, "Debe comenzar con file:///", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    File archivo = new File(uri);
                    if (archivo.exists()) {
                        Desktop.getDesktop().open(archivo);
                    } else {
                        JOptionPane.showMessageDialog(this, "El archivo no existe", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "URL inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
            etiquetaEstado.setText("Listo");
            etiquetaEstado.setForeground(new Color(100, 100, 100)); 
        });
        timer.setRepeats(false); 
        timer.start();
    }

    // =========================================================================
    // UTILIDADES UI 
    // =========================================================================
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        statusBar.setBackground(new Color(220, 220, 220)); 
        etiquetaEstado = new JLabel("Listo");
        etiquetaEstado.setForeground(new Color(100, 100, 100)); 
        etiquetaEstado.setFont(new Font("Arial", Font.BOLD, 12));
        statusBar.add(etiquetaEstado);
        return statusBar;
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

    private void hacerRedimensionable(JComponent panel) {
        MouseAdapter resizer = new MouseAdapter() {
            boolean resizing = false;
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getX() >= panel.getWidth() - 15 && e.getY() >= panel.getHeight() - 15) resizing = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) { resizing = false; }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing) {
                    int newWidth = Math.max(400, e.getXOnScreen() - getX());
                    int newHeight = Math.max(300, e.getYOnScreen() - getY());
                    setSize(newWidth, newHeight);
                    revalidate();
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
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
                setLocation(e.getXOnScreen()-mouseX[0], e.getYOnScreen()-mouseY[0]);
            }
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new leo().setVisible(true);
        });
    }
}

class Renderizador extends JPanel {
    private JTextPane areaContenido;
    private NavegacionListener listener;

    public interface NavegacionListener {
        void navegar(String urlDestino);
    }

    public Renderizador(NavegacionListener listener) {
        this.listener = listener;
        setLayout(new BorderLayout());

        areaContenido = new JTextPane();
        areaContenido.setEditable(false); 
        areaContenido.setContentType("text/html");
        
        areaContenido.setBackground(Color.WHITE);
        areaContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        areaContenido.addHyperlinkListener(e -> manejarEventosEnlace(e));

        JScrollPane scroll = new JScrollPane(areaContenido);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    public void cargarArchivo(File archivo) {
        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    sb.append(linea).append("\n");
                }
            }
            
            String contenidoHtml = sb.toString();

            String htmlMinusculas = contenidoHtml.toLowerCase();
            if (!htmlMinusculas.contains("<html>") || !htmlMinusculas.contains("<body>")) {
                areaContenido.setContentType("text/plain");
                areaContenido.setText("ERROR: El archivo no es un documento HTML válido.\nDebe contener las etiquetas <html> y <body>.");
                areaContenido.setForeground(Color.RED);
                return;
            }

            contenidoHtml = contenidoHtml.replace("\n", "<br>");

            areaContenido.setContentType("text/html");
            areaContenido.setText(contenidoHtml);
            areaContenido.setForeground(Color.BLACK); 

        } catch (Exception ex) {
            areaContenido.setContentType("text/plain");
            areaContenido.setText("Error crítico al leer el archivo: " + ex.getMessage());
        }
    }

    private void manejarEventosEnlace(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (listener != null) {
                listener.navegar(e.getDescription());
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            areaContenido.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cambiarColorEnlace(e.getSourceElement(), new Color(46, 204, 113)); 
        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
            areaContenido.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            cambiarColorEnlace(e.getSourceElement(), Color.BLUE); 
        }
    }

    private void cambiarColorEnlace(Element elementoHtml, Color color) {
        if (elementoHtml != null && areaContenido.getDocument() instanceof StyledDocument) {
            StyledDocument doc = (StyledDocument) areaContenido.getDocument();
            SimpleAttributeSet atributos = new SimpleAttributeSet();
            StyleConstants.setForeground(atributos, color);
            
            int inicio = elementoHtml.getStartOffset();
            int longitud = elementoHtml.getEndOffset() - inicio;
            doc.setCharacterAttributes(inicio, longitud, atributos, false);
        }
    }
}