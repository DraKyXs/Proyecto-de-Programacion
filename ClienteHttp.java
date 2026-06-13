import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import javax.net.ssl.HttpsURLConnection;

public class ClienteHttp {

    public static RespuestaHttp peticionHttpGET(String urlTexto) {
        HttpURLConnection conexion = null;
        String urlActualPeticion = urlTexto;

        try {
            while (true) {
                URL url = UtilidadesUrl.construirURLValida(urlActualPeticion);

                if (url.getProtocol().equalsIgnoreCase("https")) {
                    conexion = (HttpsURLConnection) url.openConnection();
                } else {
                    conexion = (HttpURLConnection) url.openConnection();
                }

                conexion.setRequestMethod("GET");
                conexion.setConnectTimeout(10000);
                conexion.setReadTimeout(10000);
                conexion.setInstanceFollowRedirects(false);
                conexion.setRequestProperty("User-Agent", "Mozilla/5.0 CodexBrowser/1.0");
                conexion.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conexion.setRequestProperty("Accept-Language", "es-ES,es;q=0.9,en;q=0.8");
                conexion.setRequestProperty("Accept-Encoding", "identity");

                int codigo = conexion.getResponseCode();

                if (esRedireccion(codigo)) {
                    String nuevaUbicacion = conexion.getHeaderField("Location");

                    if (nuevaUbicacion == null || nuevaUbicacion.isBlank()) {
                        break;
                    }

                    URL urlRedirigida = new URL(url, nuevaUbicacion);
                    urlActualPeticion = urlRedirigida.toString();

                    conexion.disconnect();
                    conexion = null;
                    continue;
                }

                break;
            }

            int codigoFinal = conexion.getResponseCode();
            String codigoEstado = codigoFinal + " " + descripcionEstado(codigoFinal);
            InputStream stream = obtenerStreamRespuesta(conexion, codigoFinal);
            String cuerpo = leerCuerpoComoTexto(conexion, stream);

            if (cuerpo.isEmpty()) {
                cuerpo = "<html><body><h1>La pagina no devolvio HTML visible.</h1></body></html>";
            }

            return new RespuestaHttp(codigoEstado, cuerpo, conexion.getURL().toString(), false);
        } catch (java.net.SocketTimeoutException e) {
            return new RespuestaHttp(
                "408 Timeout",
                "<html><body><h1>Limite de tiempo excedido (10s)</h1><p>El sitio no respondio a tiempo.</p></body></html>",
                urlTexto,
                true
            );
        } catch (javax.net.ssl.SSLException e) {
            return new RespuestaHttp(
                "Error SSL",
                "<html><body><h1>No se pudo establecer una conexion segura HTTPS.</h1><p>" + escaparHtml(e.getMessage()) + "</p></body></html>",
                urlTexto,
                true
            );
        } catch (IllegalArgumentException e) {
            return new RespuestaHttp(
                "URL invalida",
                "<html><body><h1>La URL ingresada no es valida.</h1></body></html>",
                urlTexto,
                false
            );
        } catch (IOException e) {
            return new RespuestaHttp(
                "Error de Red",
                "<html><body><h1>No se pudo cargar la pagina.</h1><p>" + escaparHtml(e.getMessage()) + "</p></body></html>",
                urlTexto,
                true
            );
        } finally {
            if (conexion != null) {
                conexion.disconnect();
            }
        }
    }

    private static InputStream obtenerStreamRespuesta(HttpURLConnection conexion, int codigoFinal) throws IOException {
        if (codigoFinal >= 400) {
            return conexion.getErrorStream();
        }

        return conexion.getInputStream();
    }

    private static String leerCuerpoComoTexto(HttpURLConnection conexion, InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }

        try (InputStream streamLectura = stream;
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] bloque = new byte[4096];
            int cantidadLeida;

            while ((cantidadLeida = streamLectura.read(bloque)) != -1) {
                buffer.write(bloque, 0, cantidadLeida);
            }

            String charsetNombre = obtenerCharsetDeRespuesta(conexion);
            if (charsetNombre == null || charsetNombre.isBlank()) {
                charsetNombre = "UTF-8";
            }

            return new String(buffer.toByteArray(), Charset.forName(charsetNombre));
        }
    }

    private static String obtenerCharsetDeRespuesta(HttpURLConnection conexion) {
        String contentType = conexion.getContentType();

        if (contentType == null || contentType.isBlank()) {
            return null;
        }

        String[] partes = contentType.split(";");
        for (String parte : partes) {
            String texto = parte.trim();

            if (texto.toLowerCase().startsWith("charset=")) {
                return texto.substring("charset=".length()).trim();
            }
        }

        return null;
    }

    private static boolean esRedireccion(int codigo) {
        return codigo == HttpURLConnection.HTTP_MOVED_PERM
            || codigo == HttpURLConnection.HTTP_MOVED_TEMP
            || codigo == HttpURLConnection.HTTP_SEE_OTHER
            || codigo == 307
            || codigo == 308;
    }

    private static String descripcionEstado(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 201: return "Created";
            case 202: return "Accepted";
            case 204: return "No Content";
            case 301: return "Moved Permanently";
            case 302: return "Found";
            case 303: return "See Other";
            case 307: return "Temporary Redirect";
            case 308: return "Permanent Redirect";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            case 502: return "Bad Gateway";
            case 503: return "Service Unavailable";
            default: return "HTTP";
        }
    }

    private static String escaparHtml(String texto) {
        if (texto == null || texto.isBlank()) {
            return "Sin detalle adicional.";
        }

        return texto
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
    public static RespuestaHttp leerArchivoLocal(String rutaTexto) {
    try {
        java.io.File archivo;
        if (rutaTexto.startsWith("file:")) {
            String normalizada = rutaTexto;
            if (normalizada.startsWith("file:///")) {
            } else if (normalizada.startsWith("file://")) {
                normalizada = "file:///" + normalizada.substring(7);
            } else if (normalizada.startsWith("file:/")) {
                normalizada = "file:///" + normalizada.substring(6);
            }
            archivo = new java.io.File(new java.net.URI(normalizada));
        } else {
            archivo = new java.io.File(rutaTexto);
        }

        if (!archivo.exists()) {
            return new RespuestaHttp(
                "404 Not Found",
                "<html><body><h1>Archivo no encontrado</h1><p>" + rutaTexto + "</p></body></html>",
                rutaTexto,
                false
            );
        }

        String contenido = new String(
            java.nio.file.Files.readAllBytes(archivo.toPath()),
            java.nio.charset.StandardCharsets.UTF_8
        );

        return new RespuestaHttp(
            "200 OK",
            contenido,
            archivo.toURI().toString(),
            false
        );
    } catch (Exception e) {
        return new RespuestaHttp(
            "Error",
            "<html><body><h1>Error al leer el archivo</h1><p>" + e.getMessage() + "</p></body></html>",
            rutaTexto,
            false
        );
    }
    }
}
