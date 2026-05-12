 import java.util.LinkedList;

public class Historial {
 private LinkedList<String> historial = new LinkedList<>();   
 private final int MAX_SIZE = 10;

    public void visitar(String url) {
        if (!historial.isEmpty() && historial.getLast().equals(url)) {
            return; 
        }
        if (historial.size() >= MAX_SIZE) {
            historial.removeFirst();
        }
        historial.addLast(url);
    }
    public LinkedList<String> getHistorial() {
        return historial;
    }
}
