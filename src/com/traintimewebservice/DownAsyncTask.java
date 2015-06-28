package com.traintimewebservice;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Mesh;

public class DownAsyncTask extends AsyncTask<Void, Void, String> {
	private ProgressDialog dialog;
	private String path = "http://www.twototwo.cn/train/Service.aspx?format=json&action=QueryAllTrainNumber&key=9451b31d-3c91-48db-b489-e9987aa6d505";
	private Context context;
	private Handler handler;
	
	public DownAsyncTask(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = new ProgressDialog(context);
		dialog.setMessage("数据拉取中...");
		dialog.show();
	}
	
	@Override
	protected String doInBackground(Void... params) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(path);
		try {
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				String result = EntityUtils.toString(response.getEntity());
				return result;
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try {
			JSONObject object = new JSONObject(result);
			if(object.has("Response")){
				JSONObject Response = object.getJSONObject("Response");
				if(Response.has("Main")){
					JSONObject Main = Response.getJSONObject("Main");
					
					ArrayList<TrainNumber> trainNumbers = new ArrayList<TrainNumber>();
					
					JSONArray Item = Main.getJSONArray("Item");
					for(int i=0;i<Item.length();i++){
						JSONObject temp = Item.getJSONObject(i);
						TrainNumber number = new TrainNumber();
						
						if(temp.has("@Id")){
							String id = temp.getString("@Id");
							number.setId(id);
						}
						if(temp.has("CheCiMingCheng")){
							String name = temp.getString("CheCiMingCheng");
							number.setCheCiMingCheng(name);
						}
						trainNumbers.add(number);
					}
					
					if(handler != null){
						Message msg = handler.obtainMessage();
						Bundle data = new Bundle();
						data.putParcelableArrayList("trainNumbers", trainNumbers);
						msg.setData(data);
						msg.what=200;
						handler.sendMessage(msg);
					}
					
				}
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

}
