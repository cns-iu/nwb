group leaf_leaf_bipartite;

nodeQuery(
		leafTable1Name,
		leafTable1Name_ForEntityType,
		leafTable2Name,
		leafTable2Name_ForEntityType,
		coreTableName,
		coreAggregates,
		leafTableAggregates,
		leafTableAggregates_Joins,
		idSize,
		entityTypeSize,
		labelSize) ::= <<
	SELECT
		CHAR ('<leafTable1Name_ForEntityType> ' || CHAR ("<leafTable1Name>".PK), <idSize>) AS ID,
		CHAR ('<leafTable1Name_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ("<leafTable1Name>"."<leafTable1Name>", <labelSize>) AS "Label"<if(coreAggregates)>,
		<coreAggregates; separator=",\n">
		<endif><if(leafTableAggregates)>,
    	<leafTableAggregates; separator=",\n">
    	<endif>
	FROM
		"<leafTable1Name>"
	JOIN
		<coreTableName>_TO_<leafTable1Name>
			ON "<leafTable1Name>".PK = <coreTableName>_TO_<leafTable1Name>.<coreTableName>_TO_<leafTable1Name>_<leafTable1Name>_FOREIGN_KEY
	JOIN
		"<coreTableName>"
			ON <coreTableName>_TO_<leafTable1Name>.<coreTableName>_TO_<leafTable1Name>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK
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
		"<leafTable1Name>".PK,
		"<leafTable1Name>"."<leafTable1Name>"
UNION
	SELECT
		CHAR ('<leafTable2Name_ForEntityType> ' || CHAR ("<leafTable2Name>".PK), <idSize>) AS ID,
		CHAR ('<leafTable2Name_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ("<leafTable2Name>"."<leafTable2Name>", <labelSize>) AS "Label"<if(coreAggregates)>,
		<coreAggregates; separator=",\n">
		<endif><if(leafTableAggregates)>,
    	<leafTableAggregates; separator=",\n">
    	<endif>
	FROM
		"<leafTable2Name>"
	JOIN
		<coreTableName>_TO_<leafTable2Name>
			ON "<leafTable2Name>".PK = <coreTableName>_TO_<leafTable2Name>.<coreTableName>_TO_<leafTable2Name>_<leafTable2Name>_FOREIGN_KEY
	JOIN
		"<coreTableName>"
			ON <coreTableName>_TO_<leafTable2Name>.<coreTableName>_TO_<leafTable2Name>_<coreTableName>_FOREIGN_KEY = "<coreTableName>".PK
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
		"<leafTable2Name>".PK,
		"<leafTable2Name>"."<leafTable2Name>"
>>

edgeQuery(
		leafTable1Name,
		leafTable1Name_ForEntityType,
		leafTable2Name,
		leafTable2Name_ForEntityType,
		coreTableName,
		coreAggregates,
		leafTableAggregates,
		leafTableAggregates_Joins,
		idSize) ::= <<
SELECT
	CHAR ('<leafTable1Name_ForEntityType> ' || CHAR ("<leafTable1Name>".PK), <idSize>) AS SOURCE,
	CHAR ('<leafTable2Name_ForEntityType> ' || CHAR ("<leafTable2Name>".PK), <idSize>) AS TARGET,
	'<leafTable1Name> to <leafTable2Name> Bipartite' AS "Network_Type",
	COUNT (*) AS "Weight"<if(coreAggregates)>,
	<coreAggregates; separator=",\n">
	<endif><if(leafTableAggregates)>,
    <leafTableAggregates; separator=",\n">
    <endif>
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
	"<leafTable1Name>".PK,
	"<leafTable2Name>".PK
>>
