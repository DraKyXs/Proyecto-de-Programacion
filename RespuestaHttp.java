public class RespuestaHttp {

    // Este objeto guarda el resultado de una peticion.
    public final String codigoEstado;
    public final String cuerpoHtml;
    public final String urlFinal;
    public final boolean errorRecuperable;

    public RespuestaHttp(String codigoEstado, String cuerpoHtml, String urlFinal, boolean errorRecuperable) {
        this.codigoEstado = codigoEstado;
        this.cuerpoHtml = cuerpoHtml;
        this.urlFinal = urlFinal;
        this.errorRecuperable = errorRecuperable;
    }

    public boolean permiteFallbackHttp() {
        return errorRecuperable;
    }
}
