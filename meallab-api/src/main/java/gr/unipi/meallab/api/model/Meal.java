// Επιλογή Πακέτου
package gr.unipi.meallab.api.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meal {

    // Πεδία Δεδομένων
    @JsonProperty("idMeal") private String idMeal;
    @JsonProperty("strMeal") private String strMeal;
    @JsonProperty("strCategory") private String strCategory;
    @JsonProperty("strArea") private String strArea;
    @JsonProperty("strInstructions") private String strInstructions;
    @JsonProperty("strMealThumb") private String strMealThumb;
    @JsonProperty("strTags") private String strTags;
    @JsonProperty("strYoutube") private String strYoutube;
    @JsonProperty("strSource") private String strSource;
    
    // Δυναμική Διαχείριση Υλικών
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    // Getters/Setters
    public String getIdMeal() { return idMeal; }
    public void setIdMeal(String idMeal) { this.idMeal = idMeal; }
    
    public String getName() { return strMeal; } 
    public String getStrMeal() { return strMeal; } 
    public void setStrMeal(String strMeal) { this.strMeal = strMeal; }

    public String getCategory() { return strCategory; }
    public void setStrCategory(String strCategory) { this.strCategory = strCategory; }

    public String getArea() { return strArea; }
    public void setStrArea(String strArea) { this.strArea = strArea; }

    public String getInstructions() { return strInstructions; }
    public String getStrInstructions() { return strInstructions; }
    public void setStrInstructions(String strInstructions) { this.strInstructions = strInstructions; }

    public String getThumbnailUrl() { return strMealThumb; }
    public String getStrMealThumb() { return strMealThumb; }
    public void setStrMealThumb(String strMealThumb) { this.strMealThumb = strMealThumb; }

    public String getStrTags() { return strTags; }
    public void setStrTags(String strTags) { this.strTags = strTags; }

    public String getStrYoutube() { return strYoutube; }
    public void setStrYoutube(String strYoutube) { this.strYoutube = strYoutube; }

    public String getStrSource() { return strSource; }
    public void setStrSource(String strSource) { this.strSource = strSource; }

    // Μέθοδος Μορφοποίησης Συστατικών
    public String getIngredientsFormatted() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        while (true) {
            String ingKey = "strIngredient" + i;
            String msrKey = "strMeasure" + i;

            if (additionalProperties.containsKey(ingKey)) {
                String ing = (String) additionalProperties.get(ingKey);
                String msr = (String) additionalProperties.get(msrKey);

                if (ing != null && !ing.trim().isEmpty()) {
                    sb.append("• ").append(ing);
                    if (msr != null && !msr.trim().isEmpty()) {
                        sb.append(": ").append(msr);
                    }
                    sb.append("\n");
                }
            } else {
                if (i > 5 && !additionalProperties.containsKey(ingKey)) break; 
                if (i >= 20) break; 
            }
            i++;
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "Meal: " + strMeal;
    }
}