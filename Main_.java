import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

class Main_ {
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

}
}