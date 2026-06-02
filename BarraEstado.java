import java.awt.*;
import javax.swing.*;

public class BarraEstado extends JPanel {

    public BarraEstado(main mainFrame) {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        setBackground(new Color(220, 220, 220));

        JLabel etiquetaEstado = new JLabel("Listo");
        etiquetaEstado.setForeground(new Color(100, 100, 100));
        etiquetaEstado.setFont(new Font("Arial", Font.BOLD, 12));
        add(etiquetaEstado);

        mainFrame.etiquetaEstado = etiquetaEstado;
    }
}
