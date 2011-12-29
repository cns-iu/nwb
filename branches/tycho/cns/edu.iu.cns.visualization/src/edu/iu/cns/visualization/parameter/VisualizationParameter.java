package edu.iu.cns.visualization.parameter;

public class VisualizationParameter<V> {
	private String name;
	private String label;
	private String description;
	private V value;

	public VisualizationParameter(String name, String label, String description, V defaultValue) {
		this.name = name;
		this.description = description;
		this.label = label;
		this.value = defaultValue;
	}

	public String getName() {
		return this.name;
	}

	public String getLabel() {
		return this.label;
	}

	public String getDescription() {
		return this.description;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}