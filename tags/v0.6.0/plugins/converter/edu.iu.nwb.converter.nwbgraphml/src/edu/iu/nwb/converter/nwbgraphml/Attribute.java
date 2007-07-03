package edu.iu.nwb.converter.nwbgraphml;

/* KeyDO class set 
 * attributes of key element of Graphml File */
 
public class Attribute {
	private String id;
	private String domain  ;
	private String name;
	private String type;
	private String value = null;
	
	 
	public String getId()
	{
		return this.id;
	}
	public void setId(String id)
	{
		 this.id = id;
	}

	public String getDomain()
	{
		return this.domain;
	}
	public void setDomain(String domain)
	{
		 this.domain = domain;
	}


	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		
		 this.name = name.replace(' ', '_');
	}

	public String getType()
	{
		return this.type;
	}
	public void setType(String type)
	{
		 this.type = type;
	}
	
	public boolean isForNode() {
		return "node".equals(domain) || "all".equals(domain);
	}
	
	public boolean isForEdge() {
		return "edge".equals(domain) || "all".equals(domain);
	}
	
	public boolean isReserved() {
		boolean reserved = false;
		if(isForNode()) {
			reserved = reserved || "id".equals(name) || "label".equals(name);
		}
		if(isForEdge()) {
			reserved = reserved || "source".equals(name) || "target".equals(name) || "weight".equals(name);
		}
		return reserved;
	}
	
	public boolean isString() {
		return "string".equals(type);
	}
	public void setDefault(String value) {
		this.value = value;
	}
	
	public String getDefault() {
		return value;
	}
	
	public boolean hasDefault() {
		return value != null;
	}
	
	public boolean isForLabel() {
		return this.name.equals("label");
	}
	
	public boolean isForWeight() {
		return this.name.equals("weight");
	}
	


}
