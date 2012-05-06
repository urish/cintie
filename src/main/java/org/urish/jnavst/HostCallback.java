package org.urish.jnavst;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public class HostCallback implements Callback {
	private enum OpCodes {
		audioMasterAutomate,
		audioMasterVersion,
		audioMasterCurrentId,
		audioMasterIdle,
		audioMasterPinConnected,
		_placeholder_1, // empty slot before VST 2.0 stuff
		audioMasterWantMidi,
		audioMasterGetTime,
		audioMasterProcessEvents,
		audioMasterSetTime,
		audioMasterTempoAt,
		audioMasterGetNumAutomatableParameters,
		audioMasterGetParameterQuantization,
		audioMasterIOChanged,
		audioMasterNeedIdle,
		audioMasterSizeWindow,
		audioMasterGetSampleRate,
		audioMasterGetBlockSize,
		audioMasterGetInputLatency,
		audioMasterGetOutputLatency,
		audioMasterGetPreviousPlug,
		audioMasterGetNextPlug,
		audioMasterWillReplaceOrAccumulate,
		audioMasterGetCurrentProcessLevel,
		audioMasterGetAutomationState,
		audioMasterOfflineStart,
		audioMasterOfflineRead,
		audioMasterOfflineWrite,
		audioMasterOfflineGetCurrentPass,
		audioMasterOfflineGetCurrentMetaPass,
		audioMasterSetOutputSampleRate,
		audioMasterGetOutputSpeakerArrangement,
		audioMasterGetVendorString,
		audioMasterGetProductString,
		audioMasterGetVendorVersion,
		audioMasterVendorSpecific,
		audioMasterSetIcon,
		audioMasterCanDo,
		audioMasterGetLanguage,
		audioMasterOpenWindow,
		audioMasterCloseWindow,
		audioMasterGetDirectory,
		audioMasterUpdateDisplay,
		audioMasterBeginEdit,
		audioMasterEndEdit,
		audioMasterOpenFileSelector,
		audioMasterCloseFileSelector,
		audioMasterEditFile,
		audioMasterGetChunkFile,
		audioMasterGetInputSpeakerArrangement;
	};

	public int Callback(AEffect effect, int opcode, int index, Pointer /* int */value, Pointer /* void */ptr, float opt) {
		if (opcode >= OpCodes.values().length) {
			System.err.println("WARN Called with unsupported VST HOST opcode " + opcode);
			return 0;
		}

		switch (OpCodes.values()[opcode]) {
		case audioMasterAutomate:
			return OnSetParameterAutomated(effect, index, opt);
		case audioMasterVersion:
			return OnGetVersion(effect);
		case audioMasterCurrentId:
			return OnGetCurrentUniqueId(effect);
		case audioMasterIdle:
			return OnIdle(effect);
		case audioMasterPinConnected:
			return (value != null) ? OnInputConnected(effect, index) : OnOutputConnected(effect, index);

			/* VST 2.0 additions... */
			// case audioMasterWantMidi :
			// return OnWantEvents(effect, value);
			// case audioMasterGetTime :
			// return (long)OnGetTime(effect, value);
			// case audioMasterProcessEvents :
			// return OnProcessEvents(effect, (VstEvents *)ptr);
			// case audioMasterSetTime :
			// return OnSetTime(effect, value, (VstTimeInfo *)ptr);
			// case audioMasterTempoAt :
			// return OnTempoAt(effect, value);
			// case audioMasterGetNumAutomatableParameters :
			// return OnGetNumAutomatableParameters(effect);
			// case audioMasterGetParameterQuantization :
			// return OnGetParameterQuantization(effect);
			// case audioMasterIOChanged :
			// return OnIoChanged(effect);
			// case audioMasterNeedIdle :
			// return OnNeedIdle(effect);
			// case audioMasterSizeWindow :
			// return OnSizeWindow(effect, index, value);
			// case audioMasterGetSampleRate :
			// return OnUpdateSampleRate(effect);
			// case audioMasterGetBlockSize :
			// return OnUpdateBlockSize(effect);
			// case audioMasterGetInputLatency :
			// return OnGetInputLatency(effect);
			// case audioMasterGetOutputLatency :
			// return OnGetOutputLatency(effect);
			// case audioMasterGetPreviousPlug :
			// return GetPreviousPlugIn(effect);
			// case audioMasterGetNextPlug :
			// return GetNextPlugIn(effect);
			// case audioMasterWillReplaceOrAccumulate :
			// return OnWillProcessReplacing(effect);
			// case audioMasterGetCurrentProcessLevel :
			// return OnGetCurrentProcessLevel(effect);
			// case audioMasterGetAutomationState :
			// return OnGetAutomationState(effect);
			// case audioMasterOfflineStart :
			// return OnOfflineStart(effect,
			// (VstAudioFile *)ptr,
			// value,
			// index);
			// break;
			// case audioMasterOfflineRead :
			// return OnOfflineRead(effect,
			// (VstOfflineTask *)ptr,
			// (VstOfflineOption)value,
			// !!index);
			// break;
			// case audioMasterOfflineWrite :
			// return OnOfflineWrite(effect,
			// (VstOfflineTask *)ptr,
			// (VstOfflineOption)value);
			// break;
			// case audioMasterOfflineGetCurrentPass :
			// return OnOfflineGetCurrentPass(effect);
			// case audioMasterOfflineGetCurrentMetaPass :
			// return OnOfflineGetCurrentMetaPass(effect);
			// case audioMasterSetOutputSampleRate :
			// OnSetOutputSampleRate(effect, opt);
			// return 1;
			// // VST_2.3
			// case audioMasterGetOutputSpeakerArrangement :
			// return 0;
			// case audioMasterGetSpeakerArrangement :
			// // see above comment
			// return 0;
			 case audioMasterGetVendorString :
			 return OnGetVendorString(ptr);
			 case audioMasterGetProductString :
			return OnGetProductString(ptr);
			// case audioMasterGetVendorVersion :
			// return OnGetHostVendorVersion();
			// case audioMasterVendorSpecific :
			// return OnHostVendorSpecific(effect, index, value, ptr, opt);
			// case audioMasterSetIcon :
			// // undefined in VST 2.0 specification
			// break;
			// case audioMasterCanDo :
			// return OnCanDo((const char *)ptr);
			// case audioMasterGetLanguage :
			// return OnGetHostLanguage();
			// case audioMasterOpenWindow :
			// return (long)OnOpenWindow(nEffect, (VstWindow *)ptr);
			// case audioMasterCloseWindow :
			// return OnCloseWindow(nEffect, (VstWindow *)ptr);
			// case audioMasterGetDirectory :
			// return (long)OnGetDirectory(nEffect);
			// case audioMasterUpdateDisplay :
			// return OnUpdateDisplay(nEffect);
			// /* VST 2.1 additions... */
			// case audioMasterBeginEdit :
			// return OnBeginEdit(nEffect);
			// case audioMasterEndEdit :
			// return OnEndEdit(nEffect);
			// case audioMasterOpenFileSelector :
			// return OnOpenFileSelector(nEffect, (VstFileSelect *)ptr);
			// /* VST 2.2 additions... */
			// case audioMasterCloseFileSelector :
			// return OnCloseFileSelector(nEffect, (VstFileSelect *)ptr);
			// case audioMasterEditFile :
			// return OnEditFile(nEffect, (char *)ptr);
			// case audioMasterGetChunkFile :
			// return OnGetChunkFile(nEffect, ptr);
			//
			// case audioMasterGetInputSpeakerArrangement :
			// return (long)OnGetInputSpeakerArrangement(nEffect);

		}

		System.err.println("WARN Called with non-implemented VST HOST opcode " + opcode + "( "
				+ OpCodes.values()[opcode] + " )");
		return 0;
	}

	private int OnGetProductString(Pointer ptr) {
		ptr.setString(0, "JNAVST");
		return VSTConst.VST_TRUE;
	}

	private int OnGetVendorString(Pointer ptr) {
		ptr.setString(0, "Uri Shaked");
		return VSTConst.VST_TRUE;
	}

	private int OnOutputConnected(AEffect effect, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int OnInputConnected(AEffect effect, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int OnIdle(AEffect effect) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int OnGetCurrentUniqueId(AEffect effect) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int OnGetVersion(AEffect effect) {
		return VSTConst.VST_VERSION_2_4;
	}

	private int OnSetParameterAutomated(AEffect effect, int index, float opt) {
		System.out.println("Set automated parameter " + index + " to " + opt);
		return VSTConst.VST_TRUE;
	}
}
