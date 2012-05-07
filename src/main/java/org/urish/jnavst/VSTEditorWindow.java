package org.urish.jnavst;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.sun.jna.Pointer;

public class VSTEditorWindow {
	private final VSTPlugin plugin;
	private final Shell shell;

	public VSTEditorWindow(VSTPlugin plugin, Shell shell) {
		super();
		this.plugin = plugin;
		this.shell = shell;
		ERect rect = plugin.getEditRect();
		shell.setSize(rect.right - rect.left + shell.getSize().x - shell.getClientArea().width, rect.bottom - rect.top
				+ shell.getSize().y - shell.getClientArea().height);
	}

	public VSTEditorWindow(VSTPlugin plugin, String title) {
		this(plugin, new Shell(SWT.DIALOG_TRIM | SWT.MIN));
		shell.setText(title);
	}

	public void open() {
		shell.open();
		plugin.editOpen(new Pointer(shell.handle));
	}

	public void close() {
		shell.close();
		plugin.editClose();
	}

	public void dispose() {
		shell.dispose();
	}

	public Shell getShell() {
		return shell;
	}

	public VSTPlugin getPlugin() {
		return plugin;
	}
}
