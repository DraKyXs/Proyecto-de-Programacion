import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class main extends JFrame {
    
    private JTabbedPane sistemaPestanas;
    public JLabel etiquetaEstado;
    
    private int contadorPestanas = 1;
    
    // Variables globales para guardar el tema elegido
    private Color fondoActual = Color.WHITE;
    private Color textoActual = Color.BLACK;

    public main() {
        initializeFrame();
        
        add(new TitleBar(this), BorderLayout.NORTH);
        
        sistemaPestanas = new JTabbedPane();
        sistemaPestanas.setBackground(new Color(235, 235, 235));
        hacerRedimensionable(sistemaPestanas); 
        add(sistemaPestanas, BorderLayout.CENTER);
        
        
        sistemaPestanas.addTab("", new JPanel()); 
        
        JLabel btnSumar = new JLabel("+"); 
        btnSumar.setFont(new Font("Arial", Font.PLAIN, 24)); 
        btnSumar.setForeground(new Color(100, 100, 100));
        btnSumar.setHorizontalAlignment(SwingConstants.CENTER);
        btnSumar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSumar.setPreferredSize(new Dimension(35, 20));

        btnSumar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnSumar.setForeground(new Color(46, 204, 113));
            }
            public void mouseExited(java.awt.event.MouseEvent e) { 
                btnSumar.setForeground(new Color(100, 100, 100)); 
            }
            public void mousePressed(java.awt.event.MouseEvent e) { 
                abrirNuevaPestana(); 
            } 
        });

        sistemaPestanas.setTabComponentAt(0, btnSumar); 
        
        sistemaPestanas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
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

    // ==================================================================================================================================================
    // LÓGICA DE PESTAÑAS Y HEADER
    // ==================================================================================================================================================
    public void abrirNuevaPestana() {
        JPanel panelContenido = new BrowserTabPanel(this);

        //Barra de estado local para cada pestañaaaaaa
        panelContenido.add(new StatusBar(this), BorderLayout.SOUTH);
        
        String titulo = "Buscador "+contadorPestanas++;
        
        int posicion = sistemaPestanas.getTabCount()-1;
        if(posicion < 0) posicion = 0;

        sistemaPestanas.insertTab(null, null, panelContenido, null, posicion);
        CustomTabHeader tabHeader = new CustomTabHeader(titulo, panelContenido, sistemaPestanas);
        sistemaPestanas.setTabComponentAt(posicion, tabHeader);
        if (panelContenido instanceof BrowserTabPanel) {
            ((BrowserTabPanel) panelContenido).setTabHeader(tabHeader);
        }
        
        sistemaPestanas.setSelectedIndex(posicion);
    }


    public JTabbedPane getSistemaPestanas() { 
        return sistemaPestanas; 
    }
    public Color getFondoActual() { 
        return fondoActual; 
    }
    public Color getTextoActual() { 
        return textoActual; 
    }
    public void setFondoActual(Color c) { 
        this.fondoActual = c; 
    }
    public void setTextoActual(Color c) { 
        this.textoActual = c; 
    }

    
    public void hacerRedimensionable(JComponent panel) {
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

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new main().setVisible(true);
        });
    }


    
}
