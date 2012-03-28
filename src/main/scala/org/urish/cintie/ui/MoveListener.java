package org.urish.cintie.ui;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

public interface MoveListener {
	void figureMoved(IFigure figure, Rectangle bounds);
}
