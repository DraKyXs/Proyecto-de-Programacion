import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CustomTabHeader extends JPanel {

    public CustomTabHeader(String titulo, JPanel panelContenido, JTabbedPane sistemaPestanas) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8)); 
        
        JLabel btnCerrar = new JLabel(" × ");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.setForeground(new Color(150, 150, 150));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int miIndice = sistemaPestanas.indexOfTabComponent(CustomTabHeader.this);
                int indiceUltimaReal = sistemaPestanas.getTabCount() - 2; // la última pestaña de contenido real

                if (miIndice != -1 && miIndice < indiceUltimaReal) {
                    sistemaPestanas.remove(miIndice);
                } else {
                    System.out.println("Acción bloqueada: no se puede cerrar la última pestaña de contenido.");
                }
            }
        });
        add(lblTitulo);
        add(btnCerrar);
    }
}