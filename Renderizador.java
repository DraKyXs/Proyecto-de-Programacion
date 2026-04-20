import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class Renderizador extends JPanel {   // ← Cambiado a public
    private JTextPane areaContenido;
    private NavegacionListener listener;

    public interface NavegacionListener {
        void navegar(String urlDestino);
    }

    public Renderizador() {
        setLayout(new BorderLayout());

        areaContenido = new JTextPane();
        areaContenido.setEditable(false); 
        areaContenido.setContentType("text/html");
        
        areaContenido.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        areaContenido.setFont(new Font("Arial", Font.PLAIN, 14));
        
        areaContenido.setBackground(Color.WHITE);
        areaContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        areaContenido.addHyperlinkListener(e -> manejarEventosEnlace(e));

        JScrollPane scroll = new JScrollPane(areaContenido);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    public void aplicarTemaVisual(Color fondo, Color texto) {
        areaContenido.setBackground(fondo);
        areaContenido.setForeground(texto);
    }

    public void setNavegacionListener(NavegacionListener listener) {
        this.listener = listener;
    }

    public void cargarArchivo(File archivo) {
        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    sb.append(linea).append("\n");
                }
            }
            
            String contenidoHtml = sb.toString();

            String htmlMinusculas = contenidoHtml.toLowerCase();
            if (!htmlMinusculas.contains("<html>") || !htmlMinusculas.contains("<body>")) {
                areaContenido.setContentType("text/plain");
                areaContenido.setText("ERROR: El archivo no es un documento HTML válido.\nDebe contener las etiquetas <html> y <body>.");
                areaContenido.setForeground(Color.RED);
                return;
            }

            contenidoHtml = contenidoHtml.replace("\n", "<br>");

            areaContenido.setContentType("text/html");
            areaContenido.setText(contenidoHtml);

        } catch (Exception ex) {
            areaContenido.setContentType("text/plain");
            areaContenido.setText("Error crítico al leer el archivo: " + ex.getMessage());
        }
    }

    private void manejarEventosEnlace(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (listener != null) {
                listener.navegar(e.getDescription());
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            areaContenido.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cambiarColorEnlace(e.getSourceElement(), new Color(46, 204, 113)); 
        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
            areaContenido.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            cambiarColorEnlace(e.getSourceElement(), Color.BLUE); 
        }
    }

    private void cambiarColorEnlace(Element elementoHtml, Color color) {
        if (elementoHtml != null && areaContenido.getDocument() instanceof StyledDocument) {
            StyledDocument doc = (StyledDocument) areaContenido.getDocument();
            SimpleAttributeSet atributos = new SimpleAttributeSet();
            StyleConstants.setForeground(atributos, color);
            
            int inicio = elementoHtml.getStartOffset();
            int longitud = elementoHtml.getEndOffset() - inicio;
            doc.setCharacterAttributes(inicio, longitud, atributos, false);
        }
    }
}