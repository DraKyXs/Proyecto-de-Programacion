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

    public interface NavegacionListener {
        void navegar(String urlDestino);
    }

    public Renderizador() {
        setLayout(new BorderLayout());

        areaContenido = new JTextPane();
        areaContenido.setEditable(false);
        areaContenido.setContentType("text/html");

        // Usamos un HTMLEditorKit nativo de Java para que el JTextPane
        // pueda interpretar HTML basico en vez de tratarlo como texto plano.
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
        // Si no hay contenido, mostramos una pagina minima de error
        // para que el area no quede en blanco.
        if (contenidoURL == null || contenidoURL.isBlank()) {
            areaContenido.setText("<html><body><h1>Error al cargar la pagina</h1></body></html>");
            areaContenido.setCaretPosition(0);
            return;
        }

        areaContenido.setContentType("text/html");

        // Antes de entregar el HTML al parser de Swing, lo limpiamos un poco
        // para quitar partes modernas que suelen romper al renderizador nativo de Java.
        String htmlSeguro = sanitizarHtmlParaSwing(contenidoURL);

        HTMLDocument documento = (HTMLDocument) htmlEditorKit.createDefaultDocument();
        try {
            // Forzamos carga sincrona para que el documento termine de parsearse
            // antes de asignarlo definitivamente al JTextPane.
            documento.setAsynchronousLoadPriority(-1);
            documento.setBase(new URL(baseUrl));
            htmlEditorKit.read(new StringReader(htmlSeguro), documento, 0);
            areaContenido.setDocument(documento);
        } catch (IOException | BadLocationException | RuntimeException e) {
            // Si incluso asi Swing no puede parsear el documento, mostramos un fallback seguro con el HTML escapado.
            areaContenido.setText(construirHtmlFallback(htmlSeguro));
        }

        areaContenido.setCaretPosition(0);
    }

    private String sanitizarHtmlParaSwing(String html) {
        // Aqui limpiamos scripts, estilos y otras partes modernas que suelen hacer fallar
        // al parser HTML/CSS antiguo de Swing
        String htmlSeguro = html;

        // Si una imagen viene dentro de un enlace, la convertimos en una imagen plana
        htmlSeguro = htmlSeguro.replaceAll("(?is)<a\\b[^>]*>\\s*(<img\\b[^>]*>)\\s*</a>", "$1");

        htmlSeguro = htmlSeguro.replaceAll("(?is)<script[^>]*>.*?</script>", "");
        htmlSeguro = htmlSeguro.replaceAll("(?is)<style[^>]*>.*?</style>", "");
        htmlSeguro = htmlSeguro.replaceAll("(?is)<link[^>]*rel\\s*=\\s*['\"]?stylesheet['\"]?[^>]*>", "");
        htmlSeguro = htmlSeguro.replaceAll("(?is)<noscript[^>]*>.*?</noscript>", "");
        htmlSeguro = htmlSeguro.replaceAll("(?i)\\sstyle\\s*=\\s*(['\"]).*?\\1", "");
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
            // Solo navegamos si el enlace no corresponde a una imagen enlazada.
            // Las imagenes deben comportarse como imagenes planas, no como botones.
            if (listener != null && !esImagenEnlazada(e.getSourceElement())) {
                String destino = e.getURL() != null ? e.getURL().toString() : e.getDescription();
                listener.navegar(destino);
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            if (esImagenEnlazada(e.getSourceElement())) {
                areaContenido.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                areaContenido.setCursor(new Cursor(Cursor.HAND_CURSOR));
                cambiarColorEnlace(e.getSourceElement(), new Color(46, 204, 113));
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
            areaContenido.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            cambiarColorEnlace(e.getSourceElement(), Color.BLUE);
        }
    }

    private boolean esImagenEnlazada(Element elementoHtml) {
        // Este metodo revisa si el elemento del evento corresponde a una imagen
        // o contiene una imagen en alguno de sus hijos.
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
                // Remover listener de hyperlinks
                for (HyperlinkListener listener : areaContenido.getHyperlinkListeners()) {
                    areaContenido.removeHyperlinkListener(listener);
                }
                
                areaContenido.setText(""); // liberar contenido HTML
                areaContenido.setDocument(null);
            }

            htmlEditorKit = null;
            listener = null;

            removeAll(); // limpiar subcomponentes (scrollpane, etc.)
        } catch (Exception e) {
            System.err.println("Error durante cleanup de Renderizador: " + e.getMessage());
        }
    }
}
