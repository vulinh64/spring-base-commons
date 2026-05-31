@echo off
setlocal

if "%~1"=="" (
  echo Usage: %~nx0 ^<JavaFile^> [more Java files...]
  echo Example: %~nx0 src\main\java\com\vulinh\utils\CommonUtils.java
  exit /b 1
)

if "%JAVA_HOME%"=="" (
  echo JAVA_HOME is not set. Google Java Format requires JDK 25 or newer for this project.
  exit /b 1
)

set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"

if not exist "%JAVA_EXE%" (
  echo JAVA_HOME does not point to a valid JDK: %JAVA_HOME%
  echo Missing Java executable: %JAVA_EXE%
  exit /b 1
)

for /f "tokens=3" %%v in ('call "%JAVA_EXE%" -version 2^>^&1 ^| findstr /i "version"') do set "JAVA_VERSION_RAW=%%~v"

if "%JAVA_VERSION_RAW%"=="" (
  echo Could not determine Java version from %JAVA_EXE%
  exit /b 1
)

for /f "tokens=1,2 delims=." %%a in ("%JAVA_VERSION_RAW%") do (
  if "%%a"=="1" (
    set "JAVA_MAJOR=%%b"
  ) else (
    set "JAVA_MAJOR=%%a"
  )
)

if "%JAVA_MAJOR%"=="" (
  echo Could not determine Java major version from "%JAVA_VERSION_RAW%"
  exit /b 1
)

if %JAVA_MAJOR% LSS 25 (
  echo JAVA_HOME must point to JDK 25 or newer for Google Java Format.
  echo Current JAVA_HOME: %JAVA_HOME%
  echo Current Java version: %JAVA_VERSION_RAW%
  exit /b 1
)

set "SCRIPT_DIR=%~dp0"
set "VERSION_FILE=%SCRIPT_DIR%google-java-format.version"

if not exist "%VERSION_FILE%" (
  echo Missing formatter version file: %VERSION_FILE%
  exit /b 1
)

for /f "usebackq tokens=1,* delims==" %%a in ("%VERSION_FILE%") do (
  if "%%a"=="repositoryUrl" set "GJF_REPOSITORY_URL=%%b"
  if "%%a"=="groupPath" set "GJF_GROUP_PATH=%%b"
  if "%%a"=="artifactId" set "GJF_ARTIFACT_ID=%%b"
  if "%%a"=="classifier" set "GJF_CLASSIFIER=%%b"
  if "%%a"=="version" set "GJF_VERSION=%%b"
)

if "%GJF_REPOSITORY_URL%"=="" (
  echo Could not resolve repositoryUrl property from %VERSION_FILE%
  exit /b 1
)

if "%GJF_GROUP_PATH%"=="" (
  echo Could not resolve groupPath property from %VERSION_FILE%
  exit /b 1
)

if "%GJF_ARTIFACT_ID%"=="" (
  echo Could not resolve artifactId property from %VERSION_FILE%
  exit /b 1
)

if "%GJF_CLASSIFIER%"=="" (
  echo Could not resolve classifier property from %VERSION_FILE%
  exit /b 1
)

if "%GJF_VERSION%"=="" (
  echo Could not resolve version property from %VERSION_FILE%
  exit /b 1
)

set "GJF_ARTIFACT=%GJF_ARTIFACT_ID%-%GJF_VERSION%-%GJF_CLASSIFIER%.jar"
set "GJF_CACHE_DIR=%SCRIPT_DIR%"
set "GJF_JAR_FILE=%GJF_CACHE_DIR%%GJF_ARTIFACT%"
set "GJF_SHA256_FILE=%GJF_JAR_FILE%.sha256"
set "GJF_BASE_URL=%GJF_REPOSITORY_URL%/%GJF_GROUP_PATH%/%GJF_ARTIFACT_ID%/%GJF_VERSION%"
set "GJF_JAR_URL=%GJF_BASE_URL%/%GJF_ARTIFACT%"
set "GJF_SHA256_URL=%GJF_JAR_URL%.sha256"

if not exist "%GJF_JAR_FILE%" (
  call :download "%GJF_JAR_URL%" "%GJF_JAR_FILE%"
  if errorlevel 1 exit /b %ERRORLEVEL%
)

if not exist "%GJF_SHA256_FILE%" (
  call :download "%GJF_SHA256_URL%" "%GJF_SHA256_FILE%"
  if errorlevel 1 exit /b %ERRORLEVEL%
)

for /f "usebackq tokens=1" %%v in ("%GJF_SHA256_FILE%") do set "EXPECTED_SHA256=%%v"

if "%EXPECTED_SHA256%"=="" (
  echo Formatter checksum file is empty: %GJF_SHA256_FILE%
  exit /b 1
)

for /f "delims=" %%v in ('powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "(Get-FileHash -Algorithm SHA256 -LiteralPath '%GJF_JAR_FILE%').Hash.ToLowerInvariant()"') do set "ACTUAL_SHA256=%%v"

if /i not "%EXPECTED_SHA256%"=="%ACTUAL_SHA256%" (
  echo Formatter jar checksum mismatch: %GJF_JAR_FILE%
  echo Expected: %EXPECTED_SHA256%
  echo Actual:   %ACTUAL_SHA256%
  exit /b 1
)

"%JAVA_EXE%" ^
  --add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED ^
  --add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED ^
  --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED ^
  --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED ^
  --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED ^
  --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED ^
  -jar "%GJF_JAR_FILE%" ^
  --replace ^
  --skip-reflowing-long-strings ^
  %*

exit /b %ERRORLEVEL%

:download
echo Downloading %~1
powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference = 'Stop'; Invoke-WebRequest -Uri '%~1' -OutFile '%~2.download'"
if errorlevel 1 (
  if exist "%~2.download" del "%~2.download"
  exit /b 1
)
move /Y "%~2.download" "%~2" >nul
exit /b %ERRORLEVEL%
