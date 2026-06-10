import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EncabezadoPestana extends JPanel {
    private final JLabel lblTitulo;

    public EncabezadoPestana(String titulo, JPanel panelContenido, JTabbedPane sistemaPestanas) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        lblTitulo = new JLabel(titulo);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

        JLabel btnCerrar = new JLabel(" x ");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.setForeground(new Color(153, 153, 153));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int miIndice = sistemaPestanas.indexOfTabComponent(EncabezadoPestana.this);
                int indiceUltimaReal = sistemaPestanas.getTabCount() - 2;

                if (miIndice != -1 && miIndice < indiceUltimaReal) {
                    Component componente = sistemaPestanas.getComponentAt(miIndice);
                    if (componente instanceof PanelNavegador) {
                        ((PanelNavegador) componente).cleanup();
                    }
                    sistemaPestanas.remove(miIndice);
                } else {
                    System.out.println("Accion bloqueada: no se puede cerrar la ultima pestaña de contenido.");
                }
            }
        });

        add(lblTitulo);
        add(btnCerrar);
    }

    public void setTitulo(String titulo) {
        lblTitulo.setText(titulo);
    }
}
