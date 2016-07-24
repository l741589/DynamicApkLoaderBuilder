set d=%~dp0
cd /d %d%
set zipalign=D:\Android\sdk\build-tools\23.0.3\zipalign
set z="D:\Program Files\7-Zip\7z"
call mvn clean package
set src=%1
set ws=%d%data
pushd E:\Projects\Project1\Project1
call %NDK_ROOT%\ndk-build.cmd
popd
pushd E:\AndroidWS\JianRMagicBox
call gradlew build
popd
call %z% x -y E:\AndroidWS\JianRMagicBox\app\build\outputs\apk\app-debug.apk -oE:\AndroidWS\JianRMagicBox\app\build\outputs\apk\e

set runableJar=%d%target\DynamicApkLoaderBuilder-1.0-SNAPSHOT-jar-with-dependencies.jar
set JAVA_DEBUG=-Xdebug -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y
set run=java -cp %runableJar%;%d%libs/* com.bigzhao.apk.Main
%run% %src% %src% %ws%
jarsigner -sigalg MD5withRSA -digestalg SHA1 -keystore %d%tools/jianrmod.keystore -storepass dashuyuxiaodidi %ws%/%~nx1 jianrmod.keystore
::jarsigner -sigalg MD5withRSA -digestalg SHA1 -keystore %d%tools/debug.keystore -storepass android %ws%/%~nx1 androiddebugkey
::call %zipalign% -c -v 4 data\warshipgirlsr_cn_2_4_0_base.apk
adb install -r data\%~nx1

del %d%data\module.zip
pushd %d%data\loader\assets\MagicBox\module\
call %z% a %d%data\module.zip *
popd

::adb shell am start -D com.bigzhao.jianrmagicbox/com.bigzhao.jianrmagicbox.LoaderActivity