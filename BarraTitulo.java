import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BarraTitulo extends JPanel {

    public BarraTitulo(main mainFrame) {
        setLayout(new BorderLayout());
        Color colorBarra = new Color(220, 220, 220);
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
        JButton favoritos = crearBoton("★", colorBarra);
        favoritos.setFont(new Font("Arial Unicode MS", Font.BOLD, 18));

        aplicarEfectoHover(btnTema, new Color(200, 200, 200), new Color(80, 80, 80));
        aplicarEfectoHover(btnMin, new Color(200, 200, 200), new Color(80, 80, 80));
        aplicarEfectoHover(btnMax, new Color(200, 200, 200), new Color(80, 80, 80));
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
        btnMin.addActionListener(e -> mainFrame.setState(JFrame.ICONIFIED));
        btnMax.addActionListener(e -> alternarMaximizado(mainFrame));
        btnCerrar.addActionListener(e -> System.exit(0));
        

        buttonsPanel.add(btnTema);
        buttonsPanel.add(btnMin);
        buttonsPanel.add(btnMax);
        buttonsPanel.add(btnCerrar);
        buttonsPanel.add(favoritos);

        add(buttonsPanel, BorderLayout.EAST);
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
