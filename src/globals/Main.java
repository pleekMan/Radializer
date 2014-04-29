package globals;

import java.io.File;

import processing.core.*;
import radializer.*;
import controlP5.*;

public class Main extends PApplet {

	BufferManager bufferManager;
	RadialBuffer radialImage;

	// ---- CONTROL P5

	ControlP5 controllers;
	Slider radialExtent;

	// ---- CONTROL P5 END

	public void setup() {
		size(700, 700, P3D);
		frameRate(30);
		smooth();

		setPAppletSingleton();

		bufferManager = new BufferManager();

		initializeControllers();

		// PImage newImage = loadImage("image_05.jpg");
		// bufferManager.createImageBuffer(newImage);
		// radialImage = new RadialBuffer();
		// radialImage.loadImage(newImage);

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
		background(127);

		/*
		 * if (radialImage.hasImage()) { radialImage.updateUsingVertex();
		 * radialImage.render(width * 0.5f, height * 0.5f); }
		 */

		bufferManager.update();
		bufferManager.render();

		// strokeWeight(1);
		// stroke(127);
		// line(width * 0.5f, 0, width * 0.5f, height);

		// noLoop();

	}

	public void selectImageInput() {

		File newFile = new File("dummyString");
		selectInput("SELECT IMAGE TO RADIALIZE: ", "fileSelector", newFile, this);

		// SelectInput RUNS ON A SEPARATE THREAD. THIS MEANS THAT ALL THE OTHER
		// CODE
		// THAT DEPENDS ON IT , STILLS RUNS IN THE BACKGROUND.
		// THUS GetImagePath RUNS FASTER THAN THE USER CAN SELECT A FILE.
		// SOMEHOW, I HAVE TO RUN SelectInput, AND THEN CHECK IT'S FINISHED TO
		// ASK FOR IMAGE-PATH

	}

	public void fileSelector(File selection) {
		if (selection == null) {
			println("Window was closed or the user hit cancel.");

		} else {
			println("User selected " + selection.getAbsolutePath());

			String inputImagePath = selection.getAbsolutePath();

			PImage newImage = loadImage(inputImagePath);
			bufferManager.createImageBuffer(newImage);
			// delay(1000);

		}
	}

	private void initializeControllers() {

		controllers = new ControlP5(this);

		controllers.addSlider("Radial Extent").setPosition(50, 50).setSize(200, 20).setMin(0).setMax(1).setValue(1);

		controllers.addRange("Ringosity")
		.setPosition(400, 50)
		.setSize(200, 20)
		.setRange(0, 1)
		.setRangeValues(0,1)
		.setValue(1);
	}

	public void controlEvent(ControlEvent theEvent) {

		if (theEvent.isFrom(controllers.getController("Radial Extent"))) {
			//bufferManager.updateRadialExtent(theEvent.getValue());
		}
		if (theEvent.isFrom(controllers.getController("Ringosity"))) {
		    bufferManager.updateRingosity(theEvent.getController().getArrayValue());
		  }
	}

	public void keyPressed() {
		if (key == 's') {
			radialImage.saveImage();
		}

		if (key == 'l') {
			selectImageInput();
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
