
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.DefaultCaret;


public class Cliente implements Runnable{
	public static final int LIST_NICK=100;
	public static final int ENV_MENSAJE=200;
	public static final int RECI_ARCHIVO=300;
    //Declaramos las variables necesarias para la conexion y comunicacion
    private Socket cliente;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectInputStream ois;
    
    
    private String ruta="C:\\Users\\troya\\Desktop";
    
    
    private BufferedInputStream entrada_bytes_fichero; // Para la lectura del fichero
	private BufferedOutputStream salida_bytes_fichero; 
    //El puerto debe ser el mismo en el que escucha el servidor
    private int puerto = 2027;
    //Si estamos en nuestra misma maquina usamos localhost si no la ip de la maquina servidor
    private String host = "localhost";
    private String mensajes = "";
    
    String user="";
    
    private ArrayList<String> usuarios=new ArrayList<String>();
    
    JEditorPane panel;
    JList list;
   
    
    //Constructor recibe como parametro el panel donde se mostraran los mensajes
    public Cliente(JEditorPane panel,JList list,String nick,FrameCliente frame){
        this.panel = panel;
        this.list=list;
        this.ruta=frame.getTxtRuta().getText();
       
      System.out.println(this.ruta);
        try {
            cliente = new Socket(host,puerto);
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());
            ois = new ObjectInputStream(cliente.getInputStream());
            
    		salida_bytes_fichero=new BufferedOutputStream(cliente.getOutputStream());

            System.out.println(nick);
            out.writeUTF(nick);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    @Override
    public void run() {
        try{
        	
            //Ciclo infinito que escucha por mensajes del servidor y los muestra en el panel
             
            while(true){
            	int pro=in.readInt();
            	//Leemos para si se envia la lista de nick un mensaje o una imagen.
            	switch (pro) {
				case LIST_NICK: //Cuando se evia el nick lo muestra en el JList.
					DefaultListModel modelo = new DefaultListModel();
					
					usuarios= (ArrayList<String>) ois.readObject();
					for(String us:usuarios){
						modelo.addElement(us);
					}
					list.setModel(modelo);
					break;
				case ENV_MENSAJE://Recibe el mensaje y lo muestra en pantall
					System.out.println("Texto");
					mensajes+=in.readUTF();
					panel.setText(mensajes);
					
					break;
				case RECI_ARCHIVO://Recibe el archivo y lo guarda en la ruta especificada.
					
					System.out.println("Recibe archivo");
					entrada_bytes_fichero = new BufferedInputStream(cliente.getInputStream());
					
					String nombreFichero = in.readUTF();
					long long_fichero=in.readLong();
					System.out.println("Nombre del fichero: "+nombreFichero);
					System.out.println("longitud del archivo: "+long_fichero);
					int lectura,cont=0;
					
					byte trozo_fichero[]=new byte[1024];
					salida_bytes_fichero = new BufferedOutputStream(new FileOutputStream(ruta+"\\"+nombreFichero));
					
					while ( cont!=long_fichero ) {			
						lectura = entrada_bytes_fichero.read(trozo_fichero);
						cont=cont+lectura;
						
						System.out.println(cont);
						// Guardamos el fichero en disco
						salida_bytes_fichero.write(trozo_fichero, 0, lectura);
						// Mostramos los bytes recibidos
						System.out.println("Recibiendo fichero..." + lectura+" bytes");
					}
					
					salida_bytes_fichero.close();
					System.out.println("fin lectura");
					System.out.println("archivo recibido");
					salida_bytes_fichero.close();
					break;
				default:
					//System.out.println("No entra en ninguno");
					break;
            	}
            	
            }
            	
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Metodo para enviar archivo a todos los usuarios.
     * @param ruta
     */
    public void enviarArcivo(String ruta){
    	
    	System.out.println(ruta);
    	try {
    		out.writeInt(200);
    		int lectura;
    		byte trozo_fichero[]=new byte[1024];
    		File f=new File(ruta);
    		
    		entrada_bytes_fichero=new BufferedInputStream(new FileInputStream(f));
    		
    		out.writeUTF(f.getName());
    		System.out.println(f.length());
    		
    		out.writeLong(f.length());
    		out.writeUTF(" ");
    		while ((lectura=entrada_bytes_fichero.read(trozo_fichero)) != -1){
    			
    			// Mandamos el trozo de fichero al servidor
    			//salida_bytes_fichero.write(trozo_fichero, 0, lectura);
    			out.write(trozo_fichero, 0, lectura);
    			// Mostramos los bytes enviados
    			System.out.println("Enviando fichero..." + lectura+" bytes");
    		}
    		System.out.println("archivo enviado");
    			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * Metodo para enviar un archivo a un usuario con el nick especifico
     * @param ruta
     * @param nick
     */
    public void enviarArcivo(String ruta,String nick){
    	System.out.println(ruta);
    	System.out.println(nick);
    	try {
    		out.writeInt(200);
    		int lectura;
    		byte trozo_fichero[]=new byte[1024];
    		File f=new File(ruta);
    		
    		entrada_bytes_fichero=new BufferedInputStream(new FileInputStream(f));
    		//Enviamos el nombre del archivo
    		out.writeUTF(f.getName());
    		System.out.println(f.length());
    		//Enviamos la longitud del archivo
    		out.writeLong(f.length());
    		
    		out.writeUTF(nick);
    		while ((lectura=entrada_bytes_fichero.read(trozo_fichero)) != -1){
    			
    			// Mandamos el trozo de fichero al servidor
    			//salida_bytes_fichero.write(trozo_fichero, 0, lectura);
    			out.write(trozo_fichero, 0, lectura);
    			// Mostramos los bytes enviados
    			System.out.println("Enviando fichero..." + lectura+" bytes");
    		}
    		System.out.println("archivo enviado");
    			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }    
    
    //Funcion sirve para enviar mensajes al servidor
    public void enviarMsg(String msg){
    
        try {
        	
        	out.writeInt(100);
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
