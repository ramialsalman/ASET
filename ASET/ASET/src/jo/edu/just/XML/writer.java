package jo.edu.just.XML;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import java.util.Properties;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.transform.dom.DOMSource;

import jo.edu.just.Shapes.JoPoint;
import jo.edu.just.Shapes.joBitmap;
import jo.edu.just.Shapes.joPath;



public class writer {

	public String GenerateXML (joBitmap joBmp,boolean UsingTimeStamps){
		String xmlString="" ; 
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder;
         Document document;
    try {
		 documentBuilder = documentBuilderFactory.newDocumentBuilder();
		 document = documentBuilder.newDocument();
        
        

		 Element rootElement = document.createElement("AppData");
		 rootElement.setAttribute("Version", "1.0");
		 rootElement.setAttribute("UsingTimeStamps", String.valueOf(UsingTimeStamps).toUpperCase());
		 rootElement.setAttribute("NumberOfObjects", String.valueOf(joBmp.content.size()));
		 rootElement.setAttribute("BackGroundColor", String.valueOf( joBmp.GetBackColor() ));
		 document.appendChild(rootElement);
   
       
       for(int i=0 ; i < joBmp.content.size();i++){
       
    	   Element articleElement = document.createElement("Object");
    	   articleElement.setAttribute("ID",String.valueOf(i+1)  );
    	   articleElement.setAttribute("Name","NULL");
    	   articleElement.setAttribute("Type","NULL");
    	   
    	   if(UsingTimeStamps){
    	   articleElement.setAttribute("CreationTimeStamp",joBmp.content.get(i).CreateTime.format("%Y-%m-%dT%H-%M-%S"));
    	   articleElement.setAttribute("FinalizationTimeStamp",joBmp.content.get(i).FinalizeTime.format("%Y-%m-%dT%H-%M-%S"));
    	   }
    	   
    	   
    	   float ct[] = ((joPath)joBmp.content.get(i)).GetCenterPoint();
    	   JoPoint CenterPoint = new JoPoint(ct[0], ct[1]);
    	   
    	   for(int j = 0 ; j < ((joPath)joBmp.content.get(i)).PointsCount();j++){
    		   Element ptML = document.createElement("Point");
    		   JoPoint tmpPT = 
    				   ((joPath)joBmp.content.get(i))
    				   .GetTrasformedPoint(CenterPoint,
    						   			 (( joPath)joBmp.content.get(i)).getPoint(j), 
    						   			    joBmp.width, 
    						   			    joBmp.height,
    						   			    true);
    		   
    		   ptML.setAttribute("X",String.valueOf(	tmpPT.x ));
    		   ptML.setAttribute("Y",String.valueOf(	tmpPT.y ));
    		   
    		   articleElement.appendChild(ptML);
    		   
    	   }
    	   rootElement.appendChild(articleElement);
       }
    	   
    	   TransformerFactory factory = TransformerFactory.newInstance();
           Transformer transformer = factory.newTransformer();
           Properties outFormat = new Properties();
           outFormat.setProperty(OutputKeys.INDENT, "yes");
           outFormat.setProperty(OutputKeys.METHOD, "xml");
           outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
           outFormat.setProperty(OutputKeys.VERSION, "1.0");
           outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
           transformer.setOutputProperties(outFormat);
           DOMSource domSource = 
           new DOMSource(document.getDocumentElement());
           OutputStream output = new ByteArrayOutputStream();
           StreamResult result = new StreamResult(output);
           transformer.transform(domSource, result);
           xmlString = output.toString();
           
           
           
           Log.d("savenfo", xmlString);
           
    		} catch (ParserConfigurationException e) 	  {return "";
    		} catch (TransformerConfigurationException e) {return "";
    		} catch (TransformerException e) 			  {return "";
       		} catch (Exception e)						  {return "";
       		}											   return xmlString;
	}
	
	
	
	
	
}
