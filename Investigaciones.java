import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class Investigaciones {
	public static void main(String[] args){      
			String url = "https://www.utalca.cl/";
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(url))
					.GET()
					.build();
			try {
					HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

					System.out.println("Status code: " + response.statusCode());
					System.out.println("Response body: " + response.body());
				} 
		catch (IOException | InterruptedException e) {
						e.printStackTrace();
				}


/*

//PRUEBA DE CONEXION TCP


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
class Main {
	public static void main(String[] args) {
		String url = "https://jsonplaceholder.typicode.com/todos/1";
		String respuesta = "";
		try {
			respuesta = peticionHttpGet(url);
			System.out.println("La respuesta es:\n" + respuesta);
		} catch (Exception e) {
			// Manejar excepción
			e.printStackTrace();
		}
	}

	public static String peticionHttpGet(String urlParaVisitar) throws Exception {
		// Esto es lo que vamos a devolver
		StringBuilder resultado = new StringBuilder();
		// Crear un objeto de tipo URL
		URL url = new URL(urlParaVisitar);

		// Abrir la conexión e indicar que será de tipo GET
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
		// Búferes para leer
		BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		String linea;
		// Mientras el BufferedReader se pueda leer, agregar contenido a resultado
		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}
		// Cerrar el BufferedReader
		rd.close();
		// Regresar resultado, pero como cadena, no como StringBuilder
		return resultado.toString();
	}
}
*/

/*

// PRUEBA CONEXION TCP

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
class Main {
	public static void main(String[] args) {
		String url = "https://jsonplaceholder.typicode.com/todos/1";
		String respuesta = "";
		try {
			respuesta = peticionHttpGet(url);
			System.out.println("La respuesta es:\n" + respuesta);
		} catch (Exception e) {
			// Manejar excepción
			e.printStackTrace();
		}
	}

	public static String peticionHttpGet(String urlParaVisitar) throws Exception {
		// Esto es lo que vamos a devolver
		StringBuilder resultado = new StringBuilder();
		// Crear un objeto de tipo URL
		URL url = new URL(urlParaVisitar);

		// Abrir la conexión e indicar que será de tipo GET
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
		// Búferes para leer
		BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		String linea;
		// Mientras el BufferedReader se pueda leer, agregar contenido a resultado
		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}
		// Cerrar el BufferedReader
		rd.close();
		// Regresar resultado, pero como cadena, no como StringBuilder
		return resultado.toString();
	}
}

*/ 
/* codigo de time outs de stack overflow
public @ResponseBody ResponseEntity<TransaccionDTO> iniciarTransaccion() throws Exception {
   ResponseEntity<TransaccionDTO> response = null;
    try {

        String s = (String) CompletableFuture.supplyAsync(() -> {

            System.out.println("test");
            System.out.println("test");
            System.out.println("test");
            System.out.println("test");
            System.out.println("test");

            return null;
        }).get(10, TimeUnit.SECONDS);
    } catch (TimeoutException | ExecutionException e) {
        System.out.println("Time out has occurred");
    } catch (InterruptedException | CommandLine.ExecutionException e) {
    }catch (Exception e) {
            e.printStackTrace();
        }
     response = transaccionService.iniciarTransaccion();
    TransaccionDTO transaccion = new TransaccionDTO();
    return new ResponseEntity<TransaccionDTO>(transaccion, HttpStatus.valueOf(200));
}
 */
/* codigo para leer el estado de la pagina web, almacenar la respuesta de los header y el body a pesar de que el body de ya estaba implementado en el render anteriormente
String statusCode = "";
Map headers = new HashMap<>();
StringBuilder body = new StringBuilder();

// lee el estado de la pagina web
statusCode = reader.readLine();


String line;
while ((line = reader.readLine()) != null && !line.isEmpty()) {
int separatorIndex = line.indexOf(":");
if (separatorIndex != -1) {
String key = line.substring(0, separatorIndex).trim();
String value = line.substring(separatorIndex + 1).trim();
headers.put(key, value);
}
}

// leer el body similar al codigo del render anterior
while ((line = reader.readLine()) != null) {
body.append(line).append("\n");
}

*/
    }
}