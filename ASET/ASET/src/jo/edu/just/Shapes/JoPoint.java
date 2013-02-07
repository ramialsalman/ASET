package jo.edu.just.Shapes;

public class JoPoint {
	public JoPoint(float _x,float _y){
		x = _x;
		y = _y;
	}
	public float x,y;
	
	//public float dx,dy;
	
	public JoPoint clone(){
		return new JoPoint(x, y);
	}
}
