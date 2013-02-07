package jo.edu.just.Shapes;

import android.R.style;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Xfermode;
import android.graphics.PathDashPathEffect.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.format.Time;

public class joRectangle extends joShape {

	
	//public float Angle=0.0f;
	//public float scale = 1.0f;
	
	
	public float x1;
	public float y1;
	public float x2;
	public float y2;
	
	static DashPathEffect effectDash = new DashPathEffect(new float[] {10, 5, 5, 5},3);
	
	
	public joRectangle(){
		Time x = new Time();
		x.setToNow();
		CreateTime  = x;
	}
	public joRectangle(int _LineColor){
		this();
		LineColor  = _LineColor;
	}
	
	
	
	
	@Override
	public void Draw(Canvas cav, Paint paint) {
		
		
		
		cav.save(Canvas.MATRIX_SAVE_FLAG);
		
		float [] ct = GetCenterPoint();
		
		cav.translate(translateX, translateY);
		cav.rotate(Angle,ct[0],ct[1]);
		cav.scale(scale, scale,ct[0],ct[1]);
		
		int tmpColor = paint.getColor();
		float tmpWidth = paint.getStrokeWidth();
		Paint.Style tmpS   = paint.getStyle();
		paint.setStyle(Paint.Style.STROKE);
 		paint.setColor(getColor());
 		paint.setStrokeWidth(3f);
 		
 		paint.setPathEffect(effectDash);
 		
 		//MaskFilter tempMask = paint.getMaskFilter();
 		//paint.setMaskFilter(new BlurMaskFilter(15, Blur.SOLID));
 		
		cav.drawRect(x1, y1, x2,y2, paint);
		paint.setColor(tmpColor);
		paint.setStyle(tmpS);
		paint.setStrokeWidth(tmpWidth);
		paint.setPathEffect(null);
		cav.restore();
	}
	
	@Override
	public float[] GetCenterPoint(){
		return new float[]{(x1+x2)/2  ,(y1+y2)/2 };
	}
	
	public boolean UseTheSecondLayer(){
		return true;
	}
	
	@Override
	public  float [] GetMinBoundBox(){
		return null;
		
	}
	
}
