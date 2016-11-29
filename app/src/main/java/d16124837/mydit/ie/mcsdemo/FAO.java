package d16124837.mydit.ie.mcsdemo;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by James on 21-Nov-16.
 * This class is a wrapper for the device local file storage.
 */

// TODO: check for external storage and only use internal if it's not available
class FAO {
    /**
     * Create an image file which the camera app can store an image in.
     * Taken from the android developers guide.
     */
    static File createImageFile(Context context) throws IOException {
        // Create an image file name using the local date format
        Date now = new Date();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(now);
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }
}
