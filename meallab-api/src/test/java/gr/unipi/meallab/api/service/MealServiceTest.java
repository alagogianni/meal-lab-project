
//Επιλογή Πακέτου 
package gr.unipi.meallab.api.service;

//Εισαγωγή Βιβλιοθηκών (JUnit και Μοντέλα)
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import gr.unipi.meallab.api.model.Meal;

//Κλάση Ελέγχου
class MealServiceTest {

	//1: Αναζήτηση με Όνομα
	@Test
	void testSearchByName() throws Exception {
		System.out.println("TEST 1: Αναζήτηση με Όνομα");
		
		MealService service = new MealService();
		List<Meal> results = service.searchMealByName("Burger");

		// Τυπώνουμε το αποτέλεσμα και ελέγχουμε ότι δεν είναι άδεια η λίστα
		System.out.println("Βρέθηκαν: " + results.size());
		assertFalse(results.isEmpty());
	}

	//2: Αναζήτηση με Υλικό
	@Test
	void testSearchByIngredient() throws Exception {
		System.out.println("TEST 2: Αναζήτηση με Υλικό");
		
		MealService service = new MealService();
		List<Meal> results = service.searchMealByIngredient("Chicken");

		// Τυπώνουμε αποτέλεσμα και ελέγχουμε ότι δεν είναι άδεια η λίστα
		System.out.println("Βρέθηκαν: " + results.size());
		assertFalse(results.isEmpty());
	}

	//3: Αναζήτηση με ID (Συγκεκριμένη συνταγή)
	@Test
	void testGetById() throws Exception {
		System.out.println("TEST 3: Αναζήτηση με ID");
		
		MealService service = new MealService();
		// Το ID 52772 αντιστοιχεί στο Teriyaki Chicken Casserole
		Meal meal = service.getMealById("52772");

		// Ελέγχουμε ότι βρέθηκε και το όνομα είναι σωστό
		System.out.println("Βρέθηκε: " + meal.getName());
		assertNotNull(meal);
		assertEquals("Teriyaki Chicken Casserole", meal.getName());
	}

	//4: Τυχαία Συνταγή
	@Test
	void testGetRandom() throws Exception {
		System.out.println("TEST 4: Τυχαία Συνταγή");
		
		MealService service = new MealService();
		Meal meal = service.getRandomMeal();

		// Ελέγχουμε ότι επιστράφηκε κάτι (δεν είναι null)
		System.out.println("Έτυχε: " + meal.getName());
		assertNotNull(meal);
	}
}