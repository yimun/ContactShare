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

	// ��people�����ķ��Ͷ���
	public SendObj(Context con, People people) {
		this.context = con;
		name = people.getName();
		number = people.getPhone();
		initPos();
	}

	// ���������򵥷��Ͷ���
	public SendObj(Context con, String name, String number) {
		this.context = con;
		this.name = name;
		this.number = number;
		initPos();
	}

	// ��ʼ��������Ϣ
	public void initPos() {
		LocationManager loctionManager;
		String contextService = Context.LOCATION_SERVICE;
		// ͨ��ϵͳ����ȡ��LocationManager����
		loctionManager = (LocationManager) context
				.getSystemService(contextService);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// �߾���
		criteria.setAltitudeRequired(false);// ��Ҫ�󺣰�
		criteria.setBearingRequired(false);// ��Ҫ��λ
		criteria.setCostAllowed(true);// �����л���
		criteria.setPowerRequirement(Criteria.POWER_LOW);// �͹���
		// �ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
		String provider = loctionManager.getBestProvider(criteria, true);
		// ������һ�α仯��λ��
		Location location = loctionManager.getLastKnownLocation(provider);
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

}
