@REM ** Generic bat to run BDM batches **
@REM ***************************************
@rem  -Dbatch.parameters="%BATCHPARAMS%"
if exist ../EJBServer/build.bat (
	echo.local
 	call ../EJBServer/build runbatch -Dbatch.program="%PROCESS%" -Djava.jvmargs="%JVMARGS%"
) else (
	echo.release
	call ../buildBatch runbatch -Dbatch.program="%PROCESS%" -Djava.jvmargs="%JVMARGS%"
)