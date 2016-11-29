package d16124837.mydit.ie.mcsdemo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * This class represents all of the information for one image and it's analysis.
 * @author James
 * @version 1.0
 * @since 23/11/16
 */

class ImageData {
	final static String PATH_KEY = "IMAGE_PATH";

	private String filename;
	private String path;
	private ArrayList<String> tags;
	private ArrayList<String> description;
	private String caption;
	private ArrayList<String> colors;

	ImageData(){}

	ImageData(String path){
		this.path = path;
		this.filename = path.substring(path.length()-2);
	}

	String getFilename() {
		return filename;
	}

	void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPath(Context context) {
		if (path == null){
			File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			path = storageDir.getPath()+"/"+getFilename();
			return path;
		}else {
			return path;
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	ArrayList<String> getTags() {
		return tags;
	}

	void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	ArrayList<String> getDescription() {
		return description;
	}

	void setDescription(ArrayList<String> description) {
		this.description = description;
	}

	String getCaption() {
		return caption;
	}

	void setCaption(String caption) {
		this.caption = caption;
	}

	ArrayList<String> getColors() {
		return colors;
	}

	void setColors(ArrayList<String> colors) {
		this.colors = colors;
	}
}