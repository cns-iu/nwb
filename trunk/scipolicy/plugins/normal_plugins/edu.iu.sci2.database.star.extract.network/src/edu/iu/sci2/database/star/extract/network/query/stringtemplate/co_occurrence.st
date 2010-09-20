group co_occurrence;


nodeQuery(
	leafTableName,
	coreTableName,
	coreAggregates,
	leafTableAggregates,
	leafTableAggregates_Joins) ::= <<
SELECT
    "<leafTableName>".PK AS "ID",
    "<leafTableName>"."<leafTableName>" AS "Label"<if(coreAggregates)>,
    <coreAggregates; separator=",\n">
    <endif><if(leafTableAggregates)>,
    <leafTableAggregates; separator=",\n">
    <endif>
FROM
    "<leafTableName>"
JOIN
    <coreTableName>_TO_<leafTableName>
        ON "<leafTableName>".PK = <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<leafTableName>_FOREIGN_KEY
JOIN
    "<coreTableName>"
        ON <coreTableName>_TO_<leafTableName>.<coreTableName>_TO_<leafTableName>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK
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
    "<leafTableName>".PK,
    "<leafTableName>"."<leafTableName>"
>>

edgeQuery(
		leafTableName,
		coreTableName,
		coreAggregates,
		leafTableAggregates,
		leafTableAggregates_Joins) ::= <<
SELECT
    <leafTableName>1.PK AS "SOURCE",
    <leafTableName>2.PK AS "TARGET",
    '<leafTableName> Co-Occurrence' AS "Network_Type",
    COUNT (*) AS "Weight"<if(coreAggregates)>,
    <coreAggregates; separator=",\n">
    <endif><if(leafTableAggregates)>,
	<leafTableAggregates; separator=",\n">
	<endif>
FROM
    "<leafTableName>" <leafTableName>1
JOIN
    <coreTableName>_TO_<leafTableName> <coreTableName>_TO_<leafTableName>1
        ON <leafTableName>1.PK = <coreTableName>_TO_<leafTableName>1.<coreTableName>_TO_<leafTableName>_<leafTableName>_FOREIGN_KEY
JOIN
    "<coreTableName>"
        ON <coreTableName>_TO_<leafTableName>1.<coreTableName>_TO_<leafTableName>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK
JOIN
    <coreTableName>_TO_<leafTableName> <coreTableName>_TO_<leafTableName>2
        ON "<coreTableName>".PK = <coreTableName>_TO_<leafTableName>2.<coreTableName>_TO_<leafTableName>_<coreTableName>_FOREIGN_KEY
JOIN
    "<leafTableName>" <leafTableName>2
        ON <coreTableName>_TO_<leafTableName>2.<coreTableName>_TO_<leafTableName>_<leafTableName>_FOREIGN_KEY = <leafTableName>2.PK
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
WHERE
    <leafTableName>1.PK \< <leafTableName>2.PK
GROUP BY
    <leafTableName>1.PK,
    <leafTableName>2.PK
>>
