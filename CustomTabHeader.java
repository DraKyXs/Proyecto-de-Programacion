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
            public void mouseEntered(MouseEvent e) { btnCerrar.setForeground(Color.RED); }
            public void mouseExited(MouseEvent e) { btnCerrar.setForeground(new Color(150, 150, 150)); }
            public void mousePressed(MouseEvent e) {
                int i = sistemaPestanas.indexOfComponent(panelContenido);
                if (i != -1) {
                    sistemaPestanas.remove(i);
                }
            }
        });

        add(lblTitulo);
        add(btnCerrar);
    }
}