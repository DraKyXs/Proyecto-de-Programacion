import java.awt.*;
public class MenuFavoritos {
    private JButton botonFavoritos;

    public interface AccionFavoritos {
        void abrirUrl(String url);
    }

    public MenuFavoritos() {
        jPopupMenu menu.fav = new JPopupMenu("Favoritos");

        if (favoritos.getFavoritos().isEmpty()) {
            JMenuItem vacio = new JMenuItem("No se han agregado favoritos");
            vacio.setEnabled(false);
            jpopupMenu.add(vacio);
        } else {
            for (String url : favoritos.getFavoritos()) {
                String etiqueta = url.length() > 60 ? url.substring(0, 57) + "..." : url;
                JMenuItem item = new JMenuItem(etiqueta);
                item.setToolTipText(url);

                item.addActionListener(e -> accion.abrirUrl(url));
                jpopupMenu.add(item);
            }
        }
        botonFavoritos = crearBotonTexto("★", "Favoritos");
        aplicarEfectoHover(botonFavoritos, new Color(255, 215, 0), new Color(80, 80, 80));
    } menu.fav.show(botonFavoritos, 0, botonFavoritos.getHeight());
}

