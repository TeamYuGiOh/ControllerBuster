package com.example.controllerbuster;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class FindControllersAround implements IAsyncResponse {
	Timer timer = new Timer();
	private double latitude;
	private double longitude;
	private IAsyncResponse context = this;
	private Context activity;

	public FindControllersAround(Context activity, double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.activity = activity;
	}

	public void findControllers() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

			}
		}, (long) 2 * 1000, (long) 2 * 1000);//(long) 2 * 60 * 1000, (long) 2 * 60 * 1000);
		ExecutorService service = Executors.newSingleThreadExecutor();

		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					new HttpRequester(context)
							.execute(
									"http://netmonster.telerik-students.org/mobile/location/index.php?",
									"POST", "latitude=" + latitude
											+ "&longitude=" + longitude);
				}
			};

			Future<?> f = service.submit(r);

			f.get(2, TimeUnit.SECONDS); // attempt the task for two seconds
		} catch (final InterruptedException e) {
			// The thread was interrupted during sleep, wait or join
		} catch (final TimeoutException e) {
			// Took too long!
		} catch (final ExecutionException e) {
			// An exception from within the Runnable task
		} finally {
			service.shutdown();
		}
	}

	@Override
	public void processFinish(String data) {
		try {
			JSONObject obj = new JSONObject(data);
			boolean success = obj.getBoolean("success");
			if (success) {
				NotificationMaker notificationMaker = new NotificationMaker(activity);
				notificationMaker.MakeNotification("Danger!",
						"There are controllers around. Run for your live...");
			}
		} catch (Exception e) {
			// ignore
			//Log.d("please work", e.getMessage());
		}
	}
}
