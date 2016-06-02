pushd %~dp0
set NDK_PROJECT_PATH=%cd%
set NDK_TOOLCHAIN_VERSION=4.8
set nkdbuild="D:\Android\android-ndk-r10e\ndk-build.cmd"
md jni
::if not exist jni\Android.mk copy ..\app\build\intermediates\ndk\debug\Android.mk jni\Android.mk
xcopy E:\AndroidWS\JianRMagicBox\app\src\main\jni	jni\ /Y
call %nkdbuild% NDK_DEBUG=0 
if not %errorlevel%==0 goto :EOF
echo su > shell.sh
echo busybox killall com.bigzhao.jianrmagicbox >> shell.sh
echo exit >> shell.sh
echo exit >> shell.sh
adb shell < shell.sh
::adb shell pm clear com.bigzhao.jianrmagicbox
adb shell rm /data/data/com.bigzhao.jianrmagicbox/lib/libMagicBox.so
adb push obj\local\armeabi\libMagicBox.so /data/data/com.bigzhao.jianrmagicbox/lib/libMagicBox.so
::adb push D:\android-ndk-r10e\sources\cxx-stl\gnu-libstdc++\4.9\libs\armeabi\libgnustl_shared.so /data/data/com.bigzhao.cbtcrack/lib/libgnustl_shared.so
::adb push obj\local\armeabi\libgnustl_shared.so /data/data/com.bigzhao.cbtcrack/libgnustl_shared.so
::adb shell rm /data/data/com.bigzhao.cbtcrack/lib/libgnustl_shared.so
::adb shell rm /data/data/com.bigzhao.cbtcrack/libgnustl_shared.so
adb shell am start com.bigzhao.jianrmagicbox
popd