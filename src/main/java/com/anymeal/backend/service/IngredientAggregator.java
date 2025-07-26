// --- PASO 4: NUEVA CLASE DE AYUDA (Ingredient Aggregator) ---
// Archivo: src/main/java/com/anymeal/backend/service/IngredientAggregator.java
package com.anymeal.backend.service;

import com.anymeal.backend.model.Ingredient;
import com.anymeal.backend.model.RecipeIngredient;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase de ayuda para sumar y agrupar ingredientes.
 */
public class IngredientAggregator {

    // Mapa para almacenar los ingredientes sumados. La clave es el ID del ingrediente.
    private final Map<Long, AggregatedIngredient> aggregated = new HashMap<>();

    /**
     * Procesa una lista de ingredientes de recetas y los suma.
     * @param ingredientsForPlan La lista de ingredientes de las recetas del plan.
     */
    public void addIngredients(List<RecipeIngredient> ingredientsForPlan) {
        for (RecipeIngredient ri : ingredientsForPlan) {
            long ingredientId = ri.getIngredient().getId();
            double amount = ri.getAmount() != null ? ri.getAmount() : 1.0; // Si no hay cantidad, asumimos 1
            String unit = ri.getUnit() != null ? ri.getUnit().toLowerCase() : "unidad";

            // Si ya tenemos este ingrediente, sumamos la cantidad.
            // NOTA: Esto solo suma si las unidades son exactamente iguales.
            // Una versión más avanzada podría convertir "tazas" a "cucharadas", etc.
            if (aggregated.containsKey(ingredientId) && aggregated.get(ingredientId).getUnit().equals(unit)) {
                aggregated.get(ingredientId).addAmount(amount);
            } else {
                // Si es un ingrediente nuevo (o una unidad diferente), lo añadimos.
                // Usamos una clave única para ingredientes con diferentes unidades.
                long uniqueKey = ingredientId + unit.hashCode();
                aggregated.put(uniqueKey, new AggregatedIngredient(ri.getIngredient(), amount, unit));
            }
        }
    }

    /**
     * Aplica lógica de redondeo para que la lista de compras sea más práctica.
     */
    public void applyShoppingLogic() {
        for (AggregatedIngredient ingredient : aggregated.values()) {
            String name = ingredient.getIngredient().getName().toLowerCase();

            // Lógica para huevos: redondear a la media docena más cercana.
            if (name.contains("egg")) {
                double amount = ingredient.getAmount();
                if (amount <= 6) {
                    ingredient.setFinalUnit("Comprar 1/2 docena");
                } else if (amount <= 12) {
                    ingredient.setFinalUnit("Comprar 1 docena");
                } else {
                    ingredient.setFinalUnit("Comprar " + (int)Math.ceil(amount / 12) + " docenas");
                }
                ingredient.setAmount(0); // Limpiamos la cantidad numérica
            }

            // Lógica para leche: si se necesita cualquier cantidad, comprar 1 cartón.
            if (name.contains("milk")) {
                ingredient.setFinalUnit("Comprar 1 cartón de leche");
                ingredient.setAmount(0);
            }
        }
    }

    public Map<Long, AggregatedIngredient> getResult() {
        return aggregated;
    }

    /**
     * Clase interna para guardar los datos de un ingrediente agregado.
     */
    @Getter
    public static class AggregatedIngredient {
        private final Ingredient ingredient;
        private double amount;
        private final String unit;
        private String finalUnit; // Para la lógica de redondeo

        public AggregatedIngredient(Ingredient ingredient, double amount, String unit) {
            this.ingredient = ingredient;
            this.amount = amount;
            this.unit = unit;
            this.finalUnit = null;
        }

        public void addAmount(double amountToAdd) {
            this.amount += amountToAdd;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public void setFinalUnit(String finalUnit) {
            this.finalUnit = finalUnit;
        }
    }
}