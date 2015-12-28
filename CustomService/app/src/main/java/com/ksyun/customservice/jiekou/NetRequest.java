package com.ksyun.customservice.jiekou;

import android.content.Context;

import com.ksyun.customservice.R;
import com.ksyun.customservice.utils.CommonUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class NetRequest {
	private MyInterface.NetRequestIterface netRequestIterface;
	private Context context;

	public NetRequest(MyInterface.NetRequestIterface netRequestIterface, Context context) {
		this.netRequestIterface = netRequestIterface;
		this.context = context;
	}

	public void httpRequest(Map<String, Object> map, final String requestUrl) {
		if (!CommonUtils.getUtilInstance().isConnectingToInternet(context)) {
			CommonUtils.getUtilInstance().showLongToast(context,
					context.getString(R.string.internet_fail_connect));
			return;
		}
		try {
		OkHttpClient mOkHttpClient = new OkHttpClient();
		FormEncodingBuilder builder = new FormEncodingBuilder();
		if (null != map && !map.isEmpty())
			for (String key : map.keySet()) {
				builder.add(key, map.get(key)+"");
			}

		Request request = new Request.Builder()
				.url(requestUrl)
				.post(builder.build())
				.build();

			mOkHttpClient.setConnectTimeout(5000, TimeUnit.MILLISECONDS);

			mOkHttpClient.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(Request request, IOException e) {
					netRequestIterface.exception(e, requestUrl);
				}

				@Override
				public void onResponse(final Response response) throws IOException {
					String result = response.body().string();
					netRequestIterface.changeView(result, requestUrl);
				}
			});
		}catch (Exception e){

		}
	}

}
