package com.yimu.contactshare.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class People implements Parcelable {

	/**
	 * 
	 */

	/** 索引字母 */
	public String index = null; // 格式为“z张”
	/** 名字 */
	public String name = null;
	/** 号码 （不止一个） */
	public String phone = null;
	/** 图片 */
	public Bitmap bitmap = null;

	@Override
	public String toString() {
		return index + name + phone;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(index);
		dest.writeString(name);
		dest.writeString(phone);
		bitmap.writeToParcel(dest, 0);
	}

	public static final Parcelable.Creator<People> CREATOR = new Creator<People>() {

		public People createFromParcel(Parcel source) {
			People pp = new People();
			pp.index = source.readString();
			pp.name = source.readString();
			pp.phone = source.readString();
			pp.bitmap = Bitmap.CREATOR.createFromParcel(source);

			return pp;

		}

		public People[] newArray(int size) {
			return new People[size];
		}

	};
}
