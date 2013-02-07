package jo.edu.just;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Templates;

import jo.edu.just.Shapes.JoPoint;
import jo.edu.just.Shapes.joBitmap;
import jo.edu.just.Shapes.joPath;
import jo.edu.just.Shapes.joRectangle;
import jo.edu.just.Shapes.joShape;
import jo.edu.just.multiTouch.MoveGestureDetector;
import jo.edu.just.multiTouch.RotateGestureDetector;


import android.R.color;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

//import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;




public class DrawView extends View implements OnTouchListener 
{
	Context father;
	Vibrator vibe; 
	private static final String TAG = "DrawView";
	Paint paint = new Paint();
	
	//touch listeners 
	static  GestureDetector gestureDetector ;
	static  MoveGestureDetector mMoveDetector;
	static  RotateGestureDetector mRotateDetector;
	static  ScaleGestureDetector mScaleDetector; 
	static Map<joShape, JoPoint> tempRefMap;
	//to switch between them as a 2 buffers 
	Bitmap AllBitmap1; 
	Bitmap AllBitmap2; 
	Canvas AllCanvas;  
	Canvas AllCanvas2; 
	boolean UseTheSecondLayer = false;  
	boolean CreatedObject  = false;    
	
	//--------
	float oldX;  
	float oldY;  
	boolean IsDown=false;
	
	
	joBitmap dynamicBitmap = new joBitmap(); 
	
	boolean IsDirtyTouch = false;
	public void PrepareForDirtyTouch(){
		IsDirtyTouch = false;
	}
	public boolean IsThereDirtyTouch(){
		return IsDirtyTouch;
	}
	public DrawView(Context context) 
	{
		super(context);
		father =context;
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		//see http://www.croczilla.com/bits_and_pieces/svg/samples/linestroke/linestroke.xml
		paint.setStrokeWidth(30.0f);
		//paint.setStrokeCap(Cap.ROUND);   
		//paint.setStrokeJoin(Join.ROUND);   
		//paint.setStrokeMiter(4.0f);        
		
		vibe = (Vibrator) father.getSystemService(Context.VIBRATOR_SERVICE) ;
		int width =  ((WindowManager)(father.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getWidth();
		int height = ((WindowManager)(father.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getHeight();
	
		AllBitmap1  = Bitmap.createBitmap(width,height,Config.ARGB_8888 );
		AllBitmap2 	= Bitmap.createBitmap(width,height,Config.ARGB_8888 );
		AllBitmap2.eraseColor(Color.argb(0,0,0,0)  );
		AllCanvas	= new Canvas(AllBitmap1);
		AllCanvas2	= new Canvas(AllBitmap2);
		
		dynamicBitmap.SetBackColor(Color.WHITE);
		dynamicBitmap.SetOptions(width,height,AllBitmap1,AllBitmap2,AllCanvas,AllCanvas2, paint);
		
		gestureDetector= new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
		  		
			
			public boolean onSingleTapUp(MotionEvent e){
				
				
				//test block only
				if (false)
					if (dynamicBitmap.LastItem()!=null){
						
						for (int i=0 ; i< ((joPath)dynamicBitmap.LastItem()).PointsCount();i++   ){
			//				((joPath)dynamicBitmap.LastItem()).setPoint(i,
			//				((joPath)dynamicBitmap.LastItem()).GetTrasformedPoint(((joPath)dynamicBitmap.LastItem()).getPoint(i)
			//						, dynamicBitmap.width,
			//						  dynamicBitmap.height,
			//						  false)
			//						);
						}
						((joPath)dynamicBitmap.LastItem()).scale =1.0f;
						((joPath)dynamicBitmap.LastItem()).Angle=0.0f;
						((joPath)dynamicBitmap.LastItem()).translateX=0.0f;
						((joPath)dynamicBitmap.LastItem()).translateY=0.0f;
						((joPath)dynamicBitmap.LastItem()).EndShape();
						invalidate();
						
						
						
					}
					
				//------------------
				
				
				
				
				
				
				if(dynamicBitmap.InEditMode){
					joShape x = dynamicBitmap.GetObjectOnPos(e.getX() , e.getY());
					if(x == null) 
						do{
							dynamicBitmap.ReleaseObjectFromEditMode(null);
						}while(dynamicBitmap.InEditMode);
					//dynamicBitmap.DrawLayer1();
					//dynamicBitmap.DrawLayer2();
					invalidate();
					}
				
				return true;
			}
			
			public void onLongPress(MotionEvent e) {
		    	Log.d("hhr","long begin");
		    			    	
		    	vibe.vibrate(100);
		    	if (IsDown){
		    		mMoveDetector.InvokeMoveEnd();
		    	}
		    	IsDown = true;
		    	
		        joShape x = dynamicBitmap.GetObjectOnPos(e.getX() , e.getY());
		       //if (x!=null) ((joPath)x).LineColor = Color.RED;
		         if (x != null){
		           if(x.inEditMode){
		        	   dynamicBitmap.ReleaseObjectFromEditMode(x);
		           }else{
		        	   dynamicBitmap.MoveObjectToEditMode(x);
		           }
		    	   //dynamicBitmap.DrawLayer2();
		    	   //dynamicBitmap.DrawLayer1();		    	   
		       }

		       IsDown = false;
		       invalidate();
		    }
		    
		    
		    
		    
		});	

		mMoveDetector = new MoveGestureDetector(father.getApplicationContext(),new MoveGestureDetector.OnMoveGestureListener() {
			
			@Override
			public void onMoveEnd(MoveGestureDetector detector) {
				if (IsDown && !dynamicBitmap.InEditMode){
					if (dynamicBitmap.content.size() >0)
						dynamicBitmap.LastItem().EndShape();
				
					//AllBitmap1.eraseColor(Color.RED);
					//dynamicBitmap.LastItem().Draw(AllCanvas, paint);
				
					IsDown = false;
				}else if (IsDown && dynamicBitmap.InEditMode){
					for(joShape x : dynamicBitmap.tempLayer ){
						x.ParentHandle.translateX = x.translateX;
						x.ParentHandle.translateY = x.translateY;
					}
					IsDown = false;
					tempRefMap = null;
					//dynamicBitmap.DrawLayer1();
					//dynamicBitmap.DrawLayer2();
					invalidate();
				}
				// TODO Auto-generated method stub
				Log.d("hhr","move end");
			}
			
			@Override
			public boolean onMoveBegin(MoveGestureDetector detector) {
				Log.d("hhr","move begin");
				
				
				
				if (!dynamicBitmap.InEditMode && !IsDown){
					Log.d("hhr serial","1x");
					oldX = detector.getFocusX();
					oldY = detector.getFocusY();
					CreatedObject = false;
					Log.d("hhr serial","2x");
					//dynamicBitmap.AddItem(new joPath(paint.getColor()));
					//UseTheSecondLayer = dynamicBitmap.LastItem().UseTheSecondLayer();
					IsDown = true;
					// TODO Auto-generated method stub
					
					return true;
				}else if (dynamicBitmap.InEditMode && !IsDown){//DragMode For selected Object
					IsDown = true;
					Log.d("hhr serial","3x");
					tempRefMap = new HashMap<joShape, JoPoint>();
					for(joShape x: dynamicBitmap.tempLayer){
						tempRefMap.put(x,new JoPoint( 
								detector.getFocusX() - x.translateX,  
								detector.getFocusY() - x.translateY
								));
					
					}
					Log.d("hhr serial","4x");
					return true;
				}
				else 
					return false;
			}

			@Override
			public boolean onMove(MoveGestureDetector detector) {
				Log.d("hhr","move con");
				if (detector.getNumberOfPoints()>1) return false;
				
				if (IsDown  && !dynamicBitmap.InEditMode   ){
					
					if (!CreatedObject){
						if (dynamicBitmap.Distance(oldX, oldY,detector.getFocusDelta().x, detector.getFocusDelta().y) < joBitmap.DRAG_THRESHOLD ) return true;
						Log.d("hhr","distance");
						CreatedObject = true;
						dynamicBitmap.AddItem(new joPath(paint.getColor(),paint.getStrokeWidth()));
						((joPath)(dynamicBitmap.LastItem())).AddPoint(oldX,oldY);
						
						//
						UseTheSecondLayer = dynamicBitmap.LastItem().UseTheSecondLayer();
					}
					if (dynamicBitmap.LastItem() !=  null){
						//AllCanvas.drawLine(oldX, oldY, detector.getFocusDelta().x,detector.getFocusDelta().y,paint);
						//((joPath)(dynamicBitmap.LastItem())).AddLine(oldX, oldY, detector.getFocusDelta().x, detector.getFocusDelta().y);
						((joPath)(dynamicBitmap.LastItem())).AddPoint(detector.getFocusDelta().x, detector.getFocusDelta().y);
						oldX =  detector.getFocusDelta().x;
						oldY = detector.getFocusDelta().y;
						postInvalidate();
					}
				} else if(IsDown && dynamicBitmap.InEditMode && dynamicBitmap.tempLayer.size()> 0){
					for(joShape x: dynamicBitmap.tempLayer){
						x.translateX = detector.getFocusX() - tempRefMap.get(x).x;
						x.translateY = detector.getFocusY() - tempRefMap.get(x).y;
		
							x.ParentHandle.translateX = x.translateX;
							x.ParentHandle.translateY = x.translateY;
						
					}
					//dynamicBitmap.DrawLayer2();
					postInvalidate();
				}
				
				// TODO Auto-generated method stub
				
				return true;
			}
		} );
		
		mRotateDetector = new RotateGestureDetector(father.getApplicationContext(),new RotateGestureDetector.OnRotateGestureListener() {
			
			@Override
			public void onRotateEnd(RotateGestureDetector detector) {
				// TODO Auto-generated method stub
				if (dynamicBitmap.InEditMode){
					if (dynamicBitmap.tempLayer.size()!=0){
						for(joShape x : dynamicBitmap.tempLayer){
							x.ParentHandle.Angle = x.Angle;
						}
						//dynamicBitmap.DrawLayer1();
						invalidate();
					}
				}
			}
			
			@Override
			public boolean onRotateBegin(RotateGestureDetector detector) {
				Log.d("hhr","rotate begin");
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean onRotate(RotateGestureDetector detector) {
				// TODO Auto-generated method stub
				Log.d("hhr","rotate con" + detector.getRotationDegreesDelta());
				if (dynamicBitmap.InEditMode){
					if (dynamicBitmap.tempLayer.size()!=0){
						for(joShape x: dynamicBitmap.tempLayer){
							x.Angle -= detector.getRotationDegreesDelta();
							x.ParentHandle.Angle = x.Angle;
						}
						
					//((joRectangle)dynamicBitmap.tempLayer.get(dynamicBitmap.tempLayer.size()-1) ).Angle -= detector.getRotationDegreesDelta();
						//dynamicBitmap.DrawLayer2();
						postInvalidate();
					}
					
					
					
				}
				
				return true;
			}
		} );
		
		mScaleDetector = new ScaleGestureDetector(father.getApplicationContext(), new ScaleGestureDetector.OnScaleGestureListener() {
			
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
				// TODO Auto-generated method stub
				if (dynamicBitmap.InEditMode){
					if (dynamicBitmap.tempLayer.size()!=0){
						for(joShape x : dynamicBitmap.tempLayer){
							x.ParentHandle.scale = x.scale;
						}
					//dynamicBitmap.DrawLayer1();
					invalidate();
					}
					
				}
				
			}
			
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				Log.d("hhr","scale con" + detector.getScaleFactor());
				if (dynamicBitmap.InEditMode){
					if (dynamicBitmap.tempLayer.size()!=0){
						for(joShape x: dynamicBitmap.tempLayer){
							x.scale *= detector.getScaleFactor();
							x.ParentHandle.scale = x.scale;
						}
						//dynamicBitmap.DrawLayer2();
						postInvalidate();
					}
					
					
					
				}
				
				
				
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		canvas.save();
		
		
		
		
		
		Canvas tmp1 = dynamicBitmap.AllCanvas,
			   tmp2 = dynamicBitmap.AllCanvas2;
		
		
		dynamicBitmap.AllCanvas = dynamicBitmap.AllCanvas2 = canvas;
		dynamicBitmap.DrawLayer1();
		
		if(dynamicBitmap.InEditMode)dynamicBitmap.DrawLayer2();
		//canvas.drawBitmap(AllBitmap1,0,0, paint);
		//if (dynamicBitmap.InEditMode)
		//canvas.drawBitmap(AllBitmap2,0,0,paint);
		dynamicBitmap.AllCanvas  = tmp1;
		dynamicBitmap.AllCanvas2 = tmp2;
		
		canvas.restore();
	}
	public boolean onTouch(View view, MotionEvent event) 
	{
		IsDirtyTouch = true;
		mMoveDetector.onTouchEvent(event);
		mRotateDetector.onTouchEvent(event);
		mScaleDetector.onTouchEvent(event);
		gestureDetector.onTouchEvent(event);

		//invalidate();
		return true;
	}
	public void UndoClick(){
		dynamicBitmap.switchLasttoRedoStack();
		AllBitmap1.eraseColor(dynamicBitmap.BackColor);
		for (joShape x : dynamicBitmap.content) {
			x.Draw(AllCanvas, paint);
		}
		invalidate();
	
	}
	public void RedoClick(){
		dynamicBitmap.switchRedoStackToLast();
		AllBitmap1.eraseColor(dynamicBitmap.BackColor);
		for (joShape x : dynamicBitmap.content) {
			x.Draw(AllCanvas, paint);
		}
		invalidate();
	}
	public void DeleteClick(){
		dynamicBitmap.RemoveSelectedObjects();
		dynamicBitmap.DrawLayer1();
		dynamicBitmap.DrawLayer2();
		invalidate();
	}
	public void UndoSelectClick(){
		dynamicBitmap.ReleaseObjectFromEditMode(null);
		dynamicBitmap.DrawLayer1();
		dynamicBitmap.DrawLayer2();
		invalidate();
	}
	public float GetStrokeWidth(){
		
		float MaxWidth=0.0f;
		if (dynamicBitmap.InEditMode){
			for(joShape x: dynamicBitmap.tempLayer){
				if (x.ParentHandle.StrokeWidth > MaxWidth )MaxWidth = x.ParentHandle.StrokeWidth;
			}
			return MaxWidth;
		}
		return paint.getStrokeWidth();
	}
	public void SetStrokeWidth(float Value){
		
		if (dynamicBitmap.InEditMode){
			for(joShape x : dynamicBitmap.content){
				if (x.inEditMode){
					x.StrokeWidth = Value;
				}
			}
			dynamicBitmap.DrawLayer1();
			dynamicBitmap.DrawLayer2();
			postInvalidate();
		}else
			paint.setStrokeWidth(Value);
	}
	public int GetSelectColor(){
		
		
		if (dynamicBitmap.InEditMode){
			for(joShape x: dynamicBitmap.tempLayer){
				return x.ParentHandle.LineColor;
			}
		}
		return paint.getColor();
	}
	public void SetBackGroundColor(int _Color){
		dynamicBitmap.SetBackColor(_Color);
		dynamicBitmap.DrawLayer1();
		postInvalidate();
	}
	public int GetBackGroundColor(){
		return dynamicBitmap.GetBackColor();
	}
	public void SetSelectColor(int _Color){
		
		if (dynamicBitmap.InEditMode){
			for(joShape x : dynamicBitmap.content){
				if (x.inEditMode){
					x.LineColor = _Color;
				}
			}
			dynamicBitmap.DrawLayer1();
			dynamicBitmap.DrawLayer2();
			postInvalidate();
		}else
			paint.setColor(_Color);
	}
	public String GetObjectAnnotation(){
		if (dynamicBitmap.InEditMode && dynamicBitmap.tempLayer.size()>0){
			return dynamicBitmap.tempLayer.get(dynamicBitmap.tempLayer.size()-1).ParentHandle.TextTAG;
		}
		return "No object Selected!";
	}
	public void SetObjectAnnotation(String Value){
		
		if (dynamicBitmap.InEditMode){
			for(joShape x : dynamicBitmap.tempLayer){
				x.ParentHandle.TextTAG= Value;	
			}
			dynamicBitmap.DrawLayer1();
			dynamicBitmap.DrawLayer2();
			postInvalidate();
		}
	}
	public Boolean BackPressed(){	//if true it means you can exit 
		if (dynamicBitmap.tempLayer.size()==0) {
			dynamicBitmap.DrawLayer1();
			dynamicBitmap.DrawLayer2();
			invalidate();
			return true; // you can exit
		}
		UndoSelectClick();
		return false;
	}

}
	
	
