select  cr.concernroleid,cr.concernroleid,cr.concernroleid,'CT5','CS4', 'FATYP01','FATYP03','FASTTS04' INTO :clientConcernRoleID,:concernRoleID,:toRecipientConcernRoleID,:caseTypeCode,:caseStatus, :foreignApplicationTypeCode1, :foreignApplicationTypeCode2, :foreignApplicationStatus 

from concernrole cr inner join caseheader  ch   ON ch.concernroleid =cr.concernroleid INNER JOIN bdmforeignapplication   fa ON ch.caseid = fa.caseid     

WHERE NOT EXISTS (select 1 from BDMCorrespondenceStaging where clientconcernroleid = cr.concernroleid and templatename = 'ISP-XXXX' and dateForSending = sysdate)