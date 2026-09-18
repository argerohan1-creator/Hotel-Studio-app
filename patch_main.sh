#!/bin/bash
awk '
/class MainActivity : ComponentActivity\(\)/ {
    print $0
    print "    var globalError by androidx.compose.runtime.mutableStateOf<String?>(null)"
    print ""
    print "    override fun onCreate(savedInstanceState: Bundle?) {"
    print "        super.onCreate(savedInstanceState)"
    print "        "
    print "        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()"
    print "        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->"
    print "            globalError = exception.stackTraceToString()"
    print "            oldHandler?.uncaughtException(thread, exception)"
    print "        }"
    print "        enableEdgeToEdge()"
    print "        setContent {"
    print "            val error = globalError"
    print "            if (error != null) {"
    print "                androidx.compose.foundation.layout.Box("
    print "                    modifier = Modifier.fillMaxSize().background(Color.Red).padding(16.dp),"
    print "                    contentAlignment = Alignment.Center"
    print "                ) {"
    print "                    androidx.compose.foundation.lazy.LazyColumn {"
    print "                        item {"
    print "                            Text(text = \"App Crashed:\", color = Color.White, fontWeight = FontWeight.Bold)"
    print "                            Spacer(modifier = Modifier.height(8.dp))"
    print "                            Text(text = error, color = Color.White, fontSize = 10.sp)"
    print "                        }"
    print "                    }"
    print "                }"
    print "            } else {"
    print "                val userProfile by viewModel.userProfile.collectAsState()"
    print "                val activeTheme = userProfile?.activeTheme ?: \"midnight\""
    print "                HotelStudioTheme(activeTheme = activeTheme) {"
    print "                    HotelStudioApp(viewModel = viewModel)"
    print "                }"
    print "            }"
    print "        }"
    print "    }"
    in_class = 1
    next
}
/override fun onCreate\(savedInstanceState: Bundle\?\)/ {
    if (in_class) {
        skip_on_create = 1
        next
    }
}
skip_on_create {
    if (/^\s*}\s*$/) {
        brace_count--
        if (brace_count < 0) {
            skip_on_create = 0
        }
    } else if (/{/) {
        brace_count++
    }
    next
}
1
' app/src/main/java/com/example/MainActivity.kt > temp3.kt && mv temp3.kt app/src/main/java/com/example/MainActivity.kt
