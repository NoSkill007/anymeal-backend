/*
 * Archivo: IngredientAggregator.java
 * Propósito: Clase de utilidad diseñada para agregar y consolidar una lista de ingredientes.
 * Su función es sumar las cantidades de ingredientes idénticos y aplicar lógica de compra
 * para generar una lista de compras más práctica y amigable para el usuario.
 */
package com.anymeal.backend.service;

import com.anymeal.backend.model.Ingredient;
import com.anymeal.backend.model.RecipeIngredient;
import lombok.Getter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientAggregator {

    // Mapa para almacenar los ingredientes agregados. La clave es un identificador único.
    private final Map<Long, AggregatedIngredient> aggregated = new HashMap<>();

    // Procesa una lista de RecipeIngredient y suma las cantidades.
    public void addIngredients(List<RecipeIngredient> ingredientsForPlan) {
        for (RecipeIngredient ri : ingredientsForPlan) {
            long ingredientId = ri.getIngredient().getId();
            // Si la cantidad es nula, se asume 1.
            double amount = ri.getAmount() != null ? ri.getAmount() : 1.0;
            // Si la unidad es nula, se asume "unidad". Se convierte a minúsculas para consistencia.
            String unit = ri.getUnit() != null ? ri.getUnit().toLowerCase() : "unidad";

            // Si el ingrediente ya existe con la misma unidad, se suma la cantidad.
            if (aggregated.containsKey(ingredientId) && aggregated.get(ingredientId).getUnit().equals(unit)) {
                aggregated.get(ingredientId).addAmount(amount);
            } else {
                // Si es un ingrediente nuevo o tiene una unidad diferente, se crea una nueva entrada.
                // Se genera una clave única combinando el ID y el hash de la unidad para evitar colisiones.
                long uniqueKey = ingredientId + unit.hashCode();
                aggregated.put(uniqueKey, new AggregatedIngredient(ri.getIngredient(), amount, unit));
            }
        }
    }

    // Aplica lógica de compra para redondear o agrupar ciertos ingredientes.
    public void applyShoppingLogic() {
        for (AggregatedIngredient ingredient : aggregated.values()) {
            String name = ingredient.getIngredient().getName().toLowerCase();
            // Lógica para huevos: redondear a la media docena o docena más cercana.
            if (name.contains("egg")) {
                double amount = ingredient.getAmount();
                if (amount <= 6) {
                    ingredient.setFinalUnit("Comprar 1/2 docena");
                } else if (amount <= 12) {
                    ingredient.setFinalUnit("Comprar 1 docena");
                } else {
                    ingredient.setFinalUnit("Comprar " + (int) Math.ceil(amount / 12) + " docenas");
                }
                ingredient.setAmount(0); // Se limpia la cantidad numérica pues ya no es necesaria.
            }
            // Lógica para leche: si se necesita cualquier cantidad, se sugiere comprar un cartón.
            if (name.contains("milk")) {
                ingredient.setFinalUnit("Comprar 1 cartón de leche");
                ingredient.setAmount(0);
            }
        }
    }

    // Devuelve el mapa con los ingredientes agregados y procesados.
    public Map<Long, AggregatedIngredient> getResult() {
        return aggregated;
    }

    // Clase interna para almacenar los datos de un ingrediente consolidado.
    @Getter
    public static class AggregatedIngredient {
        private final Ingredient ingredient;
        private double amount;
        private final String unit;
        // Campo para la unidad final tras aplicar la lógica de compra (ej: "Comprar 1 docena").
        private String finalUnit;

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