import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class leo extends JFrame{
    JFrame frame;
    
    public leo() {
        frame = new JFrame();
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(400, 300));

        frame.setUndecorated(true);           
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(30, 30, 30)); // color oscuro por ejemplo
        titleBar.setPreferredSize(new Dimension(0, 40));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);   // transparente

        // Botones con colores diferentes
        JButton btnMinimize = createButton("−", new Color(255, 180, 0));   // Amarillo/Naranja
        JButton btnMaximize = createButton("□", new Color(0, 200, 80));    // Verde
        JButton btnClose    = createButton("×", new Color(220, 50, 50));   // Rojo

        // Funciones de cada botón
        btnMinimize.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        btnMaximize.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        
        btnClose.addActionListener(e -> System.exit(0));

        buttonsPanel.add(btnMinimize);
        buttonsPanel.add(btnMaximize);
        buttonsPanel.add(btnClose);

        titleBar.add(buttonsPanel, BorderLayout.EAST);

        // Agregar la barra al JFrame
        add(titleBar, BorderLayout.NORTH);





        frame.setVisible(true);

        
    
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 434, 261);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JButton btnVer = new JButton("Buscar");
        btnVer.setBounds(217, 130, 94, 23);
        panel.add(btnVer);
    }

    public static void main(final String[] args) {
        new leo();
    }

}