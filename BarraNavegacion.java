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
    private JButton botonRecargar;
    private JButton botonHistorial;
    private JButton botonFavoritos;
    private DocumentListener documentListener;

    public BarraNavegacion() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        crearComponentes();
        ordenarComponentes();
        configurarBuscador();
    }

    private void crearComponentes() {
        botonAtras = crearBotonNav("<");
        botonAdelante = crearBotonNav(">");
        botonRecargar = crearBotonTexto("r", "Recargar");
        botonHistorial = crearBotonTexto("⌛", "Historial");
        botonFavoritos = crearBotonTexto("★", "Favoritos");

        campoUrl = new JTextField(25);
        campoUrl.setBackground(Color.WHITE);
        campoUrl.setForeground(new Color(60, 60, 60));
        campoUrl.setCaretColor(new Color(100, 100, 100));
        campoUrl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        botonIr = new JButton("Ir");
        botonIr.setFont(new Font("Arial", Font.BOLD, 13));
        botonIr.setFocusPainted(false);
        botonIr.setBorderPainted(false);
        botonIr.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        botonIr.setBackground(new Color(180, 180, 180));
        botonIr.setForeground(Color.WHITE);
        botonIr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(campoUrl, gbc);

        gbc.gridx = 5;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(botonIr, gbc);

        gbc.gridx = 6;
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
                    botonIr.setBackground(new Color(72, 93, 114));
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
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(new Color(60, 60, 60));
        return btn;
    }

    private JButton crearBotonTexto(String texto, String tooltip) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Dialog", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(45, 35));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(new Color(60, 60, 60));
        btn.setToolTipText(tooltip);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void actualizarBotonIr() {
        boolean tieneTexto = !getTextoUrl().isEmpty();

        if (!tieneTexto) {
            botonIr.setBackground(new Color(180, 180, 180));
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

    public void actualizarBotonesNavegacion(boolean hayUrlAnterior) {
        botonAtras.setEnabled(hayUrlAnterior);
        botonAdelante.setEnabled(hayUrlAnterior);
    }

    public JButton getBotonHistorial() {
        return botonHistorial;
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
        botonRecargar.addActionListener(listener);
    }

    public void alMostrarHistorial(ActionListener listener) {
        botonHistorial.addActionListener(listener);
    }

    public void cleanup() {
        if (campoUrl != null && documentListener != null) {
            campoUrl.getDocument().removeDocumentListener(documentListener);
        }

        limpiarActionListeners(campoUrl);
        limpiarActionListeners(botonIr);
        limpiarActionListeners(botonAtras);
        limpiarActionListeners(botonAdelante);
        limpiarActionListeners(botonRecargar);
        limpiarActionListeners(botonHistorial);
        limpiarMouseListeners(botonIr);
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
}
