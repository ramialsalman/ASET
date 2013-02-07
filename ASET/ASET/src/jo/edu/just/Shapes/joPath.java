package jo.edu.just.Shapes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import android.R.bool;
import android.R.color;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Path;
import android.text.format.Time;

public class joPath extends joShape{
	private List<JoPoint>  pts = new ArrayList<JoPoint>();
	
	
	private boolean finalized = false;
	private float[] ct = null;
	
	private Path path = new Path();
	
	public joPath(){
		Time x = new Time();
		x.setToNow();
		CreateTime  = x;
	}
	public joPath(int _LineColor,float Linewidth){
		this();
		LineColor  = _LineColor;
		StrokeWidth = Linewidth;
	}
	
	public JoPoint GetTrasformedPoint(JoPoint CenterPoint,
									  JoPoint pt,
									  float ScreenWidth,
									  float ScreenHeight,
									  Boolean withNormalization){
		//1-translate
		//2-rotate
		//3-scale
		
		pt = pt.clone();
		
		
		pt.x  += translateX;
		pt.y  += translateY;
		CenterPoint.x += translateX;
		CenterPoint.y += translateY;
		
		
		
		pt.x  -= ScreenWidth/2; //convert to Cartesian coordinates 
		pt.y   = ScreenHeight/2 -pt.y;
		
		
		CenterPoint.x -= ScreenWidth/2;
		CenterPoint.y  = ScreenHeight/2 - CenterPoint.y; 
		
		
		//translate points to the center
		pt.x =  pt.x -  CenterPoint.x;
		pt.y =  pt.y -  CenterPoint.y;
		
		
		//rotate around the center
		float xbar =  (float)( pt.x * Math.cos(Angle* (180/Math.PI)) + pt.y * Math.sin(Angle*(180/Math.PI)));
		float ybar =  (float)(-pt.x * Math.sin(Angle* (180/Math.PI)) + pt.y * Math.cos(Angle*(180/Math.PI)));
		
		//translate back
		pt.x = xbar + CenterPoint.x;
		pt.y = ybar + CenterPoint.y;
		
		//scale 
		pt.x *= scale;
		pt.y *= scale;
		
	//	convert to normalized coordinates   -1 .... 0 .... 1
		if (withNormalization){
			pt.x /= ScreenWidth ;
			pt.y /= ScreenHeight;
		}else{//convert again to our mode not Cartesian
			pt.x += ScreenWidth/2;
			pt.y  = - (pt.y - ScreenHeight/2);
		}
		return pt;
		
		
	}
	public int PointsCount(){
		return pts.size();
	}
	
	public JoPoint getPoint (int i){
		return pts.get(i);
	}
	public void setPoint(int i, JoPoint pt){
		pts.get(i).x = pt.x;
		pts.get(i).y = pt.y;
	}

	public void AddPoint(float x ,float y){
		pts.add(new JoPoint(x,y));
	}
	@Override
	public void EndShape(){
		ct = GetCenterPoint();
		//Lines = GetLines();
		finalized=true;
		
		UpdatePath(true);
		super.EndShape();
	}
	
	private void UpdatePath(Boolean withSmoothing ){
		if (!withSmoothing ){
		//without smoothing 
			path.reset();
			for (int i=0 ; i< pts.size();i++){
				if (i==0 ) path.moveTo(pts.get(i).x, pts.get(i).y);
				else       path.lineTo(pts.get(i).x, pts.get(i).y);
			
			}
		//with smoothing 
		}else{
			path.reset();
			if (pts.size() > 1) {
			    JoPoint prevPoint = null;
			    for (int i = 0; i < pts.size(); i++) {
			        JoPoint point = pts.get(i);

			        if (i == 0) {
			            path.moveTo(point.x, point.y);
			        } else {
			            float midX = (prevPoint.x + point.x) / 2;
			            float midY = (prevPoint.y + point.y) / 2;

			            if (i == 1) {
			                path.lineTo(midX, midY);
			            } else {
			                path.quadTo(prevPoint.x, prevPoint.y, midX, midY);
			            }
			        }
			        prevPoint = point;
			    }
			    path.lineTo(prevPoint.x, prevPoint.y);
			}
		}
	
	
	}
	

	
	@Override
	public void Draw(Canvas cav,Paint paint){
		cav.save(Canvas.MATRIX_SAVE_FLAG);
		
		if(!finalized){
			ct = GetCenterPoint();
			UpdatePath(false);	
		}
		cav.translate(translateX, translateY);
		cav.rotate(Angle,ct[0],ct[1]);
		cav.scale(scale, scale,ct[0],ct[1]);
		
		//android.util.Log.d("translate nfo","trans x "+ String.valueOf(translateX));
		//android.util.Log.d("translate nfo","trans y "+ String.valueOf(translateX));
		//android.util.Log.d("translate nfo","angle " + String.valueOf(Angle));
		//android.util.Log.d("translate nfo", "centerpx: "+ String.valueOf(ct[0]));
		//android.util.Log.d("translate nfo", "cenmterpy: " + String.valueOf(ct[1]));
		//android.util.Log.d("translate nfo","scale: " + String.valueOf(scale));
		
		
		int tmpColor = paint.getColor();
		
		float tmpWidth = paint.getStrokeWidth();
		Paint.Style tmpS   = paint.getStyle();
		
		
		
		paint.setStyle(Paint.Style.STROKE);
 		paint.setColor(getColor());
 		paint.setStrokeWidth(StrokeWidth);
 		
 		paint.setStrokeCap(Cap.ROUND);   
		paint.setStrokeJoin(Join.ROUND);   
		paint.setStrokeMiter(2.0f); 
		paint.setStyle(Paint.Style.STROKE);   	
 		
 //-		
 		//cav.drawLines(Lines ,paint );
		cav.drawPath(path, paint);
 //-
 		//------------------------
		paint.setColor(tmpColor);
		paint.setStyle(tmpS);
		paint.setStrokeWidth(tmpWidth);
		cav.restore();
		
		
		
	
	

	}

	@Override
	public  float [] GetMinBoundBox(){
		float [] tmp   =  {0,0,0,0};
		tmp[2] = tmp[0] = pts.get(0).x; //MinX
		tmp[3] = tmp[1] = pts.get(1).y; //MinY
		
		
		
		for (int i=0; i< pts.size() ;i++){
			if (tmp[0] > pts.get(i).x  ) tmp[0] = pts.get(i).x;  //minX
			if (tmp[2] < pts.get(i).x  ) tmp[2] = pts.get(i).x;  //MaxX
			
			if (tmp[1] > pts.get(i).y) tmp[1] = pts.get(i).y;  //minY
			if (tmp[3] < pts.get(i).y) tmp[3] = pts.get(i).y;  //minX

		}
		
		tmp[0] -= StrokeWidth/2.0;
		tmp[1] -= StrokeWidth/2.0;
		tmp[2] += StrokeWidth/2.0;
		tmp[3] += StrokeWidth/2.0;
		return tmp;
		
	}
	@Override
	public  float[] GetCenterPoint(){
		float []  MB = GetMinBoundBox();
		return new float[]{(MB[0]+MB[2])/2  ,(MB[1]+MB[3])/2 };
	}
}
