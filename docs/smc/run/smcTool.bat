@echo off
set check_flag=false
if "true" == "%check_flag%" (
  java -version
)
if not %errorlevel% == 0 (
  echo Can not run java,check it.
  echo %errorlevel%  
  pause
  goto END
)
:START
cmd /c start /b java -jar javafxTool-smc.jar

:END