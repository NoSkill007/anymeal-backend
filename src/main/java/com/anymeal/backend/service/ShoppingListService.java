// Archivo: ShoppingListService.java
package com.anymeal.backend.service;

import com.anymeal.backend.dto.ShoppingListDtos.*;
import com.anymeal.backend.model.*;
import com.anymeal.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingListItemRepository shoppingListItemRepository;
    private final DailyPlanRepository dailyPlanRepository;
    private final PlanEntryRepository planEntryRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @Transactional
    public void generateListFromPlan(User user, GenerateListRequest request) {
        shoppingListItemRepository.deleteByUserId(user.getId());
        LocalDate startDate = LocalDate.parse(request.startDate());
        LocalDate endDate = LocalDate.parse(request.endDate());
        List<DailyPlan> dailyPlans = dailyPlanRepository.findByUserIdAndPlanDateBetween(user.getId(), startDate, endDate);
        if (dailyPlans.isEmpty()) return;

        List<Long> dailyPlanIds = dailyPlans.stream().map(DailyPlan::getId).collect(Collectors.toList());
        List<PlanEntry> planEntries = planEntryRepository.findByDailyPlanIdIn(dailyPlanIds);
        if (planEntries.isEmpty()) return;

        List<Long> recipeIds = planEntries.stream().map(pe -> pe.getRecipe().getId()).collect(Collectors.toList());
        List<RecipeIngredient> ingredientsForPlan = recipeIngredientRepository.findAllByRecipeIdIn(recipeIds);

        // --- LÓGICA DE SUMA Y REDONDEO ---
        IngredientAggregator aggregator = new IngredientAggregator();
        aggregator.addIngredients(ingredientsForPlan); // Suma las cantidades
        aggregator.applyShoppingLogic(); // Aplica el redondeo lógico

        // Convierte el resultado agregado a ítems de la lista de compras
        List<ShoppingListItem> newItems = aggregator.getResult().values().stream().map(agg -> {
            ShoppingListItem item = new ShoppingListItem();
            item.setUser(user);
            item.setIngredient(agg.getIngredient());

            // Si hay una unidad final (ej: "Comprar 1 docena"), se usa esa.
            if (agg.getFinalUnit() != null) {
                item.setUnit(agg.getFinalUnit());
                item.setAmount(null); // No se necesita cantidad numérica
            } else {
                item.setAmount(agg.getAmount());
                item.setUnit(agg.getUnit());
            }
            // Aquí se podría añadir una categoría si tu modelo Ingredient la tiene.
            // item.setCategory(agg.getIngredient().getCategory());
            return item;
        }).collect(Collectors.toList());

        shoppingListItemRepository.saveAll(newItems);
    }

    @Transactional(readOnly = true)
    public ShoppingListResponse getShoppingListForUser(User user) {
        List<ShoppingListItem> items = shoppingListItemRepository.findByUserId(user.getId());
        Map<String, List<ShoppingItemDto>> groupedItems = items.stream()
                .map(item -> new ShoppingItemDto(
                        item.getId(),
                        item.getIngredient() != null ? item.getIngredient().getName() : item.getCustomName(),
                        item.getAmount(), item.getUnit(), item.getCategory(), item.isChecked()
                )).collect(Collectors.groupingBy(item -> Optional.ofNullable(item.category()).orElse("Otros")));
        return new ShoppingListResponse(groupedItems);
    }

    @Transactional
    public ShoppingItemDto addItem(User user, AddItemRequest request) {
        ShoppingListItem newItem = new ShoppingListItem();
        newItem.setUser(user);
        newItem.setCustomName(request.customName());
        newItem.setAmount(request.amount());
        newItem.setUnit(request.unit());
        newItem.setCategory(Optional.ofNullable(request.category()).orElse("Manual"));
        ShoppingListItem savedItem = shoppingListItemRepository.save(newItem);
        return new ShoppingItemDto(savedItem.getId(), savedItem.getCustomName(), savedItem.getAmount(), savedItem.getUnit(), savedItem.getCategory(), savedItem.isChecked());
    }

    @Transactional
    public Optional<ShoppingItemDto> updateItem(User user, Long itemId, UpdateItemRequest request) {
        return shoppingListItemRepository.findByIdAndUserId(itemId, user.getId()).map(item -> {
            item.setChecked(request.isChecked());
            ShoppingListItem updated = shoppingListItemRepository.save(item);
            return new ShoppingItemDto(updated.getId(),
                    updated.getIngredient() != null ? updated.getIngredient().getName() : updated.getCustomName(),
                    updated.getAmount(), updated.getUnit(), updated.getCategory(), updated.isChecked());
        });
    }

    @Transactional
    public void clearCheckedItems(User user) {
        shoppingListItemRepository.deleteByUserIdAndIsChecked(user.getId(), true);
    }

    @Transactional
    public boolean deleteItem(User user, Long itemId) {
        Optional<ShoppingListItem> item = shoppingListItemRepository.findByIdAndUserId(itemId, user.getId());
        if (item.isPresent()) {
            shoppingListItemRepository.delete(item.get());
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<ShoppingItemDto> editItem(User user, Long itemId, EditItemRequest request) {
        return shoppingListItemRepository.findByIdAndUserId(itemId, user.getId()).map(item -> {
            // Actualizar solo los campos que no son null en el request
            if (request.customName() != null) {
                item.setCustomName(request.customName());
            }
            if (request.amount() != null) {
                item.setAmount(request.amount());
            }
            if (request.unit() != null) {
                item.setUnit(request.unit());
            }

            ShoppingListItem updated = shoppingListItemRepository.save(item);
            return new ShoppingItemDto(updated.getId(),
                    updated.getIngredient() != null ? updated.getIngredient().getName() : updated.getCustomName(),
                    updated.getAmount(), updated.getUnit(), updated.getCategory(), updated.isChecked());
        });
    }
}