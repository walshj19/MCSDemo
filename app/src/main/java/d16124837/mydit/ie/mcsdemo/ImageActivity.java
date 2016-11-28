package d16124837.mydit.ie.mcsdemo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageActivity extends AppCompatActivity {
    ImageView imageView;
    TextView pathView;
    ImageData image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //query the database for the imagedata
        image = DAO.get(this, getIntent().getExtras().getString(ImageData.PATH_KEY));

        //draw all of the views
        populateViews();
    }

    void populateViews(){
        imageView = (ImageView)findViewById(R.id.image);
        imageView.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));

        pathView = (TextView)findViewById(R.id.path);
        pathView.setText(image.getPath());
    }
}
