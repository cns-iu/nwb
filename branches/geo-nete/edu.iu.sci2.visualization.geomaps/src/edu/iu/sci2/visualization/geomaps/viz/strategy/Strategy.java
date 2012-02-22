package edu.iu.sci2.visualization.geomaps.viz.strategy;

import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;

public interface Strategy extends PostScriptable {
	String toPostScript();
}
