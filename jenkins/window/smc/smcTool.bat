@echo off
set check_flag=true
if exist .\jre\bin\java.exe (
  .\jre\bin\java.exe -version
) else (
if "true" == "%check_flag%" (
  java -version
)
if not %errorlevel% == 0 (
  echo Can not run java,check it.
  echo %errorlevel%  
  pause
  goto END
)
)
:START
cmd /c start /b .\jre\bin\java.exe -jar javafxTool-smc.jar

:END