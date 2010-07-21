group leaf_core_aggregates;

nodeQuery(
		leafTableName,
		leafTableName_ForEntityType,
		coreTableName,
		coreTableName_ForEntityType,
		coreTableNameColumns,
		emptyCoreTableNameColumns,
		coreTableNameColumnsForGroupBy,
		aggregates,
		emptyAggregates,
		idSize,
		entityTypeSize,
		labelSize) ::= <<
	SELECT
		CHAR ('<leafTableName_ForEntityType> ' || CHAR ("<leafTableName>".PK), <idSize>) AS ID,
		CHAR ('<leafTableName_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ("<leafTableName>"."<leafTableName>", <labelSize>) AS "Label"
		<emptyCoreTableNameColumns>
		<aggregates>
	FROM
		"<leafTableName>"
	JOIN
		<coreTableName>_TO_<leafTableName>
			ON "<leafTableName>".PK = <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<leafTableName>_FOREIGN_KEY
	JOIN
		"<coreTableName>"
			ON <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK 
	GROUP BY
		"<leafTableName>".PK,
		"<leafTableName>"."<leafTableName>"
		<coreTableNameColumnsForGroupBy>
UNION
	SELECT
		CHAR ('<coreTableName_ForEntityType> ' || CHAR ("<coreTableName>".PK), <idSize>) AS ID,
		CHAR ('<coreTableName_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ('<coreTableName_ForEntityType> ' || CHAR ("<coreTableName>".PK), <labelSize>) AS "Label"
		<coreTableNameColumns>
		<emptyAggregates>
	FROM
		"<coreTableName>"
	GROUP BY
		"<coreTableName>".PK
		<coreTableNameColumnsForGroupBy>
>>

edgeQuery(
		leafTableName,
		leafTableName_ForEntityType,
		coreTableName,
		coreTableName_ForEntityType,
		aggregates,
		idSize) ::= <<
SELECT
	CHAR ('<leafTableName_ForEntityType> ' || CHAR ("<leafTableName>".PK), <idSize>) AS SOURCE,
	CHAR ('<coreTableName_ForEntityType> ' || CHAR ("<coreTableName>".PK), <idSize>) AS TARGET
	<aggregates>
	, COUNT (*) AS "Weight"
FROM
	"<leafTableName>"
JOIN
	<coreTableName>_TO_<leafTableName>
		ON "<leafTableName>".PK = <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<leafTableName>_FOREIGN_KEY
JOIN
	"<coreTableName>"
		ON <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK
GROUP BY
	"<leafTableName>".PK,
	"<coreTableName>".PK
>>
