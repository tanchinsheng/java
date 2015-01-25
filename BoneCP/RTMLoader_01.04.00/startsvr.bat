@echo off
echo ***************************************************************
echo ******************** Launching RTM Loader *********************
echo ***************************************************************

rem ***********************************************
rem ****** VARIABLES D'ENVIRONNEMENT
rem ***********************************************

set server=rtmloader

rem ***** Please provide your JRE BIN directory below set JAVA_BIN="C:\j2sdk1.4.2_06\jre\bin"
rem ***** Please update these two paths with your real paths!!!

set RTM_PATH="C:\git\RTMLoader_01.04.00"

cd %RTM_PATH%
set RTM_LIB_PATH=%RTM_PATH%\lib

rem set JAVA_BIN="C:\j2sdk1.4.2_06\jre\bin"

set JAVA_BIN=C:\Java\x86\jdk1.5.0_22\bin

set RTM_LIB_PATDBAPP=C:\git\RTMLoader_01.04.00\build\rtmloader_install\lib\rtmloader.jar
set RTM_LIB_ACTIVATION=%RTM_LIB_PATH%\activation.jar
set RTM_LIB_NET=%RTM_LIB_PATH%\commons-net-3.1.jar
set RTM_LIB_LANG=%RTM_LIB_PATH%\commons-lang-2.4.jar
set RTM_LIB_LOG=%RTM_LIB_PATH%\log4j-1.2.13.jar
set RTM_LIB_MAIL=%RTM_LIB_PATH%\mail.jar
set RTM_LIB_ORACLE10=%RTM_LIB_PATH%\ojdbc14.jar
set RTM_LIB_IO=%RTM_LIB_PATH%\commons-io-2.0.1.jar

set COMMON_LIB=%RTM_LIB_PATDBAPP%;%RTM_LIB_ACTIVATION%;%RTM_LIB_NET%;%RTM_LIB_LANG%;%RTM_LIB_LOG%;%RTM_LIB_MAIL%;%RTM_LIB_ORACLE10%;%RTM_LIB_IO%
rem ***********************
rem ****** LANCEMENT ******
rem ***********************

%JAVA_BIN%\java -Xmx128m  -classpath .;%JAVA_BIN%;%COMMON_LIB% com.st.mcc.rtm.main.Launcher

pause