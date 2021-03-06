package d16124837.mydit.ie.mcsdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Tag;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView pathView;
    private TextView tagsView;
    private TextView descriptionView;
    private TextView captionView;
    private TextView colorsView;
    private Button analyseButton;
	private Button deleteButton;
    private ProgressBar progressBar;
    private ImageData image;
    private Bitmap imageBitmap;
    private VisionServiceClient visionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        //get references to the necessary views
        imageView = (ImageView)findViewById(R.id.image);
        pathView = (TextView)findViewById(R.id.filename);
        tagsView = (TextView)findViewById(R.id.tags);
        descriptionView = (TextView)findViewById(R.id.description);
        captionView = (TextView)findViewById(R.id.caption);
        colorsView = (TextView)findViewById(R.id.colors);
        analyseButton = (Button)findViewById(R.id.button_analyse);
        deleteButton = (Button)findViewById(R.id.button_delete);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //query the database for the imagedata
        image = DatabaseAccessObject.get(this, getIntent().getExtras().getString(ImageData.PATH_KEY));

        //draw all of the views
        populateViews();

        setupListeners();

        //get the vision api client
        if (visionClient == null){
            visionClient = new VisionServiceRestClient(getString(R.string.oxford_subscription_key));
        }
    }

    private void populateViews(){
        imageBitmap = BitmapFactory.decodeFile(image.getFilename());
        imageView.setImageBitmap(imageBitmap);
        pathView.setText(image.getFilename());
        String caption = image.getCaption();
        if(caption != null && !caption.equals("")) {
            tagsView.setText(image.getTags().get(0));
            descriptionView.setText(image.getDescription().get(0));
            captionView.setText(image.getCaption());
            colorsView.setText(image.getColors().get(0));
        }
    }

    private void setupListeners(){
        analyseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
	            //check for internet connection
	            ConnectivityManager cm =
			            (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

	            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	            boolean isConnected = activeNetwork != null &&
			            activeNetwork.isConnectedOrConnecting();

	            if(isConnected) {
		            progressBar.bringToFront();
		            progressBar.setVisibility(View.VISIBLE);
		            new callAPI().execute();
	            }else{
		            Toast.makeText(getApplicationContext(), "No network connection",Toast.LENGTH_LONG).show();
	            }
            }
        });
	    deleteButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    DatabaseAccessObject.delete(getApplicationContext(), image);
			    //return to the calling activity
			    returnToCallingActivity();
		    }
	    });
    }

	private void returnToCallingActivity(){
		this.finish();
	}

    private class callAPI extends AsyncTask<String,String,String>{
        callAPI(){}

        @Override
        protected String doInBackground(String... params) {
            Gson gson = new Gson();
            String[] features = {"Categories","Tags","Color","Description"};
            String[] details = {"Celebrities"};

            //create an input stream from the image
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,output);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

            AnalysisResult response = null;
            try {
                response = visionClient.analyzeImage(inputStream, features, details);
            }catch (IOException e){
                Log.e("MCSDemo","IO error when contacting vision API");
            }catch (VisionServiceException e){
                Log.e("MCSDemo","Vision Service error when contacting the vison API");
            }

            String result = gson.toJson(response);
            Log.d("MCSDemo",result);

            return result;
        }

        @Override
        protected void onPostExecute(String data){
            super.onPostExecute(data);

            Gson gson = new Gson();
            AnalysisResult result = gson.fromJson(data, AnalysisResult.class);

            ArrayList<String> tags = new ArrayList<>();
            for (Tag tag: result.tags){
                tags.add(tag.name);
            }
            image.setTags(tags);
            ArrayList<String> descriptions = new ArrayList<>();
            for (String description: result.description.tags){
                descriptions.add(description);
            }
            image.setDescription(descriptions);
            image.setCaption(result.description.captions.get(0).text);
            ArrayList<String> colors = new ArrayList<>();
            for (String color: result.color.dominantColors){
                colors.add(color);
            }
            image.setColors(colors);

            //update row in database
            DatabaseAccessObject.update(getApplicationContext(), image);

            populateViews();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
