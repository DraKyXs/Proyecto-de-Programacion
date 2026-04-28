import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

public class BrowserTabPanel extends JPanel {

    private final main mainFrame;
    private JTextField localBuscador;
    private JButton localBoton;
    public Renderizador renderizador;

    public BrowserTabPanel(main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout()); 

        JPanel panelTop = createSearchPanel();
        add(panelTop, BorderLayout.NORTH); 

        renderizador = new Renderizador();
        renderizador.aplicarTemaVisual(mainFrame.getFondoActual(), mainFrame.getTextoActual());
        
        renderizador.setNavegacionListener(nuevaRuta -> {
            localBuscador.setText(nuevaRuta); 
            procesarURLLocal(nuevaRuta, renderizador); 
        });
        
        add(renderizador, BorderLayout.CENTER);

        setupListeners();
    }
    private JPanel createSearchPanel() {
        JPanel panelTop = new JPanel(new GridBagLayout());
        panelTop.setBackground(new Color(245, 245, 245)); 
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();

        localBuscador = new JTextField(25); 
        localBuscador.setBackground(Color.WHITE);
        localBuscador.setForeground(new Color(60, 60, 60));
        localBuscador.setCaretColor(new Color(100, 100, 100));
        localBuscador.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10) 
        ));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0; 
        panelTop.add(localBuscador, gbc);

        localBoton = new JButton("Ir"); 
        localBoton.setFont(new Font("Arial", Font.BOLD, 13));
        localBoton.setFocusPainted(false);
        localBoton.setBorderPainted(false);
        localBoton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        localBoton.setBackground(new Color(180, 180, 180)); 
        localBoton.setForeground(Color.WHITE); 
        localBoton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0; 
        panelTop.add(localBoton, gbc);

        return panelTop;
    }

    private void setupListeners() {
        localBoton.addActionListener(e -> {
            if(!localBuscador.getText().trim().isEmpty()){
                procesarURLLocal(localBuscador.getText(), renderizador);
            }
        });
        
        localBuscador.addActionListener(e -> {
            if(!localBuscador.getText().trim().isEmpty()){
                procesarURLLocal(localBuscador.getText(), renderizador);
            }
        });

        localBuscador.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarBotonLocal(); }
            public void removeUpdate(DocumentEvent e) { actualizarBotonLocal(); }
            public void changedUpdate(DocumentEvent e) { actualizarBotonLocal(); }
        });
    }

    private void actualizarBotonLocal() {
        boolean tieneTexto = !localBuscador.getText().trim().isEmpty();
        if(!tieneTexto) {
            localBoton.setBackground(new Color(180, 180, 180)); 
            localBoton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
        } else {
            localBoton.setBackground(new Color(52, 73, 94)); 
            localBoton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        }
    }

    private void procesarURLLocal(String texto, Renderizador renderizador) {
        mainFrame.etiquetaEstado.setText("Cargando...");
        mainFrame.etiquetaEstado.setForeground(new Color(41, 128, 185)); 

        javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
            try {
                String rutaLimpia = texto.trim().replace("file:///", "").replace("file://", "");
                File archivo = new File(rutaLimpia);
                
                if (archivo.exists() && archivo.isFile()) {
                    renderizador.cargarArchivo(archivo); 
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "El archivo local no existe:\n" + archivo.getAbsolutePath(), "Error 404", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Ruta inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
            mainFrame.etiquetaEstado.setText("Listo");
            mainFrame.etiquetaEstado.setForeground(new Color(100, 100, 100)); 
        });
        timer.setRepeats(false); 
        timer.start();
    }

    public void aplicarTemaVisual(Color fondo, Color texto) {
        if (renderizador != null) {
            renderizador.aplicarTemaVisual(fondo, texto);
        }
    }
}