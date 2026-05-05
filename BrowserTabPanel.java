import java.awt.*;
import java.io.*;
import java.net.Socket;
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
                procesarURLweb(localBuscador.getText(), renderizador);
            }
        });
        
        localBuscador.addActionListener(e -> {
            if(!localBuscador.getText().trim().isEmpty()){
                procesarURLweb(localBuscador.getText(), renderizador);
            }
        });
    
        localBuscador.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { actualizarBotonLocal(); }
            @Override
            public void removeUpdate(DocumentEvent e) { actualizarBotonLocal(); }
            @Override
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

























    private void procesarURLweb(String texto, Renderizador renderizador) {
        // mediante el texto.trim eliminamos los espacios en blanco de los url        
        String url = texto.trim();
        //este if hace que en caso de que la busqueda del browser no contenga "http://" o "https://" se le agregue al principio
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        
        //mensaje de carga
        mainFrame.etiquetaEstado.setText("Cargando...");
        mainFrame.etiquetaEstado.setForeground(new Color(41, 128, 185)); 
        //try y catch de conexión con el render
        
        
        try {
            String respuesta = peticionHttpGET(url);
            renderizador.cargarURL(respuesta);
        //aquí debería ir el error por tiempo de conexion del timer yo cacho
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame,
               "Error al cargar la página: " + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
        }
        //mensaje de listo
        mainFrame.etiquetaEstado.setText("Listo");
        mainFrame.etiquetaEstado.setForeground(new Color(100,100,100));

    }


			
/* codigo de timer modificado para encajar en el codigo

        String s = (String) CompletableFuture.supplyAsync(() -> {
            return null;
        }).get(10, TimeUnit.SECONDS);
    } catch (TimeoutException | ExecutionException e) {
        System.out.println("Limite de tiempo excedido");
    } catch (InterruptedException | CommandLine.ExecutionException e) 
    
 */


    public void aplicarTemaVisual(Color fondo, Color texto) {
        if (renderizador != null) {
            renderizador.aplicarTemaVisual(fondo, texto);
        }
    }


    //metodo que realiza la conexión TCP y la conexion GET
    public static String peticionHttpGET(String url){
        //establecemos el puerto 80 que es para urls con dominio}
        int puerto = 80; 
       //El StringBuilder es un String al cual se le puede ir agregando texto como una lista. Lo usamos para recibir la respuesta del servidor y despues convertirlo a String
        StringBuilder respuesta = new StringBuilder();

        try{
            String host = url;
            String ruta = "/";
            //creamos dos variables, host para contener la url sin el http que se lo quitaremos luego, y la ruta donde se guardará todo lo sigueinte a un posible slash despues del dominio (tipo "www.google.com/search" se guarda el "search" )

            if(host.startsWith("http://")){
                host = host.substring(7);
            }
            else if(host.startsWith("https://")){
                host = host.substring(8);
            }
            //con estos if y los substring, eliminaremos las etiquetas http/s:// (el substring se salta el número puesto de indices y guarda desde donde se pide )

            int indiceslash = host.indexOf("/");
            if(indiceslash != -1){
                ruta = host.substring(indiceslash);
                host = host.substring(0, indiceslash);
            }
            //con el indiceslash guardaremos el indice en el que se encuentra el slash de la ruta, y con los substring guardaremos la ruta y el host por separado (el host se guarda desde el inicio hasta el slash, y la ruta se guarda desde el slash hasta el final)                  
                    //try que permite la conexión TCP, el envio de la peticion get y la recepcion de la respuesta del servidor 
                    //El PrintWritter la usamos para enviar la petición al servidor y el BufferedReader para recibir la respuesta
                    try(Socket socket = new Socket(host, puerto);
                        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                            
                            
                            //Esta lista de prints son para depurar y verificar que el host y la ruta se hayan separado correctamente
                            //A su vez, el print de la petición GET es para verificar que se esté enviando correctamente al servidor
                            writer.println("GET "+ruta+" HTTP/1.1");
                            writer.println("Host: " + host);
                            writer.println("Connection: close");
                            writer.println();
                            String linea;
                            //este while se encarga de leer la respuesta del servidor línea por línea y agregarla al StringBuilder
                            while ((linea = reader.readLine()) != null){
                                respuesta.append(linea).append("\n");

                            }

                    }
        }
        //este catch es para notificar en caso de error en la conexión o en la petición
        catch (IOException e) {
            //necesario para poder ver el error en la consola
            e.printStackTrace();
            return "Error al realizar la petición: " + e.getMessage();
        }
        //retornamos la respuesta del servidor convertida a String
        return respuesta.toString();
    }

}