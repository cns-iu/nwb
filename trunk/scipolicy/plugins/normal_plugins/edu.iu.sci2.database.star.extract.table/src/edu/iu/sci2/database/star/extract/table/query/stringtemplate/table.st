group table;


query(
	leafTableName,
	coreTableName,
	sentenceCased_LeafTableName,
	coreNonAggregates,
	coreAggregates,
	leafTableAggregates,
	leafTableAggregates_Joins,
	groupBy) ::= <<

SELECT
	"<leafTableName>"."<leafTableName>" AS "<sentenceCased_LeafTableName>",
	"<leafTableName>".PK AS "Database <sentenceCased_LeafTableName> ID"
	<coreAggregates>
	<leafTableAggregates>
FROM
	"<leafTableName>"
JOIN
	<coreTableName>_TO_<leafTableName>
		ON ("<leafTableName>".PK = <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<leafTableName>_FOREIGN_KEY)
JOIN
	"<coreTableName>"
		ON (<coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK)
<leafTableAggregates_Joins>
<groupBy>

>>