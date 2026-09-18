import sys
import re

file_path = "app/src/main/java/com/example/util/ModernArtRenderer.kt"
with open(file_path, "r") as f:
    text = f.read()

# We need to find the start of `private fun drawProceduralFoodBackground(canvas: Canvas, w: Float, h: Float, theme: FoodVisualTheme) {`
# and the end of it. It's a bit tricky with python regex if it's long.
# Let's just find the function signature.
start_idx = text.find("private fun drawProceduralFoodBackground(canvas: Canvas, w: Float, h: Float, theme: FoodVisualTheme) {")

if start_idx != -1:
    # Find the closing brace of this function.
    # We can count braces.
    brace_count = 0
    end_idx = -1
    for i in range(start_idx, len(text)):
        if text[i] == '{':
            brace_count += 1
        elif text[i] == '}':
            brace_count -= 1
            if brace_count == 0:
                end_idx = i + 1
                break
    
    if end_idx != -1:
        new_impl = """private fun drawProceduralFoodBackground(canvas: Canvas, w: Float, h: Float, theme: FoodVisualTheme) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = android.graphics.RectF(0f, 0f, w, h)

        when (theme) {
            FoodVisualTheme.MIDNIGHT_OBSIDIAN -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#0F172A"), Color.parseColor("#020617"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
                paint.shader = null
                paint.color = Color.parseColor("#33D4AF37")
                paint.strokeWidth = 3f
                canvas.drawLine(0f, h*0.2f, w, h*0.3f, paint)
                canvas.drawLine(0f, h*0.8f, w, h*0.7f, paint)
            }
            FoodVisualTheme.IVORY_SILK -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#FDFBF7"), Color.parseColor("#EBDDC1"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.BRUSHED_GOLD -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#D4AF37"), Color.parseColor("#9C7A4A"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.EMERALD_VELVET -> {
                val grad = RadialGradient(w/2, h/2, h, Color.parseColor("#065F46"), Color.parseColor("#022C22"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.SAPPHIRE_GRADIENT -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#1E3A8A"), Color.parseColor("#0F172A"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.CRIMSON_DAMASK -> {
                val grad = RadialGradient(w/2, h/2, h, Color.parseColor("#991B1B"), Color.parseColor("#450A0A"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.CHARCOAL_MATTE -> {
                canvas.drawColor(Color.parseColor("#171717"))
            }
            FoodVisualTheme.ROSE_GOLD -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#F4C4C4"), Color.parseColor("#D39292"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.FROSTED_GLASS -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#E0F2FE"), Color.parseColor("#BAE6FD"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.PLATINUM_MESH -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#E2E8F0"), Color.parseColor("#94A3B8"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.BRONZE_TEXTURE -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#78350F"), Color.parseColor("#451A03"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.PEARL_GLAZE -> {
                val grad = RadialGradient(w/2, h/2, h, Color.parseColor("#FFFFFF"), Color.parseColor("#F1F5F9"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.ROYAL_BURGUNDY -> {
                val grad = LinearGradient(0f, 0f, w, h, Color.parseColor("#831843"), Color.parseColor("#4C0519"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
            FoodVisualTheme.TITANIUM_WEAVE -> {
                canvas.drawColor(Color.parseColor("#334155"))
            }
            FoodVisualTheme.CHAMPAGNE_SPARKLE -> {
                val grad = RadialGradient(w/2, h/2, h, Color.parseColor("#FEF3C7"), Color.parseColor("#FDE68A"), Shader.TileMode.CLAMP)
                paint.shader = grad
                canvas.drawRect(rect, paint)
            }
        }
    }"""
        text = text[:start_idx] + new_impl + text[end_idx:]
        
        with open(file_path, "w") as f:
            f.write(text)
        print("Patched backgrounds successfully")
    else:
        print("Could not find end of function")
else:
    print("Could not find start of function")
