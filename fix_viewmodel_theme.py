import sys

file_path = "app/src/main/java/com/example/viewmodel/HotelStudioViewModel.kt"
with open(file_path, "r") as f:
    text = f.read()

# find init {
init_str = "init {"
if init_str in text:
    # insert theme check
    check_theme = """
        viewModelScope.launch {
            repository.userProfile.collect { profile ->
                if (profile != null && profile.activeTheme == "midnight") {
                    repository.updateTheme("taj")
                }
            }
        }
"""
    # this might loop or conflict, better to just set it once. 
    # Actually, let's just map "midnight" to TajGoldColorScheme in Theme.kt, 
    # and in SettingsHubScreen change "Midnight Slate (Default)" to use a different key like "slate" if we still want it, 
    # or just remove it.
