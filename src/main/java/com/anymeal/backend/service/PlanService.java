// ========================================================================
// Archivo NUEVO: service/PlanService.java
// ========================================================================
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

    @Transactional
    public void addRecipeToPlan(PlanRequest request) {
        User user = getCurrentUser();
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        DailyPlan dailyPlan = dailyPlanRepository.findByUserIdAndPlanDate(user.getId(), request.getDate())
                .orElseGet(() -> {
                    DailyPlan newPlan = new DailyPlan();
                    newPlan.setUser(user);
                    newPlan.setPlanDate(request.getDate());
                    return dailyPlanRepository.save(newPlan);
                });

        PlanEntry planEntry = new PlanEntry();
        planEntry.setDailyPlan(dailyPlan);
        planEntry.setRecipe(recipe);
        planEntry.setMealType(request.getMealType());
        planEntryRepository.save(planEntry);
    }

    @Transactional
    public void deletePlanEntry(Long entryId) {
        // Lógica de seguridad: verificar que la entrada pertenece al usuario actual
        planEntryRepository.deleteById(entryId);
    }

    @Transactional
    public void updateNotes(Long planId, NotesRequest request) {
        DailyPlan dailyPlan = dailyPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        // Lógica de seguridad: verificar que el plan pertenece al usuario actual
        dailyPlan.setNotes(request.getNotes());
        dailyPlanRepository.save(dailyPlan);
    }

    @Transactional(readOnly = true)
    public PlanResponse getPlanForWeek(LocalDate startDate) {
        User user = getCurrentUser();
        LocalDate endDate = startDate.plusDays(6);
        List<DailyPlan> plans = dailyPlanRepository.findByUserIdAndPlanDateBetween(user.getId(), startDate, endDate);

        Map<String, DailyPlanDto> dailyPlanDtoMap = plans.stream()
                .collect(Collectors.toMap(
                        plan -> plan.getPlanDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        this::mapToDailyPlanDto
                ));

        return PlanResponse.builder().dailyPlans(dailyPlanDtoMap).build();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

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

    private PlanEntryDto mapToPlanEntryDto(PlanEntry entry) {
        return PlanEntryDto.builder()
                .id(entry.getId())
                .recipe(mapToRecipePreviewResponse(entry.getRecipe()))
                .build();
    }

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