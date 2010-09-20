group table;


query(
	leafTableName,
	coreTableName,
	sentenceCased_LeafTableName,
	coreAggregates,
	leafTableAggregates,
	leafTableAggregates_Joins) ::= <<

SELECT
	"<leafTableName>"."<leafTableName>" AS "<sentenceCased_LeafTableName>",
	"<leafTableName>".PK AS "Database <sentenceCased_LeafTableName> ID"<if(coreAggregates)>,
	<coreAggregates; separator=",\n">
	<endif><if(leafTableAggregates)>,
	<leafTableAggregates; separator=",\n">
	<endif>
FROM
	"<leafTableName>"
JOIN
	<coreTableName>_TO_<leafTableName>
		ON ("<leafTableName>".PK = <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<leafTableName>_FOREIGN_KEY)
JOIN
	"<coreTableName>"
		ON (<coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK)
<if(leafTableAggregates_Joins)>
<leafTableAggregates_Joins:{
LEFT JOIN
	<coreTableName>_TO_<it> <coreTableName>_TO_<it>_1
		ON ("<coreTableName>".PK = <coreTableName>_TO_<it>_1.<coreTableName>_TO_<it>_<coreTableName>_FOREIGN_KEY)
LEFT JOIN
	"<it>" <it>_1
		ON (<coreTableName>_TO_<it>_1.<coreTableName>_TO_<it>_<it>_FOREIGN_KEY = <it>_1.PK)
}>
<endif>
GROUP BY
	"<leafTableName>"."<leafTableName>",
	"<leafTableName>".PK
>>