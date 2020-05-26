@echo off

set JAR_NAME=cfc-goldencudgel-web-0.0.1-SNAPSHOT.jar

rem cd ..
echo current path %cd%
set BASE_DIR=%cd%

:mavenbuild
echo -------------------------
cd %BASE_DIR%
rem RD /S /Q %BASE_DIR%\target
mkdir %BASE_DIR%\target
rem call mvn clean install -DskipTests=true -Dfile.encoding=UTF-8
echo ========clean project================
call mvn package clean -Dmaven.test.skip=true
echo ========package project================
call mvn package -Dmaven.test.skip=true

:checkwarexists
cd %BASE_DIR%/target
if exist %JAR_NAME% (
    echo success to generate:%JAR_NAME%
) else (
    echo can't find file: %JAR_NAME%
	pause
	goto end
)

:delete lib
echo delete lib
7z d %JAR_NAME% BOOT-INF/lib

:finishmsg
echo ======================================
echo "     ^ ^                             "
echo "      v                              "
echo generate war finished: %BASE_DIR%/target/%JAR_NAME%
echo ======================================

rem cd %BASE_DIR%/build_tools
pause
goto end

:noParameter
echo branch parameter not given!
pause
goto end

:end
