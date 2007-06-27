package edu.iu.nwb.converter.nwbgraphml;

/* KeyDO class set 
 * attributes of key element of Graphml File */
 
public class KeyDO {
	private String id;
	private String domain  ;
	private String attr_name;
	private String attr_type;
	private String attr_value;
	
	 public KeyDO() {
		 this.attr_value = "*";
	 }
	 
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


	public String getAttr_name()
	{
		return this.attr_name;
	}
	
	public void setAttr_name(String attr_name)
	{
		 this.attr_name = attr_name;
	}

	public String getAttr_type()
	{
		return this.attr_type;
	}
	public void setattr_type(String attr_type)
	{
		 this.attr_type = attr_type;
	}
	public String getAttr_value() {
		return attr_value;
	}
	public void setAttr_value(String attr_value) {
		if (attr_value != null || attr_value.trim().length() > 0)
		{
			if (attr_value.equals("null"))
				this.attr_value = "*";
			else
				this.attr_value = attr_value;
		}
		else
			this.attr_value = "*";
	}
	


}
