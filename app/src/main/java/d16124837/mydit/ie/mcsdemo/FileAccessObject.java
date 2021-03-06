package d16124837.mydit.ie.mcsdemo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
  * This class is a wrapper for the device local file storage.
  * @author James
  * @version 1.0
  * @since 21/11/16
  */

		// TODO: check for external storage and only use internal if it's not available
		class FileAccessObject {
		static final String dateFormat = "yyyy-MM-dd-HH-mm-ss-SSS";
		/**
	 	* Create an image file which the camera app can store an image in.
	 	* @param context The calling context
	 	* @return File object where an image can be stored or null if the file could not be created
	 	*/
				static File createImageFile(Context context){
				// Create an image file name using the local date format
						Date now = new Date();
				String timeStamp = new SimpleDateFormat(dateFormat).format(now);
				String imageFileName = timeStamp + ".jpg";
				File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
				File imageFile = new File(storageDir, imageFileName);
				Log.d("MCSDemo", "file created: "+imageFile.getPath());
				return imageFile;
			}
	}