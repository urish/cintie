package org.urish.cintie.ui;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.urish.cintie.engine.CintieEngine;
import org.urish.cintie.engine.Player;

public class UIWindow {

	protected Shell shlMusicRoomControl;
	CintieEngine engine = new CintieEngine();

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIWindow window = new UIWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlMusicRoomControl.open();
		shlMusicRoomControl.layout();
		while (!shlMusicRoomControl.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlMusicRoomControl = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shlMusicRoomControl.setSize(575, 439);
		shlMusicRoomControl.setText("Music Room Control Panel");
		shlMusicRoomControl.setLayout(new FormLayout());

		Composite composite = new Composite(shlMusicRoomControl, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 6);
		fd_composite.left = new FormAttachment(0, 12);
		composite.setLayoutData(fd_composite);

		Button btnStart = new Button(composite, SWT.NONE);
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				engine.start();
			}
		});
		btnStart.setText("Start");

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				engine.stop();
			}
		});
		btnNewButton.setText("Stop");

		Button btnexit = new Button(composite, SWT.NONE);
		btnexit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlMusicRoomControl.close();
			}
		});
		btnexit.setText("&Exit");

		final Canvas canvas = new Canvas(shlMusicRoomControl, SWT.BORDER);
		canvas.setBackground(new Color(shlMusicRoomControl.getDisplay(), 255, 255, 255));
		FormData fd_canvas = new FormData();
		fd_canvas.right = new FormAttachment(composite, 439, SWT.RIGHT);
		fd_canvas.top = new FormAttachment(composite, 6);
		fd_canvas.bottom = new FormAttachment(0, 401);
		fd_canvas.left = new FormAttachment(0, 10);
		canvas.setLayoutData(fd_canvas);

		LightweightSystem lws = new LightweightSystem(canvas);
		Figure contents = new Figure();
		XYLayout contentsLayout = new XYLayout();
		contents.setLayoutManager(contentsLayout);

		for (int i = 0; i < engine.playerCount(); i++) {
			Font classFont = new Font(null, "Arial", 12, SWT.BOLD);
			Label classLabel1 = new Label("P" + (i + 1));
			classLabel1.setFont(classFont);

			final Player player = engine.player(i);
			PawnFigure pawnFigure = new PawnFigure(classLabel1);
			
			new FigureMover(pawnFigure).addMoveListener(new MoveListener() {
				@Override
				public void figureMoved(IFigure figure, Rectangle bounds) {
					float x = (float)bounds.x / (canvas.getBounds().width - bounds.width);
					float y = (float)bounds.y / (canvas.getBounds().height - bounds.height);
					player.moveTo(x,y);
				}
			});
			contentsLayout.setConstraint(pawnFigure, new Rectangle(10, 10, -1,
					-1));

			/* Creating the decoration */
			PolygonDecoration decoration = new PolygonDecoration();
			PointList decorationPointList = new PointList();
			decorationPointList.addPoint(0, 0);
			decorationPointList.addPoint(-2, 2);
			decorationPointList.addPoint(-4, 0);
			decorationPointList.addPoint(-2, -2);
			decoration.setTemplate(decorationPointList);

			contents.add(pawnFigure);
		}

		lws.setContents(contents);
	}
}
