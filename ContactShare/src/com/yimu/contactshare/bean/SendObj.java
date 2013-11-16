package com.yimu.contactshare.bean;

import java.io.Serializable;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class SendObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double longitude;
	private double latitude;
	private String name;
	private String number;
	private Context context;

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	// 由people创建的发送对象
	public SendObj(Context con, People people) {
		this.context = con;
		name = people.getName();
		number = people.getPhone();
		initPos();
	}

	// 产生本机简单发送对象
	public SendObj(Context con, String name, String number) {
		this.context = con;
		this.name = name;
		this.number = number;
		initPos();
	}

	// 初始化地理信息
	public void initPos() {
		LocationManager loctionManager;
		String contextService = Context.LOCATION_SERVICE;
		// 通过系统服务，取得LocationManager对象
		loctionManager = (LocationManager) context
				.getSystemService(contextService);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
		criteria.setAltitudeRequired(false);// 不要求海拔
		criteria.setBearingRequired(false);// 不要求方位
		criteria.setCostAllowed(true);// 允许有花费
		criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
		// 从可用的位置提供器中，匹配以上标准的最佳提供器
		String provider = loctionManager.getBestProvider(criteria, true);
		// 获得最后一次变化的位置
		Location location = loctionManager.getLastKnownLocation(provider);
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

}
