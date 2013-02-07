package jo.edu.just;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


public class TextNotation extends Activity {
	//TextView txt;
	//SeekBar x;
	
	EditText editTxt;
	
	@Override

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.textnotation);
        Bundle extras = getIntent().getExtras();

        editTxt = (EditText)findViewById(R.id.editText1);
        
    	 if (extras != null)  {
    		editTxt.setText(extras.getString("annotation") );
    	 }

     	 Button h= (Button)findViewById(R.id.button2);
        
        h.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
		    	data.setData(Uri.parse(editTxt.getText().toString()) );
		    	setResult(RESULT_OK, data);
				finish();
			}
		});
    }
	@Override
	public void onBackPressed(){
		Intent data = new Intent();
    	data.setData(Uri.parse(editTxt.getText().toString()) );
    	setResult(RESULT_OK, data);
		finish();
    	
	}
	

}
