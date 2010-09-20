group leaf_core_bipartite;

nodeQuery(
		leafTableName,
		leafTableName_ForEntityType,
		coreTableName,
		coreTableName_ForEntityType,
		coreTableColumnNames,
		emptyCoreTableColumnNames,
		coreTableColumnNamesForGroupBy,
		coreAggregates,
		leafTableAggregates,
		leafTableAggregates_Joins,
		emptyCoreAggregates,
		idSize,
		entityTypeSize,
		labelSize) ::= <<
	SELECT
		CHAR ('<leafTableName_ForEntityType> ' || CHAR ("<leafTableName>".PK), <idSize>) AS ID,
		CHAR ('<leafTableName_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ("<leafTableName>"."<leafTableName>", <labelSize>) AS "Label"<if(emptyCoreTableColumnNames)>,
		<emptyCoreTableColumnNames; separator=",\n">
		<endif><if(coreAggregates)>,
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
UNION
	SELECT
		CHAR ('<coreTableName_ForEntityType> ' || CHAR ("<coreTableName>".PK), <idSize>) AS ID,
		CHAR ('<coreTableName_ForEntityType>', <entityTypeSize>) AS "Entity_Type",
		VARCHAR ('<coreTableName_ForEntityType> ' || CHAR ("<coreTableName>".PK), <labelSize>) AS "Label"<if(coreTableColumnNames)>,
		<coreTableColumnNames; separator=",\n">
		<endif><if(emptyCoreAggregates)>,
		<emptyCoreAggregates; separator=",\n">
		<endif><if(leafTableAggregates)>,
    	<leafTableAggregates; separator=",\n">
    	<endif>
	FROM
		"<coreTableName>"
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
		"<coreTableName>".PK<if(coreTableColumnNamesForGroupBy)>,
		<coreTableColumnNamesForGroupBy; separator=",\n">
		<endif>
>>

edgeQuery(
		leafTableName,
		leafTableName_ForEntityType,
		coreTableName,
		coreTableName_ForEntityType,
		coreAggregates,
		idSize) ::= <<
SELECT
	CHAR ('<leafTableName_ForEntityType> ' || CHAR ("<leafTableName>".PK), <idSize>) AS SOURCE,
	CHAR ('<coreTableName_ForEntityType> ' || CHAR ("<coreTableName>".PK), <idSize>) AS TARGET,
	'<leafTableName> to <coreTableName> Bipartite' AS "Network_Type",
	COUNT (*) AS "Weight"<if(coreAggregates)>,
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
	"<coreTableName>".PK
>>
