package org.haxe.extension;


import android.app.Activity;
import android.content.res.AssetManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.util.Log;

import me.dm7.barcodescanner.core.CameraUtils;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import me.dm7.barcodescanner.zbar.ZBarScannerView.ResultHandler;

import org.haxe.lime.HaxeObject;


/* 
	You can use the Android Extension class in order to hook
	into the Android activity lifecycle. This is not required
	for standard Java code, this is designed for when you need
	deeper integration.
	
	You can access additional references from the Extension class,
	depending on your needs:
	
	- Extension.assetManager (android.content.res.AssetManager)
	- Extension.callbackHandler (android.os.Handler)
	- Extension.mainActivity (android.app.Activity)
	- Extension.mainContext (android.content.Context)
	- Extension.mainView (android.view.View)
	
	You can also make references to static or instance methods
	and properties on Java classes. These classes can be included 
	as single files using <java path="to/File.java" /> within your
	project, or use the full Android Library Project format (such
	as this example) in order to include your own AndroidManifest
	data, additional dependencies, etc.
	
	These are also optional, though this example shows a static
	function for performing a single task, like returning a value
	back to Haxe from Java.
*/
public class ZBar extends Extension implements ZBarScannerView.ResultHandler{

	private static HaxeObject eventDispatcher;

	private static Boolean scannerViewAdded = false;
	private static ZBarScannerView scannerView;
    private static AbsoluteLayout cameraLayout;
    private static AbsoluteLayout.LayoutParams cameraLayoutParams;

    private static ZBar instance;

    private static Rect rect;

	public static String EVENT_SUCCESS = "success";
	public static String EVENT_CANCELLED = "cancelled";

	public static void init(HaxeObject eventDispatcher)	{
		ZBar.eventDispatcher = eventDispatcher;
		rect = new Rect();
	}

	public static void addScanner(int x, int y, int width, int height)
	{
		rect.set(x, y, width, height);
		Extension.mainActivity.runOnUiThread(addScannerRunnable);
	}

	public static void removeScanner()
	{
		stopScanning();
		Extension.mainActivity.runOnUiThread(removeScannerRunnable);
	}

	public static void startScanning() {
		
		Extension.mainActivity.runOnUiThread(startScanningRunnable);
	}

	public static void stopScanning() {
		
		Extension.mainActivity.runOnUiThread(stopScanningRunnable);
	}
	
	
	/**
	 * Called when an activity you launched exits, giving you the requestCode 
	 * you started it with, the resultCode it returned, and any additional data 
	 * from it.
	 */
	public boolean onActivityResult (int requestCode, int resultCode, Intent data) {
		
		return true;
		
	}
	
	
	/**
	 * Called when the activity is starting.
	 */
	public void onCreate (Bundle savedInstanceState) {

		instance = this;
	}
	
	
	/**
	 * Perform any final cleanup before an activity is destroyed.
	 */
	public void onDestroy () {
		
		
		
	}
	
	
	/**
	 * Called as part of the activity lifecycle when an activity is going into
	 * the background, but has not (yet) been killed.
	 */
	public void onPause () {
		
		
	}
	
	
	/**
	 * Called after {@link #onStop} when the current activity is being 
	 * re-displayed to the user (the user has navigated back to it).
	 */
	public void onRestart () {
		
		
		
	}
	
	
	/**
	 * Called after {@link #onRestart}, or {@link #onPause}, for your activity 
	 * to start interacting with the user.
	 */
	public void onResume () {
		
	}
	
	
	/**
	 * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when  
	 * the activity had been stopped, but is now again being displayed to the 
	 * user.
	 */
	public void onStart () {
		
		
		
	}
	
	
	/**
	 * Called when the activity is no longer visible to the user, because 
	 * another activity has been resumed and is covering this one. 
	 */
	public void onStop () {
		
		
		
	}

	@Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        if(rawResult != null)
        	dispatchEvent(EVENT_SUCCESS, rawResult.getContents(), rawResult.getBarcodeFormat().getName());
    }

	/** A safe way to get an instance of the Camera object. */
    

    private static void dispatchEvent(String type, String contents, String formatName) {
    	if(eventDispatcher != null)
        	eventDispatcher.call3("dispatch", type, contents, formatName);
    }

    private static Runnable addScannerRunnable = new Runnable(){
    	public void run() {
			if(scannerViewAdded) return;
			if(scannerView == null) {
				scannerView = new ZBarScannerView(Extension.mainContext);
				// start scanning
				scannerView.setResultHandler(instance);
				scannerView.startCamera();

				// get camera preview size
		        Camera.Size size = scannerView.getCamera().getParameters().getPreviewSize();
		        double ratio = (double) size.width / size.height;

		        int orientation = CameraUtils.getDisplayOrientation();
		        if(orientation == 90 || orientation == 270)
		        	ratio = 1 / ratio;

		        if(rect.width == 0 && rect.height == 0) {
		        	rect.width = size.width;
		        	rect.height = size.height;
		        } 
		        else if (rect.width == 0) {
		        	rect.width = (int) (rect.height * ratio);
		        }
		        else if (rect.height == 0) {
		        	rect.height = (int) (rect.width / ratio);
		        }

				cameraLayout = new AbsoluteLayout(Extension.mainContext);
		        cameraLayoutParams = new AbsoluteLayout.LayoutParams(rect.width, rect.height, rect.x, rect.y);
		        cameraLayout.addView(scannerView, cameraLayoutParams);

			}
	        ViewGroup contentView = (ViewGroup) Extension.mainActivity.findViewById(android.R.id.content);
			contentView.addView(cameraLayout);
			scannerViewAdded = true;
		}
    };

    private static Runnable removeScannerRunnable = new Runnable() {
		public void run() {
			if(!scannerViewAdded) return;
			ViewGroup contentView = (ViewGroup) Extension.mainActivity.findViewById(android.R.id.content);
			contentView.removeView(cameraLayout);
			scannerViewAdded = false;
		}
	};

	private static Runnable startScanningRunnable = new Runnable() {
		public void run() {
			if(!scannerViewAdded) return; // only allow start scan if the scannerView is added
			scannerView.setResultHandler(instance);
			scannerView.startCamera();
		}
	};
	

	private static Runnable stopScanningRunnable = new Runnable() {
		public void run() {
			if(scannerView == null) return;
			scannerView.setResultHandler(null);
			scannerView.stopCamera();
		}
	};

	private static class Rect {
		int x, y, width, height;

		public void set(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}
	
	
}