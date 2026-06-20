import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BarraNavegacion extends JPanel {
    private JTextField campoUrl;
    private JButton botonIr;
    private JButton botonAtras;
    private JButton botonAdelante;
    private JLabel botonRecargar;
    private JLabel botonHistorial;
    private JLabel botonFavoritos;
    private JLabel botonBusqueda;
    private JLabel btnIA;
    private DocumentListener documentListener;
    private boolean esFavorito = false;

    public BarraNavegacion() {
        setLayout(new GridBagLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        crearComponentes();
        ordenarComponentes();
        configurarBuscador();
    }

    private void crearComponentes() {
        botonAtras = crearBotonNav("<");
        botonAdelante = crearBotonNav(">");
        botonRecargar = new JLabel("↻");
        botonRecargar.setFont(new Font("Dialog", Font.BOLD, 18));
        botonRecargar.setForeground(new Color(26, 26, 26));
        botonRecargar.setToolTipText("Recargar");
        botonRecargar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonRecargar.setPreferredSize(new Dimension(45, 35));
        botonRecargar.setHorizontalAlignment(SwingConstants.CENTER);

        botonHistorial = new JLabel("⏱");
        botonHistorial.setFont(new Font("Dialog", Font.BOLD, 18));
        botonHistorial.setForeground(new Color(60, 60, 60));
        botonHistorial.setToolTipText("Historial");
        botonHistorial.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonHistorial.setPreferredSize(new Dimension(45, 35));
        botonHistorial.setHorizontalAlignment(SwingConstants.CENTER);

        botonBusqueda = new JLabel("🔍");
        botonBusqueda.setFont(new Font("Dialog", Font.BOLD, 18));
        botonBusqueda.setForeground(new Color(60, 60, 60));
        botonBusqueda.setToolTipText("Motor de Búsqueda");
        botonBusqueda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonBusqueda.setPreferredSize(new Dimension(45, 35));
        botonBusqueda.setHorizontalAlignment(SwingConstants.CENTER);

        botonFavoritos = new JLabel("☆");



        campoUrl = new JTextField(25);
        campoUrl.setBackground(Color.WHITE);
        campoUrl.setForeground(new Color(60, 60, 60));
        campoUrl.setCaretColor(new Color(102, 102, 102));
        campoUrl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        botonIr = new JButton("Ir");
        botonIr.setFont(new Font("Arial", Font.BOLD, 13));
        botonIr.setFocusPainted(false);
        botonIr.setBorderPainted(false);
        botonIr.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        botonIr.setBackground(new Color(229, 231, 235));
        botonIr.setForeground(Color.WHITE);
        botonIr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        botonFavoritos.setFont(new Font("Dialog", Font.PLAIN, 22));
        botonFavoritos.setForeground(new Color(245, 158, 11));
        botonFavoritos.setToolTipText("Agregar a favoritos");
        botonFavoritos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonFavoritos.setPreferredSize(new Dimension(45, 35));
        botonFavoritos.setHorizontalAlignment(SwingConstants.CENTER);

        btnIA = new JLabel("🤖");
        btnIA.setToolTipText("Asistente IA");
        btnIA.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIA.setPreferredSize(new Dimension(45, 35));
        btnIA.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void ordenarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        add(botonAtras, gbc);

        gbc.gridx = 1;
        add(botonAdelante, gbc);

        gbc.gridx = 2;
        add(botonRecargar, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(botonHistorial, gbc);

        gbc.gridx = 4;
        add(botonBusqueda, gbc);

        gbc.gridx = 5;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(campoUrl, gbc);

        gbc.gridx = 6;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(botonIr, gbc);

        gbc.gridx = 8;
        add(botonFavoritos, gbc);

        
    }

    private void configurarBuscador() {
        documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarBotonIr();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarBotonIr();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarBotonIr();
            }
        };

        campoUrl.getDocument().addDocumentListener(documentListener);

        botonIr.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!getTextoUrl().isEmpty()) {
                    botonIr.setBackground(new Color(59, 130, 246));
                } else {
                    botonIr.setBackground(new Color(160, 160, 160));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                actualizarBotonIr();
            }
        });
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(45, 35));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(229, 231, 235));
        btn.setForeground(new Color(60, 60, 60));
        return btn;
    }

    private JButton crearBotonTexto(String texto, String tooltip) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Dialog", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(45, 35));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(229, 231, 235));
        btn.setForeground(new Color(60, 60, 60));
        btn.setToolTipText(tooltip);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void actualizarBotonIr() {
        boolean tieneTexto = !getTextoUrl().isEmpty();

        if (!tieneTexto) {
            botonIr.setBackground(new Color(229, 231, 235));
            botonIr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            botonIr.setBackground(new Color(52, 73, 94));
            botonIr.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public String getTextoUrl() {
        return campoUrl.getText().trim();
    }

    public void setTextoUrl(String texto) {
        campoUrl.setText(texto);
    }

    public void actualizarBotonesNavegacion(boolean puedeIrAtras, boolean puedeIrAdelante) {
        botonAtras.setEnabled(puedeIrAtras);
        botonAdelante.setEnabled(puedeIrAdelante);
    }

    public JLabel getBotonHistorial() {
        return botonHistorial;
    }
    public JLabel getBotonBusqueda() {
        return botonBusqueda;
    }
    public JLabel getBotonFavoritos() {
        return botonFavoritos;
    }
    public JLabel getBtnIA(){
        return btnIA;
    }

    public void alBuscar(ActionListener listener) {
        botonIr.addActionListener(listener);
        campoUrl.addActionListener(listener);
    }

    public void alIrAtras(ActionListener listener) {
        botonAtras.addActionListener(listener);
    }

    public void alIrAdelante(ActionListener listener) {
        botonAdelante.addActionListener(listener);
    }

    public void alRecargar(ActionListener listener) {
        botonRecargar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    listener.actionPerformed(null);
                }
            }
        });
    }

    public void alMostrarHistorial(ActionListener listener) {
        botonHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    listener.actionPerformed(null);
                }
            }
        });
    }

    public void alMostrarMotorBusqueda(ActionListener listener) {
        botonBusqueda.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    listener.actionPerformed(null);
                }
            }
        });
    }

   public void alToggleFavorito(ActionListener listener) {
        botonFavoritos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    listener.actionPerformed(null);
                }
            }
        });
    }

    public void actualizarEstrellaFavorito(boolean esFav) {
        this.esFavorito = esFav;
        if (esFav) {
            botonFavoritos.setText("★");
            botonFavoritos.setToolTipText("Quitar de favoritos");
            botonFavoritos.setForeground(new Color(245, 158, 11));
        } else {
            botonFavoritos.setText("☆");
            botonFavoritos.setToolTipText("Agregar a favoritos");
            botonFavoritos.setForeground(new Color(245, 158, 11));
        }
        botonFavoritos.repaint();
    }

    public void cleanup() {
        if (campoUrl != null && documentListener != null) {
            campoUrl.getDocument().removeDocumentListener(documentListener);
        }

        limpiarActionListeners(campoUrl);
        limpiarActionListeners(botonIr);
        limpiarActionListeners(botonAtras);
        limpiarActionListeners(botonAdelante);
        limpiarMouseListeners(botonIr);
        for (java.awt.event.MouseListener ml : botonRecargar.getMouseListeners()) {
            botonRecargar.removeMouseListener(ml);
        }
        for (java.awt.event.MouseListener ml : botonHistorial.getMouseListeners()) {
            botonHistorial.removeMouseListener(ml);
        }
        for (java.awt.event.MouseListener ml : botonFavoritos.getMouseListeners()) {
            botonFavoritos.removeMouseListener(ml);
        }
    }

    private void limpiarActionListeners(AbstractButton boton) {
        if (boton == null) {
            return;
        }

        for (ActionListener listener : boton.getActionListeners()) {
            boton.removeActionListener(listener);
        }
    }

    private void limpiarActionListeners(JTextField campo) {
        if (campo == null) {
            return;
        }

        for (ActionListener listener : campo.getActionListeners()) {
            campo.removeActionListener(listener);
        }
    }

    private void limpiarMouseListeners(AbstractButton boton) {
        if (boton == null) {
            return;
        }

        for (MouseListener listener : boton.getMouseListeners()) {
            boton.removeMouseListener(listener);
        }
    }

    public void alMostrarIA(ActionListener listener){
        btnIA.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    listener.actionPerformed(null);
                }
            }
        });
    }
}
