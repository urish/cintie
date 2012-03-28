package org.urish.cintie.ui;

import java.util.Iterator;

import org.eclipse.draw2d.EventListenerList;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class FigureMover implements MouseListener, MouseMotionListener {
	private final IFigure figure;
	private final EventListenerList eventListeners = new EventListenerList();

	public FigureMover(IFigure figure) {
		this.figure = figure;
		figure.addMouseListener(this);
		figure.addMouseMotionListener(this);
	}

	private Point location;

	@Override
	public void mousePressed(MouseEvent event) {
		location = event.getLocation();
		event.consume();
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (location == null)
			return;
		Point newLocation = event.getLocation();
		if (newLocation == null)
			return;
		Dimension offset = newLocation.getDifference(location);
		if (offset.width == 0 && offset.height == 0)
			return;
		location = newLocation;

		UpdateManager updateMgr = figure.getUpdateManager();
		LayoutManager layoutMgr = figure.getParent().getLayoutManager();
		Rectangle bounds = figure.getBounds();
		updateMgr.addDirtyRegion(figure.getParent(), bounds);
		bounds = bounds.getCopy().translate(offset.width, offset.height);
		Rectangle parentBounds = figure.getParent().getBounds();
		bounds.x = Math.max(bounds.x, 0);
		bounds.y = Math.max(bounds.y, 0);
		bounds.x = Math.min(bounds.x, parentBounds.width - bounds.width);
		bounds.y = Math.min(bounds.y, parentBounds.height - bounds.height);
		layoutMgr.setConstraint(figure, bounds);
		figure.setBounds(bounds);
		updateMgr.addDirtyRegion(figure.getParent(), bounds);
		event.consume();
		
		Iterator iter = eventListeners.getListeners(MoveListener.class);
		while (iter.hasNext()) {
			((MoveListener) iter.next()).figureMoved(figure, figure.getBounds());
		}

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (location == null)
			return;
		location = null;
		event.consume();
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseHover(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDoubleClicked(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	public void addMoveListener(MoveListener listener) {
		eventListeners.addListener(MoveListener.class, listener);
	}
}
