package com.traintimewebservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private Button getDetailInfoByTrainCodeBtn;
	private Button getAllTrainBtn;
	private Handler handler;
	private ArrayList<TrainNumber> trainNumbers;
	private File file;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		getDetailInfoByTrainCodeBtn = (Button) findViewById(R.id.getDetailInfoByTrainCodeBtn);
		getAllTrainBtn = (Button) findViewById(R.id.getAllTrainBtn);

		getDetailInfoByTrainCodeBtn.setOnClickListener(this);
		getAllTrainBtn.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 200) {
					Bundle data = msg.getData();
					trainNumbers = data.getParcelableArrayList("trainNumbers");

					System.out.println("���ؽ���� " + trainNumbers.size());
				}
				return false;
			}
		});

		file = getFilesDir();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getDetailInfoByTrainCodeBtn:
//			dialog = new ProgressDialog(this);
//			dialog.setMessage("��ȡ�����б�....");
//			dialog.show();
			if (trainNumbers != null && trainNumbers.size() > 0) {
				for (int i = 0; i < trainNumbers.size(); i++) {
					GetAyncTask ayncTask = new GetAyncTask(this, trainNumbers.get(i).getId());
					ayncTask.execute(trainNumbers.get(i).getCheCiMingCheng());
				}
			}
//			if (dialog != null && dialog.isShowing())
//				dialog.dismiss();
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// if(trainNumbers != null && trainNumbers.size() > 0){
			// for(int i=0;i<trainNumbers.size();i++){
			// getDetailInfoByTrainCode(trainNumbers.get(i).getCheCiMingCheng());
			// }
			// if(dialog != null && dialog.isShowing())
			// dialog.dismiss();
			// }
			//
			// }
			// }).start();

			break;

		case R.id.getAllTrainBtn:
			DownAsyncTask asyncTask = new DownAsyncTask(this, handler);
			asyncTask.execute();

			break;
		}

	}

	public void getDetailInfoByTrainCode(String trainCode) {
		// �����ռ�
		String nameSpace = "http://WebXml.com.cn/";
		// ���õķ�������
		String methodName = "getDetailInfoByTrainCode";
		// EndPoint
		String endPoint = "http://webservice.webxml.com.cn/WebServices/TrainTimeWebService.asmx";
		// SOAP Action
		String soapAction = "http://WebXml.com.cn/getDetailInfoByTrainCode";

		// ָ��WebService�������ռ�͵��õķ�����
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// ���������WebService�ӿ���Ҫ�������������mobileCode��userId
		rpc.addProperty("TrainCode", trainCode);
		rpc.addProperty("UserID", "");
		// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);

		envelope.bodyOut = rpc;
		// �����Ƿ���õ���dotNet������WebService
		envelope.dotNet = true;
		// �ȼ���envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// ����WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		// ��ȡ���صĽ��
		String result = object.getProperty(0).toString();
		String filename = file.getAbsolutePath() + "/data.txt";
		try {
			FileWriter fileWriter = new FileWriter(filename, true);
			fileWriter.append(result);
			fileWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(result + "----------->>");
	}

}
