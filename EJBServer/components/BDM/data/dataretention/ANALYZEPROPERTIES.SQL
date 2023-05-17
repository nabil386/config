SELECT p.CATEGORY, p.NAME,p.VALUE, 'NEW' "CHANGE" 
FROM PROPERTIES p 
WHERE p.NAME NOT IN (SELECT pc.NAME FROM BDMPROPERTIESBACKUP pc WHERE pc.BUILDDATETIME = TO_DATE('@BUILDDATETIME@', 'yy-MM-dd HH24:MI:SS')) 
UNION
SELECT pc.CATEGORY, pc.NAME,pc.VALUE, 'DELETED' "CHANGE"
FROM BDMPROPERTIESBACKUP pc
WHERE pc.NAME NOT IN (SELECT p.NAME FROM PROPERTIES p) AND pc.BUILDDATETIME = TO_DATE('@BUILDDATETIME@', 'yy-MM-dd HH24:MI:SS')
UNION
SELECT p.CATEGORY, p.NAME,p.VALUE, 'UPDATED' "CHANGE"
FROM PROPERTIES p, BDMPROPERTIESBACKUP pc 
WHERE p.NAME = pc.NAME AND
p.VALUE <> pc.VALUE AND 
pc.BUILDDATETIME = TO_DATE('@BUILDDATETIME@', 'yy-MM-dd HH24:MI:SS')
ORDER BY "CHANGE";

SELECT p.CATEGORY, p.NAME,p.VALUE "NEW VALUE", pc.VALUE "DEPLOYED VALUE", 
'UPDATE PROPERTIES SET VALUE=''' ||  pc.VALUE || ''' WHERE NAME='''|| p.NAME || ''' AND CATEGORY=''' ||  p.CATEGORY || ''';' "SQL FOR UPDATE"
FROM PROPERTIES p, BDMPROPERTIESBACKUP pc 
WHERE p.NAME = pc.NAME AND
p.VALUE <> pc.VALUE AND 
p.category = pc.category AND
pc.BUILDDATETIME = TO_DATE('@BUILDDATETIME@', 'yy-MM-dd HH24:MI:SS');

UPDATE properties p
SET
    p.value = (
        SELECT
            pc.value
        FROM
            bdmpropertiesbackup pc
        WHERE
            p.name = pc.name
            AND p.value <> pc.value
            AND p.category = pc.category
            AND pc.builddatetime = TO_DATE('@BUILDDATETIME@', 'yy-MM-dd HH24:MI:SS')
    )
WHERE
    p.name = (
        SELECT
            pc.name
        FROM
            bdmpropertiesbackup pc
        WHERE
            p.name = pc.name
            AND p.value <> pc.value
            AND p.category = pc.category
            AND pc.builddatetime = TO_DATE('@BUILDDATETIME@', 'yy-MM-dd HH24:MI:SS')
    );
