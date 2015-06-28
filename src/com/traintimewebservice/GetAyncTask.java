package com.traintimewebservice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;

public class GetAyncTask extends AsyncTask<String, Void, Void>{
	private Context context;
	private String id;
	
	public GetAyncTask(Context context, String id) {
		this.context = context;
		this.id = id;
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		String trainNumber = params[0];
		String url ="http://www.twototwo.cn/train/Service.aspx?format=json&action=QueryTrainScheduleByNumber&key=9451b31d-3c91-48db-b489-e9987aa6d505&trainNumber="+trainNumber+"&startDate=2015-06-16&ignoreStartDate=1&like=1";
		

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				String result = EntityUtils.toString(response.getEntity());
				
				File file = context.getFilesDir();
		        String filename = file.getAbsolutePath()+"/data.txt";
		        try {
					FileWriter fileWriter = new FileWriter(filename, true);
					fileWriter.append(result);
					fileWriter.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       System.out.println("序号： "+id+" 车次： "+trainNumber+"  返回的车次结果："+result);
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

}
