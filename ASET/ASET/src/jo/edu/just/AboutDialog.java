package jo.edu.just;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.graphics.Color;
import android.widget.TextView;
public class AboutDialog extends Dialog{
	private static Context mContext = null;
	
	private final  String Data = 
			"<h3>Android Sketching and Editing Tool (ASET)</h3>" +
			"Version 1.0<br>" +
			"People:<br>" +
			"<b>Rami Al-Salman</b><br>" +
			"University of Bremen<br>" +
			"<a href=\"mailto:rami@informatik.uni-bremen.com\">rami@informatik.uni-bremen.com</a><br> <br>" +
    
			"<b><a href=\"http://cosy.informatik.uni-bremen.de/staff/frank-dylla\">Dr.-Ing. Frank Dylla</a></b><br>" +
    		"University of Bremen<br> <br>" +
			
			"<b><a href=\"http://cosy.informatik.uni-bremen.de/staff/falko-schmid\">Dr.-Ing. Falko Schmid</a></b><br>" +
			"University of Bremen<br> <br>" +
    		
    		"<b>Dr.Mohammad Fraiwan</b><br>" +
    		"Jordan University of science and technology<br>" +
    		"<a href=\"mailto:mafraiwan@just.edu.jo\">mafraiwan@just.edu.jo</a><br> <br>" +
    
 			"<b>Hosam Ershedat</b><br>" +
 			"Jordan University of science and technology<br>" +
 			"<a href=\"mailto:powerhhr@gmail.com\">powerhhr@gmail.com</a>" ;
    
	
	
	private final String nfo = "ASET is a collaborative project between University of Bremen and Jordan University of Science and Technology to search the internet via sketching!";
	
	public AboutDialog(Context context) {
		super(context);
		mContext = context;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.about_dialog);
		TextView tv = (TextView)findViewById(R.id.legal_text);
		tv.setText(nfo);
		tv = (TextView)findViewById(R.id.info_text);
		tv.setText(Html.fromHtml(Data));
		
		tv.setTextColor(tv.getTextColors().getDefaultColor());
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		
		stripUnderlinesInHTML(tv);
		//tv.setLinkTextColor(Color.WHITE);
		
		//Linkify.addLinks(tv, Linkify.ALL);
	}
	
	private void stripUnderlinesInHTML(TextView textView) {
	    Spannable s = (Spannable)textView.getText();
	    URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
	    for (URLSpan span: spans) {
	        int start = s.getSpanStart(span);
	        int end = s.getSpanEnd(span);
	        s.removeSpan(span);
	        span = new URLSpanNoUnderline(span.getURL());
	        s.setSpan(span, start, end, 0);
	    }
	    textView.setText(s);
	}
	public class URLSpanNoUnderline extends URLSpan {
	    public URLSpanNoUnderline(String url) {
	        super(url);
	    }
	    @Override 
	    public void updateDrawState(TextPaint ds) {
	        super.updateDrawState(ds);
	        ds.setUnderlineText(false);
	    }
	}
    
}