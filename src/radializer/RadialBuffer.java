package radializer;

import main.Main;
import main.PAppletSingleton;
import processing.core.PImage;
import processing.core.PGraphics;

public class RadialBuffer {

	Main p5;
	PImage inputImage;
	PGraphics imageBuffer;

	int columnCounter;
	float rePxSeparation;
	float scaling;

	float angleTop, angleBottom;
	float radialSeparation;

	public RadialBuffer() {

		p5 = getP5();
		columnCounter = 0;
		rePxSeparation = 1;
		scaling = 1;
		// scaling = height / (float)inputImage.height;

		// inputImage = _inputImage;
		// imageBuffer = createGraphics(inputImage.height * 2, inputImage.height
		// * 2);
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public void update() {

		// ONLY IF WE HAVEN'T RUN OUT OF COLUMNS
		if (columnCounter < inputImage.width) {

			imageBuffer.beginDraw();

			imageBuffer.noStroke();

			imageBuffer.pushMatrix();
			imageBuffer.translate(imageBuffer.width * 0.5f, imageBuffer.height * 0.5f);
			// scale(scaling );

			// CALCULATE ANGLE (SAME COLUMN) TO DRAW POINTS IN THE QUAD
			angleTop = ((p5.TWO_PI / inputImage.width) * (columnCounter + 1));
			angleBottom = ((p5.TWO_PI / inputImage.width) * (columnCounter - 1)); // DEBERIA
																				// SER
																				// "* (columnCounter));",
																				// pero
																				// de
																				// alguna
																				// manera
																				// asi
																				// se
																				// diluye
																				// el
																				// moire
			radialSeparation = 0;

			for (int y = 0; y < inputImage.height; y++) {

				float yPosTop1 = radialSeparation * p5.sin(angleTop);
				float xPosTop1 = radialSeparation * p5.cos(angleTop);
				float yPosTop2 = radialSeparation * p5.sin(angleBottom);
				float xPosTop2 = radialSeparation * p5.cos(angleBottom);
				float yPosBottom1 = (radialSeparation - rePxSeparation) * p5.sin(angleBottom);
				float xPosBottom1 = (radialSeparation - rePxSeparation) * p5.cos(angleBottom);
				float yPosBottom2 = (radialSeparation - rePxSeparation) * p5.sin(angleTop);
				float xPosBottom2 = (radialSeparation - rePxSeparation) * p5.cos(angleTop);

				imageBuffer.fill(inputImage.get(columnCounter, y));
				// imageBuffer.stroke(inputImage.get(columnCounter, y));

				imageBuffer.quad(xPosTop1, yPosTop1, xPosTop2, yPosTop2, xPosBottom1, yPosBottom1, xPosBottom2, yPosBottom2);
				// imageBuffer.ellipse(xPosTop1, yPosTop1, 4, 4);
				// imageBuffer.point(yPos,xPos);
				radialSeparation += rePxSeparation;
			}

			imageBuffer.popMatrix();

			columnCounter += 1;

			drawBufferGizmo();

			imageBuffer.endDraw();
		}
	}

	public void render(float atX, float atY) {
		p5.pushMatrix();
		p5.translate(atX, atY);
		p5.scale((p5.mouseX / (float) p5.width) * 4);
		p5.imageMode(p5.CENTER);
		p5.image(imageBuffer, 0, 0);
		p5.popMatrix();
	}

	void drawBufferGizmo() {
		// MUST BE INSIDE imageBuffer BEGIN/END
		imageBuffer.stroke(0);
		imageBuffer.strokeWeight(2);
		imageBuffer.noFill();
		imageBuffer.rect(0, 0, imageBuffer.width, imageBuffer.height);
	}

	public void loadImage(PImage _inputImage) {
		inputImage = _inputImage;
		createImageBuffer(_inputImage);
	}

	private void createImageBuffer(PImage _inputImage) {
		imageBuffer = p5.createGraphics(_inputImage.height * 2, _inputImage.height * 2);
		imageBuffer.smooth();
	}

	public boolean hasImage() {
		return imageBuffer.width > 0 ? true : false;
	}

	public void saveImage() {
		imageBuffer.save("radiality_####.png");
	}
}
