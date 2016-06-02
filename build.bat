set d=%~dp0
cd /d %d%
call mvn clean package
::E:/IntellijWS/DynamicApkLoaderBuilder/
set src=%1
set ws=%d%data
set runableJar=%d%target\DynamicApkLoaderBuilder-1.0-SNAPSHOT-jar-with-dependencies.jar
set JAVA_DEBUG=-Xdebug -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y
set run=java -cp %runableJar%;%d%libs/* com.bigzhao.apk.Main
%run% %src% %src% %ws%
jarsigner -sigalg MD5withRSA -digestalg SHA1 -keystore %d%tools/debug.keystore -storepass android %ws%/%~nx1 androiddebugkey