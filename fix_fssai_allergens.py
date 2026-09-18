import sys

with open('app/src/main/java/com/example/ui/components/FssaiAuditDialog.kt', 'r') as f:
    text = f.read()

text = text.replace('val violations = performFssaiAudit(recipe)', 'val violations = performFssaiAudit(recipe, item.allergens)')
text = text.replace('fun performFssaiAudit(recipe: RecipeEntity?): List<FssaiRuleViolation> {', 'fun performFssaiAudit(recipe: RecipeEntity?, allergens: List<String>): List<FssaiRuleViolation> {')
text = text.replace('if (recipe.allergens.isNotEmpty()) {', 'if (allergens.isNotEmpty()) {')
text = text.replace('triggerIngredient = "Allergens (${recipe.allergens.joinToString()})",', 'triggerIngredient = "Allergens (${allergens.joinToString()})",')

with open('app/src/main/java/com/example/ui/components/FssaiAuditDialog.kt', 'w') as f:
    f.write(text)
