package edu.iu.sci2.visualization.geomaps;

import edu.iu.sci2.visualization.geomaps.viz.PageLayout;

public class GeoMapsWebRegionsFactory extends GeoMapsRegionsFactory {
	@Override
	PageLayout getPageLayout() {
		return PageLayout.WEB;
	}
}
