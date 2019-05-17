import java.awt.*;
import javax.swing.*;
import java.util.*;

class Parametros{
    //clase que contiene los objetos que entran en la simulacion, separados de la interfaz grafica

    Calendar cal;//instancia del calendario
    Estadisticas estad;//objeto de estadisticas
    Torre control;//torre de control del puerto
    ArrayList<Barco> lista;//lista de barcos
    //situacion de la posicion de la zona critica y la puerta
    JFrame ventana;
    Container contenedor;
    JPanel panel;
    JLabel l1;
    JLabel l2;
    JLabel l3;
    
    Parametros(){
	this(5,5,10);
    }

    Parametros(int cap, int met, int num){
	this.cal = Calendar.getInstance();
	this.estad = new Estadisticas();
	this.control = new Torre(cap,met,estad,cal);
	this.lista = new ArrayList<Barco>();
	for(int i=0;i<num;i++){
	    Barco b = new Barco("b",control,estad,i%10);
	    lista.add(b);
	}

	//creacion de señales de comienzo de zona critica y puerto


	this.ventana = new JFrame();
	ventana.setResizable(false);
	ventana.setLocation(0,350);
	ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.contenedor = ventana.getContentPane();
	this.panel = new JPanel();
	this.l1 = new JLabel("               PUERTO                                                                         ");
	this.l2 = new JLabel("|ZONA CRITICA | ZONA CRITICA | ZONA CRITICA|");
	this.l3 = new JLabel("                                                                MAR ");
	panel.add(l1);
	panel.add(l2);
	panel.add(l3);
	contenedor.add(panel);
	ventana.pack();
	ventana.setVisible(true);
    }

    void comienzo(){
	//comienza la simulacion


	Barco b;
	int n = lista.size();
	for(int i=0;i<n;i++){
	    b = lista.get(i);
	    b.start();
	}
    }

    void modTorre(int cap, int met){
	//modifica los parametros de la torre


	control.meteorologia = met;
	control.capacidad = cap;
	control.cargarDatos();
    }

    void aumentarBarcos(int num){
	//aumenta el numero de barcos de la simulacion


	int tam = lista.size();
	int j = tam;
	for(int i=0;i<num;i++){
	    j++;
	    Barco b = new Barco("b",control,estad,j%10);
	    lista.add(b);
	    control.cargarDatos();
	    b.start();
	}
    }

    void disminuirBarcos(int num){
	//disminuye el numero de barcos cuando terminan su recorrido


	Barco b;
	int tam = lista.size();
	if(num>=tam){
	    this.paraSimulacion();
	}
	else{
	    for(int i=0;i<num;i++){
		b = lista.get(0);
		b.move = false;
		lista.remove(0);
	    }
	}
    }

    void paraSimulacion(){
	//detiene la simulacion cuando los barcos terminan el recorrido en el que se encuentran


	Barco b;
	int tam = lista.size();
	for(int i=0;i<tam;i++){
	    b = lista.get(i);
	    b.move = false;
	}
    }

    String consultarEstadisticas(){
	//consulta de estadisticas


	String s = estad.consultarEstadisticas();
	return s;
    }
}
