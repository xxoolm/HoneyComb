./gradlew clean assembleDebug
#adb uninstall github.tornaco.practice.honeycomb
#adb uninstall github.tornaco.practice.honeycomb.locker
#adb uninstall lab.tornaco.replacer
adb install -r ./comb/build/outputs/apk/debug/comb-debug.apk
adb install -r ./locker/build/outputs/apk/debug/locker-debug.apk
adb install -r ./replacer/build/outputs/apk/debug/replacer-debug.apk
adb shell am start -n github.tornaco.practice.honeycomb/.start.StartActivity
