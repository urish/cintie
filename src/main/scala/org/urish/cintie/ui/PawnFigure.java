package org.urish.cintie.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;

public class PawnFigure extends Figure {
	public static Color classColor = new Color(null, 255, 255, 206);

	public PawnFigure(Label name) {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(classColor);
		setOpaque(true);

		add(name);
	}
}
