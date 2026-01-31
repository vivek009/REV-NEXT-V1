package com.revnext.repository.catalog;

import com.revnext.domain.catalog.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID>{
    @Modifying
    @Query("DELETE FROM Ingredient i WHERE i.formula.id = :formulaId")
    void deleteByFormulaId(@Param("formulaId") UUID formulaId);
}
