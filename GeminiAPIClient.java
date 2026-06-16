import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class GeminiAPIClient {
    private static final String API_KEY = ""; // Reemplaza con tu API key
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

public static String procesarComando(String comando) {
        if (API_KEY.equals("\"") || API_KEY.isBlank()) {
            return generarHtmlError("ERROR","API Key no configurada");
        }
        try {
            String jsonRequest = """
                {
                    "contents": [{
                        "parts": [{
                            "text": "%s"
                        }]
                    }],
                    "generationConfig": {
                        "temperature": 0.7,
                        "maxOutputTokens": 2048
                    }
                }
                """.formatted(escaparJson(comando));

            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                String errorBody = leerCuerpo(conn.getErrorStream());
                return generarHtmlError("Error " + responseCode, errorBody);
            }

            String respuestaJson = leerCuerpo(conn.getInputStream());
            String textoRespuesta = extraerTextoDeGemini(respuestaJson);

            return generarHtmlRespuesta(textoRespuesta);

        } catch (SocketTimeoutException e) {
            return generarHtmlError("Tiempo de espera agotado", "La API de Gemini tardó más de 10 segundos en responder.");
        } catch (Exception e) {
            return generarHtmlError("Error de conexión", e.getMessage());
        }
    }

    private static String extraerTextoDeGemini(String json) {
        try {
            int start = json.indexOf("\"text\": \"");
            if (start == -1) return "No se pudo procesar la respuesta de Gemini.";
            start += 9;
            int end = json.indexOf("\"", start);
            String texto = json.substring(start, end);

            
            texto = texto.replace("\\n", "\n")
                        .replace("\\\"", "\"")
                        .replace("\\\\", "\\");

            return texto;
        } catch (Exception e) {
            return "Error al procesar la respuesta de la IA.";
        }
    }

    private static String generarHtmlRespuesta(String texto) {
        String html = texto.replace("\n", "<br>")
                           .replace("**", "<b>")
                           .replace("*", "<i>");

        return """
            <html>
            <head><style>
                body { font-family: Arial, sans-serif; padding: 20px; line-height: 1.6; }
                h1, h2 { color: #1e40af; }
            </style></head>
            <body>
                <h1>Asistente IA</h1>
                <div style="background:#f8fafc; padding:15px; border-radius:8px; border-left:4px solid #3b82f6;">
                    %s
                </div>
                <hr>
                <small>Respuesta generada por Gemini • Codex Browser</small>
            </body>
            </html>
            """.formatted(html);
    }

    private static String generarHtmlError(String titulo, String mensaje) {
        return """
            <html>
            <body style="font-family:Arial; padding:30px;">
                <h1 style="color:#ef4444;">⚠️ %s</h1>
                <p>%s</p>
            </body>
            </html>
            """.formatted(titulo, mensaje.replace("\n", "<br>"));
    }

    private static String leerCuerpo(InputStream stream) throws IOException {
        if (stream == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    private static String escaparJson(String texto) {
        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");
    }
}
