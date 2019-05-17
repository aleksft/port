import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Ventana extends JFrame{

    Parametros param;//objeto que controla la simulacion
    JFrame vent;//ventana principal
    Container contenedor;//contenedor de la ventana principal
    JPanel panel;//panel de la ventana principal
    JButton start;//boton de comienzo y actualizacion
    JButton stop;//boton de parada del sistema
    JButton estadist;//consulta de estadisticas
    JTextField texto;//texto de comentarios
    Escuchador listenEvent;//escuchador de eventos
    JFrame vent1;//ventana de inicio
    Container contenedor1;//contenedor de vent1
    JPanel panel1;//panel de vent1
    JPanel panel1B;//panel de vent1 igual a panelB
    JLabel lab1;//etiqueta 1
    JLabel lab2;//etiqueta 2
    JLabel lab3;//etiqueta 3
    JTextField numB;//numero de barcos
    JButton ok1;//boton de aceptar de vent1
    JTextField numC1;//capacidad del puerto en vent1
    JTextField meteo1;//meteorologia en vent1
    JFrame vent2;//ventana de cambio de parametros
    Container contenedor2;//contenedor de vent2
    JPanel panel2;//panel unsado por vent1 y vent2
    JPanel panelB;//panel de boton de vent1 y vent2
    JLabel lab5;//etiqueta 5
    JLabel lab6;//etiqueta 6
    JLabel lab7;//etiqueta 7
    JButton ok;//boton de aceptar en vent2
    JTextField numC;//capacidad del puerto en vent2
    JTextField meteo;//meteorologia en vent2
    JTextField aumB;//numero de barcos que se aumentan en la simulacion

    Ventana(){
	//creacion de la ventana principal


	this.vent = new JFrame("SIMULACION");
	vent.setResizable(false);
	vent.setLocation(360,620);
	this.listenEvent = new Escuchador();
	this.contenedor = vent.getContentPane();
	this.panel = new JPanel();
	this.start = new JButton("PLAY");
	start.setActionCommand("play");
	start.addActionListener(listenEvent);
	this.stop = new JButton("STOP");
	stop.setActionCommand("stop");
	stop.addActionListener(listenEvent);
	this.estadist = new JButton("ESTADISTICAS");
	estadist.setActionCommand("estad");
	estadist.addActionListener(listenEvent);
	this.texto = new JTextField(10);
	panel.add(start);
	panel.add(stop);
	panel.add(estadist);
	panel.add(texto);
	contenedor.add(panel,BorderLayout.CENTER);
	vent.pack();
	vent.setVisible(true);
	vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	//creacion de la ventana de inicio


	this.vent1 = new JFrame("INICIO");
	vent1.setResizable(false);
	vent1.setLocation(500,300);
	this.contenedor1 = vent1.getContentPane();
	this.panel1 = new JPanel();
	this.panel1B = new JPanel();
	this.ok1 = new JButton("OK");
	ok1.setActionCommand("ok");
	ok1.addActionListener(listenEvent);
	this.lab1 = new JLabel("num barcos = ");
	this.lab2 = new JLabel("capacidad = ");
	this.lab3 = new JLabel("meteorologia = ");
	this.numB = new JTextField(2);
	this.numC1 = new JTextField(2);
	this.meteo1 = new JTextField(2);
	panel1.add(lab1);
	panel1.add(numB);
	panel1.add(lab2);
	panel1.add(numC1);
	panel1.add(lab3);
	panel1.add(meteo1);
	panel1B.add(ok1);
	contenedor1.add(panel1,BorderLayout.NORTH);
	contenedor1.add(panel1B,BorderLayout.SOUTH);
	vent1.pack();
	vent1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	//creacion de la ventana de modificacion


	this.vent2 = new JFrame("MODIFICACIONES");
	vent2.setResizable(false);
	vent2.setLocation(500,300);
	this.contenedor2 = vent2.getContentPane();
	this.panel2 = new JPanel();
	this.panelB = new JPanel();
	this.ok = new JButton("OK");
	ok.setActionCommand("reok");
	ok.addActionListener(listenEvent);
	this.numC = new JTextField(2);
	this.meteo = new JTextField(2);
	this.aumB = new JTextField(2);
	this.lab5 = new JLabel("capacidad = ");
	this.lab6 = new JLabel("meteorologia = ");
	this.lab7 = new JLabel("aumentar barcos = ");
	panel2.add(lab5);
	panel2.add(numC);
	panel2.add(lab6);
	panel2.add(meteo);
	panel2.add(lab7);
	panel2.add(aumB);
	panelB.add(ok);
	contenedor2.add(panel2,BorderLayout.CENTER);
	contenedor2.add(panelB,BorderLayout.SOUTH);
	vent2.pack();
	vent2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    class Escuchador implements ActionListener{

	public void actionPerformed(ActionEvent e){
	    String accion = e.getActionCommand();

	    if(accion.equals("play")){
		texto.setText("COMIENZA");
		vent1.setVisible(true);
	    }
	    if(accion.equals("replay")){
		texto.setText("MODIFICACION");
		vent2.setVisible(true);
	    }
	    if(accion.equals("ok")){
		vent1.setVisible(false);
		this.leerTextoInicio();
		start.setActionCommand("replay");
	    }
	    if(accion.equals("reok")){
		vent2.setVisible(false);
		this.leerTexto();
	    }
	    if(accion.equals("estad")){
		String s = param.consultarEstadisticas();
		JOptionPane.showMessageDialog(null,s);
	    }
	    if(accion.equals("stop")){
		String s = "la simulacion terminara cuando";
		s = s + "\nfinalicen los recorridos";
		texto.setText("FINALIZADO");
		param.paraSimulacion();
		JOptionPane.showMessageDialog(null,s);
	    }
	}

	void leerTextoInicio(){
	    //toma los parametros iniciales introducidos


	    Integer n;
	    String s;
	    int num;
	    int aux1;
	    int aux2;
	    int ini;
	    int met;
	    int cap;

	    try{
		n = new Integer(numB.getText());
		num = n.intValue();
	    }catch(NullPointerException e){
		num = 10;
	    }
	    try{
		n = new Integer(meteo1.getText());
		met = n.intValue();
	    } catch(NullPointerException e){
		met = 5;
	    }
	    try{
		n = new Integer(numC1.getText());
		cap = n.intValue();
	    } catch(NullPointerException e){
		cap = 5;
	    }
	    param = new Parametros(cap,met,num);
	    param.comienzo();
	}

	void leerTexto(){
	    //toma las modificaciones de los parametros introducidos


	    Integer n;
	    int met;
	    int cap;
	    int num;

	    try{
		n = new Integer(meteo.getText());
		met = n.intValue();
	    } catch(NullPointerException e){
		met = 5;
	    }
	    try{
		n = new Integer(numC.getText());
		cap = n.intValue();
	    } catch(NullPointerException e){
		cap = 5;
	    }
	    try{
		n = new Integer(aumB.getText());
		num = n.intValue();
	    } catch(NullPointerException e){
		num = 0;
	    }
	    param.modTorre(cap,met);
	    if(num>=0){
		param.aumentarBarcos(num);
	    }
	    else{
		num = Math.abs(num);
		param.disminuirBarcos(num);
	    }
	}
    }

    public static void main(String[] args){
	Ventana vent = new Ventana();
    }
}
