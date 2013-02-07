package jo.edu.just.Shapes;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.format.Time;
import android.util.Log;

public class joBitmap {
	
	public static final float DRAG_THRESHOLD     = 20.0f;
	public static final float MIN_WIDTH_SELECT   = 10.0f;
	public static final float MIN_SCALE_SELECT   = 0.75f;
	public static final float WIDTH_SELECT_SHIFT = 30.0f;
	
	Bitmap AllBitmap1; 
	Bitmap AllBitmap2; 
	public Canvas AllCanvas; 
	public Canvas AllCanvas2;
	public Paint  paint;
	joShape AnnotationPtr = null;
	public void SetOptions(float _width,float _height,Bitmap Layer1,Bitmap Layer2 ,Canvas Engine,Canvas Engine2,Paint paint ){
		AllBitmap1  = Layer1;
		AllBitmap2  = Layer2;
		AllCanvas   = Engine;
		AllCanvas2  = Engine2;
		this.paint  = paint; 
		width = _width;
		height = _height;
		
	}
	
	
	
	public List<joShape> content = new ArrayList<joShape>();
	public List<joShape> RedoContent = new ArrayList<joShape>();
	public List<joShape> tempLayer  = new ArrayList<joShape>();
	public float width,height;
	public Time CreateTime;
	public Time EndTime;
	public int BackColor = Color.BLACK;
	public boolean InEditMode=false;
	
	
	private joShape tempLast = null;
	public void AddItem(joShape obj){
		content.add(obj);
		tempLast = obj;
	}
	
	public void switchLasttoRedoStack(){
		if (content.size()>0){
			RedoContent.add(content.get(content.size()-1) );
			content.remove(content.size()-1);
		}
	}
	
	public void switchRedoStackToLast(){
		if (RedoContent.size()>0){
			content.add(RedoContent.get(RedoContent.size()-1) );
			RedoContent.remove(RedoContent.size()-1);
		}
	}
	public joShape LastItem(){
	//for fastest Access
		return tempLast;
	}

	public void DrawLayer1(){
		//AllBitmap1.eraseColor(this.BackColor);
		AllCanvas.drawColor(this.BackColor);
		for (joShape x : this.content) {
			//if (!x.inEditMode)
			x.Draw(AllCanvas, paint);
		}
		
		
		
		if (AnnotationPtr !=null)
			AnnotationPtr.Draw(AllCanvas, paint);
	}
	public void DrawLayer2(){
		AllBitmap2.eraseColor(0x00000000);
		//AllCanvas2.drawColor(0x00000000);
		//AllCanvas2.
		for (joShape x : this.tempLayer) {
			//if (!x.inEditMode)
			x.Draw(AllCanvas2, paint);
		}
	}
	
	public double Distance (float x1,float y1,float x2,float y2){
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
		
	}
	
	public void MoveObjectToEditMode(joShape obj){
		obj.inEditMode = true;
		InEditMode = true;
		
		joRectangle rec = new joRectangle(Color.BLUE);
		float pts [] = obj.GetMinBoundBox();
		rec.x1 = pts[0];
		rec.y1 = pts[1];
		rec.x2 = pts[2];
		rec.y2 = pts[3];
		rec.ParentHandle = obj;
		rec.scale = obj.scale;
		rec.translateX = obj.translateX;
		rec.translateY = obj.translateY;
		rec.Angle 	   = obj.Angle; 
		tempLayer.add(rec);
		AnnotationPtr = new joTextAnnotation((int)width,(int)height,obj);
		
	}
	
	public void ReleaseObjectFromEditMode(joShape obj){
		// null object means to pop the last selected object
		if (tempLayer.size()!=0)
		if (obj == null){
			tempLayer.get(tempLayer.size()-1).ParentHandle.inEditMode = false;
			tempLayer.remove(tempLayer.size()-1);
		}
		else{ 
			obj.inEditMode = false;
			
			for (joShape x : tempLayer){
				if (x.ParentHandle == obj){
					tempLayer.remove(x);
					break;
				}
			}
		}
		if (tempLayer.size()==0){
			InEditMode = false;
			AnnotationPtr = null;
		}else{
			AnnotationPtr = new joTextAnnotation((int)width,
												 (int)height,
												  tempLayer.get(tempLayer.size()-1).ParentHandle);
		}
	}

	public joShape GetObjectOnPos (float x, float y){//TODO: need to find better algorithm	
		float TempWidth =0.0f;
		float TempScale =1.0f;
		for (int i = content.size()-1 ; i >=0; i-- ){
			AllBitmap2.eraseColor(0x00000000);//transparent
			
			//temp width to increase the width for small objects because they are hard to select
			TempWidth = content.get(i).StrokeWidth;
			TempScale = content.get(i).scale;
			if (TempWidth <MIN_WIDTH_SELECT ||
				TempScale <MIN_SCALE_SELECT
					) content.get(i).StrokeWidth = TempWidth + WIDTH_SELECT_SHIFT;
			content.get(i).Draw(AllCanvas2, paint);
			content.get(i).StrokeWidth = TempWidth;
			
			Log.d("try get object", String.valueOf(i));
			if (AllBitmap2.getPixel((int)x , (int)y ) !=0 ){
				return content.get(i);
			}
		}
		
		return null;
	}
	
	public void SetBackColor(int Color){
		BackColor = Color;
	}
	public int GetBackColor(){
		return BackColor;
	}
	
	public void RemoveSelectedObjects(){
		List<joShape> tmp = new ArrayList<joShape>();
		for (joShape x : content) {
			if (x.inEditMode){
				ReleaseObjectFromEditMode(x);
				tmp.add(x);
			}
			
		}
		for(joShape x : tmp){
			content.remove(x);
			RedoContent.add(x);
		}

	}
}
