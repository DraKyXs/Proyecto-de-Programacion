import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TitleBar extends JPanel {

    public TitleBar(leo mainFrame) {
        setLayout(new BorderLayout());
        Color colorBarra = new Color(220, 220, 220); 
        setBackground(colorBarra); 
        setPreferredSize(new Dimension(0, 40));
        moverVentana(this, mainFrame);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        JButton btnTema = CreadorBotones("⚙", colorBarra);
        JButton btnmin = CreadorBotones("−", colorBarra);
        JButton btnmax = CreadorBotones("□", colorBarra);
        JButton btncerrar = CreadorBotones("×", colorBarra);

        aplicarEfectoHover(btnTema, new Color(200, 200, 200), new Color(80, 80, 80)); 
        aplicarEfectoHover(btnmin, new Color(200, 200, 200), new Color(80, 80, 80)); 
        aplicarEfectoHover(btnmax, new Color(200, 200, 200), new Color(80, 80, 80));
        aplicarEfectoHover(btncerrar, new Color(232, 17, 35), Color.WHITE); 

        // Creación del Menú Desplegable
        JPopupMenu menuTemas = new JPopupMenu();
        JMenuItem temaClaro = new JMenuItem("Tema Claro (Defecto)");
        JMenuItem temaOscuro = new JMenuItem("Tema Oscuro");
        JMenuItem temaHacker = new JMenuItem("Tema Hacker");

        temaClaro.addActionListener(e -> cambiarTemaVisual(mainFrame, Color.WHITE, Color.BLACK));
        temaOscuro.addActionListener(e -> cambiarTemaVisual(mainFrame, new Color(40, 40, 40), new Color(220, 220, 220)));
        temaHacker.addActionListener(e -> cambiarTemaVisual(mainFrame, Color.BLACK, new Color(0, 255, 0)));

        menuTemas.add(temaClaro);
        menuTemas.add(temaOscuro);
        menuTemas.add(temaHacker);

        btnTema.addActionListener(e -> menuTemas.show(btnTema, 0, btnTema.getHeight()));

        btnmin.addActionListener(e -> mainFrame.setState(JFrame.ICONIFIED)); 
        btnmax.addActionListener(e -> toggleMaximize(mainFrame));
        btncerrar.addActionListener(e -> System.exit(0));

        buttonsPanel.add(btnTema);
        buttonsPanel.add(btnmin);
        buttonsPanel.add(btnmax);
        buttonsPanel.add(btncerrar);

        add(buttonsPanel, BorderLayout.EAST);
    }

    private void cambiarTemaVisual(leo frame, Color fondo, Color texto) {
        frame.setFondoActual(fondo);
        frame.setTextoActual(texto);
        
        JTabbedPane tabs = frame.getSistemaPestanas();
        for (int i = 0; i < tabs.getTabCount() - 1; i++) {
            Component c = tabs.getComponentAt(i);
            if (c instanceof BrowserTabPanel) {
                ((BrowserTabPanel) c).aplicarTemaVisual(fondo, texto);
            }
        }
    }

    private void toggleMaximize(leo frame) {
        if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null); 
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    // === MÉTODOS ORIGINALES COPIADOS ===
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

    private void moverVentana(JPanel titleBar, leo frame) {
        final int[] mouseX = new int[1];
        final int[] mouseY = new int[1];
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX[0] = e.getX();
                mouseY[0] = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(e.getXOnScreen() - mouseX[0], e.getYOnScreen() - mouseY[0]);
            }
        });
    }
}