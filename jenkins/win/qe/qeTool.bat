@echo off
set check_flag=true
set java_path=.\jre\bin\java.exe
if exist .\jre\bin\java.exe (
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
set java_path=java
)
:START
cmd /c start /b %java_path% -jar javafxTool-qe.jar

:END