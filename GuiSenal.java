import java.awt.*;

/**
*
* @author  Jorge Valverde
* 
*/

public class GuiSenal extends javax.swing.JPanel{

	private double[] datosVoz;
	private int tyEje;
	private int txEje;
	private int alto=0,ancho=0;
	private int intervalo;
	private int origen;
	private int distAlt;
	private boolean graficarOnda;
	private boolean graficarPeriodo;
	private boolean accionGraficar;
	private Image imagenOnda;
	private Graphics pantallaOnda;
	private Color colorEje;
	private Color colorFondo;
	private Color colorSenal;
	private Color colorPitch; 
	private double factorE;
	private double factorEX;
	private double factorEY;

	/** Creates new form GuiSenal */

	public GuiSenal() {
		datosVoz = null;
		tyEje = 0;
		txEje = 0;
		alto = 0;
		ancho = 0;
		origen = 30; //distancia del borde lateral al origen de coordenadas
		distAlt = 20; //distancia del borde superior al maximo pico de una curva
		accionGraficar = false;
		graficarOnda = false;        
		colorEje = Color.RED;
		colorSenal = Color.BLUE;
		colorFondo = Color.GRAY;
		colorPitch = Color.RED;
	}

	public void setDatosVoz(double[] dataVoz) {

		this.datosVoz = dataVoz;     
	}

	public void setAncho(int anch) {

		this.ancho=anch;      
	}

	public void setAlto(int alt) {

		this.alto=alt;   
	}

	public void setIntervalo(int interv) {

		this.intervalo = interv;    
	} 

	//Coloca la distancia del borde del panel al origen de coordenadas
	public void setInitOrigen(int dist) {

			this.origen = dist; 
	}

	public void setFactorE(double fe) {

		this.factorE=fe;
	}

	public void setFactorEX(double fex) {

		this.factorEX=fex;
	}

	public void setFactorEY(double fey) {

		this.factorEY=fey;
	}

	public void setZoomOut() {

		if(this.factorEX>0 && this.factorEY>0) {
			this.factorEX +=0.001;
			this.factorEY +=0.001;
		}
	}

	public void setZoomIn() {

		if(this.factorEX-0.001>0 && this.factorEY-0.001>0) {
			this.factorEX -=0.001;
			this.factorEY -=0.001;
		}
	}

	public void setTY(int t) {

		this.tyEje+=t;
	}

	public void setTX(int t) {

		this.txEje+=t;
	}

	public void setAccionGraficar(boolean a) {

		this.accionGraficar = a;
	}

	public void setGraficarOnda(boolean a) {

		this.graficarOnda = a;
	}

	public void setGraficarPeriodo(boolean a) {

		this.graficarPeriodo = a;
	}

	public void setColorEjes(Color c) {

		this.colorEje = c;
	}

	public void setColorFondo(Color c) {

		this.colorFondo = c;
	}

	public void setColorOnda(Color c) {

		this.colorSenal = c;
	}

	public double[] getVectorVoz() {

		return this.datosVoz;
	}

	public boolean getGraficarOnda() {

		return this.graficarOnda;
	}

	public Color getColorFondo() {

		return this.colorFondo;
	}

	private double[] cambiarCoordenadasDispositivo(double[] cp) {

		double[] p = new double[2];
		double wPV = ancho/2;
		double hPV = alto/2;
		p[0]=Math.round((cp[0]/2) + wPV - txEje);
		p[1]=Math.round(hPV-(cp[1]/2));
	
		return p; 
	}   

	private void graficarSenal(Graphics g) {

		double[] puntoP=new double[2]; /*punto proyectado*/      
		double[] punto2D=new double[2]; /*punto en coordenadas del dispositivo*/   
		int xi=0,xf=0,yi=0,yf=0,i=0;   
		double[] MCoordP = getVectorVoz();
		int total = MCoordP.length;
		int interv=intervalo;

		for(i=txEje; i<total-interv; i=i+interv  ) { 

			puntoP[0]=i*factorEX;
			puntoP[1]=MCoordP[i]*factorEY;
			punto2D=cambiarCoordenadasDispositivo(puntoP);
			xi=(int)(punto2D[0]+origen)-(int)(ancho/2.0);
			yi=(int) punto2D[1]-tyEje+distAlt;  
			puntoP[0]=(i+interv)*factorEX;
			puntoP[1]=MCoordP[i+interv]*factorEY; 
			punto2D=cambiarCoordenadasDispositivo(puntoP);
			xf=(int)(punto2D[0]+origen)-(int)(ancho/2.0);
			yf=(int)punto2D[1]-tyEje+distAlt;  
			g.setColor(colorSenal);
			g.drawLine(xi,yi,xf,yf);
		}
	}

	public void resetear() {

		datosVoz = null;
		accionGraficar = false;
		graficarOnda = false;
		graficarPeriodo = false;
	}

	public void update(Graphics g) {

		paint(g);
	}

	public void paint(Graphics g) {

		imagenOnda = this.createImage(ancho, alto);
		pantallaOnda = imagenOnda.getGraphics();
		pantallaOnda.setColor(colorFondo);

		if(accionGraficar==true) {           
			if(txEje<origen) {
			
				pantallaOnda.fillRect(0, 0, ancho, alto);
				pantallaOnda.setColor(colorEje);
				pantallaOnda.drawLine(origen,0,origen,alto);
				pantallaOnda.drawLine(0,alto/2-tyEje+distAlt,
				ancho,alto/2-tyEje+distAlt);	
			}
		
			if(graficarOnda==true) {
				if( txEje < 0) {
					txEje = 0;
				}

				graficarSenal(pantallaOnda);                                    
			}
			
		} else {

			pantallaOnda.clearRect(0,0,ancho,alto);
			pantallaOnda.fillRect(0,0,ancho,alto);
			pantallaOnda.setColor(colorEje);
			pantallaOnda.drawLine(origen,0,origen,alto);
			pantallaOnda.drawLine(0,alto/2-tyEje+distAlt, ancho,alto/2-tyEje+distAlt);
		}

		g.drawImage(imagenOnda, 0, 0,this);  
	}
}

