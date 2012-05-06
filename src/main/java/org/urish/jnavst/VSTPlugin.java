package org.urish.jnavst;

import java.awt.Canvas;
import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;

import org.urish.jnavst.AEffect.Opcode;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class VSTPlugin {
	private final AEffect effect;

	public VSTPlugin(File vstFile) {
		VSTSharedLibrary vst = (VSTSharedLibrary) Native.loadLibrary(vstFile.getAbsolutePath(),
				VSTSharedLibrary.class);
		this.effect = vst.VSTPluginMain(new HostCallback());
		if (effect == null) {
			throw new VSTException("VST Plugin Creation Failed, returned null");
		}
		if (effect.magic != AEffect.K_EFFECT_MAGIC) {
			throw new VSTException("VST Plugin Creation Failed, returned invalid magic: " + effect.magic);
		}

	}

	private boolean b(long value) {
		return value != VSTConst.VST_FALSE;
	}

	private boolean booleanDispatch(Opcode opcode) {
		return b(effect.dispatcher.callback(effect, opcode.code, 0, 0, null, 0));
	}

	private boolean booleanDispatch(Opcode opcode, Pointer pointer) {
		return b(effect.dispatcher.callback(effect, opcode.code, 0, 0, pointer, 0));
	}

	public boolean open() {
		return booleanDispatch(Opcode.effOpen);
	}

	public boolean close() {
		return booleanDispatch(Opcode.effClose);
	}

	public boolean editOpen(Pointer window) {
		return booleanDispatch(Opcode.effEditOpen, window);
	}

	public boolean editOpen(Component component) {
		return editOpen(Native.getComponentPointer(component));
	}

	public boolean editOpen(String windowTitle) {
		ERect rect = getEditRect();
		JFrame frame = new JFrame("VST Plugin");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setVisible(true);
		Canvas panel = new Canvas();
		panel.setSize(400, 400);
		panel.setVisible(true);
		frame.add(panel);
		System.out.println(rect);
		return editOpen(panel);
	}

	public boolean editClose() {
		return booleanDispatch(Opcode.effEditClose);
	}

	public boolean editIdle() {
		return booleanDispatch(Opcode.effEditIdle);
	}

	public ERect getEditRect() {
		ERect result = new ERect();
		if (booleanDispatch(Opcode.effEditGetRect, result.getPointer())) {
			return result;
		} else {
			return null;
		}
	}

	public int getNumInputs() {
		return effect.numInputs;
	}

	public int getNumOutputs() {
		return effect.numOutputs;
	}

	public void processReplacing(float[][] inputs, float[][] outputs, int numFrames) {
		effect.processReplacing.callback(effect, inputs, outputs, numFrames);
	}

	/**
	 * Returns the name of the VST Plugin
	 */
	public String getName() {
		Pointer name = new Memory(VSTConst.VST_MaxEffectNameLen + 1);
		effect.dispatcher.callback(effect, Opcode.effGetEffectName.code, 0, 0, name, 0.f);
		return name.getString(0);
	}

	/**
	 * Returns the name of the VST Plugin Vendor
	 */
	public String getVendorString() {
		Pointer name = new Memory(VSTConst.VST_MaxVendorStrLen + 1);
		effect.dispatcher.callback(effect, Opcode.effGetVendorString.code, 0, 0, name, 0.f);
		return name.getString(0);
	}

	/**
	 * Returns the name of the VST Plugin Product
	 */
	public String getProductString() {
		Pointer name = new Memory(VSTConst.VST_MaxProductStrLen + 1);
		effect.dispatcher.callback(effect, Opcode.effGetProductString.code, 0, 0, name, 0.f);
		return name.getString(0);
	}
}
