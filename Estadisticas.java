class Estadisticas{

    int numES;//numero de peticiones en entrada y salida
    int bES;//numero de barcos en entrada y salida
    long timeES;//tiempo de espera en entrada y salida
    int numE;//numero de peticiones en entrada
    int bE;//numero de barcos en entrada
    long timeE;//tiempo de espera en entrada
    int numS;//numero de peticiones en salida
    int bS;//numero de barcos en salida
    long timeS;//tiempo de espera en salida
    int numPE;//numero de pasajeros en entrada
    int numPS;//numero de pasajeros en salida
    int numPPE;//numero de peticiones en entrada en pasajeros
    int numPPS;//numero de peticiones en salida en pasajeros

    Estadisticas(){
	this.numES = 0;
	this.bES = 0;
	this.timeES = 0;
	this.numE = 0;
	this.bE = 0;
	this.timeE = 0;
	this.numS = 0;
	this.bS = 0;
	this.timeS = 0;
	this.numPE = 0;
	this.numPS = 0;
	this.numPPE = 0;
	this.numPPS = 0;
    }

    void aumentarBarcosEnEntrada(){
	//contador de barcos que quieren entrar y total de barcos que se mueven


	this.bE++;
	this.bES++;
    }

    void aumentarBarcosEnSalida(){
	//contador de barcos que quieren salir y total de barcos que se mueven


	this.bS++;
	this.bES++;
    }

    void aumentarPeticionesEnEntrada(Barco b){
	//contador de peticiones hechas para entrar y en total


	this.numE++;
	this.numES++;
	this.numPPE = numPPE + b.pasajeros;
    }

    void aumentarPeticionesEnSalida(Barco b){
	//contador de peticiones hechas para salir y en total


	this.numS++;
	this.numES++;
	this.numPPS = numPPS + b.pasajeros;
    }

    void aumentarTiempoEnEntrada(long entrada, long salida){
	//contador de tiempo esperado en entrada y total


	long tiempo = salida - entrada;

	this.timeE = this.timeE + tiempo;
	this.timeES = this.timeES + tiempo;
    }

    void aumentarTiempoEnSalida(long entrada, long salida){
	//contador te tiempo esperado en salida y total


	long tiempo = salida - entrada;

	this.timeS = this.timeS + tiempo;
	this.timeES = this.timeES + tiempo;
    }

    void aumentarPasajerosEnEntrada(Barco b){
	//aumenta el numero de pasajeros que han querido entrar

	
	this.numPE = numPE + b.pasajeros;
    }

    void aumentarPasajerosEnSalida(Barco b){
	//aumenta el numero de pasajeros que han querido salir

	
	this.numPS = numPS + b.pasajeros;
    }
    
    Double mediaGlobal(){
	//media de peticiones por barco para entrar o salir, mayor o igual que 1


	Double media;
	Double num = new Double(this.numES);

	if(bES!=0){
	    media = num/bES;
	}
	else{
	    media = new Double(0);
	}
	return media;
    }

    Double mediaEntrada(){
	//media de peticiones por barco para entrar, mayor o igual que 1


	Double media;
	Double num = new Double(this.numE);

	if(bE!=0){
	    media = num/bE;
	}
	else{
	    media = new Double(0);
	}
	return media;
    }

    Double mediaSalida(){
	//media de peticiones por barco para salir, mayor o igual que 1


	Double media;
	Double num = new Double(this.numS);

	if(bS!=0){
	    media = num/bS;
	}
	else{
	    media = new Double(0);
	}
	return media;
    }

    Double mediaPasGlobal(){
	//media de peticiones por pasajero

	
	Double media;
	int pas = this.numPE + this.numPS;
	int dato = this.numPPE + this.numPPS;
	Double num = new Double(dato);
	
	if (pas!=0) {
	    media = num/pas;
	}
	else {
	    media = new Double(0);   
	}
	return media;
    }
    
    Double mediaPasEntrada(){
	//media de peticiones por pasajero en entrada

	
	Double media;
	Double num = new Double(this.numPPE);
	
	if (numPE!=0) {
	    media = num/numPE;
	}
	else {
	    media = new Double(0);
	}
	return media;
    }

    Double mediaPasSalida(){
	//media de peticiones por pasajero en salida

	
	Double media;
	Double num = new Double(this.numPPS);
	
	if (numPS!=0) {
	    media = num/numPS;
	}
	else {
	    media = new Double(0);
	}
	return media;
    }
    
    Double mediaPasajerosPorBarco(){
	//media de pasajeros que viajan en un barco


	Double media;
	int valor = this.numPE + this.numPS;
	Double num = new Double(valor);

	if(bES!=0){
	    media = num/bES;
	}
	else{
	    media = new Double(0);
	}
	return media;
    }

    long mediaTiempo(){
	//media de tiempo total en las peticiones de entrada o salida


	long media;

	if(bES!=0){
	    media = timeES/bES;
	}
	else{
	    media = 0;
	}
	return media;
    }

    long mediaTiempoEntrada(){
	//media de tiempo total en las peticiones de entrada


	long media;

	if(bE!=0){
	    media = timeE/bE;
	} 
	else{
	    media = 0;
	}
	return media;
    }

    long mediaTiempoSalida(){
	//media de tiempo total en las peticiones de salida


	long media;

	if(bS!=0){
	    media = timeS/bS;
	}
	else{
	    media = 0;
	}
	return media;
    }

    String consultarEstadisticas(){
	//resultados de la estadistica


	String s;
	Double med = this.mediaGlobal();
	Double medE = this.mediaEntrada();
	Double medS = this.mediaSalida();
	Double medP = this.mediaPasGlobal();
	Double medPE = this.mediaPasEntrada();
	Double medPS = this.mediaPasSalida();
	long medT = this.mediaTiempo();
	long medTE = this.mediaTiempoEntrada();
	long medTS = this.mediaTiempoSalida();
	Double medPasBar = this.mediaPasajerosPorBarco();

	s = "media global = " + med;
	s = s + "\nmedia entrada = " + medE;
	s = s + "\nmedia salida = " + medS;
	s = s + "\nmedia pasajero global = " + medP;
	s = s + "\nmedia pasajero entrada = " + medPE;
	s = s + "\nmedia pasajero salida = " + medPS;
	s = s + "\nmedia tiempo = " + medT;
	s = s + "\nmedia tiempo entrada = " + medTE;
	s = s + "\nmedia tiempo salida = " + medTS;
	s = s + "\ntiempo global de espera de los barcos = " + timeES;
	s = s + "\nmedia de pasajeros por barco = " + medPasBar;
	return s;
    }
}