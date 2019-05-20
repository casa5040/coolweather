package com.coolweather.app.model;

import android.database.sqlite.*;
import android.database.*;
import com.coolweather.app.db.*;
import android.content.*;
import java.util.*;

public class CoolWeatherDB {
	public static final String DB_NAME="cool_weather";
	public static final int VERSION=1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db=dbHelper.getWritableDatabase();
	}
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB ==null) {
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	public void saveProvince(Province province) {
		if(province!=null) {
			ContentValues values=new ContentValues();
			values.put("provinceName",province.getProvinceName());
			values.put("provinceCode", province.getProvinceCode());
			db.insert("Province",null,values);
		}

	}
	public List<Province> loadProvinces(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null,null,null,null);
		if (cursor.moveToFirst()) {
			do {
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("provinceName")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("provinceCode")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		return list;
		
	}
	public void saveCity(City city) {
		ContentValues values=new ContentValues();
		values.put("id", city.getId());
		values.put("cityName", city.getCityName());
		values.put("cityCode", city.getCityCode());
		values.put("provinceId",city.getProvinceId());
		db.insert("City", null,values);
	}
	public List<City> loadCity() {
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			do {
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("cityCode")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("provinceId")));
				list.add(city);
			}while (cursor.moveToNext());
		}
		return list;
	}
	public void saveCounty(County county) {
		ContentValues values=new ContentValues();
		values.put("id", county.getCityId());
		values.put("countyName",county.getCountyName());
		values.put("countyCode",county.getCountyCode());
		values.put("cityId",county.getCityId());
		db.insert("County", null, values);
	}
	public List<County> loadCounty(){
		List<County> list=new ArrayList<County>();
		Cursor cursor=db.query("County", null,null,null,null,null,null);
		if (cursor.moveToFirst()) {
			do {
				County county=new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("countyName")));
			    county.setCountyCode(cursor.getString(cursor.getColumnIndex("countyCode")));
			    county.setCityId(cursor.getInt(cursor.getColumnIndex("cityId")));		
			}while(cursor.moveToNext());
		}
		return list;
	}
}
