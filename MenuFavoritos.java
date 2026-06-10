import java.awt.*;
import javax.swing.*;

public class MenuFavoritos {

    public interface AccionFavoritos {
        void abrirUrl(String url);
        void eliminarUrl(String url);
    }

    public static void mostrar(JComponent boton, Favoritos favoritos, AccionFavoritos accion) {
        // Buscar la ventana padre
        Window ventanaPadre = SwingUtilities.getWindowAncestor(boton);

        JDialog dialogo = new JDialog(ventanaPadre);
        dialogo.setUndecorated(true);
        dialogo.setModal(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        panel.setBackground(Color.WHITE);

        java.util.List<String> lista = favoritos.getFavoritos();

        if (lista.isEmpty()) {
            JLabel vacio = new JLabel("  No hay favoritos guardados  ");
            vacio.setForeground(new Color(153, 153, 153));
            vacio.setFont(new Font("Arial", Font.ITALIC, 13));
            panel.add(vacio);
        } else {
            for (String url : lista) {
                JPanel fila = new JPanel(new BorderLayout(6, 0));
                fila.setBackground(Color.WHITE);
                fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

                String etiqueta = url.length() > 50 ? url.substring(0, 47) + "..." : url;
                JLabel lblUrl = new JLabel(etiqueta);
                lblUrl.setFont(new Font("Arial", Font.PLAIN, 13));
                lblUrl.setToolTipText(url);
                lblUrl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                lblUrl.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

                lblUrl.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        fila.setBackground(new Color(239, 245, 254));
                        lblUrl.setBackground(new Color(239, 245, 254));
                    }
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        fila.setBackground(Color.WHITE);
                        lblUrl.setBackground(Color.WHITE);
                    }
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        dialogo.dispose();
                        accion.abrirUrl(url);
                    }
                });
                JLabel btnEliminar = new JLabel("✕");
                btnEliminar.setFont(new Font("Dialog", Font.PLAIN, 14));
                btnEliminar.setPreferredSize(new Dimension(24, 24));
                btnEliminar.setForeground(new Color(239, 68, 68));
                btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEliminar.setToolTipText("Eliminar de favoritos");
                btnEliminar.setHorizontalAlignment(SwingConstants.CENTER);

                btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        btnEliminar.setForeground(new Color(255, 107, 107));
                    }
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        btnEliminar.setForeground(new Color(239, 68, 68));
                    }
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        dialogo.dispose();
                        accion.eliminarUrl(url);
                    }
                });

                fila.add(lblUrl, BorderLayout.CENTER);
                fila.add(btnEliminar, BorderLayout.EAST);
                panel.add(fila);
            }
        }

        dialogo.add(panel);
        dialogo.pack();

        // Posicionar debajo del botón
        Point ubicacion = boton.getLocationOnScreen();
        dialogo.setLocation(ubicacion.x, ubicacion.y + boton.getHeight());

        // Cerrar al hacer clic fuera
        dialogo.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent e) {}
            public void windowLostFocus(java.awt.event.WindowEvent e) {
                dialogo.dispose();
            }
        });

        dialogo.setVisible(true);
    }
}