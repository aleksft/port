import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Barco extends Thread implements Comparable<Barco>{

    String nombre;//nombre del barco
    Torre control;//torre de control del puerto
    Estadisticas estad;//estadisticas asociadas al programa
    long tiempoE;//tiempo de comienzo de la espera
    long tiempoS;//tiempo de fin de la espera
    Double velocidad;//velocidad del barco entre 10 y 50 metros por segundo
    int pasajeros;//numero de pasajeros del barco, entre 0 y 1000
    int prioridad;//prioridad del barco
    Barco anterior;//barco anterior en la cola en movimiento
    Double distanciaPuerta;//distancia de la puerta a tierra
    Double distanciaCritica;//distancia a tierra o alta mar del comienzo de la zona critica
    boolean dentro;//esta dentro o fuera
    boolean turno;//en true si tiene el turno para entrar en la zona critica
    boolean move;//cuando es false termina con su recorrido
    int j;//posicion de la ventana
    Double tiempo;//1 segundo
    JFrame ventana;//ventana del barco
    Container contenedor;//contenedor de la ventana
    JPanel panel;//panel de la ventana
    JTextArea info;//informacion del barco


    Barco(String n, Torre c, Estadisticas e, int k){
	this.nombre = n;
	this.control = c;
	this.estad = e;
	this.tiempoE = 0;
	this.tiempoS = 0;
	this.velocidad = new Double(0);
	this.pasajeros = 0;
	this.prioridad = 0;
	this.anterior = null;
	this.distanciaPuerta = new Double(500);
	this.distanciaCritica = new Double(300);
	this.turno = true;
	this.move = true;
	this.j = k;
	this.tiempo = new Double(1000);

	//creacion de la ventana


	this.ventana = new JFrame();
	ventana.setResizable(false);
	ventana.setLocation(0,j*40+170);
	this.contenedor = ventana.getContentPane();
	this.panel = new JPanel();
	this.info = new JTextArea(1,6);
	panel.add(info);
	contenedor.add(panel);
	ventana.pack();
	ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    void posicion(){
	//posicionamiento inicial del barco


	Double posicion = new Double(Math.random()*10);
	int sit = posicion.intValue();
	boolean ok;

	this.dentro = ((sit%2)==1);
	ok = dentro;
	if(dentro){
	    ok = control.actualizarOcupacion();
	}
	dentro = ok;
	ventana.setVisible(true);
    }
    
    public String toString(){
	String s;

	s = this.nombre();
	return s;
    }
    
    public boolean equals(Barco b){
	boolean t;

	t = this.velocidad.equals(b.velocidad);
	t = t & (this.pasajeros==b.pasajeros);
	t = t & (this.dentro==b.dentro);
	t = t & (this.getName().equals(b.getName()));
	return t;
    }

    public int compareTo(Barco b){
	int valor;

	valor = this.prioridad - b.prioridad;
	return valor;
    }

    public void run(){
	Double posicion;
	int sit;

	this.posicion();
	while (move) {
	    this.todosLosValoresPredeterminados();
	    this.datosBarco();
	    this.turno = true;
	    int v = velocidad.intValue();
	    if(!dentro){
		this.movimientoEntrada(v);
		this.retardo();
	    }
	    if(!move){break;}
	    this.movimientoSalida(v);
	}
	if (!dentro) {
	    ventana.setVisible(false);
	}
    }

    void movimientoEntrada(int v){
	//movimiento global de entrada


	this.movimiento(v,1030,700);
	control.previoPideEntrar(this);
	control.pideEntrar(this);
	control.finPideEntrar(this);
	this.entra();
	this.datosBarco();
	this.dentro = true;
	control.terminaEntrar(this);
	this.movimiento(v,300,0);
    }

    void movimientoSalida(int v){
	//movimiento global de salida


	this.movimiento(v,0,300);
	control.previoPideSalir(this);
	control.pideSalir(this);
	control.finPideSalir(this);
	this.sale();
	this.datosBarco();
	this.dentro = false;
	control.terminaSalir(this);
	this.movimiento(v,700,1030);
    }

    void entra(){
	//movimiento de entrada tras concesion de permiso


	int v = velocidad.intValue();
	this.movimiento(v,700,300);
    }

    void sale(){
	//movimiento de salida tras concesion de permiso


	int v = velocidad.intValue();
	this.movimiento(v,300,700);
    }

    void movimiento(int v, int ini, int fin){
	//movimiento del barco


	int p;

	if(ini>fin){
	    p = (ini-fin)/v;
	    for(int k=0;k<=p;k++){
		try{
		    this.sleep(tiempo.longValue());
		} catch(InterruptedException e){}
		ventana.setLocation(ini-k*v,j*40+170);
	    }
	}
	else{
	    p = (fin-ini)/v;
	    for(int i=0;i<=p;i++){
		try{
		    this.sleep(tiempo.longValue());
		} catch(InterruptedException e){}
		ventana.setLocation(ini+i*v,j*40+170);
	    }
	}
	ventana.setLocation(fin,j*40+170);
    }

    void valoresPredeterminados(){
	//restauracion de algunos valores predeterminados del barco


	this.tiempoE = 0;
	this.tiempoS = 0;
    }

    void todosLosValoresPredeterminados(){
	//restauracion de todos los valores predeterminados del barco


	this.valoresPredeterminados();
	this.velocidad = new Double(Math.random()*40+10);
	this.pasajeros = new Double(Math.random()*1000).intValue();
	this.prioridad = 5;
	this.anterior = null;
	this.nombre = pasajeros + "B"+ velocidad.intValue();
    }

    void retardo(){
	//para el barco cuando llega a puerto durante unos segundos


	Double t = new Double(Math.random()*10000);
	try{
	    this.sleep(t.longValue());
	} catch(InterruptedException e){}
    }

    void datosBarco(){
	//carga datos del barco en la ventana


	info.setText(this.toString());
    }

    String nombre(){
	//devuelve el nombre del barco


	return this.nombre;
    }
}
