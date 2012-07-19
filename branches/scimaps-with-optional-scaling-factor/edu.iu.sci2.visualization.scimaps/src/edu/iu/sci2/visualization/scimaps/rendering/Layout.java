package edu.iu.sci2.visualization.scimaps.rendering;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.full.FullRenderablePageManager;
import edu.iu.sci2.visualization.scimaps.rendering.simple.SimpleRenderablePageManager;

public enum Layout {
	SIMPLE(new Dimension(1280, 960)) {
		@Override
		public AbstractRenderablePageManager createPageManager(MapOfScience mapOfScience,
				float scalingFactor, String generatedFrom) {
			return new SimpleRenderablePageManager(getDimensions(), mapOfScience, scalingFactor);
		}
	},
	FULL(new Dimension((int) inch(11.0f), (int) inch(8.5f))) {
		@Override
		public AbstractRenderablePageManager createPageManager(MapOfScience mapOfScience,
				float scalingFactor, String generatedFrom) {
			return new FullRenderablePageManager(getDimensions(), mapOfScience, scalingFactor,
					generatedFrom);
		}
	};
	
	private final Dimension dimensions;

	private Layout(Dimension dimensions) {
		this.dimensions = dimensions;
	}
	
	public abstract AbstractRenderablePageManager createPageManager(MapOfScience mapOfScience,
			float scalingFactor, String generatedFrom);
	
	public Dimension getDimensions() {
		return dimensions;
	}
}
