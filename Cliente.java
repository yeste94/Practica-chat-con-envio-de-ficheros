
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
    public Cliente(JEditorPane panel,JList list,String nick){
        this.panel = panel;
        this.list=list;
        
       
       //System.out.println(user);
        try {
            cliente = new Socket(host,puerto);
            in = new DataInputStream(cliente.getInputStream());
            out = new DataOutputStream(cliente.getOutputStream());
            ois = new ObjectInputStream(cliente.getInputStream());
            
            out.writeUTF(nick);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    @Override
    public void run() {
        try{
            //Ciclo infinito que escucha por mensajes del servidor y los muestra en el panel
            
        	//System.out.println(HiloServidor.users.size());
 
        	
            while(true){
            	int pro=in.readInt();
            	switch (pro) {
				case LIST_NICK:
					DefaultListModel modelo = new DefaultListModel();
					
					usuarios= (ArrayList<String>) ois.readObject();
					for(String us:usuarios){
						modelo.addElement(us);
					}
					list.setModel(modelo);
					break;
				case ENV_MENSAJE:
					System.out.println("Texto");
					mensajes+=in.readUTF();
					panel.setText(mensajes);
					
					break;
				case RECI_ARCHIVO:
					
					System.out.println("Recibe archivo");
					entrada_bytes_fichero = new BufferedInputStream(cliente.getInputStream());
					
					String nombreFichero = in.readUTF();
					System.out.println(nombreFichero);
					
					int lectura;
					byte trozo_fichero[]=new byte[1024];
					salida_bytes_fichero = new BufferedOutputStream(new FileOutputStream(ruta+"\\"+nombreFichero));
					
					//Comenzamos a leer del cliente, cuando la lectura vale -1
					while((lectura=entrada_bytes_fichero.read(trozo_fichero)) != -1){
						//guardamos el fichero en disco
						salida_bytes_fichero.write(trozo_fichero, 0, lectura);
						//Monstramos los bytes recibidos
						System.out.println("Recibiendo fichero..."+ lectura+ " bytes");
					}
					
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
    
    public void enviarArcivo(String ruta){
    	System.out.println(ruta);
    	try {
			out.writeInt(200);
			byte trozo_fichero[]=new byte[8192];
			int lectura;
			File f=new File(ruta);
			salida_bytes_fichero=new BufferedOutputStream(cliente.getOutputStream());
			entrada_bytes_fichero=new BufferedInputStream(new FileInputStream(f));
			
			
			out.writeUTF(f.getName());
			//Bucle para enviar los bytes del fichero.
			
			while((lectura=entrada_bytes_fichero.read(trozo_fichero)) != -1){
				System.out.println("leer archivo");
				//Mandamos  el trozo de fichero al servidor
				System.out.println("mandar archivo");
				salida_bytes_fichero.write(trozo_fichero,0,lectura);
				//Monstramos los bytes enviados
				System.out.println("Enviando fichero..." + lectura+" bytes");
			}
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
