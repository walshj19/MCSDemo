package d16124837.mydit.ie.mcsdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView pathView;
    private Button analyseButton;
    private ImageData image;
    private Bitmap imageBitmap;
    private VisionServiceClient visionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        //get references to the necessary views
        imageView = (ImageView)findViewById(R.id.image);
        pathView = (TextView)findViewById(R.id.path);
        analyseButton = (Button)findViewById(R.id.button_analyse);

        //query the database for the imagedata
        image = DAO.get(this, getIntent().getExtras().getString(ImageData.PATH_KEY));

        //draw all of the views
        populateViews();

        setupListeners();

        //get the vision api client
        if (visionClient == null){
            visionClient = new VisionServiceRestClient(getString(R.string.oxford_subscription_key));
        }
    }

    private void populateViews(){
        imageBitmap = BitmapFactory.decodeFile(image.getPath());
        imageView.setImageBitmap(imageBitmap);

        pathView.setText(image.getPath());
    }

    private void setupListeners(){
        analyseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new callAPI().execute();
            }
        });
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
    }
}
