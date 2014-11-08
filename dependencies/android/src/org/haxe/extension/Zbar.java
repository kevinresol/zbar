package org.haxe.extension;


import android.app.Activity;
import android.content.res.AssetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.util.Log;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;


import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;


import org.haxe.lime.HaxeObject;
import org.haxe.extension.CameraPreview;
import org.haxe.extension.CameraUtil;


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
public class Zbar extends Extension {
	
	
    private static Camera camera;
    private static Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    private static CameraPreview preview;
    private static Handler autoFocusHandler;
    private static ImageScanner scanner;
    private static boolean barcodeScanned = false;
    private static boolean previewing = true;

    private static boolean inited = false;
    private static HaxeObject eventDispatcher;

    private static AbsoluteLayout cameraLayout;
    private static AbsoluteLayout.LayoutParams cameraLayoutParams;
    private static View originalContentView;


	public static String EVENT_SUCCESS = "success";
	public static String EVENT_CANCELLED = "cancelled";

	public static void init(HaxeObject eventDispatcher)	{
		inited = true;
		Zbar.eventDispatcher = eventDispatcher;
	}

	public static void startScanning(final int x, final int y, final int width, final int height) {
		if(inited) {
			Extension.mainActivity.runOnUiThread(new Runnable() {
				public void run() {
					
					Camera c = getCameraInstance();
					if(c == null) return;
					camera = c;
					int displayOrientation = CameraUtil.getCorrectDisplayOrientation(Extension.mainActivity, cameraInfo);
		            camera.setDisplayOrientation(displayOrientation);


			        Camera.Size size = camera.getParameters().getPreviewSize();
			        double ratio = (float)size.width / size.height;
			        int w = width;
			        int h = height;

			        int rotation = Extension.mainActivity.getWindowManager().getDefaultDisplay().getRotation();
			        Log.i("z","zbar rotation " + rotation + " displayOrientation " + displayOrientation);

					if(displayOrientation == 0 || displayOrientation == 180) { // at the camera's natural orientation
						if (width == 0 && height == 0) {
				        	w = size.width;
				        	h = size.height;
			        	}
			        	else if (w == 0) {
			        		w = (int) (h * ratio);
			        	}
			        	else if (h == 0) {
			        		h = (int) (w / ratio);
			        	}

					}
					else { // at the camera's 90degree-rotated orientation
						if (width == 0 && height == 0) { 
				        	h = size.width;
				        	w = size.height;
			        	}
			        	else if (w == 0) {
			        		w = (int) (h / ratio);
			        	}
			        	else if (h == 0) {
			        		h = (int) (w * ratio);
			        	}

					}
			        preview = new CameraPreview(Extension.mainContext, Extension.mainActivity, camera, cameraInfo, previewCallback, autoFocusCallback);

			        // get the current content view
			        ViewGroup contentView = (ViewGroup) Extension.mainActivity.findViewById(android.R.id.content);


			        android.util.Log.i("Z", "zbar " +size.width+ "x" + size.height);
			        android.util.Log.i("Z", "zbar " +ratio + ":" + w + "x" + h);

	    			cameraLayout = new AbsoluteLayout(Extension.mainContext);
			        cameraLayoutParams = new AbsoluteLayout.LayoutParams(w,h,x,y);
			        cameraLayout.addView(preview, cameraLayoutParams);
					contentView.addView(cameraLayout);

				}
			});
/*
			if (barcodeScanned) {
	            barcodeScanned = false;
	            camera.setPreviewCallback(previewCb);
	            camera.startPreview();
	            previewing = true;
	            camera.autoFocus(autoFocusCallback);
		    }*/
		}
	}

	public static void stopScanning() {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
        cameraLayout.removeView(preview);
        preview.destroy();
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

		
        autoFocusHandler = new Handler();

        // Instance barcode scanner 
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);


		//((ViewGroup)Extension.mainView.getParent()).removeView(Extension.mainView);



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

	/** A safe way to get an instance of the Camera object. */
    private static Camera getCameraInstance() {
    	int cameraId = -1;
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
  			Camera.getCameraInfo(i, cameraInfo);
  			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
    			cameraId = i;
    			break;
  			}
		}
		Camera c = null;
        try {
            c = Camera.open(cameraId);
        } catch (Exception e){

        }
        return c;
    }

    private static void dispatchEvent(String type, String contents, String formatName) {
    	if(inited)
        	eventDispatcher.call3("dispatch", type, contents, formatName);
    }

    private static Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    camera.autoFocus(autoFocusCallback);
            }
        };

    private static PreviewCallback previewCallback = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                
                if (result != 0) {
                    previewing = false;
                    camera.setPreviewCallback(null);
                    camera.stopPreview();

                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                    	Log.i("Z", "zbar format " + sym.getType());
                    	dispatchEvent(EVENT_SUCCESS, sym.getData(), getFormatName(sym.getType()));
                        barcodeScanned = true;
                    }
                }
            }
        };

    // Mimic continuous auto-focusing
    private static AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };
	
	private static String getFormatName(int type) {
		String formatName = "";

		switch(type) {
			/** EAN-8. */
			case Symbol.EAN8: formatName = "EAN-8"; break;
			/** UPC-E. */
			case Symbol.UPCE: formatName = "UPC-E"; break;
			/** ISBN-10 (from EAN-13). */
			case Symbol.ISBN10: formatName = "ISBN-10"; break;
			/** UPC-A. */
			case Symbol.UPCA: formatName = "UPC-A"; break;
			/** EAN-13. */
			case Symbol.EAN13: formatName = "EAN-13"; break;
			/** ISBN-13 (from EAN-13). */
			case Symbol.ISBN13: formatName = "ISBN-13"; break;
			/** Interleaved 2 of 5. */
			case Symbol.I25: formatName = "I2/5"; break;
			/** DataBar (RSS-14). */
			case Symbol.DATABAR: formatName = "DataBar"; break;
			/** DataBar Expanded. */
			case Symbol.DATABAR_EXP: formatName = "DataBar-Exp"; break;
			/** Codabar. */
			case Symbol.CODABAR: formatName = "Codabar"; break;
			/** Code 39. */
			case Symbol.CODE39: formatName = "CODE-39"; break;
			/** PDF417. */
			case Symbol.PDF417: formatName = "PDF417"; break;
			/** QR Code. */
			case Symbol.QRCODE: formatName = "QR-Code"; break;
			/** Code 93. */
			case Symbol.CODE93: formatName = "CODE-93"; break;
			/** Code 128. */
			case Symbol.CODE128: formatName = "CODE-128"; break;

			default: formatName = "UNKNOWN";
		}
		return formatName;
	}
	
}