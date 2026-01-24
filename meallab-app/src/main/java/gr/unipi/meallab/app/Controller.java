
// Επιλογή Πακέτου
package gr.unipi.meallab.app;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import gr.unipi.meallab.api.model.Meal;
import gr.unipi.meallab.api.service.MealService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.awt.Desktop;
import java.net.URI;

// Κλάση Ελεγκτή (Controller) για τη διαχείριση της λογικής του UI
public class Controller {

    // Στοιχεία διασύνδεσης (FXML)
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button randomButton;
    
    @FXML private ListView<Meal> resultsList;
    @FXML private ListView<Meal> favoritesList;
    @FXML private ListView<Meal> cookedList;

    // Διαχείριση δεδομένων και υπηρεσιών
    private MealService service = new MealService();
    private ObservableList<Meal> favoriteMeals = FXCollections.observableArrayList();
    private ObservableList<Meal> cookedMeals = FXCollections.observableArrayList();

    // Εργαλεία αποθήκευσης (JSON) 
    private static final String DATA_FILE = "meal_data.json";
    private ObjectMapper mapper = new ObjectMapper();

    // Μέθοδος αρχικοποίησης (εκτελείται αυτόματα κατά το φόρτωμα του FXML)
    @FXML
    public void initialize() {
        // Σύνδεση ενεργειών κουμπιών
        searchButton.setOnAction(event -> search());
        randomButton.setOnAction(event -> fetchRandomMeal());
        
        // Δυνατότητα αναζήτησης με το πάτημα του Enter στο πεδίο κειμένου
        searchField.setOnAction(event -> search());

        // Σύνδεση των λιστών του UI με τα δεδομένα της Java
        favoritesList.setItems(favoriteMeals);
        cookedList.setItems(cookedMeals);

        // Ρύθμιση εμφάνισης των κελιών για κάθε λίστα
        setupCustomCellFactory(resultsList, "SEARCH");
        setupCustomCellFactory(favoritesList, "FAVORITES");
        setupCustomCellFactory(cookedList, "COOKED");

        // Ρύθμιση γεγονότων κλικ για το άνοιγμα λεπτομερειών
        setupListClickEvent(resultsList);
        setupListClickEvent(favoritesList);
        setupListClickEvent(cookedList);

        // Εφαρμογή χρυσού θέματος στην αρχική οθόνη
        String listStyle = "-fx-control-inner-background: #fffcf0; -fx-background-color: #fffcf0; -fx-background-radius: 10; -fx-border-color: #f9f1d0; -fx-border-radius: 10;";
        resultsList.setStyle(listStyle);
        favoritesList.setStyle(listStyle);
        cookedList.setStyle(listStyle);
        
        // Στυλ για το πεδίο αναζήτησης
        searchField.setStyle("-fx-background-color: #fffdf5; -fx-border-color: #d4af37; -fx-border-radius: 15; -fx-background-radius: 15; -fx-text-fill: #8b7355;");

        // Ρύθμιση μηνυμάτων για άδειες λίστες (Placeholders) 
        resultsList.setPlaceholder(createPlaceholder("🔍", "Search for an ingredient..."));
        favoritesList.setPlaceholder(createPlaceholder("❤️", "No favorites yet"));
        cookedList.setPlaceholder(createPlaceholder("🍳", "No cooked meals yet"));

        // Φόρτωση Δεδομένων & Αυτόματη Αποθήκευση
        loadData();
        favoriteMeals.addListener((ListChangeListener<Meal>) c -> saveData());
        cookedMeals.addListener((ListChangeListener<Meal>) c -> saveData());
    }

    // Βοηθητική μέθοδος για τη δημιουργία όμορφων placeholders με Emoji
    private VBox createPlaceholder(String emoji, String text) {
        Label icon = new Label(emoji);
        icon.setStyle("-fx-font-size: 50px;");
        Label msg = new Label(text);
        // Χρήση Gold/Brown απόχρωσης για το κείμενο
        msg.setStyle("-fx-font-size: 16px; -fx-text-fill: #b8860b; -fx-font-weight: bold;");
        VBox box = new VBox(10, icon, msg);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    // Μέθοδοι διαχείρισης αρχείου JSON
    private void saveData() {
        try {
            AppData data = new AppData();
            data.setFavorites(new ArrayList<>(favoriteMeals));
            data.setCooked(new ArrayList<>(cookedMeals));
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                AppData data = mapper.readValue(file, AppData.class);
                if (data.getFavorites() != null) favoriteMeals.addAll(data.getFavorites());
                if (data.getCooked() != null) cookedMeals.addAll(data.getCooked());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Εσωτερική κλάση-κέλυφος για την αποθήκευση των δεδομένων
    public static class AppData {
        private List<Meal> favorites;
        private List<Meal> cooked;
        public List<Meal> getFavorites() { return favorites; }
        public void setFavorites(List<Meal> favorites) { this.favorites = favorites; }
        public List<Meal> getCooked() { return cooked; }
        public void setCooked(List<Meal> cooked) { this.cooked = cooked; }
    }

    // Ρύθμιση προσαρμοσμένων κελιών
    private void setupCustomCellFactory(ListView<Meal> list, String listType) {
        list.setCellFactory(param -> new ListCell<Meal>() {
            private ImageView imageView = new ImageView();
            private Label nameLabel = new Label();
            private Button actionBtn1 = new Button();
            private Button actionBtn2 = new Button();
            private Region spacer = new Region();
            private HBox hbox = new HBox(10);

            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                // Στυλ κειμένου συνταγής σε σκούρο χρυσό
                nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #8b7355;");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setPadding(new Insets(8));
                // Απαλό φόντο για κάθε γραμμή
                hbox.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
            }

            @Override
            protected void updateItem(Meal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    hbox.setStyle("-fx-background-color: transparent;");
                } else {
                    nameLabel.setText(item.getName());
                    if (item.getThumbnailUrl() != null) {
                        imageView.setImage(new Image(item.getThumbnailUrl(), 50, 50, true, true));
                    }
                    configureButtonsForListType(listType, item);
                    hbox.getChildren().clear();
                    hbox.getChildren().addAll(imageView, nameLabel, spacer);
                    
                    if (!actionBtn1.getText().isEmpty()) hbox.getChildren().add(actionBtn1);
                    if (!actionBtn2.getText().isEmpty()) hbox.getChildren().add(actionBtn2);
                    
                    // Εναλλαγή χρώματος γραμμών 
                    hbox.setStyle("-fx-background-color: #fffcf0; -fx-border-color: #f9f1d0; -fx-border-width: 0 0 1 0;");
                    setGraphic(hbox);
                }
            }

            // Διαμόρφωση κουμπιών ανάλογα με τη λίστα στην οποία βρίσκεται η συνταγή
            private void configureButtonsForListType(String type, Meal item) {
                actionBtn1.setText(""); actionBtn1.setOnAction(null);
                actionBtn2.setText(""); actionBtn2.setOnAction(null);

                // Σταθερό στυλ για στρογγυλεμένα κουμπιά
                String btnBaseStyle = "-fx-background-radius: 15; -fx-cursor: hand; -fx-font-weight: bold;";

                if (type.equals("SEARCH")) {
                    actionBtn1.setText("❤️");
                    actionBtn1.setStyle(btnBaseStyle + "-fx-background-color: #fdf5e6; -fx-text-fill: #d4af37; -fx-border-color: #d4af37; -fx-border-radius: 15;");
                    actionBtn1.setOnAction(e -> {
                        if (!isMealInList(favoriteMeals, item)) { 
                            favoriteMeals.add(item); 
                            showAlert("Meal Lab", "Added to Favorites!"); 
                        } else {
                            showAlert("Meal Lab", "Already in Favorites!");
                        }
                    });

                    actionBtn2.setText("🍳"); 
                    actionBtn2.setStyle(btnBaseStyle + "-fx-background-color: #faf3e0; -fx-text-fill: #b8860b; -fx-border-color: #b8860b; -fx-border-radius: 15;");
                    actionBtn2.setOnAction(e -> {
                        if (!isMealInList(cookedMeals, item)) { 
                            cookedMeals.add(item); 
                            showAlert("Meal Lab", "Added to Cooked list!"); 
                        } else {
                            showAlert("Meal Lab", "Already in Cooked list!");
                        }
                    });
                } else if (type.equals("FAVORITES")) {
                    actionBtn1.setText("🍳 Cook it!");
                    actionBtn1.setStyle(btnBaseStyle + "-fx-background-color: #d4af37; -fx-text-fill: white;");
                    actionBtn1.setOnAction(e -> {
                        if (!isMealInList(cookedMeals, item)) { 
                            cookedMeals.add(item); 
                            showAlert("Meal Lab", "Added to Cooked list!"); 
                        }
                        else { 
                            showAlert("Meal Lab", "Already in Cooked list!"); 
                        }
                    });
                    actionBtn2.setText("❌");
                    actionBtn2.setStyle(btnBaseStyle + "-fx-background-color: #fcf4dd; -fx-text-fill: #8b7355;");
                    actionBtn2.setOnAction(e -> favoriteMeals.remove(item));
                } else if (type.equals("COOKED")) {
                    actionBtn1.setText("❤️ Add To Favorites!");
                    actionBtn1.setStyle(btnBaseStyle + "-fx-background-color: #fcf4dd; -fx-text-fill: #d4af37;");
                    actionBtn1.setOnAction(e -> {
                        if (!isMealInList(favoriteMeals, item)) { 
                            favoriteMeals.add(item); 
                            showAlert("Meal Lab", "Added back to Favorites!"); 
                        } else {
                            showAlert("Meal Lab", "Already in Favorites!");
                        }
                    });
                    actionBtn2.setText("❌");
                    actionBtn2.setStyle(btnBaseStyle + "-fx-background-color: #e0e0e0;");
                    actionBtn2.setOnAction(e -> cookedMeals.remove(item));
                }
            }
        });
    }

    // Διαχείριση κλικ στη λίστα για το άνοιγμα του παραθύρου λεπτομερειών
    private void setupListClickEvent(ListView<Meal> list) {
        list.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                openDetailsWindow(newVal);
                list.getSelectionModel().clearSelection();
            }
        });
    }

    // Κύρια μέθοδος αναζήτησης συνταγών
    private void search() {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) { 
            showAlert("Meal Lab", "Please enter an ingredient!"); 
            return; 
        }
        
        searchField.clear();
        
        try {
            resultsList.getItems().clear();
            List<Meal> nameResults = service.searchMealByName(query);
            if (nameResults != null && !nameResults.isEmpty()) {
                resultsList.getItems().addAll(nameResults);
            }
            
            List<Meal> ingResults = service.searchMealByIngredient(query);
            if (ingResults != null) {
                for (Meal m : ingResults) {
                    boolean exists = false;
                    for (Meal ex : resultsList.getItems()) {
                        if (ex.getIdMeal().equals(m.getIdMeal())) exists = true;
                    }
                    if (!exists) resultsList.getItems().add(m);
                }
            }
            
            if (resultsList.getItems().isEmpty()) {
                showAlert("Meal Lab", "No recipes found for: " + query);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Meal Lab", "Something went wrong: " + e.getMessage());
        }
    }

    // Ανάκτηση τυχαίας συνταγής
    private void fetchRandomMeal() {
        try {
            Meal randomMeal = service.getRandomMeal();
            if (randomMeal != null) {
                resultsList.getItems().clear();
                resultsList.getItems().add(randomMeal);
                openDetailsWindow(randomMeal);
            } else showAlert("Meal Lab", "No random recipe found.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Meal Lab", "Connection problem: " + e.getMessage());
        }
    }

    // Δημιουργία και εμφάνιση του παραθύρου λεπτομερειών της συνταγής
    private void openDetailsWindow(Meal meal) {
        try {
            if (meal.getStrInstructions() == null || meal.getStrInstructions().isEmpty()) {
                Meal fullDetails = service.getMealById(meal.getIdMeal());
                if (fullDetails != null) meal = fullDetails;
            }

            Stage detailStage = new Stage();
            detailStage.setTitle(meal.getName()); 
            detailStage.initModality(Modality.APPLICATION_MODAL);

            try {
                Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
                detailStage.getIcons().add(appIcon);
            } catch (Exception e) {
                System.out.println("Icon load error.");
            }

            ImageView imageView = new ImageView();
            if (meal.getThumbnailUrl() != null) {
                imageView.setImage(new Image(meal.getThumbnailUrl(), 300, 300, true, true));
            }

            String infoText = "CATEGORY: " + meal.getCategory() + "  |  AREA: " + meal.getArea();
            if (meal.getStrTags() != null) infoText += "  |  TAGS: " + meal.getStrTags();
            Label infoLabel = new Label(infoText);
            
            // Στυλ κειμένου πληροφοριών
            infoLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #b8860b;");

            HBox linksBox = new HBox(10);
            linksBox.setAlignment(Pos.CENTER);
            if (meal.getStrYoutube() != null && !meal.getStrYoutube().isEmpty()) {
                Button ytBtn = new Button("▶ Watch on YouTube");
                ytBtn.setStyle("-fx-background-color: #d4af37; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 15;");
                final String ytUrl = meal.getStrYoutube();
                ytBtn.setOnAction(e -> openWebpage(ytUrl));
                linksBox.getChildren().add(ytBtn);
            }
            if (meal.getStrSource() != null && !meal.getStrSource().isEmpty()) {
                Button srcBtn = new Button("🌐 Original Source");
                srcBtn.setStyle("-fx-background-color: #8b7355; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 15;");
                final String srcUrl = meal.getStrSource();
                srcBtn.setOnAction(e -> openWebpage(srcUrl));
                linksBox.getChildren().add(srcBtn);
            }

            Label ingTitle = new Label("INGREDIENTS"); 
            ingTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #d4af37; -fx-font-size: 16px;");
            TextArea ingredientsArea = new TextArea(meal.getIngredientsFormatted());
            ingredientsArea.setEditable(false); ingredientsArea.setWrapText(true);
            // Στυλ για τα text areas σε απαλό φόντο
            ingredientsArea.setStyle("-fx-control-inner-background: #fffdf5; -fx-text-fill: #5d4037;");
            
            VBox.setVgrow(ingredientsArea, Priority.ALWAYS);
            VBox leftBox = new VBox(5, ingTitle, ingredientsArea);
            HBox.setHgrow(leftBox, Priority.ALWAYS);

            Label instTitle = new Label("INSTRUCTIONS"); 
            instTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #d4af37; -fx-font-size: 16px;");
            TextArea instructionsArea = new TextArea(meal.getStrInstructions());
            instructionsArea.setEditable(false); instructionsArea.setWrapText(true);
            instructionsArea.setStyle("-fx-control-inner-background: #fffdf5; -fx-text-fill: #5d4037;");
            
            VBox.setVgrow(instructionsArea, Priority.ALWAYS);
            VBox rightBox = new VBox(5, instTitle, instructionsArea);
            HBox.setHgrow(rightBox, Priority.ALWAYS);

            HBox splitContent = new HBox(15, leftBox, rightBox);
            VBox.setVgrow(splitContent, Priority.ALWAYS);

            VBox mainLayout = new VBox(15); 
            mainLayout.setPadding(new Insets(15));
            mainLayout.setAlignment(Pos.TOP_CENTER);
            // Φόντο παραθύρου λεπτομερειών
            mainLayout.setStyle("-fx-background-color: #fffcf0;");
            mainLayout.getChildren().addAll(imageView, infoLabel, linksBox, splitContent);

            Scene scene = new Scene(mainLayout, 850, 750); 
            detailStage.setScene(scene);
            detailStage.show(); 

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Meal Lab", "Αδυναμία ανοίγματος λεπτομερειών.");
        }
    }

    // Βοηθητική μέθοδος για το άνοιγμα συνδέσμων στον browser
    private void openWebpage(String urlString) {
        try { Desktop.getDesktop().browse(new URI(urlString)); } 
        catch (Exception e) { showAlert("Meal Lab", "Could not open link: " + e.getMessage()); }
    }

    // Βοηθητική μέθοδος για την εμφάνιση παραθύρων ειδοποίησης
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Στυλ για το Alert (περιορισμένο λόγω JavaFX Alert implementation)
        alert.showAndWait();
    }

    // Βοηθητική μέθοδος για την αποφυγή διπλοτύπων
    private boolean isMealInList(ObservableList<Meal> list, Meal meal) {
        if (meal == null) return false;
        return list.stream().anyMatch(m -> m.getIdMeal().equals(meal.getIdMeal()));
    }
}