@echo off
set check_flag=true
set java_path=.\jre\bin\javaw.exe
if exist .\jre\bin\javaw.exe (
  %java_path% -version
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
set java_path=javaw
)
:START
cmd /c start /b %java_path% -jar javafxTool-smc.jar

:END
