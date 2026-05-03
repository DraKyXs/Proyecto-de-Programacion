import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class Renderizador extends JPanel {  
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

    public void cargarURL(String contenidoURL) {
        //cambié el metodo por este para que sea especifico de URL y no de local, a demas puse prints porque me estaba dando errores que no se mostraban en el render y los tiré a la consola
        if(contenidoURL != null ){
            areaContenido.setText(contenidoURL);
            System.out.println(contenidoURL);
        } else {
            areaContenido.setText("<html><body><h1>Error al cargar la página</h1></body></html>");
            //en el setText puse las etiquetas html para que se muestre el error en el render
            System.out.println("no hay contenido");
        }
        areaContenido.setCaretPosition(0);
        //barra de scroll que se resetea cada vez que carga un URL nuevo
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