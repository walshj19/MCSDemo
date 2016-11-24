package d16124837.mydit.ie.mcsdemo;

/**
 * Created by James on 23-Nov-16.
 *
 * This class represents all of the information for one image and it's analysis.
 */

class ImageData {
    private String path;

    ImageData(){}

    ImageData(String path){
        this.path = path;
    }

    String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }
}
