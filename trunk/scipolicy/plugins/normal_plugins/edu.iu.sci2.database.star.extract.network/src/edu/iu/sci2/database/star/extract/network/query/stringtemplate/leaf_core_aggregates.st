group leaf_core_aggregates;

nodeQuery(
		leafTableName,
		leafTableName_SentenceCased,
		leafTableName_SentencedCased_WithUnderscorePostfix,
		coreTableName,
		coreTableName_SentenceCased,
		coreTableName_SentencedCased_WithUnderscorePostfix,
		nonAggregateCoreTableNameColumns,
		aggregates,
		idSize,
		entityTypeSize,
		labelSize) ::= <<
	SELECT
		CHAR ('$leafTableName_SentencedCased_WithUnderscorePostfix$' || CHAR ($leafTableName$.PK), $idSize$) AS ID,
		CHAR ('$leafTableName_SentenceCased$', $entityTypeSize$) AS "Entity_Type",
		CHAR ($leafTableName$.$leafTableName$, $labelSize$) AS "Label",
		$nonAggregateCoreTableNameColumns$,
		$aggregates$
	FROM
		$leafTableName$
	JOIN
		$coreTableName$_TO_$leafTableName$
			ON $leafTableName$.PK = $coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$leafTableName$_FOREIGN_KEY
	GROUP BY
		$leafTableName$.PK,
		$leafTableName$.$leafTableName$,
		$nonAggregateCoreTableNameColumns$
UNION
	SELECT
		CHAR ('$coreTableName_SentencedCased_WithUnderscorePostfix$' || CHAR ($coreTableName$.PK), $idSize$) AS ID,
		CHAR ('$coreTableName_SentenceCased$', $entityTypeSize$) AS "Entity_Type",
		CHAR ('$coreTableName$ ' || CHAR ($coreTableName$.PK), $labelSize) AS "Label",
		$nonAggregateCoreTableNameColumns$,
		$aggregates$
	FROM
		$coreTableName$
	GROUP BY
		$coreTableName$.PK,
		$nonAggregateCoreTableNameColumns$
>>

edgeQuery(
		leafTableName,
		coreTableName,
		leafTableName_SentencedCased_WithUnderscorePostfix,
		coreTableName_SentenceCased,
		leafEdgeEnd,
		coreEdgeEnd,
		nonAggregateCoreTableNameColumns,
		aggregates,
		idSize) ::= <<
SELECT
	CHAR ('$leafTableName_SentencedCased_WithUnderscorePostfix$' || CHAR ($leafTableName$.PK), $idSize$) AS $leafEdgeEnd$,
	CHAR ('$coreTableName_SentenceCased$' || CHAR ($coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$coreTableName$_FOREIGN_KEY), $idSize$) AS $coreEdgeEnd$,
	$nonAggregateCoreTableNameColumns$,
	$aggregates$,
	COUNT (*) AS "Weight"
FROM
	$leafTableName$
JOIN
	$coreTableName$_TO_$leafTableName$
		ON $leafTableName$.PK = $coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$leafTableName$_FOREIGN_KEY
GROUP BY
	$leafTableName$.PK,
	$coreTableName$_TO_$leafTableName$.$coreTableName$_TO_$leafTableName$_$coreTableName$_FOREIGN_KEY,
	$nonAggregateCoreTableNameColumns$
>>
