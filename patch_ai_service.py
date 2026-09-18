import sys

file_path = "app/src/main/java/com/example/data/remote/HotelStudioAiService.kt"
with open(file_path, "r") as f:
    text = f.read()

new_func = """
    suspend fun generateOutletChecklist(prompt: String): List<String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            val buildConfigClass = Class.forName("com.example.BuildConfig")
            val field = buildConfigClass.getField("GEMINI_API_KEY")
            field.get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                val systemPrompt = "Generate a professional checklist for a hotel F&B outlet based on this request: '$prompt'. Return strictly a JSON array of strings, where each string is a task. Max 15 tasks."
                val requestJson = JSONObject().apply {
                    put("contents", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", org.json.JSONArray().apply {
                                put(JSONObject().apply { put("text", systemPrompt) })
                            })
                        })
                    })
                }

                conn.outputStream.use { os ->
                    val input = requestJson.toString().toByteArray(Charsets.UTF_8)
                    os.write(input, 0, input.size)
                }

                if (conn.responseCode == 200) {
                    val responseStr = conn.inputStream.bufferedReader().use { it.readText() }
                    val respObj = JSONObject(responseStr)
                    val candidates = respObj.getJSONArray("candidates")
                    val textResp = candidates.getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text")
                    
                    val cleanText = textResp.replace("```json", "").replace("```", "").trim()
                    val arr = org.json.JSONArray(cleanText)
                    val resultList = mutableListOf<String>()
                    for (i in 0 until arr.length()) {
                        resultList.add(arr.getString(i))
                    }
                    return@withContext resultList
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Mock fallback
        val mock = mutableListOf(
            "Verify all staff are present and in full uniform",
            "Check temperature of display fridges and chillers",
            "Ensure POS system is active and menus are updated",
            "Inspect cleanliness of guest areas and tables",
            "Brief team on VIP guests and daily specials"
        )
        if (prompt.contains("clos", ignoreCase = true)) {
            mock.clear()
            mock.addAll(listOf(
                "Settle all final bills and batch out POS",
                "Secure cash float and deposit in drop safe",
                "Lock all alcohol and premium inventory cabinets",
                "Turn off non-essential lights and AC",
                "Hand over keys to security"
            ))
        }
        return@withContext mock
    }
"""

if "suspend fun generateOutletChecklist" not in text:
    text = text.replace("class HotelStudioAiService {", "class HotelStudioAiService {\n" + new_func)
    with open(file_path, "w") as f:
        f.write(text)
