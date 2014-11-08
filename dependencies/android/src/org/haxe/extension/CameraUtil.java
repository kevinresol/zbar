/*
 * Barebones implementation of displaying camera preview.
 * 
 * Created by lisah0 on 2012-02-24
 */
package org.haxe.extension;

import android.app.Activity;
import android.view.Surface;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

/** A basic Camera preview class */
public class CameraUtil {
	public static int getCorrectDisplayOrientation(Activity activity, Camera.CameraInfo cameraInfo) {
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
         switch (rotation) {
             case Surface.ROTATION_0: degrees = 0; break;
             case Surface.ROTATION_90: degrees = 90; break;
             case Surface.ROTATION_180: degrees = 180; break;
             case Surface.ROTATION_270: degrees = 270; break;
         }

         int result;
         if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
             result = (cameraInfo.orientation + degrees) % 360;
             result = (360 - result) % 360;  // compensate the mirror
         } else {  // back-facing
             result = (cameraInfo.orientation - degrees + 360) % 360;
         }
         return result;
	}
}