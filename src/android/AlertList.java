package org.apache.cordova.alertlist;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import com.idmexico.tutag.R;

public class AlertList extends CordovaPlugin {
	static String TAG = "AlertListPlugin";

	/**
	 * Constructor.
	 */
	public AlertList() {
	}

	/**
	 * Executes the request and returns PluginResult.
	 * 
	 * @param action
	 *            The action to execute.
	 * @param args
	 *            JSONArry of arguments for the plugin.
	 * @param callbackId
	 *            The callback id used when calling back into JavaScript.
	 * @return A PluginResult object with a status and message.
	 */
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		List<String> list = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		String title = "Seleccione un valor:";
		int selected = -1;
		
		JSONArray records = args.optJSONArray(1);
		
		try {
			title = args.getString(0);
			selected = args.getInt(2);
		} catch (Exception e) {
			LOG.d(TAG, "Text parameter not valid, using default");
		}		
		
		for (int i = 0; i < records.length(); i++) {
			JSONObject record = records.getJSONObject(i);
			Object value = record.get("value");
			Object name = record.get("name");
			values.add(value);
			list.add(name.toString());
		}
		
		if (action.equals("alertlist") && list.size() > 0) {
			this.showList(title, list, selected, callbackContext);
		}
		return true;
	}

	public void showList(final String title, final List<String> values, final int selected, final CallbackContext callbackContext) {
		final CordovaInterface cordova = this.cordova;

		Runnable runnable = new Runnable() {

			public void run() {

				CharSequence[] items = values.toArray(new CharSequence[values.size()]);

				AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(cordova.getActivity(), R.style.popup_theme));
				builder.setTitle(title);
				builder.setSingleChoiceItems(items, selected, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						dialog.dismiss();
						LOG.d(TAG, "Alert list value is selected : " + item);
						callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, item));
					}
				});
				AlertDialog alert = builder.create();
				alert.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
				alert.show();
				LOG.d(TAG, "Alert list shown, waiting for select value.");
			}
		};
		this.cordova.getActivity().runOnUiThread(runnable);		
	}
	
}
