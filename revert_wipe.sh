#!/bin/bash
sed -i '/\/\/ --- SAFEGUARD: WIPE DB ON STARTUP IF IT CRASHES ---/,/\/\/ --------------------------------------------------/d' app/src/main/java/com/example/MainActivity.kt
