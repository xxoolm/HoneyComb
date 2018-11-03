./gradlew clean assembleDebug
adb install -r /Users/hguo/Workspace/HoneyComb/comb/build/outputs/apk/debug/comb-debug.apk
adb install -r /Users/hguo/Workspace/HoneyComb/locker/build/outputs/apk/debug/locker-debug.apk
adb shell am start -n github.tornaco.practice.honeycomb.locker/.ui.start.StartActivity
