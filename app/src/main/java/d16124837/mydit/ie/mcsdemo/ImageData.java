package d16124837.mydit.ie.mcsdemo;

import java.util.ArrayList;

/**
 * This class represents all of the information for one image and it's analysis.
 * @author James
 * @version 1.0
 * @since 23/11/16
 */

class ImageData {
	final static String PATH_KEY = "IMAGE_PATH";

	private String path;
	private ArrayList<String> tags;
	private ArrayList<String> description;
	private String caption;
	private ArrayList<String> colors;

	ImageData(){}

	ImageData(String path){
		this.path = path;
	}

	ImageData(String path, ArrayList<String> tags, ArrayList<String> description, String caption, ArrayList<String> colors){
		setPath(path);
		setTags(tags);
		setDescription(description);
		setCaption(caption);
		setColors(colors);
	}

	String getPath() {
		return path;
	}

	void setPath(String path) {
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