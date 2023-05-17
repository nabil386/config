
-- script to remove duplicate primary key constraints

-- both infrastructure and application attempt to add a primary key constraint
-- to the USERS table, the second of which will fail, giving a (misleading)
-- error message

alter table USERS drop primary key;


