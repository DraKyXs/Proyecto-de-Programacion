import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class Renderizador extends JPanel {
    private JTextPane areaContenido;
    private HTMLEditorKit htmlEditorKit;
    private NavegacionListener listener;
    private String mensaje_no_render = "<p style ='display:inline;color:red; font-weight:bold;'>Este elemento no se puede renderizar</p>";
    private String mensaje_detecta = "<p style ='display:inline;color:green; font-weight:bold;'>Elemento detectado por el programa</p>";


    public interface NavegacionListener {
        void navegar(String urlDestino);
    }

    public Renderizador() {
        setLayout(new BorderLayout());

        areaContenido = new JTextPane();
        areaContenido.setEditable(false);
        areaContenido.setContentType("text/html");

        htmlEditorKit = new HTMLEditorKit();
        areaContenido.setEditorKit(htmlEditorKit);

        areaContenido.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        areaContenido.setFont(new Font("Arial", Font.PLAIN, 14));

        areaContenido.setBackground(Color.WHITE);
        areaContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        areaContenido.addHyperlinkListener(this::manejarEventosEnlace);
        
        
        
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

    public void cargarURL(String contenidoURL, String baseUrl) {
        if (contenidoURL == null || contenidoURL.isBlank()) {
            areaContenido.setText("<html><body><h1>Error al cargar la pagina</h1></body></html>");
            areaContenido.setCaretPosition(0);
            return;
        }

        areaContenido.setContentType("text/html");
        System.out.println(contenidoURL);

        String htmlSeguro = sanitizarHtmlParaSwing(contenidoURL);
        HTMLDocument documento = (HTMLDocument) htmlEditorKit.createDefaultDocument();
        try {
            documento.setAsynchronousLoadPriority(-1);

            documento.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

            documento.setBase(new URL(baseUrl));
            htmlEditorKit.read(new StringReader(htmlSeguro), documento, 0);
            areaContenido.setDocument(documento);
        } catch (IOException | BadLocationException | RuntimeException e) {
            areaContenido.setText(construirHtmlFallback(htmlSeguro));
        }

        areaContenido.setCaretPosition(0);
    }


    private String sanitizarHtmlParaSwing(String html) {
        String htmlSeguro = html;

        htmlSeguro = htmlSeguro.replaceAll("(?is)<a\\b[^>]*>\\s*(<img\\b[^>]*>)\\s*</a>", "$1");

        htmlSeguro = htmlSeguro.replaceAll("(?is)<script[^>]*>.*?</script>", "");
        htmlSeguro = htmlSeguro.replaceAll("(?is)<style[^>]*>.*?</style>", "");
        htmlSeguro = htmlSeguro.replaceAll("(?is)<link[^>]*rel\\s*=\\s*['\"]?stylesheet['\"]?[^>]*>", "");
        htmlSeguro = htmlSeguro.replaceAll("(?is)<noscript[^>]*>.*?</noscript>", "");
        htmlSeguro = htmlSeguro.replaceAll("(?i)\\sstyle\\s*=\\s*(['\"]).*?\\1", "");

        htmlSeguro = htmlSeguro.replaceAll("(?is)<audio[^>]*>.*?</audio>", mensaje_detecta);
        htmlSeguro = htmlSeguro.replaceAll("(?is)<video[^>]*>.*?</video>", mensaje_detecta);
        htmlSeguro = htmlSeguro.replaceAll("(?is)<source[^>]*>.*?</source>", mensaje_detecta);
        htmlSeguro = htmlSeguro.replaceAll("(?is)<track[^>]*>.*?</track>", mensaje_detecta);
        htmlSeguro = htmlSeguro.replaceAll("(?is)<embed[^>]*>.*?</embed>", mensaje_detecta);
        htmlSeguro = htmlSeguro.replaceAll("(?is)<object[^>]*>.*?</object>", mensaje_detecta);
        
        return htmlSeguro;
    }

    private String construirHtmlFallback(String html) {
        return "<html><body><pre>" + escaparHtml(html) + "</pre></body></html>";
    }

    private String escaparHtml(String texto) {
        if (texto == null) {
            return "";
        }
        return texto
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }

    private void manejarEventosEnlace(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (listener != null && !esImagenEnlazada(e.getSourceElement())) {
                String destino = e.getURL() != null ? e.getURL().toString() : e.getDescription();
                listener.navegar(destino);
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            if (esImagenEnlazada(e.getSourceElement())) {
                areaContenido.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                areaContenido.setCursor(new Cursor(Cursor.HAND_CURSOR));
                cambiarColorEnlace(e.getSourceElement(), new Color(16, 185, 129));
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
            areaContenido.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            cambiarColorEnlace(e.getSourceElement(), Color.BLUE);
        }
    }

    private boolean esImagenEnlazada(Element elementoHtml) {
        if (elementoHtml == null) {
            return false;
        }

        Object nombreTag = elementoHtml.getAttributes().getAttribute(StyleConstants.NameAttribute);
        if (HTML.Tag.IMG.equals(nombreTag)) {
            return true;
        }

        for (int i = 0; i < elementoHtml.getElementCount(); i++) {
            if (esImagenEnlazada(elementoHtml.getElement(i))) {
                return true;
            }
        }
        return false;
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
    public void cleanup() {
        try {
            if (areaContenido != null) {
                for (HyperlinkListener listener : areaContenido.getHyperlinkListeners()) {
                    areaContenido.removeHyperlinkListener(listener);
                }
                
                areaContenido.setText("");

                if (htmlEditorKit != null) {
                    areaContenido.setDocument(htmlEditorKit.createDefaultDocument());
                }
            }

            htmlEditorKit = null;
            listener = null;

            removeAll();
        } catch (Exception e) {
            System.err.println("Error durante cleanup de Renderizador: " + e.getMessage());
        }
    }
}
