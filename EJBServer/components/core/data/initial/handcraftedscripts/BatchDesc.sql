
-- script  to provide basic entries for description of batch processes and parameters
-- this script must be run AFTER the batchprocdef and batchparamdef tables have been populated

insert into BATCHPROCDESC(PROCESSDEFNAME, PROCESSLONGNAME,  DESCRIPTION, BATCHTYPE, VERSIONNO)
                   select PROCESSDEFNAME, PROCESSDEFNAME,  RTRIM(PROCESSDEFNAME), '', 1
                   from BATCHPROCDEF;
                   
insert into BATCHPARAMDESC(PARAMNAME, PROCESSDEFNAME, DESCRIPTION, VERSIONNO)
                    select PARAMNAME, PROCESSDEFNAME, RTRIM(PARAMTYPE), 1
                    from BATCHPARAMDEF;

update BATCHPARAMDESC set DEFAULTVALUE = NULL;

