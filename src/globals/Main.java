package globals;

import processing.core.*;
import radializer.*;

public class Main extends PApplet {

	RadialBuffer radialImage;

	public void setup() {
		size(700, 700, P3D);
		frameRate(120);
		smooth();

		setPAppletSingleton();

		radialImage = new RadialBuffer();
		PImage newImage = loadImage("image_05.jpg");
		radialImage.loadImage(newImage);
		
		textureMode(NORMAL);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { Main.class.getName() });
		// PApplet.main(new String[] { "--present", Main.class.getName() }); //
		// PRESENT MODE
	}

	private void setPAppletSingleton() {
		PAppletSingleton.getInstance().setP5Applet(this);
	}

	public void draw() {
		background(200);

		if (radialImage.hasImage()) {
			radialImage.updateUsingVertex();
			radialImage.render(width * 0.5f, height * 0.5f);
		}

		strokeWeight(1);
		stroke(127);
		line(width * 0.5f, 0, width * 0.5f, height);
		
		//noLoop();

	}

	/*
	 * HACER BIEN EN OTRO MOMENTO, PARA PODER FOCALIZARME EN LAS PARTES
	 * CREATIVAS DE LA APP
	 * 
	 * public void selectImageInput(){
	 * 
	 * 
	 * File newFile = new File("dummyString");
	 * p5.selectInput("SELECT IMAGE TO RADIALIZE: ", "fileSelector",
	 * newFile,this);
	 * 
	 * // SelectInput RUNS ON A SEPARATE THREAD. THIS MEANS THAT ALL THE OTHER
	 * CODE // THAT DEPENDS ON IT , STILLS RUNS IN THE BACKGROUND. // THUS
	 * GetImagePath RUNS FASTER THAN THE USER CAN SELECT A FILE. // SOMEHOW, I
	 * HAVE TO RUN SelectInput, AND THEN CHECK IT'S FINISHED TO ASK FOR
	 * IMAGE-PATH
	 * 
	 * 
	 * }
	 * 
	 * public void fileSelector(File selection) { if (selection == null) {
	 * p5.println("Window was closed or the user hit cancel.");
	 * 
	 * } else { p5.println("User selected " + selection.getAbsolutePath());
	 * 
	 * inputImagePath = selection.getAbsolutePath();
	 * 
	 * // USE THE imageDataManager SINGLETON ACCESS TO CREATE THE imageData
	 * imageDataManager.createImageData(p5.loadImage(inputImagePath));
	 * setImageDataLink(imageDataManager.getLastImageData());
	 * 
	 * 
	 * } }
	 */

	public void keyPressed() {
		if (key == 's') {
			radialImage.saveImage();
		}

		if (key == CODED) {
			if (keyCode == UP) {
			}
		}
	}

	public void mousePressed() {
	}

	public void mouseReleased() {
	}

	public void mouseClicked() {
	}

	public void mouseDragged() {
	}

	public void mouseMoved() {
	}
}
