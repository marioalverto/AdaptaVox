/** 
 * @author Jorge Guevara Díaz
 */
import java.awt.*;
import java.io.*;
import java.io.IOException;
 
import java.util.*;
 
public class Datos {
    
    byte[] bits ;              
    boolean formato;//formato bigEndian y littleEndian      
    double mayor, menor;
    
    public  Datos(int tamano, boolean formato) {
        bits = new byte[tamano];
        this.formato=formato;
        mayor=0;
        menor=0;                
    }
        
    public void llenarByte(byte[] bits) {
        
        this.bits = bits;                       
    }    
  
    /* ejemplo bits[2]=2 (00000010) fbits[3]=3 (00000011)          
     se aplica bits[2]<<8>
     en total da 10 00000011  que es el numero 515 ,
     este es un short de 16 bits, han entrado dos bytes en uno
     (short[i]=contacenar byte[i]+byte[i+1])          
     los valores negativos estan en complemento a 2         
     */          
    public double[] convertirByteADouble() {

        double[] arrayDouble = new double[bits.length/2];                 
        if (formato==true)
        {        
            int temp = 0x00000000;        
            for (int i = 0, j = 0; j < arrayDouble.length  ;
                 j++, temp = 0x00000000) 
            {            
                temp=(int)bits[i++]<<8;            
                temp |= (int) (0x000000FF & bits[i++]);            
                arrayDouble[j]=(double)temp;        
            }
        
            return arrayDouble;
        }
        
        if(formato==false)
        {  // si el formato es littleEndian                    
            int temp = 0x00000000;        
            for (int i = 0, j = 0; j< arrayDouble.length ;
                 j++, temp = 0x00000000) 
            {                    
                temp=(int)bits[i+1]<<8;            
                temp |= (int) (0x000000FF & bits[(i)]);          
                i=i+2;                  
                arrayDouble[j]=(double)temp;        
        //calcular mayor y menor esto me servira para establecer
        //los parametros en el eje y para la grafica          
                if(mayor<arrayDouble[j])                
                    mayor=arrayDouble[j];               
                               
                if(menor>arrayDouble[j])                                                   
                    menor=arrayDouble[j];                                                  
            }
        
            return arrayDouble;        
        } else {            
          
          System.out.println("orden de Bytes desconocido o no soportado");                        
        }        
        
        return arrayDouble;  

    }        
} 