import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BarraTitulo extends JPanel {
    private JLabel btnOffline;
    public BarraTitulo(main mainFrame) {
        setLayout(new BorderLayout());
        Color colorBarra = new Color(234, 238, 244);
        setBackground(colorBarra);
        setPreferredSize(new Dimension(0, 40));
        moverVentana(this, mainFrame);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        JButton btnTema = crearBoton("⚘", colorBarra);
        btnTema.setFont(new Font("Arial Unicode MS", Font.BOLD, 18));
        btnTema.setToolTipText("Temas");
        JButton btnMin = crearBoton("-", colorBarra);
        JButton btnMax = crearBoton("□", colorBarra);
        JButton btnCerrar = crearBoton("x", colorBarra);
        JLabel btnFavoritos = new JLabel("★");
        btnFavoritos.setFont(new Font("Dialog", Font.PLAIN, 20));
        btnFavoritos.setForeground(new Color(245, 158, 11));
        btnFavoritos.setToolTipText("Ver favoritos");
        btnFavoritos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFavoritos.setPreferredSize(new Dimension(45, 40));
        btnFavoritos.setHorizontalAlignment(SwingConstants.CENTER);
        
        btnOffline = new JLabel("☁");
        btnOffline.setFont(new Font("Dialog", Font.PLAIN, 20));
        btnOffline.setForeground(new Color(16, 185, 129));
        btnOffline.setToolTipText("Online — clic para modo offline");
        btnOffline.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOffline.setPreferredSize(new Dimension(45, 40));
        btnOffline.setHorizontalAlignment(SwingConstants.CENTER);

        
        JButton btnIA = crearBoton("IA", colorBarra);
        btnIA.setFont(new Font("Arial", Font.BOLD, 13));
        btnIA.setForeground(new Color(16, 168, 185));
        btnIA.setToolTipText("Asistente IA");
        btnIA.setPreferredSize(new Dimension(45, 40));

        aplicarEfectoHover(btnTema, new Color(209, 213, 219), new Color(26, 26, 26));
        aplicarEfectoHover(btnIA, new Color(209, 213, 219), new Color(26, 26, 26));       
        aplicarEfectoHover(btnMin, new Color(209, 213, 219), new Color(26, 26, 26));
        aplicarEfectoHover(btnMax, new Color(209, 213, 219), new Color(26, 26, 26));
        aplicarEfectoHover(btnCerrar, new Color(232, 17, 35), Color.WHITE);

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
        btnIA.addActionListener(e -> mostrarAsistenteIAEnPestanaActual(mainFrame));
        btnMin.addActionListener(e -> mainFrame.setState(JFrame.ICONIFIED));
        btnMax.addActionListener(e -> alternarMaximizado(mainFrame));
        btnCerrar.addActionListener(e -> System.exit(0));
        btnFavoritos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainFrame.mostrarMenuFavoritosGlobal(btnFavoritos);
            }
            public void mouseEntered(MouseEvent e) {
                btnFavoritos.setForeground(new Color(245, 158, 11));
            }
            public void mouseExited(MouseEvent e) {
                btnFavoritos.setForeground(new Color(245, 158, 11));
            }
        });
        btnOffline.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainFrame.toggleOffline(btnOffline);
            }
            public void mouseEntered(MouseEvent e) {
                btnOffline.setForeground(btnOffline.getForeground().darker());
            }
            public void mouseExited(MouseEvent e) {
                btnOffline.setForeground(
                    mainFrame.isModoOffline()
                        ? new Color(239, 68, 68)
                        : new Color(80, 180, 80)
                );
            }
        });
        

        buttonsPanel.add(btnTema);
        buttonsPanel.add(btnIA);
        buttonsPanel.add(btnOffline);
        buttonsPanel.add(btnFavoritos);
        buttonsPanel.add(btnMin);
        buttonsPanel.add(btnMax);
        buttonsPanel.add(btnCerrar);

        add(buttonsPanel, BorderLayout.EAST);
    }

    private void mostrarAsistenteIAEnPestanaActual(main mainFrame) {
        JTabbedPane tabs = mainFrame.getSistemaPestanas();
        int indice = tabs.getSelectedIndex();
        if (indice < 0) {
            return;
        }

        Component componente = tabs.getComponentAt(indice);
        if (componente instanceof PanelNavegador) {
            ((PanelNavegador) componente).mostrarAsistenteIA();
        }
    }
    
    private void cambiarTemaVisual(main frame, Color fondo, Color texto) {
        frame.setFondoActual(fondo);
        frame.setTextoActual(texto);

        JTabbedPane tabs = frame.getSistemaPestanas();
        for (int i = 0; i < tabs.getTabCount() - 1; i++) {
            Component c = tabs.getComponentAt(i);
            if (c instanceof PanelNavegador) {
                ((PanelNavegador) c).aplicarTemaVisual(fondo, texto);
            }
        }
    }

    private void alternarMaximizado(main frame) {
        if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(colorFondo);
        btn.setForeground(new Color(26, 26, 26));
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

    private void moverVentana(JPanel titleBar, main frame) {
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
