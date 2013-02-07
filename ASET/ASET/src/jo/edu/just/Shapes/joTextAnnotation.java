package jo.edu.just.Shapes;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.text.format.Time;

public class joTextAnnotation extends joShape{
	private List<Float>  pts = new ArrayList<Float>();
	
	//screen width and height 
	private int width=0;
	private int height =0;
	
	public joTextAnnotation(){
		Time x = new Time();
		x.setToNow();
		CreateTime  = x;
	}
	
	private String GetText(){
		if (ParentHandle.TextTAG.compareTo("") ==0)
			return "No TAG yet!";
		else
			return "TAG: " + ParentHandle.TextTAG;
	}
	
	public joTextAnnotation(int _width,int _height,joShape Parent){
		this();
		width = _width;
		height = _height;
		ParentHandle = Parent;
	}
	@Override
	public void Draw(Canvas cav,Paint paint){
		cav.save(Canvas.MATRIX_SAVE_FLAG);
		
		cav.translate(translateX, translateY);
		cav.rotate(Angle,0,0);
		cav.scale(scale, scale,0,0);
		int tmpColor = paint.getColor();
		
		float tmpWidth = paint.getStrokeWidth();
		Paint.Style tmpS   = paint.getStyle();
		Shader tempShader  = paint.getShader();
		
		
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
 		paint.setColor(Color.argb(70,255, 86,4));
 		paint.setStrokeWidth(1.0f);	
 		paint.setTextSize(36.0f);
 		
 //		paint.setShader(new LinearGradient(0f, 0f, width -width/16, 0f,0xffffff64,0x00ffff64, Shader.TileMode.CLAMP));
 //		paint.setShader(new SweepGradient(0, -100, new int[]{0xffffff64,0x00ffff64,0x00ffff64,0x00ffff64,0x00ffff64},null));
 //-		
 		cav.drawRect(0,height/48, width, height/9, paint);
 		paint.setColor(Color.WHITE);
 		paint.setShader(tempShader);
 		
 		//MaskFilter tempMask = paint.getMaskFilter();
 		//paint.setMaskFilter(new BlurMaskFilter(1, Blur.NORMAL));
 		cav.drawText(GetText(), width/16, height/12, paint);
 //-
		paint.setColor(tmpColor);
		paint.setStyle(tmpS);
		paint.setStrokeWidth(tmpWidth);
		//paint.setMaskFilter(tempMask);
		cav.restore();
	}

	@Override
	public  float [] GetMinBoundBox(){
		//no need for implementation
		float [] tmp   =  {0,0,0,0};
		return tmp;
		
	}
	@Override
	public  float[] GetCenterPoint(){
		//no need for implementation
		return new float[]{0 ,0};
	}
}
