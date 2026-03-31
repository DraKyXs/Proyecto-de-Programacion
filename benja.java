import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class benja extends JFrame{
    JFrame frame;
    
    public benja() { //Recordar cambiar nombre de usuario
        frame = new JFrame();
        frame.setTitle("Web");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(400, 300));
        //frame.setLayout(null);

        JToolBar toolbar = new JToolBar();
        JButton exit = new JButton("Salir");
        JButton min = new JButton("Minimizar");
        JButton max = new JButton("Maximizar");
        
        Color azulBonito = new Color(70, 130, 180); //Color nuevo para que se note

        MouseAdapter hoverEfecto = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                JButton b = (JButton)e.getSource();
                b.setContentAreaFilled(true); // Esto arregla que no cambie el color
                b.setBackground(azulBonito); 
                b.setForeground(Color.WHITE); // Texto blanco para que se vea bien
            }
            public void mouseExited(MouseEvent e) { 
                JButton b = (JButton)e.getSource();
                b.setBackground(null); 
                b.setForeground(Color.BLACK); // Vuelve a texto negro
            }
        };

        // Aplicamos el efecto y quitamos el borde pintado para que el color sea total
        JButton[] botones = {exit, min, max};
        for(JButton b : botones) {
            b.addMouseListener(hoverEfecto);
            b.setFocusPainted(false);
            b.setContentAreaFilled(false); // Para que el cambio sea notable
        }

        toolbar.add(exit);
        toolbar.add(min);
        toolbar.add(max);

        Container pane = this.getContentPane();
        frame.add(toolbar, BorderLayout.NORTH);

        frame.setVisible(true);

        
    
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 434, 261);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JButton btnVer = new JButton("Buscar");
        btnVer.setBounds(67, 92, 94, 23);
        panel.add(btnVer);
    }

    public static void main(final String[] args) {
        new benja();
    }

}