package edu.iu.sci2.visualization.scimaps.rendering;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.full.FullRenderablePageManager;
import edu.iu.sci2.visualization.scimaps.rendering.simple.SimpleRenderablePageManager;

public enum Layout {
	SIMPLE() {
		@Override
		public AbstractRenderablePageManager createPageManager(MapOfScience mapOfScience,
				float scalingFactor, String generatedFrom) {
			return new SimpleRenderablePageManager(this, getDimensions(), mapOfScience, scalingFactor);
		}

		@Override
		public Dimension getDimensions() {
			return new Dimension(1280, 960);
		}

		@Override
		public float getAreaForLegendMax() {
			return 100;
		}

		@Override
		public float getMapSpecificScalingFactor() {
			return 2.1f;
		}
	},
	FULL() {
		@Override
		public AbstractRenderablePageManager createPageManager(MapOfScience mapOfScience,
				float scalingFactor, String generatedFrom) {
			return new FullRenderablePageManager(this, getDimensions(), mapOfScience, scalingFactor,
					generatedFrom);
		}
		
		@Override
		public float getMapSpecificScalingFactor() {
			return 1.3f;
		}

		@Override
		public Dimension getDimensions() {
			return new Dimension((int) inch(11.0f), (int) inch(8.5f));
		}

		@Override
		public float getAreaForLegendMax() {
			return 50;
		}
	};
	
	public abstract AbstractRenderablePageManager createPageManager(MapOfScience mapOfScience,
			float scalingFactor, String generatedFrom);
	
	public abstract Dimension getDimensions();

	public abstract float getAreaForLegendMax();

	/**
	 * This scaling factor is applied to the graphic state in both dimensions while rendering the
	 * map nodes, edges, etc.
	 * 
	 * <p>
	 * <strong>Note:</strong> As the code is written now, this scaling factor is
	 * <strong>not</strong> applied to the sizes of the subdiscipline circles. This keeps the
	 * subdiscipline circle sizes on the same scale as the legend circle sizes.
	 */
	public abstract float getMapSpecificScalingFactor();
}
