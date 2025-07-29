/*
 * Archivo: PlanService.java
 * Propósito: Este servicio contiene la lógica de negocio para la gestión del planificador de comidas.
 * Se encarga de crear y modificar planes, y de prepararlos para ser enviados al cliente.
 */
package com.anymeal.backend.service;

import com.anymeal.backend.dto.*;
import com.anymeal.backend.model.*;
import com.anymeal.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final DailyPlanRepository dailyPlanRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final PlanEntryRepository planEntryRepository;

    // Añade una receta al plan de un día específico.
    @Transactional
    public void addRecipeToPlan(PlanRequest request) {
        User user = getCurrentUser();
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
        // Busca si ya existe un plan para ese día; si no, crea uno nuevo.
        DailyPlan dailyPlan = dailyPlanRepository.findByUserIdAndPlanDate(user.getId(), request.getDate())
                .orElseGet(() -> {
                    DailyPlan newPlan = new DailyPlan();
                    newPlan.setUser(user);
                    newPlan.setPlanDate(request.getDate());
                    return dailyPlanRepository.save(newPlan);
                });
        // Crea la nueva entrada en el plan y la guarda.
        PlanEntry planEntry = new PlanEntry();
        planEntry.setDailyPlan(dailyPlan);
        planEntry.setRecipe(recipe);
        planEntry.setMealType(request.getMealType());
        planEntryRepository.save(planEntry);
    }

    // Elimina una entrada del planificador.
    @Transactional
    public void deletePlanEntry(Long entryId) {
        // TODO: Añadir lógica de seguridad para verificar que la entrada pertenece al usuario actual.
        planEntryRepository.deleteById(entryId);
    }

    // Actualiza las notas de un plan diario.
    @Transactional
    public void updateNotes(Long planId, NotesRequest request) {
        DailyPlan dailyPlan = dailyPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        // TODO: Añadir lógica de seguridad para verificar que el plan pertenece al usuario actual.
        dailyPlan.setNotes(request.getNotes());
        dailyPlanRepository.save(dailyPlan);
    }

    // Obtiene y formatea el plan para una semana completa a partir de una fecha de inicio.
    @Transactional(readOnly = true)
    public PlanResponse getPlanForWeek(LocalDate startDate) {
        User user = getCurrentUser();
        LocalDate endDate = startDate.plusDays(6);
        List<DailyPlan> plans = dailyPlanRepository.findByUserIdAndPlanDateBetween(user.getId(), startDate, endDate);
        // Convierte la lista de planes en un mapa, usando la fecha como clave.
        Map<String, DailyPlanDto> dailyPlanDtoMap = plans.stream()
                .collect(Collectors.toMap(
                        plan -> plan.getPlanDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        this::mapToDailyPlanDto
                ));
        return PlanResponse.builder().dailyPlans(dailyPlanDtoMap).build();
    }

    // Método de utilidad para obtener el usuario actualmente autenticado.
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    // Método privado para mapear una entidad DailyPlan a su DTO correspondiente.
    private DailyPlanDto mapToDailyPlanDto(DailyPlan plan) {
        Map<String, List<PlanEntryDto>> meals = plan.getEntries().stream()
                .collect(Collectors.groupingBy(
                        PlanEntry::getMealType,
                        Collectors.mapping(this::mapToPlanEntryDto, Collectors.toList())
                ));
        return DailyPlanDto.builder()
                .id(plan.getId())
                .planDate(plan.getPlanDate())
                .notes(plan.getNotes())
                .meals(meals)
                .build();
    }

    // Método privado para mapear una entidad PlanEntry a su DTO.
    private PlanEntryDto mapToPlanEntryDto(PlanEntry entry) {
        return PlanEntryDto.builder()
                .id(entry.getId())
                .recipe(mapToRecipePreviewResponse(entry.getRecipe()))
                .build();
    }

    // Método privado para mapear una entidad Recipe a un DTO de previsualización.
    private RecipePreviewResponse mapToRecipePreviewResponse(Recipe recipe) {
        return RecipePreviewResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .imageUrl(recipe.getImageUrl())
                .readyInMinutes(recipe.getReadyInMinutes() + " min")
                .difficulty(recipe.getDifficulty())
                .category(recipe.getCategory())
                .build();
    }
}