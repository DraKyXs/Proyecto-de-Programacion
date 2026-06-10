import java.awt.*;
import javax.swing.*;

public class PanelNavegador extends JPanel {
    private final main mainFrame;
    private BarraNavegacion barraNavegacion;
    public Renderizador renderizador;
    private String urlActual = "";
    private String urlAnterior = "";
    private Historial historial = new Historial();
    private Favoritos favoritos;
    private EncabezadoPestana encabezadoPestana;

     public PanelNavegador(main mainFrame, Favoritos favoritos) {
        this.mainFrame = mainFrame;
        this.favoritos = favoritos;
        setLayout(new BorderLayout());

        barraNavegacion = new BarraNavegacion();
        add(barraNavegacion, BorderLayout.NORTH);

        renderizador = new Renderizador();
        renderizador.aplicarTemaVisual(mainFrame.getFondoActual(), mainFrame.getTextoActual());

        renderizador.setNavegacionListener(nuevaRuta -> {
            barraNavegacion.setTextoUrl(nuevaRuta);
            procesarURLweb(nuevaRuta);
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
        if (!urlAnterior.isEmpty()) {
            String temp = urlActual;
            urlActual = urlAnterior;
            urlAnterior = temp;

            barraNavegacion.setTextoUrl(urlActual);
            procesarURLweb(urlActual);
            actualizarBotones();
        }
    }

    private void irAdelante() {
        irAtras();
    }

    private void actualizarBotones() {
        barraNavegacion.actualizarBotonesNavegacion(!urlAnterior.isEmpty());
    }

    private void recargarPagina() {
        if (!urlActual.isEmpty()) {
            procesarURLweb(urlActual);
        }
    }

    private void procesarURLweb(String texto) {
    
        if (mainFrame.isModoOffline()) {
            mainFrame.etiquetaEstado.setText("modo offline solo se pueden archivos locales");
            mainFrame.etiquetaEstado.setForeground(new Color(180, 80, 80));
            return;
        }

        
        String textoLimpio = texto.trim();
        boolean usarFallbackHttp = !UtilidadesUrl.tieneProtocoloHttp(textoLimpio);
        final String urlFinal = UtilidadesUrl.agregarHttpsSiFalta(textoLimpio);

        mainFrame.etiquetaEstado.setText("Cargando...");
        mainFrame.etiquetaEstado.setForeground(new Color(41, 128, 185));

        new Thread(() -> {
            try {
                RespuestaHttp respuesta = ClienteHttp.peticionHttpGET(urlFinal);

                if (usarFallbackHttp && respuesta.permiteFallbackHttp()) {
                    respuesta = ClienteHttp.peticionHttpGET("http://" + texto.trim());
                }

                final RespuestaHttp respuestaFinal = respuesta;
                

                SwingUtilities.invokeLater(() -> {
                    System.out.println(respuestaFinal.cuerpoHtml);
                    renderizador.cargarURL(respuestaFinal.cuerpoHtml, respuestaFinal.urlFinal);
                    barraNavegacion.setTextoUrl(respuestaFinal.urlFinal);
                    historial.visitar(respuestaFinal.urlFinal);
                    urlAnterior = urlActual;
                    urlActual = respuestaFinal.urlFinal;
                    actualizarEstrellaFavorito();
                    actualizarTituloPestana(respuestaFinal.urlFinal);
                    actualizarBotones();
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

    public void aplicarTemaVisual(Color fondo, Color texto) {
        if (renderizador != null) {
            renderizador.aplicarTemaVisual(fondo, texto);
        }
    }
    private void toggleFavorito() {
        if (urlActual.isBlank()) return;
        if (favoritos.contiene(urlActual)) {
            favoritos.eliminar(urlActual);
        } else {
            favoritos.agregar(urlActual);
        }
        actualizarEstrellaFavorito();
    }

    private void actualizarEstrellaFavorito() {
        barraNavegacion.actualizarEstrellaFavorito(
            !urlActual.isBlank() && favoritos.contiene(urlActual)
        );
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
        } catch (Exception e) {
            System.err.println("Error durante cleanup de PanelNavegador: " + e.getMessage());
        }
    }
}
