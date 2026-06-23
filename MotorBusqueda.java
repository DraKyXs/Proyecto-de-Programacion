import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotorBusqueda {
    private Map<String, List<ResultadoBusqueda>> busquedasPreestablecidas;

    public MotorBusqueda() {
        inicializarBusquedas();
    }

    private void inicializarBusquedas() {
        busquedasPreestablecidas = new HashMap<>();

        busquedasPreestablecidas.put("listar universidades chilenas", Arrays.asList(
            new ResultadoBusqueda("Universidades de Chile - Wikipedia", "https://es.wikipedia.org/wiki/Universidades_de_Chile"),
            new ResultadoBusqueda("Anexo: Universidades de Chile", "https://es.wikipedia.org/wiki/Anexo:Universidades_de_Chile"),
            new ResultadoBusqueda("Universidad de Chile - Wikipedia", "https://es.wikipedia.org/wiki/Universidad_de_Chile"),
            new ResultadoBusqueda("Pontificia Universidad Catolica de Chile", "https://es.wikipedia.org/wiki/Pontificia_Universidad_Cat%C3%B3lica_de_Chile"),
            new ResultadoBusqueda("Universidad de Concepcion", "https://es.wikipedia.org/wiki/Universidad_de_Concepci%C3%B3n"),
            new ResultadoBusqueda("Universidad de Valparaiso", "https://es.wikipedia.org/wiki/Universidad_de_Valpara%C3%ADso"),
            new ResultadoBusqueda("Universidad Tecnica Federico Santa Maria", "https://es.wikipedia.org/wiki/Universidad_T%C3%A9cnica_Federico_Santa_Mar%C3%ADa"),
            new ResultadoBusqueda("Universidad Austral de Chile", "https://es.wikipedia.org/wiki/Universidad_Austral_de_Chile"),
            new ResultadoBusqueda("Universidad de La Frontera", "https://es.wikipedia.org/wiki/Universidad_de_La_Frontera"),
            new ResultadoBusqueda("Educacion superior en Chile", "https://es.wikipedia.org/wiki/Educaci%C3%B3n_superior_en_Chile")
        ));

        busquedasPreestablecidas.put("clima en santiago", Arrays.asList(
            new ResultadoBusqueda("Clima de Santiago de Chile", "https://es.wikipedia.org/wiki/Santiago_de_Chile#Clima"),
            new ResultadoBusqueda("Santiago de Chile - Wikipedia", "https://es.wikipedia.org/wiki/Santiago_de_Chile"),
            new ResultadoBusqueda("Region Metropolitana de Santiago", "https://es.wikipedia.org/wiki/Regi%C3%B3n_Metropolitana_de_Santiago"),
            new ResultadoBusqueda("Clima mediterraneo", "https://es.wikipedia.org/wiki/Clima_mediterr%C3%A1neo"),
            new ResultadoBusqueda("Direccion Meteorologica de Chile", "https://es.wikipedia.org/wiki/Direcci%C3%B3n_Meteorol%C3%B3gica_de_Chile"),
            new ResultadoBusqueda("Meteorologia", "https://es.wikipedia.org/wiki/Meteorolog%C3%ADa"),
            new ResultadoBusqueda("Temperatura", "https://es.wikipedia.org/wiki/Temperatura"),
            new ResultadoBusqueda("Humedad", "https://es.wikipedia.org/wiki/Humedad"),
            new ResultadoBusqueda("Presion atmosferica", "https://es.wikipedia.org/wiki/Presi%C3%B3n_atmosf%C3%A9rica"),
            new ResultadoBusqueda("Viento", "https://es.wikipedia.org/wiki/Viento")
        ));

        busquedasPreestablecidas.put("python tutorial", Arrays.asList(
            new ResultadoBusqueda("Python Tutorial Oficial", "https://docs.python.org/es/3/tutorial/"),
            new ResultadoBusqueda("Python Library Reference", "https://docs.python.org/3/library/"),
            new ResultadoBusqueda("Python - W3Schools", "https://www.w3schools.com/python/"),
            new ResultadoBusqueda("Python Introduction", "https://www.w3schools.com/python/python_intro.asp"),
            new ResultadoBusqueda("Python Syntax", "https://www.w3schools.com/python/python_syntax.asp"),
            new ResultadoBusqueda("Python Variables", "https://www.w3schools.com/python/python_variables.asp"),
            new ResultadoBusqueda("Python Lists", "https://www.w3schools.com/python/python_lists.asp"),
            new ResultadoBusqueda("Python Functions", "https://www.w3schools.com/python/python_functions.asp"),
            new ResultadoBusqueda("Python - Wikipedia", "https://es.wikipedia.org/wiki/Python"),
            new ResultadoBusqueda("Python Software Foundation", "https://www.python.org/about/")
        ));

        busquedasPreestablecidas.put("java swing tutorial", Arrays.asList(
            new ResultadoBusqueda("Java Swing Tutorial Oficial", "https://docs.oracle.com/javase/tutorial/uiswing/"),
            new ResultadoBusqueda("Swing Components", "https://docs.oracle.com/javase/tutorial/uiswing/components/index.html"),
            new ResultadoBusqueda("Using Swing Components", "https://docs.oracle.com/javase/tutorial/uiswing/components/componentlist.html"),
            new ResultadoBusqueda("Swing Layout Managers", "https://docs.oracle.com/javase/tutorial/uiswing/layout/index.html"),
            new ResultadoBusqueda("How to Use Buttons", "https://docs.oracle.com/javase/tutorial/uiswing/components/button.html"),
            new ResultadoBusqueda("How to Use Text Fields", "https://docs.oracle.com/javase/tutorial/uiswing/components/textfield.html"),
            new ResultadoBusqueda("How to Use Panels", "https://docs.oracle.com/javase/tutorial/uiswing/components/panel.html"),
            new ResultadoBusqueda("How to Write Action Listeners", "https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html"),
            new ResultadoBusqueda("Java Swing - Wikipedia", "https://es.wikipedia.org/wiki/Swing_(biblioteca_gr%C3%A1fica)"),
            new ResultadoBusqueda("Java - Wikipedia", "https://es.wikipedia.org/wiki/Java_(lenguaje_de_programaci%C3%B3n)")
        ));

        busquedasPreestablecidas.put("recetas de cocina", Arrays.asList(
            new ResultadoBusqueda("Gastronomia de Chile", "https://es.wikipedia.org/wiki/Gastronom%C3%ADa_de_Chile"),
            new ResultadoBusqueda("Empanada", "https://es.wikipedia.org/wiki/Empanada"),
            new ResultadoBusqueda("Cazuela", "https://es.wikipedia.org/wiki/Cazuela_(comida)"),
            new ResultadoBusqueda("Pastel de choclo", "https://es.wikipedia.org/wiki/Pastel_de_choclo"),
            new ResultadoBusqueda("Humita", "https://es.wikipedia.org/wiki/Humita"),
            new ResultadoBusqueda("Porotos granados", "https://es.wikipedia.org/wiki/Porotos_granados"),
            new ResultadoBusqueda("Sopaipilla", "https://es.wikipedia.org/wiki/Sopaipilla"),
            new ResultadoBusqueda("Curanto", "https://es.wikipedia.org/wiki/Curanto"),
            new ResultadoBusqueda("Cocina", "https://es.wikipedia.org/wiki/Cocina"),
            new ResultadoBusqueda("Receta de cocina", "https://es.wikipedia.org/wiki/Receta_de_cocina")
        ));

        busquedasPreestablecidas.put("peliculas estrenos 2024", Arrays.asList(
            new ResultadoBusqueda("Cine en 2024 - Wikipedia", "https://es.wikipedia.org/wiki/Anexo:Cine_en_2024"),
            new ResultadoBusqueda("2024 in film", "https://en.wikipedia.org/wiki/2024_in_film"),
            new ResultadoBusqueda("Dune: parte dos", "https://es.wikipedia.org/wiki/Dune:_parte_dos"),
            new ResultadoBusqueda("Inside Out 2", "https://es.wikipedia.org/wiki/Inside_Out_2"),
            new ResultadoBusqueda("Furiosa", "https://es.wikipedia.org/wiki/Furiosa:_de_la_saga_Mad_Max"),
            new ResultadoBusqueda("Kung Fu Panda 4", "https://es.wikipedia.org/wiki/Kung_Fu_Panda_4"),
            new ResultadoBusqueda("Godzilla y Kong: el nuevo imperio", "https://es.wikipedia.org/wiki/Godzilla_y_Kong:_el_nuevo_imperio"),
            new ResultadoBusqueda("Cine", "https://es.wikipedia.org/wiki/Cine"),
            new ResultadoBusqueda("Pelicula", "https://es.wikipedia.org/wiki/Pel%C3%ADcula"),
            new ResultadoBusqueda("Historia del cine", "https://es.wikipedia.org/wiki/Historia_del_cine")
        ));

        busquedasPreestablecidas.put("musica rock clasico", Arrays.asList(
            new ResultadoBusqueda("Rock clasico", "https://es.wikipedia.org/wiki/Rock_cl%C3%A1sico"),
            new ResultadoBusqueda("Rock and roll", "https://es.wikipedia.org/wiki/Rock_and_roll"),
            new ResultadoBusqueda("The Beatles", "https://es.wikipedia.org/wiki/The_Beatles"),
            new ResultadoBusqueda("The Rolling Stones", "https://es.wikipedia.org/wiki/The_Rolling_Stones"),
            new ResultadoBusqueda("Led Zeppelin", "https://es.wikipedia.org/wiki/Led_Zeppelin"),
            new ResultadoBusqueda("Queen", "https://es.wikipedia.org/wiki/Queen"),
            new ResultadoBusqueda("Pink Floyd", "https://es.wikipedia.org/wiki/Pink_Floyd"),
            new ResultadoBusqueda("David Bowie", "https://es.wikipedia.org/wiki/David_Bowie"),
            new ResultadoBusqueda("Historia del rock", "https://es.wikipedia.org/wiki/Historia_del_rock"),
            new ResultadoBusqueda("Musica rock", "https://es.wikipedia.org/wiki/Rock")
        ));

        busquedasPreestablecidas.put("tecnologia inteligencia artificial", Arrays.asList(
            new ResultadoBusqueda("Inteligencia artificial", "https://es.wikipedia.org/wiki/Inteligencia_artificial"),
            new ResultadoBusqueda("Aprendizaje automatico", "https://es.wikipedia.org/wiki/Aprendizaje_autom%C3%A1tico"),
            new ResultadoBusqueda("Aprendizaje profundo", "https://es.wikipedia.org/wiki/Aprendizaje_profundo"),
            new ResultadoBusqueda("Red neuronal artificial", "https://es.wikipedia.org/wiki/Red_neuronal_artificial"),
            new ResultadoBusqueda("Procesamiento de lenguaje natural", "https://es.wikipedia.org/wiki/Procesamiento_de_lenguajes_naturales"),
            new ResultadoBusqueda("Vision artificial", "https://es.wikipedia.org/wiki/Visi%C3%B3n_artificial"),
            new ResultadoBusqueda("Test de Turing", "https://es.wikipedia.org/wiki/Test_de_Turing"),
            new ResultadoBusqueda("Alan Turing", "https://es.wikipedia.org/wiki/Alan_Turing"),
            new ResultadoBusqueda("arXiv inteligencia artificial", "https://arxiv.org/list/cs.AI/recent"),
            new ResultadoBusqueda("Historia de la inteligencia artificial", "https://es.wikipedia.org/wiki/Historia_de_la_inteligencia_artificial")
        ));

        busquedasPreestablecidas.put("futbol estadisticas", Arrays.asList(
            new ResultadoBusqueda("Futbol", "https://es.wikipedia.org/wiki/F%C3%BAtbol"),
            new ResultadoBusqueda("Reglas del futbol", "https://es.wikipedia.org/wiki/Reglas_del_f%C3%BAtbol"),
            new ResultadoBusqueda("Primera Division de Chile", "https://es.wikipedia.org/wiki/Primera_Divisi%C3%B3n_de_Chile"),
            new ResultadoBusqueda("Campeonato Chileno", "https://es.wikipedia.org/wiki/Primera_Divisi%C3%B3n_de_Chile"),
            new ResultadoBusqueda("Copa Mundial de Futbol", "https://es.wikipedia.org/wiki/Copa_Mundial_de_F%C3%BAtbol"),
            new ResultadoBusqueda("Liga de Campeones de la UEFA", "https://es.wikipedia.org/wiki/Liga_de_Campeones_de_la_UEFA"),
            new ResultadoBusqueda("Premier League", "https://es.wikipedia.org/wiki/Premier_League"),
            new ResultadoBusqueda("LaLiga", "https://es.wikipedia.org/wiki/Primera_Divisi%C3%B3n_de_Espa%C3%B1a"),
            new ResultadoBusqueda("Estadistica deportiva", "https://es.wikipedia.org/wiki/Estad%C3%ADstica_deportiva"),
            new ResultadoBusqueda("FIFA", "https://es.wikipedia.org/wiki/FIFA")
        ));

        busquedasPreestablecidas.put("viajes turismo", Arrays.asList(
            new ResultadoBusqueda("Turismo", "https://es.wikipedia.org/wiki/Turismo"),
            new ResultadoBusqueda("Turismo en Chile", "https://es.wikipedia.org/wiki/Turismo_en_Chile"),
            new ResultadoBusqueda("Santiago de Chile", "https://es.wikipedia.org/wiki/Santiago_de_Chile"),
            new ResultadoBusqueda("Valparaiso", "https://es.wikipedia.org/wiki/Valpara%C3%ADso"),
            new ResultadoBusqueda("San Pedro de Atacama", "https://es.wikipedia.org/wiki/San_Pedro_de_Atacama"),
            new ResultadoBusqueda("Torres del Paine", "https://es.wikipedia.org/wiki/Parque_nacional_Torres_del_Paine"),
            new ResultadoBusqueda("Isla de Pascua", "https://es.wikipedia.org/wiki/Isla_de_Pascua"),
            new ResultadoBusqueda("Chiloe", "https://es.wikipedia.org/wiki/Archipi%C3%A9lago_de_Chilo%C3%A9"),
            new ResultadoBusqueda("Viaje", "https://es.wikipedia.org/wiki/Viaje"),
            new ResultadoBusqueda("Pasaporte", "https://es.wikipedia.org/wiki/Pasaporte")
        ));

    }

    public String obtenerPaginaBusqueda() {
        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<style>\n");
        html.append("body { background-color: #f0f0f0; font-family: Arial, sans-serif; padding: 40px; margin: 0; }\n");
        html.append(".container { max-width: 600px; margin: 0 auto; }\n");
        html.append("h1 { color: #333; font-size: 44px; text-align: center; margin-bottom: 10px; }\n");
        html.append(".subtitle { color: #666; font-size: 16px; text-align: center; margin-bottom: 40px; }\n");
        html.append(".search-box { background-color: white; padding: 30px; border-radius: 8px; }\n");
        html.append(".search-item { margin-bottom: 15px; }\n");
        html.append(".search-item a { color: #4285F4; text-decoration: none; font-size: 15px; font-weight: 500; }\n");
        html.append(".search-item a:hover { text-decoration: underline; }\n");
        html.append(".footer { color: #999; font-size: 13px; text-align: center; margin-top: 30px; }\n");
        html.append("</style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <div class='container'>\n");
        html.append("    <h1>&#128269; Motor de Busqueda</h1>\n");
        html.append("    <p class='subtitle'>Selecciona una busqueda disponible:</p>\n");
        html.append("    <div class='search-box'>\n");

        for (String busqueda : busquedasPreestablecidas.keySet()) {
            String urlBusqueda = "search://query?q=" + java.net.URLEncoder.encode(busqueda, java.nio.charset.StandardCharsets.UTF_8);
            html.append("      <div class='search-item'>\n");
            html.append("        <a href='").append(urlBusqueda).append("'>\n");
            html.append("          &#10142; ").append(busqueda).append("\n");
            html.append("        </a>\n");
            html.append("      </div>\n");
        }

        html.append("    </div>\n");
        html.append("    <p class='footer'>\n");
        html.append("      O escribe tu busqueda en la barra de direccion usando:<br/>\n");
        html.append("      search://query?q=tu+busqueda\n");
        html.append("    </p>\n");
        html.append("  </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        return html.toString();
    }

    public String procesarBusqueda(String consulta) {
        if (consulta == null || consulta.trim().isEmpty()) {
            return generarHtmlSinResultados();
        }

        String consultaNormalizada = consulta.toLowerCase().trim();
        List<ResultadoBusqueda> resultados = busquedasPreestablecidas.get(consultaNormalizada);

        if (resultados == null || resultados.isEmpty()) {
            return generarHtmlSinResultados();
        }

        return generarHtmlResultados(consultaNormalizada, resultados);
    }

    private String generarHtmlResultados(String consulta, List<ResultadoBusqueda> resultados) {
        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; margin: 0; }\n");
        html.append(".container { max-width: 800px; margin: 0 auto; }\n");
        html.append("h1 { color: #333; font-size: 24px; }\n");
        html.append(".results-count { color: #888; font-size: 14px; }\n");
        html.append("hr { border: none; border-top: 1px solid #ddd; margin: 20px 0; }\n");
        html.append(".result { margin-bottom: 25px; padding-bottom: 15px; }\n");
        html.append(".result a { color: #1a73e8; text-decoration: none; font-size: 18px; font-weight: bold; }\n");
        html.append(".result a:hover { text-decoration: underline; }\n");
        html.append(".result-url { color: #006621; font-size: 14px; margin: 5px 0; }\n");
        html.append(".result-desc { color: #545454; font-size: 14px; margin: 8px 0; }\n");
        html.append("</style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <div class='container'>\n");
        html.append("    <h1>Resultados para: <em>").append(consulta).append("</em></h1>\n");
        html.append("    <p class='results-count'>Se encontraron ").append(Math.min(resultados.size(), 10)).append(" resultados</p>\n");
        html.append("    <hr/>\n");

        for (int i = 0; i < Math.min(resultados.size(), 10); i++) {
            ResultadoBusqueda resultado = resultados.get(i);
            String urlEspecial = "newtab://" + resultado.url;
            html.append("    <div class='result'>\n");
            html.append("      <a href='").append(urlEspecial).append("'>").append(resultado.titulo).append("</a>\n");
            html.append("      <p class='result-url'>").append(resultado.url).append("</p>\n");
            html.append("      <p class='result-desc'>Resultado de busqueda del indice simulado...</p>\n");
            html.append("    </div>\n");
        }

        html.append("  </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        return html.toString();
    }

    private String generarHtmlSinResultados() {
        return "<html>\n" +
            "<head><meta charset='UTF-8'></head>\n" +
            "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 40px; text-align: center;'>\n" +
            "  <h1 style='color: #333; font-size: 28px;'>No se encontraron resultados</h1>\n" +
            "  <p style='color: #888; font-size: 16px; margin-top: 20px;'>Intenta con una de las busquedas preestablecidas disponibles</p>\n" +
            "</body>\n" +
            "</html>";
    }

    private static class ResultadoBusqueda {
        String titulo;
        String url;

        ResultadoBusqueda(String titulo, String url) {
            this.titulo = titulo;
            this.url = url;
        }
    }
}
