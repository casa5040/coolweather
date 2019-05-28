package com.coolweather.app.receiver;

import com.coolweather.app.service.AutoUpdateService;
import android.content.*;

import android.content.Context;

public class AutoUpdateReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context,Intent intent) {
		Intent i=new Intent(context,AutoUpdateService.class);
		context.startActivity(i);
	}

}
