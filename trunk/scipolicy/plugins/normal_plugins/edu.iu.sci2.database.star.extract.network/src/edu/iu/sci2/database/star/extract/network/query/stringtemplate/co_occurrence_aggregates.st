group co_occurrence_aggregates;


nodeQuery(leafTableName, coreTableName, aggregates) ::= <<
SELECT
    $leafTableName$.PK AS ID,
    $leafTableName$.$leafTableName$ AS "Label",
    $aggregates$
FROM
    $leafTableName$
JOIN
    $coreTableName$_TO_$leafTableName$
        ON $leafTableName$.PK = $coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$leafTableName$_FOREIGN_KEY
JOIN
    $coreTableName$
        ON $coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$coreTableName$_FOREIGN_KEY = $coreTableName$.PK
GROUP BY
    $leafTableName$.PK,
    $leafTableName$.$leafTableName$
>>

edgeQuery(leafTableName, coreTableName, nonAggregateCoreTableNameColumns, aggregates) ::= <<
SELECT
    $leafTableName$1.PK AS SOURCE,
    $leafTableName$2.PK AS TARGET,
    $nonAggregateCoreTableNameColumns$,
    $aggregates$,
	COUNT (*) AS "Weight"
FROM
    $leafTableName$ $leafTableName$1
JOIN
    $coreTableName$_TO_$leafTableName$ $coreTableName$_TO_$leafTableName$1
        ON $leafTableName$1.PK = $coreTableName$_TO_$leafTableName$1.$coreTableName$_TO_$leafTableName$_$leafTableName$_FOREIGN_KEY
JOIN
    $coreTableName$
        ON $coreTableName$_TO_$leafTableName$1.$coreTableName$_TO_$leafTableName$_$coreTableName$_FOREIGN_KEY = $coreTableName$.PK
JOIN
    $coreTableName$_TO_$leafTableName$ $coreTableName$_TO_$leafTableName$2
        ON $coreTableName$.PK = $coreTableName$_TO_$leafTableName$2.$coreTableName$_TO_$leafTableName$_$coreTableName$_FOREIGN_KEY
JOIN
    $leafTableName$ $leafTableName$2
        ON $coreTableName$_TO_$leafTableName$2.$coreTableName$_TO_$leafTableName$_$leafTableName$_FOREIGN_KEY = $leafTableName$2.PK
WHERE
    $leafTableName$1.PK \< $leafTableName$2.PK
GROUP BY
    $leafTableName$1.PK,
    $leafTableName$2.PK,
    $nonAggregateCoreTableNameColumns$
>>
