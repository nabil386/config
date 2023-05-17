-- The BDMPROPERTIESBACKUP table is used to backup the current properties 
-- during data retention process and to analyze the properties added in 
-- the latest build for updates, addition and deletion.  

CREATE TABLE BDMPROPERTIESBACKUP(
	PROPERTYID NUMBER(19,0) not null, 
	CATEGORY VARCHAR2(10), 
	NAME VARCHAR2(254), 
	VALUE VARCHAR2(500), 
	TYPE VARCHAR2(10), 
	DEFAULTVALUE VARCHAR2(500), 
	DYNAMIC CHAR(1) not null, 
	VERSIONNO INTEGER not null, 
	LASTWRITTEN DATE,
	BUILDDATETIME DATE
);

