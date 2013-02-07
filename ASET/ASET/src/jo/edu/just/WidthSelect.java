package jo.edu.just;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


public class WidthSelect extends Activity {
	TextView txt;
	SeekBar x;
	@Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widthselect);
        
      
        
        txt = (TextView)findViewById(R.id.textView1);
          x = (SeekBar)findViewById(R.id.seekBar1);
        Bundle extras = getIntent().getExtras();
        
        if (extras != null){
     	   
     	   
     	
     	   x.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				 txt.setText(      String.valueOf(   progress +1           )     );
			}
		});
     	   x.setMax(20);
     	   x.setProgress((int) extras.getFloat("width"));
     	   txt.setText(String.valueOf(   x.getProgress() +1           )           );
        }
    }
	@Override
	public void onBackPressed(){
		Intent data = new Intent();
    	
    	data.setData(Uri.parse(String.valueOf((x.getProgress()+1)*3)));
    
    	setResult(RESULT_OK, data);
		finish();
    	
	}
	

}
