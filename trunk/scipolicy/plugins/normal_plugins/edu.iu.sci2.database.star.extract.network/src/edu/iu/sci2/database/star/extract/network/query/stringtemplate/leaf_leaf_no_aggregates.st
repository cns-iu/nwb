group leaf_leaf_no_aggregates;

nodeQuery(
		leafTable1Name,
		leafTable1Name_ForEntityType,
		leafTable2Name,
		leafTable2Name_ForEntityType,
		coreTableName,
		idSize,
		entityTypeSize,
		labelSize) ::= <<
	SELECT
		CHAR ('<leafTable1Name_ForEntityType> ' || CHAR ("<leafTable1Name>".PK), <idSize>) AS ID,
		CHAR ('<leafTable1Name_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ("<leafTable1Name>"."<leafTable1Name>", <labelSize>) AS "Label"
	FROM
		"<leafTable1Name>"
	JOIN
		<coreTableName>_TO_<leafTable1Name>
			ON "<leafTable1Name>".PK = <coreTableName>_TO_<leafTable1Name>.<coreTableName>_TO_<leafTable1Name>_<leafTable1Name>_FOREIGN_KEY
	JOIN
		"<coreTableName>"
			ON <coreTableName>_TO_<leafTable1Name>.<coreTableName>_TO_<leafTable1Name>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK
UNION
	SELECT
		CHAR ('<leafTable2Name_ForEntityType> ' || CHAR ("<leafTable2Name>".PK), <idSize>) AS ID,
		CHAR ('<leafTable2Name_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ("<leafTable2Name>"."<leafTable2Name>", <labelSize>) AS "Label"
	FROM
		"<leafTable2Name>"
	JOIN
		<coreTableName>_TO_<leafTable2Name>
			ON "<leafTable2Name>".PK = <coreTableName>_TO_<leafTable2Name>.<coreTableName>_TO_<leafTable2Name>_<leafTable2Name>_FOREIGN_KEY
	JOIN
		"<coreTableName>"
			ON <coreTableName>_TO_<leafTable2Name>.<coreTableName>_TO_<leafTable2Name>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK
>>

edgeQuery(
		leafTable1Name,
		leafTable1Name_ForEntityType,
		leafTable2Name,
		leafTable2Name_ForEntityType,
		coreTableName,
		idSize) ::= <<
SELECT
	CHAR ('<leafTable1Name_ForEntityType> ' || CHAR ("<leafTable1Name>".PK), <idSize>) AS TARGET,
	CHAR ('<leafTable2Name_ForEntityType> ' || CHAR ("<leafTable2Name>".PK), <idSize>) AS SOURCE,
	COUNT (*) AS "Weight"
FROM
	"<leafTable1Name>"
JOIN
	<coreTableName>_TO_<leafTable1Name>
		ON "<leafTable1Name>".PK = <coreTableName>_TO_<leafTable1Name>.<coreTableName>_TO_<leafTable1Name>_<leafTable1Name>_FOREIGN_KEY
JOIN
	"<coreTableName>"
		ON <coreTableName>_TO_<leafTable1Name>.<coreTableName>_TO_<leafTable1Name>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK
JOIN
	<coreTableName>_TO_<leafTable2Name>
		ON "<coreTableName>".PK = <coreTableName>_TO_<leafTable2Name>_<coreTableName>_FOREIGN_KEY
JOIN
	"<leafTable2Name>"
		ON <coreTableName>_TO_<leafTable2Name>.<coreTableName>_TO_<leafTable2Name>_<leafTable2Name>_FOREIGN_KEY = "<leafTable2Name>".PK
GROUP BY
	"<leafTable1Name>".PK,
	"<leafTable2Name>".PK
>>
