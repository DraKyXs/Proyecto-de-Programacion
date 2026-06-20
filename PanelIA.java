import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PanelIA extends JPanel{
    private JTextPane areaRespuesta;
    private JTextArea peticion;
    private JButton btnenviar;

    public PanelIA(){
        setLayout(new BorderLayout());

        areaRespuesta = new JTextPane();
        areaRespuesta.setEditable(false);
        areaRespuesta.setContentType("text/html");
        
        JScrollPane scrollRespuesta = new JScrollPane(areaRespuesta);
        add(scrollRespuesta, BorderLayout.CENTER);
        
        JPanel panelInferior = new JPanel(new BorderLayout(8,8));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));

        peticion = new JTextArea(3,40);
        peticion.setLineWrap(true);
        peticion.setWrapStyleWord(true);

        btnenviar = new JButton("Preguntar");

        panelInferior.add(new JScrollPane(peticion), BorderLayout.CENTER);
        panelInferior.add(btnenviar, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

    }


    public String getPeticionIA(){
        return peticion.getText().trim();
    }

    public void setRespuesta(String respuesta){
        areaRespuesta.setText(respuesta);
        areaRespuesta.setCaretPosition(0);
    }

    public void borrarPregunta(){
        peticion.setText("");
    }

    public void alEnviar(ActionListener listener){
        btnenviar.addActionListener(listener);
    }
}
