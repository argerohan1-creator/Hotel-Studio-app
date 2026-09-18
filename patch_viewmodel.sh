#!/bin/bash
sed -i '/val recipePickerDiet:/a \
    private val _recipePickerPrepTime = MutableStateFlow("ALL")\
    val recipePickerPrepTime: StateFlow<String> = _recipePickerPrepTime.asStateFlow()' app/src/main/java/com/example/viewmodel/HotelStudioViewModel.kt

sed -i '/fun setRecipePickerDiet(/i \
    fun setRecipePickerPrepTime(time: String) {\
        _recipePickerPrepTime.value = time\
    }\
' app/src/main/java/com/example/viewmodel/HotelStudioViewModel.kt
