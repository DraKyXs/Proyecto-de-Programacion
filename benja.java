import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class benja extends JFrame {

    public benja() {
        // Configuración básica del JFrame
        setUndecorated(true);                    // Quita la barra nativa
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);             // Centra la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ====================== BARRA DE TÍTULO PERSONALIZADA =======
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(30, 30, 30));
        titleBar.setPreferredSize(new Dimension(0, 40));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        // Botones
        JButton btnMinimize = createTitleButton("−", new Color(255, 180, 0));
        JButton btnMaximize = createTitleButton("□", new Color(0, 200, 80));
        JButton btnClose    = createTitleButton("×", new Color(220, 50, 50));

        buttonsPanel.add(btnMinimize);
        buttonsPanel.add(btnMaximize);
        buttonsPanel.add(btnClose);

        titleBar.add(buttonsPanel, BorderLayout.EAST);

        add(titleBar, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); 

        JButton btnVer = new JButton("Buscar");
        btnVer.setBounds(217, 130, 94, 23);
        mainPanel.add(btnVer);

        add(mainPanel, BorderLayout.CENTER);

        btnMinimize.addActionListener(e -> setState(JFrame.ICONIFIED));

        btnMaximize.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        btnClose.addActionListener(e -> System.exit(0));
    }

    private JButton createTitleButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(45, 40));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new benja().setVisible(true);
        });
    }
}