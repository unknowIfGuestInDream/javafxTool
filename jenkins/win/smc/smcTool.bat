@echo off
set java_path=.\jre\bin\javaw.exe
if exist .\jre\bin\javaw.exe (
) else (
set java_path=javaw
)
:START
cmd /c start /b %java_path% -jar javafxTool-smc.jar

:END
