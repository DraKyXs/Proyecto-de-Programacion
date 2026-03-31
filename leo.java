import java.awt.BorderLayout;
import java.util.*;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.Color;

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

        JToolBar toolbar = new JToolBar();
        JButton exit = new JButton("Salir");
        JButton min = new JButton("Minimizar");
        JButton max = new JButton("Maximizar");

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
        btnVer.setBounds(217, 130, 94, 23);
        panel.add(btnVer);
    }

    public static void main(final String[] args) {
        new leo();
    }

}