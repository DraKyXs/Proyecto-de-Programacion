import java.util.LinkedList;
import javax.swing.*;

public class MenuHistorial {

    public interface AccionHistorial {
        void abrirUrl(String url);
    }

    public static void mostrar(JButton boton, Historial historial, AccionHistorial accion) {
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

                item.addActionListener(e -> accion.abrirUrl(url));
                menu.add(item);
            }
        }

        menu.show(boton, 0, boton.getHeight());
    }
}
