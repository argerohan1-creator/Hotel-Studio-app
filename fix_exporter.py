import sys

file_path = "app/src/main/java/com/example/util/BuffetMenuExporter.kt"
with open(file_path, "r") as f:
    text = f.read()

# I need to move the function inside the object.
# I'll just remove the last `}` before `fun printKitchenPrepSheet` and append it at the end.
# Wait, the function might be at the end, and the previous `}` was before it.
# Let's just find the function, remove it, put it before the LAST `}` of the original file.

func_start = text.find("fun printKitchenPrepSheet(")
if func_start != -1:
    func_text = text[func_start:]
    text = text[:func_start]
    
    # Now text should end with `}`
    # Let's find the last `}`
    last_brace = text.rfind("}")
    if last_brace != -1:
        text = text[:last_brace] + func_text + "\n}\n"
    
with open(file_path, "w") as f:
    f.write(text)
    
print("Fixed BuffetMenuExporter")
