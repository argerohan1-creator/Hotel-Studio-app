import sys

file_path = "app/build.gradle.kts"
with open(file_path, "r") as f:
    text = f.read()

deps_addition = """    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.core)
"""

text = text.replace('  "ksp"(libs.moshi.kotlin.codegen)\n}', '  "ksp"(libs.moshi.kotlin.codegen)\n' + deps_addition + '}')

with open(file_path, "w") as f:
    f.write(text)

print("Patched build.gradle.kts")
