package com.ecosorter.repository;

import com.ecosorter.model.WasteCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Waste category repository interface
 */
@Repository
public interface WasteCategoryRepository extends MongoRepository<WasteCategory, String> {
    
    Optional<WasteCategory> findByName(String name);
    
    Optional<WasteCategory> findByCode(String code);
    
    List<WasteCategory> findByType(WasteCategory.WasteType type);
    
    List<WasteCategory> findByIsActive(Boolean isActive);
    
    List<WasteCategory> findByParentCategory(String parentCategory);
    
    List<WasteCategory> findByPriorityGreaterThanEqual(Integer priority);
    
    @Query("{'examples': {'$in': [?0]}}")
    List<WasteCategory> findByExamplesContaining(String example);
    
    @Query("{'name': {'$regex': ?0, '$options': 'i'}}")
    List<WasteCategory> findByNameContainingIgnoreCase(String name);
    
    @Query("{'description': {'$regex': ?0, '$options': 'i'}}")
    List<WasteCategory> findByDescriptionContainingIgnoreCase(String description);
    
    @Query("{'disposalMethods': {'$in': [?0]}}")
    List<WasteCategory> findByDisposalMethodsContaining(String method);
    
    List<WasteCategory> findByColor(String color);
    
    long countByType(WasteCategory.WasteType type);
    
    long countByIsActive(Boolean isActive);
    
    @Query(value = "{}", fields = "{'name': 1, 'code': 1, 'type': 1, 'color': 1, 'icon': 1}")
    List<WasteCategory> findAllBasicInfo();
    
    @Query("{'type': ?0, 'isActive': true}")
    List<WasteCategory> findActiveByType(WasteCategory.WasteType type);
    
    @Query("{'parentCategory': null, 'isActive': true}")
    List<WasteCategory> findActiveRootCategories();
    
    List<WasteCategory> findBySubCategoriesContaining(String subCategoryId);
}