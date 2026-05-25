import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import javax.swing.event.*;

public class BrowserTabPanel extends JPanel {

    // Referencia a la ventana principal.
    // La usamos para cambiar la barra de estado y para leer el tema visual actual.
    private final main mainFrame;

    // Campo de texto donde el usuario escribe la URL.
    private JTextField localBuscador;

    // Boton que sirve para cargar la pagina escrita.
    private JButton localBoton;

    // Componente encargado de mostrar el HTML.
    public Renderizador renderizador;

    // Variables simples para la navegacion original del proyecto.
    // Por ahora no representan un historial real completo.
    private String urlActual = "";   //Esto se hara mas adelante cuando se implementen los botones de navegacion, por ahora no es necesario guardar el url actual ni el anterior
    private String urlAnterior = ""; //Esto se hara mas adelante cuando se implementen los botones de navegacion, por ahora no es necesario guardar el url actual ni el anterior

    // Botones de atras y adelante.
    private JButton btnAtras;
    private JButton btnAdelante;

    // Historial simple de URLs visitadas.
    private Historial historial = new Historial();

    // Para poder remover el DocumentListener correctamente al cerrar la pestaña
    private DocumentListener documentListener;

    // Guardamos el encabezado de la pesta?a para poder cambiar su titulo.
    private CustomTabHeader tabHeader;

    public BrowserTabPanel(main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        JPanel panelTop = createSearchPanel();
        add(panelTop, BorderLayout.NORTH);

        renderizador = new Renderizador();
        renderizador.aplicarTemaVisual(mainFrame.getFondoActual(), mainFrame.getTextoActual());

        // Si se hace click en un enlace de texto dentro del HTML,
        // el renderizador nos avisa y volvemos a procesar esa nueva URL.
        renderizador.setNavegacionListener(nuevaRuta -> {
            localBuscador.setText(nuevaRuta);
            procesarURLweb(nuevaRuta, renderizador);
        });

        add(renderizador, BorderLayout.CENTER);
        setupListeners();
        actualizarBotones();
    }

    // Esta funcion recibe el encabezado que se crea en la ventana principal.
    public void setTabHeader(CustomTabHeader tabHeader) {
        this.tabHeader = tabHeader;
    }

    private JPanel createSearchPanel() {
        // Este panel contiene botones de navegacion, buscador y boton Ir.
        JPanel panelTop = new JPanel(new GridBagLayout());
        panelTop.setBackground(new Color(245, 245, 245));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        btnAtras = crearBotonNav("<");
        btnAdelante = crearBotonNav(">");

        btnAtras.addActionListener(e -> irAtras());
        btnAdelante.addActionListener(e -> irAdelante());

        JButton btnHistorial = new JButton("◔");
        btnHistorial.setFont(new Font("Arial", Font.PLAIN, 16));
        btnHistorial.setPreferredSize(new Dimension(45, 35));
        btnHistorial.setFocusPainted(false);
        btnHistorial.setBorderPainted(false);
        btnHistorial.setBackground(new Color(230, 230, 230));
        btnHistorial.setForeground(new Color(60, 60, 60));

        btnHistorial.addActionListener(e -> mostrarHistorial(btnHistorial));
        
        JButton btnRecargar = new JButton("↺");
        btnRecargar.setFont(new Font("Arial", Font.BOLD, 18));
        btnRecargar.setPreferredSize(new Dimension(45, 35));
        btnRecargar.setFocusPainted(false);
        btnRecargar.setBorderPainted(false);
        btnRecargar.setBackground(new Color(230, 230, 230));
        btnRecargar.setForeground(new Color(60, 60, 60));
        btnRecargar.addActionListener(e -> recargarPagina());
        
        localBuscador = new JTextField(25);
        localBuscador.setBackground(Color.WHITE);
        localBuscador.setForeground(new Color(60, 60, 60));
        localBuscador.setCaretColor(new Color(100, 100, 100));
        localBuscador.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        localBoton = new JButton("Ir");
        localBoton.setFont(new Font("Arial", Font.BOLD, 13));
        localBoton.setFocusPainted(false);
        localBoton.setBorderPainted(false);
        localBoton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        localBoton.setBackground(new Color(180, 180, 180));
        localBoton.setForeground(Color.WHITE);
        localBoton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        panelTop.add(btnAtras, gbc);

        gbc.gridx = 1;
        panelTop.add(btnAdelante, gbc);

        gbc.gridx = 2;
        panelTop.add(btnRecargar, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 5);
        panelTop.add(btnHistorial, gbc);

        gbc.gridx = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelTop.add(localBuscador, gbc);

        gbc.gridx = 5; 
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelTop.add(localBoton, gbc);
        
        return panelTop;
    }

    private JButton crearBotonNav(String texto) {
        // Esta funcion crea un boton de navegacion con el mismo estilo visual.
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(45, 35));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(new Color(60, 60, 60));
        return btn;
    }

// Funciones para botones de navegacion que se implementaran correctamente mas adelante

    private void irAtras() {
        // Si existe una URL anterior, intercambiamos la actual por la anterior.
        if (!urlAnterior.isEmpty()) {
            String temp = urlActual;
            urlActual = urlAnterior;
            urlAnterior = temp;

            localBuscador.setText(urlActual);
            procesarURLweb(urlActual, renderizador);
            actualizarBotones();
        }
    }

    private void irAdelante() {
        // En el proyecto original este boton usaba la misma logica del boton atras.
        irAtras();
    }

    private void actualizarBotones() {
        // Si no hay URL anterior, ambos botones se desactivan.
        btnAtras.setEnabled(!urlAnterior.isEmpty());
        btnAdelante.setEnabled(!urlAnterior.isEmpty());
    }

//-----

    private void recargarPagina() {
        if (!urlActual.isEmpty()) {
            procesarURLweb(urlActual, renderizador);
        }
    }

    private void setupListeners() {
        // Si se presiona el boton Ir y el campo no esta vacio, cargamos la URL.
        localBoton.addActionListener(e -> {
            if (!localBuscador.getText().trim().isEmpty()) {
                procesarURLweb(localBuscador.getText(), renderizador);
            }
        });

        // Si se presiona Enter dentro del buscador, hacemos lo mismo que con el boton Ir.
        localBuscador.addActionListener(e -> {
            if (!localBuscador.getText().trim().isEmpty()) {
                procesarURLweb(localBuscador.getText(), renderizador);
            }
        });

        // Este listener detecta si el texto cambia para actualizar el aspecto del boton.
        documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarBotonLocal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarBotonLocal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarBotonLocal();
            }
        };

        // Efecto hover del boton Ir.
        localBoton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!localBuscador.getText().trim().isEmpty()) {
                    localBoton.setBackground(new Color(72, 93, 114));
                } else {
                    localBoton.setBackground(new Color(160, 160, 160));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                actualizarBotonLocal();
            }
        });
    }

    private void actualizarBotonLocal() {
        // Revisamos si el usuario escribio algo.
        boolean tieneTexto = !localBuscador.getText().trim().isEmpty();

        // Si no hay texto, el boton parece inactivo.
        if (!tieneTexto) {
            localBoton.setBackground(new Color(180, 180, 180));
            localBoton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            // Si si hay texto, el boton parece activo.
            localBoton.setBackground(new Color(52, 73, 94));
            localBoton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private void procesarURLweb(String texto, Renderizador renderizador) {
        // Quitamos espacios al inicio y al final.
        String textoLimpio = texto.trim();

        // Revisamos si el usuario ya escribio http:// o https://
        boolean teniaHttp = textoLimpio.startsWith("http://");
        boolean teniaHttps = textoLimpio.startsWith("https://");

        // Si no escribio protocolo, intentamos primero con https://
        if (!teniaHttp && !teniaHttps) {
            textoLimpio = "https://" + textoLimpio;
        }

        final String urlFinal = textoLimpio;

        // Esta variable nos dice si despues podemos intentar un fallback a http.
        final boolean usarFallbackHttp = !teniaHttp && !teniaHttps;

        // Mensaje visual mientras carga la pagina.
        mainFrame.etiquetaEstado.setText("Cargando...");
        mainFrame.etiquetaEstado.setForeground(new Color(41, 128, 185));

        // Usamos un Thread para que la interfaz no se congele.
        new Thread(() -> {
            try {
                RespuestaHttp respuesta = peticionHttpGET(urlFinal);

                // Si el usuario no escribio protocolo y el primer intento falla de forma recuperable,
                // intentamos la misma direccion por http.
                if (usarFallbackHttp && respuesta.permiteFallbackHttp()) {
                    respuesta = peticionHttpGET("http://" + texto.trim());
                }

                final RespuestaHttp respuestaFinal = respuesta;

                SwingUtilities.invokeLater(() -> {
                    renderizador.cargarURL(respuestaFinal.cuerpoHtml, respuestaFinal.urlFinal);
                    localBuscador.setText(respuestaFinal.urlFinal);
                    historial.visitar(respuestaFinal.urlFinal);
                    urlActual = respuestaFinal.urlFinal;
                    actualizarTituloPestana(respuestaFinal.urlFinal);
                    mainFrame.etiquetaEstado.setText(respuestaFinal.codigoEstado);
                    mainFrame.etiquetaEstado.setForeground(new Color(46, 204, 113));
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    urlActual = urlFinal;
                    mainFrame.etiquetaEstado.setText("Error");
                    mainFrame.etiquetaEstado.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(mainFrame, "Error al cargar la pagina: " + e.getMessage());
                });
            }
        }).start();
    }
    
    // Esta funcion toma la URL final y usa su dominio como titulo de la pesta?a.
    private void actualizarTituloPestana(String urlTexto) {
        // Si no hay encabezado, no hacemos nada.
        if (tabHeader == null) {
            return;
        }
        try {
            URL url = new URL(urlTexto);
            String dominio = url.getHost();
            // Quitamos el prefijo www. para que el titulo quede mas corto.
            if (dominio.startsWith("www.")) {
                dominio = dominio.substring(4);
            }
            // Si el dominio esta vacio, usamos la URL completa.
            if (dominio.isEmpty()) {
                dominio = urlTexto;
            }
            tabHeader.setTitulo(dominio);
        } catch (Exception e) {
            // Si la URL no se puede leer bien, dejamos el titulo actual.
        }
    }
private void mostrarHistorial(JButton btnHistorial) {
    JPopupMenu menu = new JPopupMenu("Historial");
    LinkedList<String> urls = historial.getHistorial();

    if (urls.isEmpty()) {
        JMenuItem vacio = new JMenuItem("No se han visitado paginas");
        vacio.setEnabled(false);
        menu.add(vacio);
    } else {
        for (int i = urls.size() - 1; i >= 0; i--) {
            String url = urls.get(i);
            String etiqueta = url.length() > 60 ? url.substring(0, 57) + "..." : url;
            JMenuItem item = new JMenuItem(etiqueta);
            item.setToolTipText(url);

            item.addActionListener(e -> {
                localBuscador.setText(url);
                procesarURLweb(url, renderizador);
            });
            menu.add(item);
        }
    }

    menu.show(btnHistorial, 0, btnHistorial.getHeight());
}
    public void aplicarTemaVisual(Color fondo, Color texto) {
        if (renderizador != null) {
            renderizador.aplicarTemaVisual(fondo, texto);
        }
    }

    public static RespuestaHttp peticionHttpGET(String urlTexto) {
        // Esta funcion hace toda la conexion.
        // La idea es que la parte de red quede aqui y no arriba en una variable global.

        HttpURLConnection conexion = null;
        String urlActualPeticion = urlTexto;

        try {
            // Usamos un while por si la pagina redirige a otra.
            while (true) {
                URL url = construirURLValida(urlActualPeticion);

                // Si es https usamos HttpsURLConnection.
                // Si es http usamos HttpURLConnection normal.
                if (url.getProtocol().equalsIgnoreCase("https")) {
                    conexion = (HttpsURLConnection) url.openConnection();
                } else {
                    conexion = (HttpURLConnection) url.openConnection();
                }

                // Configuracion basica de la conexion.
                conexion.setRequestMethod("GET");
                conexion.setConnectTimeout(10000);
                conexion.setReadTimeout(10000);
                conexion.setInstanceFollowRedirects(false);
                conexion.setRequestProperty("User-Agent", "Mozilla/5.0 CodexBrowser/1.0");
                conexion.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conexion.setRequestProperty("Accept-Language", "es-ES,es;q=0.9,en;q=0.8");
                conexion.setRequestProperty("Accept-Encoding", "identity");

                // Leemos el codigo de respuesta del servidor.
                int codigo = conexion.getResponseCode();

                // Si el servidor responde con redireccion, buscamos la nueva direccion.
                if (codigo == HttpURLConnection.HTTP_MOVED_PERM || codigo == HttpURLConnection.HTTP_MOVED_TEMP || codigo == HttpURLConnection.HTTP_SEE_OTHER || codigo == 307 || codigo == 308) {

                    String nuevaUbicacion = conexion.getHeaderField("Location");

                    // Si no vino ubicacion nueva, salimos del while.
                    if (nuevaUbicacion == null || nuevaUbicacion.isBlank()) {
                        break;
                    }

                    // Construimos la nueva URL tomando en cuenta que podria ser relativa.
                    URL urlRedirigida = new URL(url, nuevaUbicacion);
                    urlActualPeticion = urlRedirigida.toString();

                    // Cerramos la conexion actual y repetimos el ciclo con la nueva URL.
                    conexion.disconnect();
                    conexion = null;
                    continue;
                }

                // Si no hubo redireccion, ya tenemos la respuesta final.
                break;
            }

            int codigoFinal = conexion.getResponseCode();
            String codigoEstado = codigoFinal + " " + descripcionEstado(codigoFinal);

            // Segun el codigo, algunos servidores mandan el contenido por el stream normal
            // y otros por el error stream.
            InputStream stream;
            if (codigoFinal >= 400) {
                stream = conexion.getErrorStream();
            } else {
                stream = conexion.getInputStream();
            }

            StringBuilder cuerpo = new StringBuilder();

            // Si el stream existe, leemos el contenido linea por linea.
            if (stream != null) {
                try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8))) {

                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        cuerpo.append(linea).append("\n");
                    }
                }
            }

            // Si no llego HTML visible, fabricamos una pagina minima.
            if (cuerpo.length() == 0) {
                cuerpo.append("<html><body><h1>La pagina no devolvio HTML visible.</h1></body></html>");
            }

            return new RespuestaHttp(
                codigoEstado,
                cuerpo.toString(),
                conexion.getURL().toString(),
                false
            );
        } catch (java.net.SocketTimeoutException e) {
            return new RespuestaHttp(
                "408 Timeout",
                "<html><body><h1>Limite de tiempo excedido (10s)</h1><p>El sitio no respondio a tiempo.</p></body></html>",
                urlTexto,
                true
            );
        } catch (javax.net.ssl.SSLException e) {
            return new RespuestaHttp(
                "Error SSL",
                "<html><body><h1>No se pudo establecer una conexion segura HTTPS.</h1><p>" + escaparHtml(e.getMessage()) + "</p></body></html>",
                urlTexto,
                true
            );
        } catch (IllegalArgumentException e) {
            return new RespuestaHttp(
                "URL invalida",
                "<html><body><h1>La URL ingresada no es valida.</h1></body></html>",
                urlTexto,
                false
            );
        } catch (IOException e) {
            return new RespuestaHttp(
                "Error de Red",
                "<html><body><h1>No se pudo cargar la pagina.</h1><p>" + escaparHtml(e.getMessage()) + "</p></body></html>",
                urlTexto,
                true
            );
        } finally {
            // Si la conexion se abrio, aqui la cerramos.
            if (conexion != null) {
                conexion.disconnect();
            }
        }
    }

    private static URL construirURLValida(String textoURL) throws IOException {

        String urlLimpia = textoURL.trim();

        // Si la direccion no tiene ruta despues del dominio, agregamos /.
        int indiceProtocolo = urlLimpia.indexOf("://");
        int indicePrimerSlash = urlLimpia.indexOf("/", indiceProtocolo + 3);
        if (indicePrimerSlash == -1) {
            urlLimpia = urlLimpia + "/";
        }

        URL urlBase = new URL(urlLimpia);
        String protocolo = urlBase.getProtocol();
        String host = urlBase.getHost();
        String archivo = urlBase.getFile();

        if (archivo == null || archivo.isEmpty()) {
            archivo = "/";
        }

        // Si el usuario no escribio puerto, ponemos 80 para http y 443 para https.
        int puerto = urlBase.getPort();
        if (puerto == -1) {
            if (protocolo.equalsIgnoreCase("http")) {
                puerto = 80;
            } else if (protocolo.equalsIgnoreCase("https")) {
                puerto = 443;
            }
        }

        return new URL(protocolo, host, puerto, archivo);
    }

    private static String descripcionEstado(int statusCode) {
        // Este switch traduce algunos codigos HTTP comunes.
        switch (statusCode) {
            case 200: return "OK";
            case 201: return "Created";
            case 202: return "Accepted";
            case 204: return "No Content";
            case 301: return "Moved Permanently";
            case 302: return "Found";
            case 303: return "See Other";
            case 307: return "Temporary Redirect";
            case 308: return "Permanent Redirect";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            case 502: return "Bad Gateway";
            case 503: return "Service Unavailable";
            default: return "HTTP";
        }
    }

    private static String escaparHtml(String texto) {
        // Esto evita que un mensaje de error rompa el HTML del renderizador.
        if (texto == null || texto.isBlank()) {
            return "Sin detalle adicional.";
        }
        return texto
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
    /**
     * Libera recursos cuando se cierra la pestaña
     */
    public void cleanup() {
        try {
            // Detener listener de navegacion
            if (renderizador != null) {
                renderizador.setNavegacionListener(null);
                renderizador.cleanup();
            }

            // Remover listeners de componentes
            if (localBuscador != null) {
                // Remover DocumentListener
                if (documentListener != null && localBuscador.getDocument() != null) {
                    localBuscador.getDocument().removeDocumentListener(documentListener);
                }

                // Remover ActionListeners
                ActionListener[] actionListeners = localBuscador.getActionListeners();
                for (ActionListener l : actionListeners) {
                    localBuscador.removeActionListener(l);
                }
            }

            if (localBoton != null) {
                ActionListener[] actionListenersBoton = localBoton.getActionListeners();
                for (ActionListener l : actionListenersBoton) {
                    localBoton.removeActionListener(l);
                }

                MouseListener[] mouseListeners = localBoton.getMouseListeners();
                for (MouseListener l : mouseListeners) {
                    localBoton.removeMouseListener(l);
                }
            }

            // Limpiar botones de navegacion
            if (btnAtras != null) {
                ActionListener[] atrasListeners = btnAtras.getActionListeners();
                for (ActionListener l : atrasListeners) {
                    btnAtras.removeActionListener(l);
                }
            }

            if (btnAdelante != null) {
                ActionListener[] adelanteListeners = btnAdelante.getActionListeners();
                for (ActionListener l : adelanteListeners) {
                    btnAdelante.removeActionListener(l);
                }
            }

            // Limpiar panel
            removeAll();

            // Ayudar al Garbage Collector
            renderizador = null;
            localBuscador = null;
            localBoton = null;
            btnAtras = null;
            btnAdelante = null;
            historial = null;
            documentListener = null;

        } catch (Exception e) {
            System.err.println("Error durante cleanup de BrowserTabPanel: " + e.getMessage());
        }
    }
    
}



