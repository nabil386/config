--  This script updates the primary keys in the PROCESSID column of the PROCESSDEFINITION table so that they are reset to the correct values, these are the values that existed prior to the migration of the data in the --PROCESSDEFINITION table.

UPDATE PROCESSDEFINITION p
   SET p.PROCESSID=(SELECT pc.PROCESSID from PROCESSDEFINITIONCOPY pc
        WHERE p.PROCESSNAME = pc.PROCESSNAME
        AND pc.PROCESSVERSION = (SELECT MIN(PROCESSVERSION) from PROCESSDEFINITIONCOPY pc2
        where pc.PROCESSNAME=pc2.PROCESSNAME))  
   WHERE p.PROCESSNAME IN (SELECT PROCESSNAME FROM PROCESSDEFINITIONCOPY) 