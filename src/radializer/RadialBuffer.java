package radializer;

import globals.Main;
import globals.PAppletSingleton;
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

			// ESTOY RECORRIENDO LA IMAGEN DE ARRIBA HACIA ABAJO,
			// PERO DIBUJANDOLA RADIALMENTE DE ABAJO HACIA ARRIBA (SALE ESPEJADA)
			// ESTA BUENO, PORQ LOS OBJETOS EN LAS IMAGENES GRALMENTE ESTAN CENTRADOS
			// PERO HABRIA Q RECORRER y invertido, SINO
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
	
	public void updateUsingVertex(){
		
		//int xDivs = (int)(((float)p5.mouseX / p5.width) * 10);
		int xDivs = 8;
		
		//System.out.println(xDivs);
		float[] vX = new float[xDivs * 2]; // PAR (arriba, abajo)
		float[] vY = new float[xDivs * 2];
		float[] vTexX = new float[xDivs * 2];
		float[] vTexY = new float [xDivs * 2];
		
		for (int i = 0; i < xDivs * 2; i++) {
			
			if(i % 2 == 0){
				
				vX[i] = (imageBuffer.height * 0.5f) * p5.cos((p5.TWO_PI / xDivs) * (i * 0.5f));
				vY[i] = (imageBuffer.height * 0.5f) * p5.sin((p5.TWO_PI / xDivs) * (i * 0.5f));
				//p5.println("Angle: " + (p5.TWO_PI / xDivs) * (i * 0.5f));
				
				//vTexX[i] = (i / (float)xDivs);
				vTexY[i] = 1f;

			} else {
				vX[i] = 0f;
				vY[i] = 0f;
				
				vTexY[i] = 0f;
			}
			
			vTexX[i] = (i / (float)xDivs);
			//vTexX[i] = p5.mouseX / (float)p5.width;
			
		}
		
		imageBuffer.beginDraw();
		imageBuffer.background(0);
		
		//--
		
		imageBuffer.beginShape(imageBuffer.QUAD_STRIP);
		imageBuffer.translate(imageBuffer.width * 0.5f, imageBuffer.height * 0.5f);

		imageBuffer.texture(inputImage);
		for (int i = 0; i < xDivs * 2; i++) {
			imageBuffer.vertex(vX[i], vY[i], vTexX[i], vTexY[i]);
		}
		
		imageBuffer.vertex(imageBuffer.width * 0.5f, 0, 1, 0);
		imageBuffer.vertex(0,0, 1, 1);

		imageBuffer.endShape();
		
		//-
		
		for (int i = 0; i < xDivs * 2; i++) {
			imageBuffer.text(i, vX[i] + 5, vY[i] - 5);
			imageBuffer.ellipse(vX[i], vY[i], 10, 10);
			
			imageBuffer.stroke(255);
			imageBuffer.line(p5.mouseX, p5.mouseY, vX[1], vY[1]);
			imageBuffer.stroke(255);

		}
		
		
		
		//--
		
		/*
		imageBuffer.beginShape(p5.QUADS);
		imageBuffer.texture(inputImage);
		imageBuffer.vertex(100,100,0,0); // 0
		imageBuffer.vertex(500,100, 1,0); // 1
		imageBuffer.vertex(p5.mouseX, p5.mouseY, 1, 1); // 2 Esquina
		imageBuffer.vertex(100, 400, 0, 1);	// 3	
		imageBuffer.endShape();
		*/
		//imageBuffer.image(inputImage, p5.mouseX, p5.mouseY);
		imageBuffer.endDraw();
	}

	public void render(float atX, float atY) {
		p5.pushMatrix();
		p5.translate(atX, atY);
		//p5.scale((p5.mouseY / (float) p5.height) * 4);
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
		imageBuffer = p5.createGraphics(_inputImage.height * 2, _inputImage.height * 2, p5.P3D);
		imageBuffer.smooth();
		
		imageBuffer.beginDraw();
		imageBuffer.textureMode(p5.NORMAL);
		imageBuffer.endDraw();
	}

	public boolean hasImage() {
		return imageBuffer.width > 0 ? true : false;
	}

	public void saveImage() {
		imageBuffer.save("radiality_####.png");
	}
}
