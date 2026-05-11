import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket; 
import javax.swing.*;
import javax.swing.event.*;




public class BrowserTabPanel extends JPanel {

    private final main mainFrame;
    private JTextField localBuscador;
    private JButton localBoton;
    public Renderizador renderizador;
    private String urlActual = "";
    private String urlAnterior = "";
    private JButton btnAtras;
    private JButton btnAdelante;

    public BrowserTabPanel(main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout()); 

        JPanel panelTop = createSearchPanel();
        add(panelTop, BorderLayout.NORTH); 

        renderizador = new Renderizador();
        renderizador.aplicarTemaVisual(mainFrame.getFondoActual(), mainFrame.getTextoActual());
        
        renderizador.setNavegacionListener(nuevaRuta -> {
            localBuscador.setText(nuevaRuta); 
            procesarURLweb(nuevaRuta, renderizador); 
        });
        
        add(renderizador, BorderLayout.CENTER);
        setupListeners();
    }

    private JPanel createSearchPanel() {
        JPanel panelTop = new JPanel(new GridBagLayout());
        panelTop.setBackground(new Color(245, 245, 245)); 
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();

        btnAtras = crearBotonNav("<");
        btnAdelante = crearBotonNav(">");

        btnAtras.addActionListener(e -> irAtras());
        btnAdelante.addActionListener(e -> irAdelante());

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
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 0, 5);
        panelTop.add(btnAtras, gbc);
        gbc.gridx = 1;
        panelTop.add(btnAdelante, gbc);
        gbc.gridx = 2; 
        gbc.weightx = 1.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        panelTop.add(localBuscador, gbc);
        gbc.gridx = 3; 
        gbc.weightx = 0; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelTop.add(localBoton, gbc);

        return panelTop;
    }

    private JButton crearBotonNav(String texto) {
    JButton btn = new JButton(texto);
    btn.setFont(new Font("Arial", Font.BOLD, 18));
    btn.setPreferredSize(new Dimension(45, 35));
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setBackground(new Color(230, 230, 230));
    btn.setForeground(new Color(60, 60, 60));
    return btn;
}

    private void irAtras() {
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
        irAtras();
    }
    private void actualizarBotones() {
        btnAtras.setEnabled(!urlAnterior.isEmpty());
        btnAdelante.setEnabled(!urlAnterior.isEmpty()); 
    }

    private void setupListeners() {
        localBoton.addActionListener(e -> {
            if(!localBuscador.getText().trim().isEmpty()){
                procesarURLweb(localBuscador.getText(), renderizador);
            }
        });
        
        localBuscador.addActionListener(e -> {
            if(!localBuscador.getText().trim().isEmpty()){
                procesarURLweb(localBuscador.getText(), renderizador);
            }
        });

        localBuscador.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { actualizarBotonLocal(); }
            @Override public void removeUpdate(DocumentEvent e) { actualizarBotonLocal(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizarBotonLocal(); }
        });

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
        boolean tieneTexto = !localBuscador.getText().trim().isEmpty();
        
        if(!tieneTexto) {
            localBoton.setBackground(new Color(180, 180, 180)); 
            localBoton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
        } else {
            localBoton.setBackground(new Color(52, 73, 94)); 
            localBoton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        }
    }


    private void procesarURLweb(String texto, Renderizador renderizador) {
        // mediante el texto.trim eliminamos los espacios en blanco de los url         
        String url = texto.trim();
        
        // este if hace que en caso de que la busqueda del browser no contenga "http://" o "https://" se le agregue al principio
        // Esto valida que la URL sea absoluta
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        
        final String urlFinal = url;

        //este if es para guardar el url, el actual pasa a ser el anterior
        if (!urlFinal.equals(urlActual)) {
        urlAnterior = urlActual;
        urlActual = urlFinal;
    }
        
        // mensaje de carga
        mainFrame.etiquetaEstado.setText("Cargando...");
        mainFrame.etiquetaEstado.setForeground(new Color(41, 128, 185)); 

        // Usamos un Thread para que el timer no congele la ventana
        new Thread(() -> {
            try {
                // El metodo ahora devuelve un String[]: [0] es el Código de estado, [1] es el HTML
                String[] respuesta = peticionHttpGET(urlFinal);
                
                SwingUtilities.invokeLater(() -> {
                    renderizador.cargarURL(respuesta[1]);
                    // mensaje de listo y el codigo de estado
                    mainFrame.etiquetaEstado.setText(respuesta[0]);
                    mainFrame.etiquetaEstado.setForeground(new Color(46, 204, 113));
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    mainFrame.etiquetaEstado.setText("Error");
                    mainFrame.etiquetaEstado.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(mainFrame, "Error al cargar la página: " + e.getMessage());
                });
            }
        }).start();
    }

    public void aplicarTemaVisual(Color fondo, Color texto) {
        if (renderizador != null) {
            renderizador.aplicarTemaVisual(fondo, texto);
        }
    }

    // metodo que realiza la conexión TCP y la conexion GET
    public static String[] peticionHttpGET(String url){
        // establecemos el puerto 80
        int puerto = 80; 
        // El StringBuilder es un String al cual se le puede ir agregando texto como una lista 
        // Lo usamos para recibir la respuesta del servidor y despues convertirlo a String
        String[] resultado = new String[2]; 
        StringBuilder cuerpo = new StringBuilder();
        String codigoEstado = "Desconocido";

        try {
            String host = url;
            String ruta = "/";
            // creamos dos variables, host para contener la url sin el http que se lo quitaremos luego, 
            // y la ruta donde se guardara todo lo sigueinte a un posible slash despues del dominio

            if(host.startsWith("http://")) host = host.substring(7);
            else if(host.startsWith("https://")) host = host.substring(8);
            // con estos if y los substring se eliminan las etiquetas http/s://

            int indiceslash = host.indexOf("/");
            if(indiceslash != -1){
                ruta = host.substring(indiceslash);
                host = host.substring(0, indiceslash);
            }
            // con el indiceslash guardaremos el indice en el que se encuentra el slash de la ruta, 
            // y con los substring guardaremos la ruta y el host por separado

            // Establecemos conexión TCP con el servidor web en el puerto 80
            Socket socket = new Socket();
            // Manejar timeouts  de los 10 segundos
            socket.connect(new InetSocketAddress(host, puerto), 10000);
            socket.setSoTimeout(10000);

            // try que permite la conexión TCP, el envio de la peticion get y la recepcion de la respuesta del servidor 
            // El PrintWriter la usamos para enviar la petición al servidor y el BufferedReader para recibir la respuesta
            try(PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                
                // Enviar solicitud HTTP GET con headers básicos
                writer.print("GET " + ruta + " HTTP/1.1\r\n");
                writer.print("Host: " + host + "\r\n");
                writer.print("User-Agent: ClienteHTTP/1.0\r\n");
                writer.print("Connection: close\r\n\r\n");
                writer.flush();

                String linea;
                boolean esPrimeraLinea = true;
                boolean esCuerpo = false;

                // este while se encarga de leer la respuesta del servidor línea por línea y agregarla al StringBuilder
                while ((linea = reader.readLine()) != null){
                    // Recibir y almacenar la respuesta: código de estado
                    if (esPrimeraLinea) {
                        if(linea.contains(" ")) {
                            codigoEstado = linea.substring(linea.indexOf(" ") + 1);
                        }
                        esPrimeraLinea = false;
                        continue;
                    }

                    // Separador de headers y cuerpo
                    if (linea.isEmpty() && !esCuerpo) {
                        esCuerpo = true;
                        continue;
                    }

                    // Almacenamos el cuerpo HTML
                    if (esCuerpo) {
                        cuerpo.append(linea).append("\n");
                    }
                }
            }
            socket.close();
        } catch (java.net.SocketTimeoutException e) {
            codigoEstado = "408 Timeout";
            cuerpo.append("<html><body>Limite de tiempo excedido (10s)</body></html>");
        } catch (IOException e) {
            // para poder ver el error en la consola
            e.printStackTrace();
            codigoEstado = "Error de Red";
        }

        // retornamos la respuesta del servidor: [0] es el estado, [1] es el HTML convertido a String
        resultado[0] = codigoEstado;
        resultado[1] = cuerpo.toString();
        return resultado;
    }
}