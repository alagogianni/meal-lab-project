// Επιλογή Πακέτου
package gr.unipi.meallab.app;

// Εισαγωγή Βιβλιοθηκών (JavaFX)
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image; // ΝΕΟ: Για την εικόνα
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

// Κλάση Εφαρμογής (Κληρονομεί από την Application)
public class App extends Application {

    // Μέθοδος Εκκίνησης (Η "main" του παραθύρου)
    @Override
    public void start(Stage stage) throws IOException {
        
        // Φόρτωση του αρχείου σχεδίασης (FXML)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
        
        // Δημιουργία της "ρίζας" του παραθύρου από το αρχείο σχεδίασης
        Parent root = loader.load();
        
        // Δημιουργία σκηνικού (Scene) με διαστάσεις 640x480
        Scene scene = new Scene(root, 640, 480);
        
        // Προσθήκη εικονιδίου εφαρμογής
        // Ψάχνει το αρχείο "icon.png" στον ίδιο φάκελο με το App.class (στα resources)
        try {
            // Χρησιμοποιούμε getResourceAsStream για να το βρει και μέσα στο JAR αργότερα
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Δεν βρέθηκε το εικονίδιο (icon.png). Συνεχίζω χωρίς αυτό.");
        }

        // Ρύθμιση και εμφάνιση παραθύρου (Stage)
        stage.setTitle("Meal Lab");
        //Εντολή για άνοιγμα σε πλήρες παράθυρο (Maximized)
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    // Κλασική μέθοδος main (Εκκινεί το JavaFX)
    public static void main(String[] args) {
        launch();
    }
}