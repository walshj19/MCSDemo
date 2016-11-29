package d16124837.mydit.ie.mcsdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageListActivity extends ListActivity {
    final static int CAMERA_REQUEST_CODE = 1;
    FloatingActionButton addButton;
    ArrayList<ImageData> images;
    File lastPhotoFile = null;  //photo file for the last called camera intent
    ArrayAdapter<ImageData> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        setupListeners();

        //get the image data from the database
        images = DAO.get(this);

        //setup the list adapter
        adapter = new ArrayAdapter<>(this,R.layout.row_image_list,R.id.path,images);
        setListAdapter(adapter);
    }

    @Override
    /**
     * When a list item is clicked it opens up an activity for that item.
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //start image activity
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(ImageData.PATH_KEY,images.get(position).getPath());
        startActivity(intent);
    }

    /**
     * setup the event listeners
     */
    void setupListeners(){
        //setup the add image button listener
        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            // Start a photo capture intent and save the image to a file
            @Override
            public void onClick(View v) {
                // Create the file and get the image
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					lastPhotoFile = FileAccessObject.createImageFile(getApplicationContext());
					if (lastPhotoFile == null){
						Toast.makeText(getApplicationContext(),
							"Image file could not be created",
							Toast.LENGTH_SHORT).show();
						return;
					}
					// Continue only if the File was successfully created
					Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
							"d16124837.mydit.ie.mcsdemo.fileprovider",
							lastPhotoFile);
					Log.d("MCSDemo",photoURI.getPath());
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
					startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
				}
			}
		});
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check result code
        if (resultCode != RESULT_OK){
            return;
        }
        // switch based on what activity is being returned from
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                String imagePath = lastPhotoFile.getPath();

                // Add the image to the database
                DAO.insert(getApplicationContext(), new ImageData(imagePath));

                // Refresh the list of images
                adapter.add(new ImageData(imagePath));

                // Start the image activity for the new image
                Intent intent = new Intent(this, ImageActivity.class);
                intent.putExtra(ImageData.PATH_KEY,imagePath);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
