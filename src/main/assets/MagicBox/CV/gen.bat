setlocal ENABLEDELAYEDEXPANSION
echo off
set dir=%~dp0
echo { > manifest.json
for /R %%i in (*.mp3) do (
	set x=%%i
	set x=!x:%dir%=!
	set y=!x:\=.!
	set y=!y:~0,-4!
	echo "!y!":"!x!", >> manifest.json 
)
echo } >> manifest.json