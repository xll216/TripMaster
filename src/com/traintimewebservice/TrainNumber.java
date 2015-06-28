package com.traintimewebservice;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainNumber implements Parcelable{
	private String id;
	private String CheCiMingCheng;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCheCiMingCheng() {
		return CheCiMingCheng;
	}
	public void setCheCiMingCheng(String cheCiMingCheng) {
		CheCiMingCheng = cheCiMingCheng;
	}
	public TrainNumber(String id, String cheCiMingCheng) {
		super();
		this.id = id;
		CheCiMingCheng = cheCiMingCheng;
	}
	public TrainNumber() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(CheCiMingCheng);
		
	}
	
	private TrainNumber(Parcel source){
		id = source.readString();
		CheCiMingCheng = source.readString();		
	}
	
	public static final Parcelable.Creator<TrainNumber> CREATOR = new Creator<TrainNumber>() {
		
		@Override
		public TrainNumber[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TrainNumber[size];
		}
		
		@Override
		public TrainNumber createFromParcel(Parcel source) {
			return new TrainNumber(source);
		}
	};

	
}
