import java.awt.*;
import javax.swing.*;

public class PanelNavegador extends JPanel {
    private final main mainFrame;
    private BarraNavegacion barraNavegacion;
    public Renderizador renderizador;
    private java.util.List<String> historialNavegacion = new java.util.ArrayList<>();
    private int indiceNavegacion = -1;
    private Historial historial = new Historial();
    private Favoritos favoritos;
    private EncabezadoPestana encabezadoPestana;
    private volatile boolean estaCargando = false;
    private MotorBusqueda motorBusqueda;

     public PanelNavegador(main mainFrame, Favoritos favoritos) {
        this.mainFrame = mainFrame;
        this.favoritos = favoritos;
        this.motorBusqueda = new MotorBusqueda();
        setLayout(new BorderLayout());

        barraNavegacion = new BarraNavegacion();
        add(barraNavegacion, BorderLayout.NORTH);

        renderizador = new Renderizador();
        renderizador.aplicarTemaVisual(mainFrame.getFondoActual(), mainFrame.getTextoActual());

        renderizador.setNavegacionListener(nuevaRuta -> {
            System.out.println("DEBUG: Navegación interceptada: " + nuevaRuta);
            if (nuevaRuta.startsWith("NEWTAB:")) {
                String urlReal = nuevaRuta.substring(7);
                System.out.println("DEBUG: Abriendo en nueva pestaña: " + urlReal);
                mainFrame.abrirNuevaPestana();
                Component comp = mainFrame.getSistemaPestanas().getComponentAt(mainFrame.getSistemaPestanas().getSelectedIndex());
                if (comp instanceof PanelNavegador) {
                    ((PanelNavegador) comp).navegar_A(urlReal);
                }
            } else {
                System.out.println("DEBUG: Navegación en pestaña actual: " + nuevaRuta);
                barraNavegacion.setTextoUrl(nuevaRuta);
                procesarURLweb(nuevaRuta);
            }
        });

        add(renderizador, BorderLayout.CENTER);
        configurarAcciones();
        actualizarBotones();
    }

    public void setEncabezadoPestana(EncabezadoPestana encabezadoPestana) {
        this.encabezadoPestana = encabezadoPestana;
    }

    private void configurarAcciones() {
        barraNavegacion.alBuscar(e -> {
            if (!barraNavegacion.getTextoUrl().isEmpty()) {
                procesarURLweb(barraNavegacion.getTextoUrl());
            }
        });

        barraNavegacion.alIrAtras(e -> irAtras());
        barraNavegacion.alIrAdelante(e -> irAdelante());
        barraNavegacion.alRecargar(e -> recargarPagina());
        barraNavegacion.alMostrarHistorial(e -> mostrarHistorial());
        barraNavegacion.alMostrarMotorBusqueda(e -> mostrarMotorBusqueda());
        barraNavegacion.alToggleFavorito(e -> toggleFavorito());
        barraNavegacion.getBotonFavoritos().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    mostrarMenuFavoritos();
                }
            }
        });
    }

    private void irAtras() {
        if (estaCargando || indiceNavegacion <= 0) return;
        indiceNavegacion--;
        String url = historialNavegacion.get(indiceNavegacion);
        barraNavegacion.setTextoUrl(url);
        procesarURLwebSinHistorial(url);
    }

    private void irAdelante() {
        if (estaCargando || indiceNavegacion >= historialNavegacion.size() - 1) return;
        indiceNavegacion++;
        String url = historialNavegacion.get(indiceNavegacion);
        barraNavegacion.setTextoUrl(url);
        procesarURLwebSinHistorial(url);
    }

    private void actualizarBotones() {
        boolean puedeIrAtras = !estaCargando && indiceNavegacion > 0;
        boolean puedeIrAdelante = !estaCargando && indiceNavegacion < historialNavegacion.size() - 1;
        barraNavegacion.actualizarBotonesNavegacion(puedeIrAtras, puedeIrAdelante);
    }

    private void recargarPagina() {
        if (indiceNavegacion >= 0 && indiceNavegacion < historialNavegacion.size()) {
            procesarURLwebSinHistorial(historialNavegacion.get(indiceNavegacion));
        }
    }

    private void procesarURLwebSinHistorial(String texto) {
        procesarURLwebInterno(texto, false);
    }

    private void procesarURLweb(String texto) {
        procesarURLwebInterno(texto, true);
    }

    private void procesarURLwebInterno(String texto, boolean agregarAlHistorial) {

        String textoLimpio = texto.trim();
        System.out.println("DEBUG: procesarURLwebInterno llamado con: " + textoLimpio);

        if (textoLimpio.startsWith("search://")) {
            System.out.println("DEBUG: Detectada búsqueda: " + textoLimpio);
            procesarBusqueda(textoLimpio, agregarAlHistorial);
            return;
        }

        boolean esLocal = UtilidadesUrl.esRutaLocal(textoLimpio);
        boolean usarFallbackHttp = !esLocal && !UtilidadesUrl.tieneProtocoloHttp(textoLimpio);
        final String urlFinal = esLocal ? textoLimpio : UtilidadesUrl.agregarHttpsSiFalta(textoLimpio);

       if (mainFrame.isModoOffline() && !UtilidadesUrl.esRutaLocal(textoLimpio)) {
            mainFrame.etiquetaEstado.setText("Modo offline — solo archivos locales");
            mainFrame.etiquetaEstado.setForeground(new Color(239, 68, 68));
            JOptionPane.showMessageDialog(mainFrame,
                "Estás en modo offline.\nIngresa una ruta local, por ejemplo:\nC:\\paginas\\index.html");
            return;
        }

        estaCargando = true;
        actualizarBotones();


        mainFrame.etiquetaEstado.setText("Cargando...");
        mainFrame.etiquetaEstado.setForeground(new Color(59, 130, 246));

        new Thread(() -> {
            try {
                RespuestaHttp respuesta;
                if (esLocal) {
                    respuesta = ClienteHttp.leerArchivoLocal(textoLimpio);
                } else {
                    respuesta = ClienteHttp.peticionHttpGET(urlFinal);
                    if (usarFallbackHttp && respuesta.permiteFallbackHttp()) {
                        respuesta = ClienteHttp.peticionHttpGET("http://" + texto.trim());
                    }
                }

                final RespuestaHttp respuestaFinal = respuesta;

                SwingUtilities.invokeLater(() -> {
                    System.out.println(respuestaFinal.cuerpoHtml);
                    renderizador.cargarURL(respuestaFinal.cuerpoHtml, respuestaFinal.urlFinal);
                    barraNavegacion.setTextoUrl(normalizarUrlMostrada(respuestaFinal.urlFinal));
                    historial.visitar(respuestaFinal.urlFinal);

                    if (agregarAlHistorial) {
                        indiceNavegacion++;
                        historialNavegacion.add(respuestaFinal.urlFinal);
                        historialNavegacion.subList(indiceNavegacion + 1, historialNavegacion.size()).clear();
                    }

                    actualizarEstrellaFavorito();
                    actualizarTituloPestana(respuestaFinal.urlFinal);
                    mainFrame.etiquetaEstado.setText(respuestaFinal.codigoEstado);
                    mainFrame.etiquetaEstado.setForeground(new Color(16, 185, 129));

                    estaCargando = false;
                    actualizarBotones();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    mainFrame.etiquetaEstado.setText("Error");
                    mainFrame.etiquetaEstado.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(mainFrame, "Error al cargar la pagina: " + e.getMessage());

                    estaCargando = false;
                    actualizarBotones();
                });
            }
        }).start();
    }

    private void actualizarTituloPestana(String urlTexto) {
        if (encabezadoPestana != null) {
            encabezadoPestana.setTitulo(UtilidadesUrl.obtenerDominio(urlTexto));
        }
    }

    private void mostrarHistorial() {
        MenuHistorial.mostrar(barraNavegacion.getBotonHistorial(), historial, url -> {
            barraNavegacion.setTextoUrl(url);
            procesarURLweb(url);
        });
    }

    private void mostrarMotorBusqueda() {
        System.out.println("DEBUG: mostrarMotorBusqueda() llamado");
        String htmlBusqueda = motorBusqueda.obtenerPaginaBusqueda();
        renderizador.cargarURL(htmlBusqueda, "search://index");
        barraNavegacion.setTextoUrl("search://index");
        indiceNavegacion++;
        historialNavegacion.add("search://index");
        historialNavegacion.subList(indiceNavegacion + 1, historialNavegacion.size()).clear();
        actualizarBotones();
        System.out.println("DEBUG: Motor de búsqueda mostrado");
    }

    private void procesarBusqueda(String urlBusqueda, boolean agregarAlHistorial) {
        String consulta = "";
        if (urlBusqueda.contains("?q=")) {
            consulta = urlBusqueda.substring(urlBusqueda.indexOf("?q=") + 3);
            consulta = java.net.URLDecoder.decode(consulta, java.nio.charset.StandardCharsets.UTF_8);
        }

        String htmlResultados = motorBusqueda.procesarBusqueda(consulta);
        renderizador.cargarURL(htmlResultados, "search://results");

        if (agregarAlHistorial) {
            indiceNavegacion++;
            historialNavegacion.add(urlBusqueda);
            historialNavegacion.subList(indiceNavegacion + 1, historialNavegacion.size()).clear();
        }

        actualizarBotones();
        mainFrame.etiquetaEstado.setText("Búsqueda completada");
        mainFrame.etiquetaEstado.setForeground(new Color(16, 185, 129));
    }

    public void aplicarTemaVisual(Color fondo, Color texto) {
        if (renderizador != null) {
            renderizador.aplicarTemaVisual(fondo, texto);
        }
    }
    private void toggleFavorito() {
        String urlActual = obtenerUrlActual();
        if (urlActual.isBlank()) return;
        if (favoritos.contiene(urlActual)) {
            favoritos.eliminar(urlActual);
        } else {
            favoritos.agregar(urlActual);
        }
        actualizarEstrellaFavorito();
    }

    private void actualizarEstrellaFavorito() {
        String urlActual = obtenerUrlActual();
        barraNavegacion.actualizarEstrellaFavorito(
            !urlActual.isBlank() && favoritos.contiene(urlActual)
        );
    }

    private String obtenerUrlActual() {
        if (indiceNavegacion >= 0 && indiceNavegacion < historialNavegacion.size()) {
            return historialNavegacion.get(indiceNavegacion);
        }
        return "";
    }
    private String normalizarUrlMostrada(String url) {
        if (url == null) return "";
        if (url.startsWith("file:///")) return url;
        if (url.startsWith("file://")) return "file:///" + url.substring(7);
        if (url.startsWith("file:/"))  return "file:///" + url.substring(6);
        return url;
    }

    private void mostrarMenuFavoritos() {
        MenuFavoritos.mostrar(
            barraNavegacion.getBotonFavoritos(),
            favoritos,
            new MenuFavoritos.AccionFavoritos() {
                @Override
                public void abrirUrl(String url) {
                    barraNavegacion.setTextoUrl(url);
                    procesarURLweb(url);
                }
                @Override
                public void eliminarUrl(String url) {
                    favoritos.eliminar(url);
                    actualizarEstrellaFavorito();
                }
            }
        );
    }
    public void navegar_A(String url) {
        barraNavegacion.setTextoUrl(url);
        procesarURLweb(url);
    }

    public void cleanup() {
        try {
            if (renderizador != null) {
                renderizador.setNavegacionListener(null);
                renderizador.cleanup();
            }

            if (barraNavegacion != null) {
                barraNavegacion.cleanup();
            }

            removeAll();
            renderizador = null;
            barraNavegacion = null;
            historial = null;
            favoritos = null;
            encabezadoPestana = null;
            motorBusqueda = null;
        } catch (Exception e) {
            System.err.println("Error durante cleanup de PanelNavegador: " + e.getMessage());
        }
    }
    private String detectarComandoIA(String texto) {
    String t = texto.toLowerCase();
    if (t.startsWith("ai") || t.startsWith("gemini ") || 
        t.startsWith("asistente ") || t.startsWith("ia:")) {
        
        if (t.startsWith("ai")) return texto.substring(3).trim();
        if (t.startsWith("ia")) return texto.substring(3).trim();
        if (t.startsWith("gemini ")) return texto.substring(7).trim();
        if (t.startsWith("asistente ")) return texto.substring(10).trim();
    }
    return null;
}

private void procesarComandoIA(String comando, boolean agregarAlHistorial) {
    estaCargando = true;
    actualizarBotones();
    mainFrame.etiquetaEstado.setText("Consultando Asistente IA...");
    mainFrame.etiquetaEstado.setForeground(new Color(59, 130, 246));

    new Thread(() -> {
        try {
            String htmlRespuesta = GeminiAPIClient.procesarComando(comando);

            SwingUtilities.invokeLater(() -> {
                renderizador.cargarURL(htmlRespuesta, "https://asistente.local");
                barraNavegacion.setTextoUrl("Asistente IA: " + comando);
                
                if (agregarAlHistorial) {
                    indiceNavegacion++;
                    String urlFake = "ia://" + comando;
                    historialNavegacion.add(urlFake);
                    historialNavegacion.subList(indiceNavegacion + 1, historialNavegacion.size()).clear();
                }

                actualizarTituloPestana("Asistente IA");
                mainFrame.etiquetaEstado.setText("200 OK - IA");
                mainFrame.etiquetaEstado.setForeground(new Color(16, 185, 129));

                estaCargando = false;
                actualizarBotones();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                mainFrame.etiquetaEstado.setText("Error IA");
                mainFrame.etiquetaEstado.setForeground(Color.RED);
                estaCargando = false;
                actualizarBotones();
            });
        }
    }).start();
}
}
