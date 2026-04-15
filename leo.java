import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class leo extends JFrame {
    
    private JTabbedPane sistemaPestanas;
    private JLabel etiquetaEstado;
    private int contadorPestanas = 1;
    
    // Variables globales para guardar el tema elegido
    private Color fondoActual = Color.WHITE;
    private Color textoActual = Color.BLACK;

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

        btnSumar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnSumar.setForeground(new Color(46, 204, 113)); }
            public void mouseExited(MouseEvent e) { btnSumar.setForeground(new Color(100, 100, 100)); }
            public void mousePressed(MouseEvent e) { abrirNuevaPestana(); } 
        });

        sistemaPestanas.setTabComponentAt(0, btnSumar); 
        
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
    // BARRA DE TÍTULO CON MENÚ DE TEMAS (⚙)
    // =========================================================================
    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        Color colorBarra = new Color(220, 220, 220); 
        titleBar.setBackground(colorBarra); 
        titleBar.setPreferredSize(new Dimension(0, 40));
        moverVentana(titleBar);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        // NUEVO: Botón de Menú de Temas
        JButton btnTema = CreadorBotones("⚙", colorBarra);
        JButton btnmin = CreadorBotones("−", colorBarra);
        JButton btnmax = CreadorBotones("□", colorBarra);
        JButton btncerrar = CreadorBotones("×", colorBarra);

        aplicarEfectoHover(btnTema, new Color(200, 200, 200), new Color(80, 80, 80)); 
        aplicarEfectoHover(btnmin, new Color(200, 200, 200), new Color(80, 80, 80)); 
        aplicarEfectoHover(btnmax, new Color(200, 200, 200), new Color(80, 80, 80));
        aplicarEfectoHover(btncerrar, new Color(232, 17, 35), Color.WHITE); 

        // Creación del Menú Desplegable Simple para los colores
        JPopupMenu menuTemas = new JPopupMenu();
        JMenuItem temaClaro = new JMenuItem("Tema Claro (Defecto)");
        JMenuItem temaOscuro = new JMenuItem("Tema Oscuro");
        JMenuItem temaHacker = new JMenuItem("Tema Hacker");

        // Lógica de colores (Fondo, Texto)
        temaClaro.addActionListener(e -> cambiarTemaVisual(Color.WHITE, Color.BLACK));
        temaOscuro.addActionListener(e -> cambiarTemaVisual(new Color(40, 40, 40), new Color(220, 220, 220)));
        temaHacker.addActionListener(e -> cambiarTemaVisual(Color.BLACK, new Color(0, 255, 0)));

        menuTemas.add(temaClaro);
        menuTemas.add(temaOscuro);
        menuTemas.add(temaHacker);

        // Al hacer clic en el botón del engranaje, mostramos el menú debajo
        btnTema.addActionListener(e -> menuTemas.show(btnTema, 0, btnTema.getHeight()));

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

        buttonsPanel.add(btnTema); // Añadimos el botón del tema
        buttonsPanel.add(btnmin);
        buttonsPanel.add(btnmax);
        buttonsPanel.add(btncerrar);

        titleBar.add(buttonsPanel, BorderLayout.EAST);
        return titleBar;
    }

    // =========================================================================
    // LÓGICA DE APLICACIÓN DE TEMA VISUAL A TODAS LAS PESTAÑAS
    // =========================================================================
    private void cambiarTemaVisual(Color fondo, Color texto) {
        this.fondoActual = fondo;
        this.textoActual = texto;
        
        // Recorremos todas las pestañas para aplicar el color al Renderizador
        for (int i = 0; i < sistemaPestanas.getTabCount() - 1; i++) {
            Component c = sistemaPestanas.getComponentAt(i);
            if (c instanceof JPanel) {
                JPanel panelPrincipal = (JPanel) c;
                BorderLayout layout = (BorderLayout) panelPrincipal.getLayout();
                Component center = layout.getLayoutComponent(BorderLayout.CENTER);
                
                // Si el centro es nuestro Renderizador, le aplicamos el tema
                if (center instanceof Renderizador) {
                    ((Renderizador) center).aplicarTemaVisual(fondo, texto);
                }
            }
        }
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
    // CREACIÓN DEL PANEL INDEPENDIENTE 
    // =========================================================================
    private JPanel crearPanelBuscador() {
        JPanel panelPrincipal = new JPanel(new BorderLayout()); 

        JPanel panelTop = new JPanel(new GridBagLayout());
        panelTop.setBackground(new Color(245, 245, 245)); 
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();

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
        panelTop.add(localBuscador, gbc);

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
        
        panelTop.add(localBoton, gbc);
        panelPrincipal.add(panelTop, BorderLayout.NORTH); 

        // Creamos la instancia del renderizador y aplicamos el tema de inmediato
        Renderizador renderizador = new Renderizador();
        renderizador.aplicarTemaVisual(fondoActual, textoActual);
        
        renderizador.setNavegacionListener(nuevaRuta -> {
            localBuscador.setText(nuevaRuta); 
            procesarURLLocal(nuevaRuta, renderizador); 
        });
        
        panelPrincipal.add(renderizador, BorderLayout.CENTER);

       localBoton.addActionListener(e -> {
            if(!localBuscador.getText().trim().isEmpty()){
                procesarURLLocal(localBuscador.getText(), renderizador);
            }
        });
        
        localBuscador.addActionListener(e -> {
            if(!localBuscador.getText().trim().isEmpty()){
                procesarURLLocal(localBuscador.getText(), renderizador);
            }
        });

        localBuscador.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarBotonLocal(localBuscador, localBoton); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarBotonLocal(localBuscador, localBoton); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarBotonLocal(localBuscador, localBoton); }
        });

        return panelPrincipal;
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

    private void procesarURLLocal(String texto, Renderizador renderizador) {
        etiquetaEstado.setText("Cargando...");
        etiquetaEstado.setForeground(new Color(41, 128, 185)); 

        javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
            try {
                String rutaLimpia = texto.trim().replace("file:///", "").replace("file://", "");
                File archivo = new File(rutaLimpia);
                
                if (archivo.exists() && archivo.isFile()) {
                    renderizador.cargarArchivo(archivo); 
                } else {
                    JOptionPane.showMessageDialog(this, "El archivo local no existe:\n" + archivo.getAbsolutePath(), "Error 404", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ruta inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
            etiquetaEstado.setText("Listo");
            etiquetaEstado.setForeground(new Color(100, 100, 100)); 
        });
        timer.setRepeats(false); 
        timer.start();
    }

    // =========================================================================
    // UTILIDADES BOTONES Y PANELES
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

// =============================================================================
// CLASE RENDERIZADOR (ACTUALIZADA PARA SOPORTAR TEMAS VISUALES)
// =============================================================================
class Renderizador extends JPanel {
    private JTextPane areaContenido;
    private NavegacionListener listener;

    public interface NavegacionListener {
        void navegar(String urlDestino);
    }

    public Renderizador() {
        setLayout(new BorderLayout());

        areaContenido = new JTextPane();
        areaContenido.setEditable(false); 
        areaContenido.setContentType("text/html");
        
        // TRUCO: Esto obliga al JTextPane HTML a obedecer nuestros colores de Java
        areaContenido.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        areaContenido.setFont(new Font("Arial", Font.PLAIN, 14));
        
        areaContenido.setBackground(Color.WHITE);
        areaContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        areaContenido.addHyperlinkListener(e -> manejarEventosEnlace(e));

        JScrollPane scroll = new JScrollPane(areaContenido);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    // MÉTODO NUEVO PARA APLICAR EL COLOR
    public void aplicarTemaVisual(Color fondo, Color texto) {
        areaContenido.setBackground(fondo);
        areaContenido.setForeground(texto);
    }

    public void setNavegacionListener(NavegacionListener listener) {
        this.listener = listener;
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
            
            // Ya NO forzamos el color a negro aquí, respetamos el tema que esté activo
            // areaContenido.setForeground(Color.BLACK); <-- Eliminado

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