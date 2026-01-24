// Επιλογή Πακέτου
package gr.unipi.meallab.api.service;

// Εισαγωγή Βιβλιοθηκών
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.unipi.meallab.api.model.Meal;
import gr.unipi.meallab.api.model.MealResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

// Κλάση Υπηρεσίας (Service)
public class MealService {

	// Σταθερές και Εργαλεία
	private static final String API_URL = "https://www.themealdb.com/api/json/v1/1/";
	private ObjectMapper mapper = new ObjectMapper();
	private HttpClient client = HttpClient.newHttpClient();

	// Αναζήτηση με Όνομα
	public List<Meal> searchMealByName(String name) throws IOException, InterruptedException {
		System.out.println("Ζητήθηκε αναζήτηση με όνομα: " + name);
		String url = API_URL + "search.php?s=" + name.replace(" ", "%20");
		return getMealsFromUrl(url);
	}

	// Αναζήτηση με Υλικό
	public List<Meal> searchMealByIngredient(String ingredient) throws IOException, InterruptedException {
		System.out.println("Ζητήθηκε αναζήτηση με υλικό: " + ingredient);
		String url = API_URL + "filter.php?i=" + ingredient.replace(" ", "%20");
		return getMealsFromUrl(url);
	}

	// Αναζήτηση με ID
	public Meal getMealById(String id) throws IOException, InterruptedException {
		System.out.println("Ζητήθηκε αναζήτηση με ID: " + id);
		String url = API_URL + "lookup.php?i=" + id;
		List<Meal> results = getMealsFromUrl(url);

		if (results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

	// Τυχαία Συνταγή
    public Meal getRandomMeal() throws IOException, InterruptedException {
        // Καλεί το endpoint random.php
        List<Meal> results = getMealsFromUrl(API_URL + "random.php");
        
        // Επιστρέφει το πρώτο αποτέλεσμα
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
	
	// Βοηθητική Μέθοδος Σύνδεσης (HttpClient)
	private List<Meal> getMealsFromUrl(String url) throws IOException, InterruptedException {

		System.out.println("--> Σύνδεση στο URL: " + url);

		// Δημιουργία Αιτήματος
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

		// Αποστολή και Λήψη Απάντησης
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		// Έλεγχος Κωδικού (200 OK)
		System.out.println("<-- Κωδικός Απάντησης Server: " + response.statusCode());

		if (response.statusCode() != 200) {

			throw new IOException("Error: " + response.statusCode());
		}

		// Μετατροπή JSON σε Αντικείμενα
		System.out.println("Μετατροπή JSON σε Java αντικείμενα...");
		MealResponse mealResponse = mapper.readValue(response.body(), MealResponse.class);

		return mealResponse.getMeals();
	}
}