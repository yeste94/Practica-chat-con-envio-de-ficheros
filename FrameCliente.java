
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;


public class FrameCliente extends JFrame {
	
    private JButton jButton1;
    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JEditorPane peMsg;
    private JTextField txMsg;
    private Cliente cliente;
	private JTextField txtLocalhost;
	private JTextField textField;
	private JTextField txtRuta;
	private JLabel lblHost;
	private JLabel lblPuerto;
	private JLabel lblRuta;
	private JButton btnConectar;

	private String users;
	
	private String nick;
	private JList list;
	private JButton btnEnviarArchivo;
	
    public FrameCliente() {
		this.setTitle("Añadir Articulo");
		this.setBounds(100, 100, 700, 600);
        initComponents();
        
        

        
        //Hacemos que el scroll baje automaticamente con los mensajes
        jScrollPane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
            public void adjustmentValueChanged(AdjustmentEvent e) {  
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
            }
        });
    }


    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        peMsg = new JEditorPane();
        jLabel1 = new JLabel();
        txMsg = new JTextField();
        jButton1 = new JButton();
        
        jButton1.setEnabled(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        peMsg.setContentType("text/html"); 
        jScrollPane1.setViewportView(peMsg);
        
        peMsg.getAccessibleContext().setAccessibleDescription("text/plain");

        jLabel1.setText("Mensaje:");

        jButton1.setText("Enviar");
        jButton1.setActionCommand("enviarmensaje");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        lblHost = new JLabel("Host:");
        
        txtLocalhost = new JTextField();
        txtLocalhost.setText("localhost");
        txtLocalhost.setColumns(10);
        
        lblPuerto = new JLabel("Puerto:");
        
        textField = new JTextField();
        textField.setColumns(10);
        
        lblRuta = new JLabel("Ruta:");
        
        txtRuta = new JTextField();
        txtRuta.setColumns(10);
        
        btnConectar = new JButton("Conectar");
        btnConectar.setActionCommand("botonConectar");
        
        btnConectar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        list = new JList();
        
        
        list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				
				String selec=(String) list.getSelectedValue();
				
				System.out.println("Has seleccionado una elemento");
				JFileChooser  fc=new JFileChooser ();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = fc.showOpenDialog(getParent());
				
				File archivo = fc.getSelectedFile();
				String rutaArchi=archivo.getAbsolutePath();
				cliente.enviarArcivo(rutaArchi,selec);				
			}
		});
        
        btnEnviarArchivo = new JButton("Enviar Archivo");
        
        btnEnviarArchivo.setEnabled(false);
        btnEnviarArchivo.setActionCommand("enviarArchivo");
        btnEnviarArchivo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(lblHost)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(txtLocalhost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblPuerto)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(lblRuta)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(txtRuta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(26)
        					.addComponent(btnConectar))
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jLabel1)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txMsg, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
        							.addGap(18)
        							.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
        							.addGap(18)
        							.addComponent(btnEnviarArchivo))
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 430, GroupLayout.PREFERRED_SIZE)
        							.addGap(18)
        							.addComponent(list, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.RELATED)))
        			.addContainerGap(133, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblHost)
        				.addComponent(txtLocalhost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblPuerto)
        				.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnConectar)
        				.addComponent(lblRuta)
        				.addComponent(txtRuta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
        				.addComponent(list, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel1)
        				.addComponent(txMsg, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnEnviarArchivo))
        			.addContainerGap(43, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);  
    }

    
    
    private void jButton1ActionPerformed(ActionEvent evt) {
    	
    	if(evt.getActionCommand().equals("enviarArchivo")){
			JFileChooser  fc=new JFileChooser ();
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int result = fc.showOpenDialog(getParent());
			
			File archivo = fc.getSelectedFile();
			String rutaArchi=archivo.getAbsolutePath();
			cliente.enviarArcivo(rutaArchi);
    	}
    	
    	
    	
    	if(evt.getActionCommand().equals("botonConectar")){
    		
    		this.setNick(JOptionPane.showInputDialog("Dame tu nick"));
    		cliente= new Cliente(peMsg,list,this.nick,this);
    		
            
            Thread hilo = new Thread(cliente);
            hilo.start();
            
            this.getBtnConectar().setEnabled(false);
            this.getjButton1().setEnabled(true);
            this.btnEnviarArchivo.setEnabled(true);
    	}
    	
    	
    	if(evt.getActionCommand().equals("enviarmensaje")){
            //Sacamos el nombre y el mensaje de las cajas de texto
            String msg = txMsg.getText();
            //Limpiamos el cuadro de texto del mensaje
            txMsg.setText("");
            //Utilizamos la funcion del cliente para enviar el mensaje
            cliente.enviarMsg("<strong>"+this.nick+": </strong> "+msg+"<br>");
            
    	}
    }

    public static void main(String args[]) {
      
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameCliente().setVisible(true);
            }
        });
    }
    
    
    
	public JButton getjButton1() {
		return jButton1;
	}
	public void setjButton1(JButton jButton1) {
		this.jButton1 = jButton1;
	}
	public JLabel getjLabel1() {
		return jLabel1;
	}
	public void setjLabel1(JLabel jLabel1) {
		this.jLabel1 = jLabel1;
	}
	public JScrollPane getjScrollPane1() {
		return jScrollPane1;
	}
	public void setjScrollPane1(JScrollPane jScrollPane1) {
		this.jScrollPane1 = jScrollPane1;
	}
	public JEditorPane getPeMsg() {
		return peMsg;
	}
	public void setPeMsg(JEditorPane peMsg) {
		this.peMsg = peMsg;
	}
	public JTextField getTxMsg() {
		return txMsg;
	}
	public void setTxMsg(JTextField txMsg) {
		this.txMsg = txMsg;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public JTextField getTxtLocalhost() {
		return txtLocalhost;
	}
	public void setTxtLocalhost(JTextField txtLocalhost) {
		this.txtLocalhost = txtLocalhost;
	}
	public JTextField getTextField() {
		return textField;
	}
	public void setTextField(JTextField textField) {
		this.textField = textField;
	}
	public JTextField getTxtRuta() {
		return txtRuta;
	}
	public void setTxtRuta(JTextField textField_1) {
		this.txtRuta = textField_1;
	}
	public JLabel getLblHost() {
		return lblHost;
	}
	public void setLblHost(JLabel lblHost) {
		this.lblHost = lblHost;
	}
	public JLabel getLblPuerto() {
		return lblPuerto;
	}
	public void setLblPuerto(JLabel lblPuerto) {
		this.lblPuerto = lblPuerto;
	}
	public JLabel getLblRuta() {
		return lblRuta;
	}
	public void setLblRuta(JLabel lblRuta) {
		this.lblRuta = lblRuta;
	}
	public JButton getBtnConectar() {
		return btnConectar;
	}
	public void setBtnConectar(JButton btnConectar) {
		this.btnConectar = btnConectar;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getUsers() {
		return users;
	}
	public void setUsers(String users) {
		this.users = users;
	}
	public JList getList() {
		return list;
	}
	public void setList(JList list) {
		this.list = list;
	}
	public JButton getBtnEnviarArchivo() {
		return btnEnviarArchivo;
	}
	public void setBtnEnviarArchivo(JButton btnEnviarArchivo) {
		this.btnEnviarArchivo = btnEnviarArchivo;
	}
	
}
