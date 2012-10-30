
import java.awt.*;
import java.awt.event.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.*;

public class AdaptaVox extends JFrame implements ActionListener {

	private JButton menuAbrir, zoomIn, zoomOut, fEX, fEY, fE, reproducirSonido;
	private JPanel Grafico, Botones;
	private JFileChooser fc;
	private Reproducir audioR;

	private double[] datosVoz = null; //vector de amplitud de la señal
	private double fex = 0.01; //factor de escalamiento en las abscisas
	private double fey = 0.01; //factor de escalamiento en las coorenadas
	private int FrecuenciaDeSampleo; //frecuencia de muestras de la señal 
	private int finterv = 1; //factor de intervalo de muestreo de la señal
	private String Informe; 
	//private String NameAudio; //nombre del archivo de audio
	private String rutaAudio; //ruta del archivo de audio
	private GuiSenal panelSenal; 

	private AdaptaVox(){
		this.setSize(500,400);
		this.setTitle("AdaptaVox");
		
		menuAbrir = new JButton("Cargar Sonido");
		menuAbrir.addActionListener(this);

		zoomIn = new JButton("-");
		zoomIn.addActionListener(this);

		zoomOut = new JButton("+");
		zoomOut.addActionListener(this);

		reproducirSonido = new JButton("> Play");
		reproducirSonido.addActionListener(this);

		panelSenal = new GuiSenal();
		panelSenal.setAncho(500);
		panelSenal.setAlto(300);

		fc = new JFileChooser();
		
		Botones = new JPanel();
		Botones.add(menuAbrir);
		Botones.add(zoomIn);
		Botones.add(zoomOut);
		Botones.add(reproducirSonido);
		Botones.setBorder(BorderFactory.createLineBorder(Color.RED));
		

		//Grafico = new JPanel();
		//Grafico.add(panelSenal);
		//Grafico.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		//Grafico.setSize(400,300);

		this.getContentPane().add(panelSenal, BorderLayout.CENTER);
		this.add("South", Botones);
		this.setVisible(true);

	}



	private void menuAbrirActionPerformed(java.awt.event.ActionEvent evt) {

		if (evt.getSource() == menuAbrir) {            

			int valorRetorno = fc.showOpenDialog(null);
			
			if (valorRetorno == JFileChooser.APPROVE_OPTION) {                
				File archivoEntrada = fc.getSelectedFile();         
				String rutaAudio = archivoEntrada.getPath();
				System.out.println("Ruta del audio = "+rutaAudio);
				int totalFramesLeidos = 0;
				int totalBytesLeidos = 0;
		        
		        //1. flujoEntradaAudio 
				//El objeto flujoEntradaAudio trata el archivo de entrada como un
				//archivo de audio y es util para establecer y leer propiedades
				//de los archivos de audio

				//2. Los archivos de audio se guardan con unidades minimas llamadas
				//frames, el valor de cada frame determina la amplitud del sonido,
				//un frame puede tener 1 byte 2 bytes 4 bytes, etc

				//3. Se utilizara un buffer especificado por el arreglo audioBytes
				//para leer los datos del archivo de audio a memoria	
		
				try {

					AudioInputStream flujoEntradaAudio = AudioSystem.getAudioInputStream(archivoEntrada);
					int bytesPorFrame = flujoEntradaAudio.getFormat().getFrameSize();                        
					int numBytes = 1024 * bytesPorFrame; 
					byte[] audioBytes = new byte[numBytes];
	
					// longitud de archivo de audio en bytes        
					int longitudArchivoBytes = (int)flujoEntradaAudio.getFormat().getFrameSize()*(int)flujoEntradaAudio.getFrameLength();
	
					// objeto datos mas informacion ver clase Datos.java
					Datos datos = new Datos(longitudArchivoBytes,
					flujoEntradaAudio.getFormat().isBigEndian());

					//arreglo temporal de bytes 
					byte[] datosTemporal=new byte[longitudArchivoBytes];     
					int pos=0;         

					//procedimiento que lee los bytes del archivo de audio a memoria                    
					try {
						int numeroBytesLeidos = 0;
						int numeroFramesLeidos = 0;                    
						while((numeroBytesLeidos=flujoEntradaAudio.read(audioBytes))!=-1) {                      
							numeroFramesLeidos = numeroBytesLeidos/bytesPorFrame;
							totalFramesLeidos += numeroFramesLeidos;                  
							System.arraycopy(audioBytes, 0, datosTemporal, pos, numeroBytesLeidos);
							pos=pos+numeroBytesLeidos;                      
						}

						datos.llenarByte(datosTemporal);

						//datosVoz : contiene los valores de amplitud del archivo de audio es decir 
						//nuestros algoritmos de procesamiento digital
						//de voz lo haremos en este arreglo                         

						datosVoz = new double[longitudArchivoBytes/bytesPorFrame];
						datosVoz = datos.convertirByteADouble();

						//preparamos para graficar la señal de audio en el panel

						graficarSenal();

						//obtenemos informacion del archivo de las propiedades
						//del archivo de audio
						Informe = "";                      
						Informe+="Abriendo:   "+archivoEntrada.getName()+".\n";
						Informe+="Ruta: "+rutaAudio+"\n\n";       
						Informe+="   NroBytes    Frames    BigEndian \n";
						Informe+="   "+longitudArchivoBytes+"    "+
						flujoEntradaAudio.getFrameLength()+"    "+
						flujoEntradaAudio.getFormat().isBigEndian()+"    \n";
						Informe+="\n Formato: "+flujoEntradaAudio.getFormat().toString();
						Informe+="\n\n Tamaño del arreglo: "+datosVoz.length+"\n"; 

						//hallamos la frecuencia de sampleo
						FrecuenciaDeSampleo=(int)flujoEntradaAudio.getFormat().getSampleRate();
						Informe+=" Frecuencia de sampleo: "+FrecuenciaDeSampleo+"m/s\n";
						audioR = new Reproducir(rutaAudio);                        
						audioR.playAudio();                                                            
					} catch (Exception ex) { 
						
						Informe = "No se pudo leer \n";                              
					}                        
				} catch (Exception e) {          
					Informe = "Tipo incompatible\n";    
				}                                                                        
			} else {                

				Informe = "Acción de abrir cancelada por el usuario\n";
			}                                                 
		}                     
	}
	
	private void graficarSenal() {
	//graficamos en el panel principal
		panelSenal.setDatosVoz(datosVoz);
		panelSenal.setAccionGraficar(true);
		//panelSenal.setGraficarOnda(!panelSenal.getGraficarOnda());
		panelSenal.setGraficarOnda(true);
		panelSenal.setIntervalo((finterv*datosVoz.length)/panelSenal.getWidth());
		panelSenal.setFactorEX(fex);
		panelSenal.setFactorEY(fey);
		panelSenal.setTY(0);
		panelSenal.repaint();  
	
	}

	private void zoomIO(java.awt.event.ActionEvent evt) {
		if (evt.getSource() == zoomIn) {
			panelSenal.setZoomIn();
		}else if (evt.getSource() == zoomOut) {
			panelSenal.setZoomOut();
		}

		panelSenal.repaint();
	}

	private void reproducirSonido(java.awt.event.ActionEvent evt) {
		if (evt.getSource() == reproducirSonido)
			audioR.playAudio();
	}

	public void actionPerformed(ActionEvent e) {
		this.menuAbrirActionPerformed(e);
		this.zoomIO(e);
		this.reproducirSonido(e);
	}

	public static void main(String[] args) {
		
		AdaptaVox av = new AdaptaVox();
		av.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

}
