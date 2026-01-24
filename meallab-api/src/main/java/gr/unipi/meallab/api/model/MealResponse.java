// Επιλογή Πακέτου
package gr.unipi.meallab.api.model;

// Εισαγωγή Βιβλιοθηκών
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Κλάση Απάντησης API (Wrapper)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MealResponse {

	// Λίστα Συνταγών
	private List<Meal> meals;

	// Getter
	public List<Meal> getMeals() {
		return meals;
	}

	// Setter με Print
	public void setMeals(List<Meal> meals) {
		// Τυπώνουμε πόσες συνταγές ήρθαν για να ξέρουμε ότι δούλεψε
		if (meals != null) {
			System.out.println("Setting Meal List: Found " + meals.size() + " meals.");
		} else {
			System.out.println("Setting Meal List: No meals found (null).");
		}
		this.meals = meals;
	}
}