package edu.iu.nwb.shared.isiutil;

public class ContentType {

	//IMPORTANT: If you add a tag here, make sure to add it in the typesArray below as well.

	public static final ContentType MULTI_VALUE_TEXT = new ContentType(
			"multi-value-text", String.class);

	public static final ContentType TEXT = new ContentType("text", String.class);

	public static final ContentType INTEGER = new ContentType("integer",
			int.class);

	public static final ContentType NULL = new ContentType("null", null);

	private static final ContentType[] typesArray = { MULTI_VALUE_TEXT, TEXT,
			INTEGER, NULL };

	private String name;

	private Class tableDataType;

	private ContentType(String name, Class tableDataType) {
		this.name = name;
		this.tableDataType = tableDataType;
	}

	public String toString() {
		return name;
	}

	//returns null if the contentType is null (that is, the tag has no contents)
	public Class getTableDataType() {
		return this.tableDataType;
	}

	public static ContentType[] getAllContentTypes() {
		return typesArray;
	}
}
