package d16124837.mydit.ie.mcsdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.IOException;

public class ImageListActivity extends ListActivity {
    FloatingActionButton addButton;
    ImageData[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        setupListeners();

        //populate the list of images

        //get the image data from the database
        images = DAO.get(this);

        setListAdapter(new ArrayAdapter<>(this, R.layout.row_image_list, R.id.path, images));
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
                // Create the File where the photo should go
                File photoFile = null;
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) == null) {
                    return;
                }else{
                    try {
                        photoFile = FAO.createImageFile(getApplicationContext());
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        return;
                    }
                    // Continue only if the File was successfully created
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            "d16124837.mydit.ie.mcsdemo.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 1);
                }
                // Add the image to the database
                DAO.insert(getApplicationContext(), new ImageData(photoFile.getPath()));
            }
        });
    }
}
