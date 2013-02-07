package jo.edu.just;

import jo.edu.just.Helpers.*;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coboltforge.slidemenu.SlideMenu;
import com.coboltforge.slidemenu.SlideMenu.SlideMenuItem;
import com.coboltforge.slidemenu.SlideMenuInterface.OnSlideMenuItemClickListener;





public class MainContainer extends Activity implements OnSlideMenuItemClickListener {
    /** Called when the activity is first created. */
	DrawView dv;
	SlideMenu slidemenu;
	boolean ReadyToExit = false;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        
       
		
		dv = new DrawView(this);
        setContentView(R.layout.main);
        //setContentView(dv);
        
       slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
       slidemenu.init(this, R.menu.slide, this, 300);
	   slidemenu.setHeaderImage(getResources().getDrawable(R.drawable.paint_icon));
		
		
        LinearLayout ContainerLayout = (LinearLayout) findViewById(R.id.button1);
        ContainerLayout.addView(dv);
        
        /*
        ImageButton cmdUndo   = (ImageButton)findViewById(R.id.ImageButton01);
        ImageButton cmdRedo   = (ImageButton)findViewById(R.id.ImageButton02);
        ImageButton cmdDelete = (ImageButton)findViewById(R.id.ImageButton03);
        ImageButton cmdUndoSelect = (ImageButton)findViewById(R.id.ImageButton04);
        ImageButton cmdLineWidth = (ImageButton)findViewById(R.id.ImageButton05);
        ImageButton cmdLineColor = (ImageButton)findViewById(R.id.ImageButton06);
        ImageButton cmdObjectAnnotation = (ImageButton)findViewById(R.id.ImageButton07);
        
        cmdUndo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			dv.UndoClick();
			slidemenu.show();

			}
		});
        cmdRedo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			dv.RedoClick();
			}
		});
        cmdDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			dv.DeleteClick();
			}
		});
        cmdUndoSelect.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			dv.UndoSelectClick();
			}
		});
        cmdLineWidth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent("jo.edu.just.WidthSelect");
				//Intent i = new Intent("jo.edu.just.Helpers.ColorDialog");
				Bundle extras = new Bundle();
				extras.putFloat("width", dv.GetStrokeWidth()/3.0f -1);
				i.putExtras(extras);
				startActivityForResult(i,1);}
		
		});
        cmdLineColor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent("jo.edu.just.Helpers.ColorDialog");
				Bundle extras = new Bundle();
				extras.putInt("color", dv.GetSelectColor());
				i.putExtras(extras);
				startActivityForResult(i,2);}
		});
        
        cmdObjectAnnotation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent("jo.edu.just.TextNotation");
				Bundle extras = new Bundle();
				extras.putString("annotation","hhr");
				i.putExtras(extras);
				startActivityForResult(i,3);
			}
		});
        */
        
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		//clicking any button will interrupt the second back click for exit
		
		Log.d("hhr key",String.valueOf(keyCode));
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK : return super.onKeyUp(keyCode, event);
		case KeyEvent.KEYCODE_MENU :
        {
        	if (slidemenu.menuIsShown) 
        		slidemenu.hide();
        	else
        		slidemenu.show();
            //your Action code
        	dv.PrepareForDirtyTouch();
        	ReadyToExit = false;
            return true;
        }
    }
	dv.PrepareForDirtyTouch();
	ReadyToExit = false;
    return super.onKeyUp(keyCode, event);
		
	}
	@Override
	public void onBackPressed(){
		//if (dv.BackPressed()) finish();
		if (slidemenu.menuIsShown) {
			slidemenu.hide();
		}else
			if(dv.BackPressed()) {
				
				
				
				if (ReadyToExit)
					if (dv.IsThereDirtyTouch()){
						Toast.makeText(this, "click Back again to exit", Toast.LENGTH_SHORT).show();
						dv.PrepareForDirtyTouch();
					}
					else
						finish();
				else{
					Toast.makeText(this, "click Back again to exit", Toast.LENGTH_SHORT).show();
					ReadyToExit= true;
					dv.PrepareForDirtyTouch();
				}
			}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	    {
		    if (requestCode == 1) //width window callback
		    	if (resultCode == RESULT_OK) 
		    		dv.SetStrokeWidth(Float.valueOf(data.getData().toString()) );
		    
		    if (requestCode == 2) //width window callback
			    if (resultCode == RESULT_OK) 
			    	dv.SetSelectColor(Integer.valueOf(data.getData().toString()) );
		 
		    if (requestCode == 3) //annotation window callback
			    if (resultCode == RESULT_OK) 
			    	dv.SetObjectAnnotation(data.getData().toString());
		    if (requestCode == 4) //color window callback
			    if (resultCode == RESULT_OK) 
			    	dv.SetBackGroundColor(Integer.valueOf(data.getData().toString()) );
	    }
	@Override
	public void onSlideMenuItemClick(int itemId) {
		slidemenu.hide();
		switch(itemId) {
		case R.id.item_one:
			dv.UndoClick();
			break;
		case R.id.item_two:
			dv.RedoClick();
			break;
		case R.id.item_three:
			dv.DeleteClick();
			break;
		case R.id.item_four:
			dv.UndoSelectClick();
			break;
		case R.id.item_five:
			Intent i = new Intent("jo.edu.just.WidthSelect");
			//Intent i = new Intent("jo.edu.just.Helpers.ColorDialog");
			Bundle extras = new Bundle();
			extras.putFloat("width", dv.GetStrokeWidth()/3.0f -1);
			i.putExtras(extras);
			startActivityForResult(i,1);
			break;
		case R.id.item_six:
			Intent i1 = new Intent("jo.edu.just.Helpers.ColorDialog");
			Bundle extras1 = new Bundle();
			extras1.putInt("color", dv.GetSelectColor());
			i1.putExtras(extras1);
			startActivityForResult(i1,2);
			break;
		case R.id.item_seven:
			Intent i2 = new Intent("jo.edu.just.TextNotation");
			Bundle extras2 = new Bundle();
			extras2.putString("annotation",dv.GetObjectAnnotation());
			i2.putExtras(extras2);
			startActivityForResult(i2,3);
			break;
		case R.id.item_ground:
			Intent i3 = new Intent("jo.edu.just.Helpers.ColorDialog");
			Bundle extras3 = new Bundle();
			extras3.putInt("color", dv.GetBackGroundColor());
			i3.putExtras(extras3);
			startActivityForResult(i3,4);
			break;
		case R.id.item_export_with_stamps:
		case R.id.item_export_without_stamps:
			AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
			TextView myMsg = new TextView(this);
					 myMsg.setMovementMethod(new ScrollingMovementMethod());
					 myMsg.setTextColor(myMsg.getTextColors().getDefaultColor());
					 
			myMsg.setText((new jo.edu.just.XML.writer()).GenerateXML(dv.dynamicBitmap, itemId == R.id.item_export_with_stamps)
					);
			myMsg.setGravity(Gravity.LEFT);
			popupBuilder.setView(myMsg);
			popupBuilder.show();
			break;
		case R.id.item_about:
			AboutDialog abt = new AboutDialog(this);
			abt.setTitle("About ASET");
			abt.show();
			
			
			
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home: // this is the app icon of the actionbar
			slidemenu.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}