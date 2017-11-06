@echo off
REM
REM Startup Script for mcf2pdf program.
REM

REM
REM Adjust these variables to your local installation of the MCF software
REM A common value is: C:\Program Files\cewe-fotobuch
REM
SET MCF_INSTALL_DIR=

REM
REM Enter here the location for temporary files of the MCF software. Refer to
REM the options dialog in the software for this information.
REM
SET MCF_TEMP_DIR=

REM
REM Java Options. You can adjust reserved memory (RAM) here
REM
SET MCF2PDF_JAVA_OPTS=-Xms64M -Xmx128M




REM check if this file has to be adjusted!
if "%MCF_INSTALL_DIR%" == "" GOTO echo_adjust 
if "%MCF_TEMP_DIR%" == "" GOTO echo_adjust

java %MCF2PDF_JAVA_OPTS% -classpath "${windowsClasspath};%MCF_INSTALL_DIR%;mcf2pdf-${project.version}.jar" net.sf.mcf2pdf.Main -i "%MCF_INSTALL_DIR%" -t "%MCF_TEMP_DIR%" %1 %2 %3 %4 %5 %6 %7 %8 %9

GOTO end

:echo_adjust
ECHO The installation and/or temporary directory of the MCF software has not been set! Please edit the file mcf2pdf.bat with a text editor first.

:end
