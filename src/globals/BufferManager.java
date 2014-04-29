package globals;

import java.util.ArrayList;

import processing.core.PImage;
import radializer.RadialBuffer;

public class BufferManager {

	Main p5;
	ArrayList<RadialBuffer> buffers;

	BufferManager() {
		p5 = getP5();

		buffers = new ArrayList<RadialBuffer>();

	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
	
	public void createImageBuffer(PImage inputImage){
		
		RadialBuffer newBuffer = new RadialBuffer();
		newBuffer.loadImage(inputImage);
		
		buffers.add(newBuffer);
	}
	
	public void update(){
		
		/*
		for (int i = 0; i < buffers.size(); i++) {
			
		}
		*/
		
		if (!buffers.isEmpty()) {
			buffers.get(buffers.size() - 1).update();
			//buffers.get(buffers.size() - 1).render(p5.width * 0.5f, p5.height * 0.5f);
		}
		
	}
	
	public void render(){
		
		if (!buffers.isEmpty()) {
			buffers.get(buffers.size() - 1).render(p5.width * 0.5f, p5.height * 0.5f);
		}
	}

	public void updateRadialExtent(float value) {
		if (!buffers.isEmpty()) {
			buffers.get(buffers.size() - 1).updateRadialExtent(value);
		}		
	}

	public void updateRingosity(float[] arrayValue) {
		if (!buffers.isEmpty()) {
			buffers.get(buffers.size() - 1).updateRingosity(arrayValue);
		}
	}
}
