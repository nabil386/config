-- SQL to delete the backed up properties from BDMPROPERTIESBACKUP table older than 365 days.   

DELETE FROM BDMPROPERTIESBACKUP WHERE BUILDDATETIME < trunc(SYSDATE) - 365 ;
