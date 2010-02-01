group match_reference_to_paper_group;

matchReferenceToPaperQuery(
	referenceTableName,
	referencePK,
	referenceAuthorFK,
	referencePageNumber,
	referencePaperFK,
	referenceSource,
	referenceVolume,
	referenceYear,
	documentTableName,
	documentPK,
	documentBeginningPage,
	documentFirstAuthorFK,
	documentSource,
	documentVolume,
	documentYear) ::= <<

UPDATE
	<referenceTableName>
SET
	<referenceTableName>.<referencePaperFK> = <documentTableName>.<documentPK>
WHERE
	<referenceTableName>.<referencePK>
IN (
	SELECT
		<referenceTableName>.<referencePK>
	FROM
			<referenceTableName>
		JOIN
			<documentTableName>
		ON
			(<referenceTableName>.<referenceAuthorFK> = <documentTableName>.<documentFirstAuthorFK>)
		WHERE
			(<referenceTableName>.<referencePaperFK> IS NULL) AND
			((<referenceTableName>.<referenceAuthorFK> IS NOT NULL) AND (<referenceTableName>.<referenceAuthorFK> = <documentTableName>.<documentFirstAuthorFK>)) AND
			((<referenceTableName>.<referencePageNumber> IS NOT NULL) AND (<referenceTableName>.<referencePageNumber> = <documentTableName>.<documentBeginningPage>)) AND
			((<referenceTableName>.<referenceSource> IS NOT NULL) AND (<referenceTableName>.<referenceSource> = <documentTableName>.<documentSource>)) AND
			((<referenceTableName>.<referenceVolume> IS NOT NULL) AND (<referenceTableName>.<referenceVolume> = <documentTableName>.<documentVolume>)) AND
			((<referenceTableName>.<referenceYear> IS NOT NULL) AND (<referenceTableName>.<referenceYear> = <documentTableName>.<documentYear>)))

>>