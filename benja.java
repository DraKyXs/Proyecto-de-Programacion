import java.awt.BorderLayout;
import java.util.*;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.ActionListener;

public class web extends JFrame{
    JFrame frame;
    
    public web() {
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
        new web();
    }

}