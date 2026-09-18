import sys

file_path = "gradle/libs.versions.toml"
with open(file_path, "r") as f:
    text = f.read()

text = text.replace('[versions]\n', '[versions]\nvico = "1.15.0"\n')
text = text.replace('[libraries]\n', '[libraries]\nvico-compose = { group = "com.patrykandpatrick.vico", name = "compose", version.ref = "vico" }\nvico-compose-m3 = { group = "com.patrykandpatrick.vico", name = "compose-m3", version.ref = "vico" }\nvico-core = { group = "com.patrykandpatrick.vico", name = "core", version.ref = "vico" }\n')

with open(file_path, "w") as f:
    f.write(text)

print("Patched libs.versions.toml")
