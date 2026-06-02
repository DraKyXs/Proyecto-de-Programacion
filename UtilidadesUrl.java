import java.io.IOException;
import java.net.URL;

public class UtilidadesUrl {

    public static String agregarHttpsSiFalta(String textoURL) {
        String textoLimpio = textoURL.trim();

        if (!tieneProtocoloHttp(textoLimpio)) {
            textoLimpio = "https://" + textoLimpio;
        }

        return textoLimpio;
    }

    public static boolean tieneProtocoloHttp(String textoURL) {
        return textoURL.startsWith("http://") || textoURL.startsWith("https://");
    }

    public static URL construirURLValida(String textoURL) throws IOException {
        String urlLimpia = textoURL.trim();

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

    public static String obtenerDominio(String urlTexto) {
        try {
            URL url = new URL(urlTexto);
            String dominio = url.getHost();

            if (dominio.startsWith("www.")) {
                dominio = dominio.substring(4);
            }

            if (dominio.isEmpty()) {
                return urlTexto;
            }

            return dominio;
        } catch (Exception e) {
            return urlTexto;
        }
    }
}
