import sys

file_path = "app/src/main/AndroidManifest.xml"
with open(file_path, "r") as f:
    text = f.read()

permissions = """    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
"""
text = text.replace('    <uses-permission android:name="android.permission.INTERNET" />\n    <uses-permission android:name="android.permission.RECORD_AUDIO" />\n', permissions)

receiver = """        <provider android:name="androidx.core.content.FileProvider" android:authorities="${applicationId}.provider" android:exported="false" android:grantUriPermissions="true"><meta-data android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/file_paths" /></provider>
        
        <receiver android:name=".util.ChecklistDeadlineReceiver" android:exported="false" />
"""
text = text.replace('        <provider android:name="androidx.core.content.FileProvider" android:authorities="${applicationId}.provider" android:exported="false" android:grantUriPermissions="true"><meta-data android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/file_paths" /></provider>\n', receiver)

with open(file_path, "w") as f:
    f.write(text)

print("Manifest patched")
