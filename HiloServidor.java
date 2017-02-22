
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JOptionPane;


public class HiloServidor implements Runnable{
	
    public static Object hilos;
	//Declaramos las variables que utiliza el hilo para estar recibiendo y mandando mensajes
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream oos;
    
	private BufferedInputStream entrada_bytes_fichero; // Para la lectura del fichero
	private BufferedOutputStream salida_bytes_fichero;
    //Lista de los usuarios conectados al servidor
    private LinkedList<Socket> usuariosSocket = new LinkedList<Socket>();
    private static ArrayList<String> users = new ArrayList<String>();
    
    private HashMap<String,Socket> user_socket=new HashMap<String,Socket>();
    
    private static final String ruta="C:\\Users\\troya\\Documents\\";
	public Object salida;
    
	
    //Constructor que recibe el socket que atendera el hilo y la lista de usuarios conectados
    public HiloServidor(Socket soc,LinkedList users){
        socket = soc;
        usuariosSocket = users;
        //this.users+=nick;
    }
    
    public HiloServidor(){
    	
    }
    
    public ArrayList<String> listNick(){
    	return users;
    }
	@Override
    public void run() {
        try {
            //Inicializamos los canales de comunicacion y mandamos un mensaje de bienvenida
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            
            String nick=in.readUTF();
            users.add(nick);
            user_socket.put(nick, socket);    
            
            
            
            for(Socket s:user_socket.values()){
            	System.out.println(s.toString());
            }
            for(String u:user_socket.keySet()){
            	System.out.println(u);
            }
            
            
            
            for (int i = 0; i < usuariosSocket.size(); i++) {
            	ObjectOutputStream oos = new ObjectOutputStream(usuariosSocket.get(i).getOutputStream());
            	out = new DataOutputStream(usuariosSocket.get(i).getOutputStream());
                out.writeInt(Cliente.LIST_NICK);
                oos.writeObject(users);
            }
            
            
            
            //Ciclo infinito para escuchar por mensajes del cliente
            
            while(true){
            	int num=in.readInt();
            	
            	switch (num) {
            	
				case 100:
		             String recibido = in.readUTF();
		               
		               //Cuando se recibe un mensaje se envia a todos los usuarios conectados 
		                for (int i = 0; i < usuariosSocket.size(); i++) {
		                    out = new DataOutputStream(usuariosSocket.get(i).getOutputStream());
		                    out.writeInt(Cliente.ENV_MENSAJE);
		                    out.writeUTF(recibido);
		                }
					break;
					
				case 200: //Recibe el archivo y lo envia ha todos lo usuarios conectados 
					
					
					int lectura,cont=0;
					long long_archivo;
					byte trozo_fichero[]=new byte[1024];
					entrada_bytes_fichero = new BufferedInputStream(socket.getInputStream());
					
					
					String nombreFichero = in.readUTF();
					long_archivo=in.readLong();
					System.out.println(nombreFichero);
					System.out.println(long_archivo);
					
					
					salida_bytes_fichero = new BufferedOutputStream(new FileOutputStream(ruta+nombreFichero));

					

					
					
					
					while ( cont!=long_archivo ) {			
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
					
					
					
					
					
					
					//Mandamos a todos los usuarios el archivo recibido.
					byte trozo_fichero1[]=new byte[8192];
					int lectura1;
					File f=new File(ruta+nombreFichero);
					for(int i=0; i < usuariosSocket.size(); i++){
						out.writeInt(300);
						salida_bytes_fichero=new BufferedOutputStream(usuariosSocket.get(i).getOutputStream());
						
						entrada_bytes_fichero=new BufferedInputStream(new FileInputStream(f));
						
						out.writeUTF(f.getName());
						out.writeLong(f.length());
						//Bucle para enviar los bytes del fichero.
						while((lectura1=entrada_bytes_fichero.read(trozo_fichero1)) != -1){
							
							//Mandamos  el trozo de fichero al servidor
							out.write(trozo_fichero1,0,lectura1);
							//Monstramos los bytes enviados
							System.out.println("Enviando fichero..." + lectura1+" bytes");
						
					}
					
				}
					
					
					break;
				default:
					break;
				}
  
            }
        } catch (IOException e) {
            //Si ocurre un excepcion lo mas seguro es que sea por que el cliente se desconecto asi que lo quitamos de la lista de conectados
            for (int i = 0; i < usuariosSocket.size(); i++) {
                if(usuariosSocket.get(i) == socket){
                	usuariosSocket.remove(i);
                    break;
                } 
            }
        }
    }
}
