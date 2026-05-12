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
            public void mousePressed (MouseEvent e){
                int i = sistemaPestanas.indexOfComponent(panelContenido);
                int indiceUltima = sistemaPestanas.getTabCount() -1;
                if (i != -1 && i < indiceUltima ){
                    sistemaPestanas.remove(i);
                } else {
                    System.out.println("No se puede cerrar la pestaña");
                }
            }
        });

        add(lblTitulo);
        add(btnCerrar);
    }
}