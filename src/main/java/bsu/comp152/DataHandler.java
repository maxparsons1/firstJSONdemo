package bsu.comp152;

import com.google.gson.Gson;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class DataHandler {
    private HttpClient dataGrabber;
    private String webLocation;

    public DataHandler(String webLocation) {
        dataGrabber = HttpClient.newHttpClient();
        this.webLocation = webLocation;
    }

    public ArrayList<recipeDataType> getData(){
        var requestBuilder = HttpRequest.newBuilder();// create request
        var dataRequest = requestBuilder.uri(URI.create(webLocation)).build(); // create request
        HttpResponse<String> response = null;
        try{
            response = dataGrabber.send(dataRequest, HttpResponse.BodyHandlers.ofString()); // data expected to be text
        }
        catch(IOException e){
            System.out.println("Error connecting to network or site");
        }
        catch (InterruptedException e){
            System.out.println("Connections to site broke");
        }
        if(response == null){
            System.out.println("Something went terribly wrong, ending program");
            System.exit(-1);
        }
        var usefulData = response.body(); // has the actual data
        var jsonInterrupted = new Gson();
        var recipeData = jsonInterrupted.fromJson(usefulData, responseDataType.class); // response data type
        return recipeData.results;
    }

    class responseDataType{
        String title;
        float version;
        String href;
        ArrayList<recipeDataType> results;
    }
    class recipeDataType {
        String title;
        String href;
        String ingredients;
        String thumbnail;
        @Override
        public String toString(){
            return "Recipe Title: " + title ;
        }
    }
}
