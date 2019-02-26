./gradlew clean assembleDebug
adb install -r /Users/hguo/Workspace/HoneyComb/comb/build/outputs/apk/debug/comb-debug.apk
adb shell am start -n github.tornaco.practice.honeycomb/.start.StartActivity
