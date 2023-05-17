SELECT
    cr.concernroleid,
    cr.concernroleid,
    cr.concernroleid
    INTO
    :clientconcernroleid,
    :concernroleid,
    :torecipientconcernroleid
FROM
    concernrole             cr
    INNER JOIN caseheader              ch ON ch.concernroleid = cr.concernroleid
    INNER JOIN bdmforeignapplication   fa ON ch.caseid = fa.caseid
WHERE
    NOT EXISTS
    (
        SELECT
            1
        FROM
            bdmcorrespondencestaging
        WHERE
            clientconcernroleid = cr.concernroleid
            AND templatename = 'ISS-6587'
                AND trunc(dateforsending) = TO_DATE('13/APR/2023', 'dd/mon/yyyy')
    );