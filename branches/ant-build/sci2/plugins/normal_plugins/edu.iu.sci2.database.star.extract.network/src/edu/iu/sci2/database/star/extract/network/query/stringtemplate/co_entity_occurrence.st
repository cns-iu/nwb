group co_entity_occurrence;


quote() ::= <<
"<it>"
>>

nodeQuery(
	nodeLeafTableName,
	coreTableName,
	coreAggregates,
	leafTableAggregates,
	leafTableAggregates_Joins) ::= <<
SELECT
	"<nodeLeafTableName>".PK AS "ID",
	"<nodeLeafTableName>".PK AS "<nodeLeafTableName>ID",
	"<nodeLeafTableName>"."<nodeLeafTableName>" AS "Label"<if(coreAggregates)>,
	<coreAggregates; separator=",\n">
	<endif><if(leafTableAggregates)>,
    <leafTableAggregates; separator=",\n">
    <endif>
FROM
	"<nodeLeafTableName>"
JOIN
	<coreTableName>_TO_<nodeLeafTableName> <coreTableName>_TO_<nodeLeafTableName>1
		ON ("<nodeLeafTableName>".PK = <coreTableName>_TO_<nodeLeafTableName>1.<coreTableName>_TO_<nodeLeafTableName>_<nodeLeafTableName>_FOREIGN_KEY)
JOIN
	"<coreTableName>"
		ON (<coreTableName>_TO_<nodeLeafTableName>1.<coreTableName>_TO_<nodeLeafTableName>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK)
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
	"<nodeLeafTableName>".PK,
	"<nodeLeafTableName>"."<nodeLeafTableName>"
>>

edgeQuery(
	nodeLeafTableName,
	viaTableName,
	coreTableName,
	coreAggregates,
	leafTableAggregates,
	leafTableAggregates_Joins,
	groupBy) ::= <<
SELECT
	<nodeLeafTableName>1.PK AS "SOURCE",
	<nodeLeafTableName>2.PK AS "TARGET",
	'<nodeLeafTableName> Co-<viaTableName> Occurrence' AS "Network_Type",
	COUNT (DISTINCT "<viaTableName>"."<viaTableName>") AS "Weight"<if(coreAggregates)>,
	<coreAggregates; separator=",\n">
	<endif><if(leafTableAggregates)>,
    <leafTableAggregates; separator=",\n">
    <endif>
FROM
	"<nodeLeafTableName>" <nodeLeafTableName>1
JOIN
	<coreTableName>_TO_<nodeLeafTableName> <coreTableName>_TO_<nodeLeafTableName>1
		ON (<nodeLeafTableName>1.PK = <coreTableName>_TO_<nodeLeafTableName>1.<coreTableName>_TO_<nodeLeafTableName>_<nodeLeafTableName>_FOREIGN_KEY)
JOIN
	"<coreTableName>" <coreTableName>1
		ON (<coreTableName>_TO_<nodeLeafTableName>1.<coreTableName>_TO_<nodeLeafTableName>_<coreTableName>_FOREIGN_KEY = <coreTableName>1.PK)
JOIN
	<coreTableName>_TO_<viaTableName> <coreTableName>_TO_<viaTableName>1
		ON (<coreTableName>1.PK = <coreTableName>_TO_<viaTableName>1.<coreTableName>_TO_<viaTableName>_<coreTableName>_FOREIGN_KEY)
JOIN
	"<viaTableName>"
		ON (<coreTableName>_TO_<viaTableName>1.<coreTableName>_TO_<viaTableName>_<viaTableName>_FOREIGN_KEY = "<viaTableName>".PK)
JOIN
	<coreTableName>_TO_<viaTableName> <coreTableName>_TO_<viaTableName>2
		ON ("<viaTableName>".PK = <coreTableName>_TO_<viaTableName>2.<coreTableName>_TO_<viaTableName>_<viaTableName>_FOREIGN_KEY)
JOIN
	"<coreTableName>" <coreTableName>2
		ON (<coreTableName>_TO_<viaTableName>2.<coreTableName>_TO_<viaTableName>_<coreTableName>_FOREIGN_KEY = <coreTableName>2.PK)
JOIN
	<coreTableName>_TO_<nodeLeafTableName> <coreTableName>_TO_<nodeLeafTableName>2
		ON (<coreTableName>2.PK = <coreTableName>_TO_<nodeLeafTableName>2.<coreTableName>_TO_<nodeLeafTableName>_<coreTableName>_FOREIGN_KEY)
JOIN
	"<nodeLeafTableName>" <nodeLeafTableName>2
		ON (<coreTableName>_TO_<nodeLeafTableName>2.<coreTableName>_TO_<nodeLeafTableName>_<nodeLeafTableName>_FOREIGN_KEY = <nodeLeafTableName>2.PK)
<if(leafTableAggregates_Joins)>
<leafTableAggregates_Joins:{
LEFT JOIN
	<coreTableName>_TO_<it> <coreTableName>_TO_<it>_1
		ON (<coreTableName>1.PK = <coreTableName>_TO_<it>_1.<coreTableName>_TO_<it>_<coreTableName>_FOREIGN_KEY)
LEFT JOIN
	"<it>" <it>_1
		ON (<coreTableName>_TO_<it>_1.<coreTableName>_TO_<it>_<it>_FOREIGN_KEY = <it>_1.PK)
}>
<endif>
WHERE
	<nodeLeafTableName>1.PK \< <nodeLeafTableName>2.PK
GROUP BY
	<nodeLeafTableName>1.PK,
	<nodeLeafTableName>2.PK
>>