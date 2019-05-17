import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

class Torre{

    int nent;//numero de barcos entrando
    int ns;//numero de barcos saliendo
    int we;//numero de barcos esperando para entrar
    int ws;//numero de barcos esperando para salir
    int N;//limite de barcos esperando en cola
    boolean puerta;//sentido de entrada de la puerta
    Estadisticas estad;//estadisticas asociadas al programa
    Calendar cal;//calendario de la torre
    int meteorologia;//valoracion de la meteorologia, 1 muy buen tiempo, 10 tormenta
    int capacidad;//capacidad del puerto
    int ocupacion;//numero de barcos en el puerto
    PriorityQueue<Barco> entrada;;//cola de espera de barcos esperando para entrar
    PriorityQueue<Barco> salida;//cola de espera de barcos esperando para salir
    PriorityQueue<Barco> aux;//cola de espera auxiliar
    Barco ultimoE;//ultimo barco entrando
    Barco ultimoS;//ultimo barco saliendo
    JFrame ventana;//ventana de la torre
    Container contenedor;//contenedor de la ventana
    JPanel panel;//panel de ocupacion
    JPanel panelE;//panel de entrada
    JPanel panelS;//panel de salida
    JTextField ocup;//ocupacion del puerto
    JTextField capac;//capacidad del puerto
    JTextField bne;//barcos entrando
    JTextField bns;//barcos saliendo
    JTextField bwe;//barcos esperando para entrar
    JTextField bws;//barcos esperando para salir
    
    Torre(int cap, int m, Estadisticas e, Calendar c){
	this.nent = 0;
	this.ns = 0;
	this.we = 0;
	this.ws = 0;
	this.puerta = true;
	this.estad = e;
	this.cal = c;
	this.meteorologia = m;
	this.capacidad = cap;
	this.ocupacion = 0;
	this.entrada = new PriorityQueue<Barco>();
	this.salida = new PriorityQueue<Barco>();
	this.aux = new PriorityQueue<Barco>();
	this.ultimoE = null;
	this.ultimoS = null;

	//creacion de la ventana


	this.ventana = new JFrame("TORRE");
	ventana.setResizable(false);
	ventana.setLocation(384,0);
	this.contenedor = ventana.getContentPane();
	this.panelE = new JPanel();
	this.panelS = new JPanel();
	this.panel = new JPanel();
	this.ocup = new JTextField(10);
	this.capac = new JTextField(10);
	this.bne = new JTextField(10);
	this.bns = new JTextField(10);
	this.bwe = new JTextField(10);
	this.bws = new JTextField(10);
	panel.add(ocup);
	panel.add(capac);
	panelE.add(bne);
	panelE.add(bwe);
	panelS.add(bns);
	panelS.add(bws);
	contenedor.add(panel,BorderLayout.NORTH);
	contenedor.add(panelE,BorderLayout.CENTER);
	contenedor.add(panelS,BorderLayout.SOUTH);
	ventana.pack();
	ventana.setVisible(true);
	ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.cargarDatos();
    }

    synchronized boolean actualizarOcupacion(){
	//actualiza la ocupacion y devuelve true si el puerto aun no esta completo


	boolean b;

	if(ocupacion<capacidad){
	    this.ocupacion++;
	    b = true;
	}
	else{
	    b = false;
	}
	return b;
    }

    void previoPideEntrar(Barco b){
	//valores previos a la peticion de entrada sin necesidad de sincronizacion


	b.tiempoE = cal.getTimeInMillis();
	this.prioridadEntrada(b);
    }

    void previoPideSalir(Barco b){
	//valores previos a la peticion de salida sin necesidadde sincronizacion


	b.tiempoE = cal.getTimeInMillis();
	this.prioridadSalida(b);
    }

    synchronized void pideEntrar(Barco b){
	//peticion de entrada de los barcos, si no hay barcos en la otra direccion, pasan
	//si los hay, deben esperar, cambiando la direccion de la puerta cuando superen el
	//limite


	entrada.add(b);
	System.out.println(b.toString()+" pide entrar con ns = "+ns);
	//modificacion de estadisticas
	estad.aumentarBarcosEnEntrada();
	estad.aumentarPasajerosEnEntrada(b);
	if((b.turno)&(ns==0)&((ws==0)|(puerta))&(capacidad-ocupacion!=0)&(nent+ocupacion<capacidad)){
	    estad.aumentarPeticionesEnEntrada(b);
	}
	//fin de modificacion de estadisticas
	while ((!b.turno)|(ns>0)|((ws>0)&(!puerta))|(capacidad-ocupacion==0)|(nent+ocupacion>=capacidad)) {
	    this.actualizacionPrioridadEntrada(b);
	    this.we++;
	    this.cargarDatos();
	    b.turno = false;
	    //modificacion de estadisticas
	    estad.aumentarPeticionesEnEntrada(b);
	    //fin de modificacion de estadisticas
	    System.out.println(b.toString()+" en espera de entrar, we = "+we);
	    try{
		this.wait();
	    } catch(InterruptedException e){System.out.println("excepcion en entrada");}
	    if((ocupacion+nent<capacidad)&&(aux.size()>0)&&(b.equals(aux.peek()))){
		b.turno = true;
	    }
	    this.we--;
	}
	this.nent++;
	this.cargarDatos();
	entrada.remove(b);
	aux.remove(b);
    }

    synchronized void pideSalir(Barco b){
	//peticion de salida de los barcos, si no hay barcos en la otra direccion, pasan
	//si los hay, deben esperar, cambiando la direccion de la puerta cuando superen el
	//limite


	salida.add(b);
	System.out.println(b.toString()+" pide salir con ne = "+nent);
	//modificacion de estadisticas
	estad.aumentarBarcosEnSalida();
	estad.aumentarPasajerosEnSalida(b);
	if (((b.turno)&(nent==0)&((we==0)|(!puerta)))|(capacidad-ocupacion==0)){
	    estad.aumentarPeticionesEnSalida(b);
	}
	//fin de modificacion de estadisticas
	while (((!b.turno)|(nent>0)|((we>0)&(puerta)))&(capacidad-ocupacion!=0)) {
	    this.actualizacionPrioridadSalida(b);
	    this.ws++;
	    this.cargarDatos();
	    b.turno = false;
	    //modificacion de estadisticas
	    estad.aumentarPeticionesEnSalida(b);
	    //fin de modificacion de estadisticas
	    System.out.println(b.toString()+" en espera de salir, ws = "+ws);
	    try{
		this.wait();
	    } catch(InterruptedException e){System.out.println("excepcion en salida");}
	    if((aux.size()>0)&&(b.equals(aux.peek()))){
		b.turno = true;
	    }
	    this.ws--;
	}
	this.ns++;
	this.cargarDatos();
	salida.remove(b);
	aux.remove(b);
    }

    void actualizacionPrioridadEntrada(Barco b){
	//envejecimiento


	long tiempo = cal.getTimeInMillis();

	if(tiempo-b.tiempoE>=5000){
	    b.prioridad = Math.min(b.prioridad,10);
	}
    }

    void actualizacionPrioridadSalida(Barco b){
	//envejecimiento


	long tiempo = cal.getTimeInMillis();

	if(tiempo-b.tiempoE>=5000){
	    b.prioridad = Math.min(b.prioridad,10);
	}
    }

    void finPideEntrar(Barco b){
	//valores tras obtener permiso para entrar sin necesidad de sincronizacion


	//modificacion de estadisticas
	b.tiempoS = cal.getTimeInMillis();
	estad.aumentarTiempoEnEntrada(b.tiempoE,b.tiempoS);
	b.valoresPredeterminados();
	//fin de modificacion de estadisticas
	//valores de media de espera en salida
	String s = estad.consultarEstadisticas();
	System.out.println("estadisticas:\n"+s);
	//fin de valores de media de espera en salida
	System.out.println("ns = "+ns);
	this.testEntrada(b);
    }

    void finPideSalir(Barco b){
	//valores tras obtener permiso para salir sin necesidad de sincronizacion


	//modificacion de estadisticas
	b.tiempoS = cal.getTimeInMillis();
	estad.aumentarTiempoEnSalida(b.tiempoE,b.tiempoS);
	b.valoresPredeterminados();
	//fin de modificacion de estadisticas
	//valores de media de espera en salida
	String s = estad.consultarEstadisticas();
	System.out.println("estadisticas:\n"+s);
	//fin de valores de media de espera en salida
	System.out.println("ns = "+ns);
	this.testSalida(b);
    }

    synchronized void testEntrada(Barco b){
	//ajuste de entrada del barco


	if(ultimoE!=null){b.velocidad = Math.min(b.velocidad,ultimoE.velocidad);}
	b.anterior = this.ultimoE;
	this.ultimoE = b;
	if(aux.size()>0){
	    this.notifyAll();
	}
    }

    synchronized void testSalida(Barco b){
	//ajuste de salida del barco


	if(ultimoS!=null){b.velocidad = Math.min(b.velocidad,ultimoS.velocidad);}
	b.anterior = this.ultimoS;
	this.ultimoS = b;
	if(aux.size()>0){
	    this.notifyAll();
	}
    }
    
    void prioridadEntrada(Barco b){
	//caulculo de la prioridad para la entrada


	Double valor;
	int prior;

	if(this.meteorologia<=5){
	    valor = new Double(b.pasajeros)/100;
	    prior = this.meteorologia + valor.intValue();
	    b.prioridad = Math.min(prior,10);
	}
	else{
	    valor = new Double(1000 - b.pasajeros)/100;
	    prior = this.meteorologia - valor.intValue();
	    b.prioridad = Math.max(prior,0);
	}
    }

    void prioridadSalida(Barco b){
	//calculo de la prioridad para la salida


	Double valor;
	int prior;

	if(this.meteorologia<=5){
	    valor = new Double (1000 - b.pasajeros)/100;
	    prior = 10 - this.meteorologia - valor.intValue();
	    b.prioridad = Math.max(prior,0);
	}
	else{
	    valor = new Double(b.pasajeros)/100;
	    prior = 10 - this.meteorologia + valor.intValue();
	    b.prioridad = Math.min(prior,10);
	}
    }

    synchronized void terminaEntrar(Barco b){
	//finalizacion de entrada, cuando no hay barcos entrando, si tampoco hay barcos
	//esperando para entrar, se cambiara el sentido de la puerta para salir, en cualquier
	//caso, ne notificara a los barcos en espera, cuando no haya barcos entrando


	this.nent--;
	this.ocupacion++;
	this.cargarDatos();
	System.out.println(b.toString()+" Notificacion de entrada recibida, ne = "+nent);
	this.puerta = false;
	if(nent==0){
	    this.ultimoE = null;
	    if(salida.size()>0){
		this.aux = new PriorityQueue<Barco>(salida);
	    }
	    else{
		if(capacidad-ocupacion==0){
		    System.out.println("PUERTO COMPLETO");
		}
		else{
		    this.aux = new PriorityQueue<Barco>(entrada);
		}
	    }
	    this.notifyAll();
	}
    }

    synchronized void terminaSalir(Barco b){
	//finalizacion de salida, cuando no hay barcos saliendo, si tampoco hay barcos
	//esperando para salir, se cambiara el sentido de la puerta para entrar, en cualquier
	//caso, ne notificara a los barcos en espera, cuando no haya barcos saliendo


	this.ns--;
	this.ocupacion--;
	this.cargarDatos();
	System.out.println(b.toString()+" Notificacion de salida recibida, ns = "+ns);
	this.puerta = true;
	if(ns==0){
	    this.ultimoS = null;
	    if((this.capacidad-this.ocupacion==0)&(salida.size()>0)){
		this.puerta = false;
		this.aux = new PriorityQueue<Barco>(salida);
	    }
	    else{
		if(this.capacidad-this.ocupacion>0){
		    this.aux = new PriorityQueue<Barco>(entrada);
		}
		else{
		    System.out.println("PUERTO COMPLETO sin barcos que quieran salir");
		}
	    }
	    this.notifyAll();
	}
    }

    synchronized void cargarDatos(){
	//carga de datos en la ventana de la torre


	Integer n;

	n = new Integer(this.ocupacion);
	ocup.setText("ocup = " + n.toString());
	n = new Integer(this.capacidad);
	capac.setText("cap = " + n.toString());
	n = new Integer(this.nent);
	bne.setText("ne = " + n.toString());
	n = new Integer(this.we);
	bwe.setText("we = " + n.toString());
	n = new Integer(this.ns);
	bns.setText("ns = " + n.toString());
	n = new Integer(this.ws);
	bws.setText("ws = " + n.toString());
    }
}
