package hr.algebra.util;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class HttpUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private HttpUtil() {}

    public static Optional<String> getJSON(){
        try{
            HttpRequest requrest = HttpRequest.newBuilder().uri(URI.create("https://dummyjson.com/c/6753-b72c-4b23-89e9"))
                    .header("Accept", "application/json").build();

            HttpResponse<String>response = CLIENT.send(requrest, HttpResponse.BodyHandlers.ofString());
            return Optional.of(response.body());
        }
        catch (Exception e){
            LoggingUtil.logError("Http error", e); // da se ne hvata u mainu, samo nego se logira korisnik moze idalje normalno koristit aplikaciju
            return Optional.empty();
        }
    }
}
