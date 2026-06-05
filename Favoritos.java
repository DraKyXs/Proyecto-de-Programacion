import java.util.ArrayList;
import java.util.List;

public class Favoritos {

    private final List<String> favoritos = new ArrayList<>();

    public void agregar(String url) {
        if (url == null || url.isBlank()) return;
        if (!favoritos.contains(url)) favoritos.add(url);
    }

    public void eliminar(String url) {
        favoritos.remove(url);
    }

    public boolean contiene(String url) {
        return favoritos.contains(url);
    }

    public List<String> getFavoritos() {
        return new ArrayList<>(favoritos);
    }
}