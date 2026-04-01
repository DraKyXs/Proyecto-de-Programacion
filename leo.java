import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;





public class leo extends JFrame {
    public String url1 = "";

    public leo() {

        //Realizamos las dimensiones mediante un setSize dandole una dimensión inicial y una dimensión minima de 400x300
        //A su vez, eliminamos la barra de titulo realizando un setUndecorated para así crear la nuestra
        setUndecorated(true);                    
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);             
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ========================================================== BARRA DE TÍTULO PERSONALIZADA =====================================================================================
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(30, 30, 30));
        titleBar.setPreferredSize(new Dimension(0, 40));

        moverVentana(titleBar);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        // Botones
        JButton btnmin = CreadorBotones("−", new Color(255, 180, 0));
        JButton btnmax = CreadorBotones("▢", new Color(0, 200, 80));
        JButton btncerrar    = CreadorBotones("×", new Color(220, 50, 50));

        buttonsPanel.add(btnmin);
        buttonsPanel.add(btnmax);
        buttonsPanel.add(btncerrar);

        titleBar.add(buttonsPanel, BorderLayout.EAST);

        add(titleBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); 

        JButton botonBuscar = new JButton("Ir");
        botonBuscar.setBounds(528,251, 94, 23);
        mainPanel.add(botonBuscar);

        JTextField buscador = new JTextField();
        buscador.setBounds(224,251, 297, 25);
        mainPanel.add(buscador);

        /*botonBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                url1 = buscador.getText();
                
            }


        });
        */

        add(mainPanel, BorderLayout.CENTER);


        btnmin.addActionListener(e -> setSize(400, 300));

        btnmax.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setSize(800, 600);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }


        });

        btncerrar.addActionListener(e -> System.exit(0));
    }

    private JButton CreadorBotones(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(45, 40));
        return btn;
    }

    private void moverVentana(JPanel titleBar) {
        
        final int[] mouseX= new int[1];
        final int[] mouseY= new int[1];

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX[0]= e.getX();
                mouseY[0]= e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int newX= e.getXOnScreen()-mouseX[0];
                int newY= e.getYOnScreen()-mouseY[0];
                setLocation(newX, newY);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new leo().setVisible(true);
        });
    }
}

