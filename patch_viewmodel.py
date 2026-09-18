import sys

file_path = "app/src/main/java/com/example/viewmodel/HotelStudioViewModel.kt"
with open(file_path, "r") as f:
    text = f.read()

new_func = """
    fun generateOutletChecklist(prompt: String, onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            _isAiLoading.value = true
            showToast("Consulting F&B Assistant for Custom Checklist...")
            try {
                val items = aiService.generateOutletChecklist(prompt)
                onResult(items)
                showToast("Checklist generated successfully!")
            } catch (e: Exception) {
                showToast("Failed to generate checklist. Fallback used.")
                onResult(emptyList())
            } finally {
                _isAiLoading.value = false
            }
        }
    }
"""

if "fun generateOutletChecklist(" not in text:
    text = text.replace("fun showToast(", new_func + "\n    fun showToast(")
    with open(file_path, "w") as f:
        f.write(text)
