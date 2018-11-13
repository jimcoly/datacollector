title streamsets 3.5.0
rem ----- if JAVA_HOME is not set we're not happy ------------------------------
:checkJava

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
goto checkServer

:noJavaHome
echo "You must set the JAVA_HOME variable before running CARBON."
goto end

:checkServer
if "%SDC_HOME%"=="" set SDC_HOME=%~sdp0..

echo %SDC_HOME%

cd %SDC_HOME%

set SDC_DIST=%SDC_HOME%
set SDC_HOSTNAME="localhost"
set SDC_CONF=%SDC_HOME%\etc
set SDC_DATA=%SDC_HOME%\data
set SDC_LOG=%SDC_HOME%\log
set SDC_RESOURCES=%SDC_HOME%\resources
set SDC_JAVA_OPTS=-Xmx8g -Xms8g -server -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -Djdk.nio.maxCachedBufferSize=262144  -agentlib:jdwp=transport=dt_socket,address=0.0.0.0:5005,server=y,suspend=n  -Dfile.encoding=UTF-8 -Duser.language=zh -Duser.region=CN -Dsun.jnu.encoding=UTF-8
set SDC_MAIN_CLASS=com.streamsets.datacollector.main.DataCollectorMain
set SDC_SECURITY_MANAGER_ENABLED="false"
set SDC_HEAPDUMP_PATH=%SDC_LOG%\sdc_heapdump_.hprof
set SDC_ROOT_CLASSPATH=%SDC_DIST%\root-lib\*

set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -Dsdc.dist.dir=%SDC_DIST%
set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -Dsdc.resources.dir=%SDC_RESOURCES%
set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -Dsdc.hostname=%SDC_HOSTNAME%
set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -Dsdc.conf.dir=%SDC_CONF%
set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -Dsdc.data.dir=%SDC_DATA%
set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -Dsdc.log.dir=%SDC_LOG%

set BOOTSTRAP_JAR=%SDC_DIST%\libexec\bootstrap-libs\main\streamsets-datacollector-bootstrap-${project.version}.jar

set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -javaagent:%BOOTSTRAP_JAR%

set BOOTSTRAP_CLASSPATH=%BOOTSTRAP_JAR%
set BOOTSTRAP_CLASSPATH=%BOOTSTRAP_CLASSPATH%;%SDC_ROOT_CLASSPATH%

set API_CLASSPATH=%SDC_DIST%\api-lib\*.jar

set CONTAINER_CLASSPATH=%SDC_CONF%;%SDC_DIST%\container-lib\*.jar

set LIBS_COMMON_LIB_DIR=%SDC_DIST%\libs-common-lib\

set STREAMSETS_LIBRARIES_DIR=%SDC_DIST%\streamsets-libs\

set STREAMSETS_LIBRARIES_EXTRA_DIR=%SDC_DIST%\streamsets-libs-extras\

set USER_LIBRARIES_DIR=%SDC_DIST%\user-libs

set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%SDC_HEAPDUMP_PATH%

set SDC_JAVA_OPTS=%SDC_JAVA_OPTS% -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:%SDC_LOG%\gc.log
set EXTRA_OPTIONS=-streamsetsLibrariesExtraDir %STREAMSETS_LIBRARIES_EXTRA_DIR%

"%JAVA_HOME%\bin\java" -classpath %BOOTSTRAP_CLASSPATH% %SDC_JAVA_OPTS% com.streamsets.pipeline.BootstrapMain -mainClass %SDC_MAIN_CLASS% -apiClasspath "%API_CLASSPATH%" -userLibrariesDir "%USER_LIBRARIES_DIR%" -containerClasspath "%CONTAINER_CLASSPATH%" -streamsetsLibrariesDir %STREAMSETS_LIBRARIES_DIR% -configDir %SDC_CONF% -libsCommonLibDir %LIBS_COMMON_LIB_DIR% -streamsetsLibrariesExtraDir %STREAMSETS_LIBRARIES_EXTRA_DIR%


