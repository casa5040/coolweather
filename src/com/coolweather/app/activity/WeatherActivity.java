package com.coolweather.app.activity;

import com.coolweather.app.R;
import android.os.*;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View.*;
import android.view.View;
import android.view.Window;
import android.app.*;
import android.widget.*;
import com.coolweather.app.util.*;
import android.content.*;
import android.util.*;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView temp1;
	private TextView temp2;
	private TextView weatherDespText;
	private TextView currentDateText;
	private Button switchCity;
	private Button refreshWeather;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText=(TextView)findViewById(R.id.city_name);
		publishText=(TextView)findViewById(R.id.publish_text);
		temp1=(TextView)findViewById(R.id.temp1);
		temp2=(TextView)findViewById(R.id.temp2);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
		currentDateText=(TextView)findViewById(R.id.current_date);
		String countyCode=getIntent().getStringExtra("county_code");
		switchCity=(Button)findViewById(R.id.switch_city);
		refreshWeather=(Button)findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		if(!TextUtils.isEmpty(countyCode)) {
			publishText.setText("同步中。。。");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else {
			showWeather();
		}
		
	}
	@Override
    public void onClick(View v) {
			switch(v.getId()) {
			case R.id.switch_city:
				Intent intent=new Intent(this,ChooseAreaActivity.class);
				intent.putExtra("from_weather_activity", true);
				startActivity(intent);
				finish();
				break;
			case R.id.refresh_weather:
				publishText.setText("同步中...");
				SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
				String weatherCode=prefs.getString("weather_code", "");
				if(!TextUtils.isEmpty(weatherCode)) {
					queryWeatherInfo(weatherCode);
				}
				break;
			default:
				break;
				
			}
	}
	
	private void queryWeatherCode(String countyCode) {
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	private void queryWeatherInfo(String weatherCode) {
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
	}
	private void queryFromServer(final String address,final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)) {
						String[] array=response.split("\\|");
						if(array!=null && array.length==2) {
							String weatherCode=array[1];
							Log.d("ddd",array[1]);
							queryWeatherInfo(weatherCode);
							
						}
						
					}
				}else if ("weatherCode".equals(type)) {
					Log.d("ddd",response.toString());
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	private void showWeather() {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1.setText(prefs.getString("temp1", ""));
		temp2.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天"+prefs.getString("publish_time", "")+"发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}

}
