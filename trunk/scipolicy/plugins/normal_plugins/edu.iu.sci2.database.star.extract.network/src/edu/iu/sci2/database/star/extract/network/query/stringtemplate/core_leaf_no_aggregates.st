group leaf_core_no_aggregates;

nodeQuery(
		leafTableName,
		leafTableName_ForEntityType,
		coreTableName,
		coreTableName_ForEntityType,
		coreTableNameColumns,
		emptyCoreTableNameColumns,
		idSize,
		entityTypeSize,
		labelSize) ::= <<
	SELECT
		CHAR ('<leafTableName_ForEntityType> ' || CHAR ("<leafTableName>".PK), <idSize>) AS ID,
		CHAR ('<leafTableName_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ("<leafTableName>"."<leafTableName>", <labelSize>) AS "Label"
		<emptyCoreTableNameColumns>
	FROM
		"<leafTableName>"
	JOIN
		<coreTableName>_TO_<leafTableName>
			ON "<leafTableName>".PK = <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<leafTableName>_FOREIGN_KEY
UNION
	SELECT
		CHAR ('<coreTableName_ForEntityType> ' || CHAR ("<coreTableName>".PK), <idSize>) AS ID,
		CHAR ('<coreTableName_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ('<coreTableName> ' || CHAR ("<coreTableName>".PK), <labelSize>) AS "Label"
		<coreTableNameColumns>
	FROM
		"<coreTableName>"
>>

edgeQuery(
		leafTableName,
		leafTableName_ForEntityType,
		coreTableName,
		coreTableName_ForEntityType,
		idSize) ::= <<
SELECT
	CHAR ('<leafTableName_ForEntityType> ' || CHAR ("<leafTableName>".PK), <idSize>) AS TARGET,
	CHAR ('<coreTableName_ForEntityType> ' || CHAR ("<coreTableName>".PK), <idSize>) AS SOURCE,
	COUNT (*) AS "Weight"
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
