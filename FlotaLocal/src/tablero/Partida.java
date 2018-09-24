package tablero;

import java.util.Random;
import java.util.Vector;

import tablero.Barco;

public class Partida {
	
	public static final int AGUA = -1, TOCADO = -2, HUNDIDO = -3;
	
	/**
	 * El mar se representa mediante una matriz de casillas
	 * En una casilla no tocada con un barco se guarda el indice del barco en el vector de barcos
	 * El resto de valores posibles (AGUA, TOCADO y HUNDIDO) se representan mediante 
	 * constantes enteras negativas.
	 */
	private int mar[][] = null;				// matriz que contendra el mar y los barcos en distintos estados
	private int numFilas, 					// numero de filas del tablero
				numColumnas;				// numero de columnas del tablero
	private Vector<Barco> barcos = null;	// vector dinamico de barcos
	private int numBarcos, 					// numero de barcos de la partida
	            quedan,  					// numero de barcos no hundidos
	            disparos; 					// numero de disparos efectuados
	
	/**
	 * Contructor por defecto. No hace nada
	 */
	public Partida() {}
	
	/**
	 * Constructor de una partida
	 * @param	nf	numero de filas del tablero
	 * @param   nf  numero de columnas del tablero
	 * @param   nc  numero de barcos
	 */
	public Partida(int nf, int nc, int nb) {
		this.numFilas = nf;
		this.numColumnas = nc;
		this.numBarcos = nb;
		this.quedan = nb;
		this.disparos = 0;
		iniciaMatriz(nf, nc); // Inicia toda la matriz a agua
		barcos = new Vector<Barco>();
		ponBarcos();		
	}
	
	/**
	 * Dispara sobre una casilla y devuelve el resultado
	 * @param	f	fila de la casilla
	 * @param   c   columna de la casilla
	 * @return		resultado de marcar la casilla: AGUA, ya TOCADO, ya HUNDIDO, identidad del barco recien hundido
	 */	
    public int pruebaCasilla(int f, int c) {
        // POR IMPLEMENTAR
    	
    	//posicion al clicar y lo que nos devuelve
    	
    	if (mar[f][c] == AGUA || mar[f][c] == TOCADO || mar[f][c] == HUNDIDO) {
    		return mar[f][c];
    	} 
    	
    	//Aqu� tratamos con los barcos
    	else {
    		
    		int posicionBarco = mar[f][c];
    		Barco barco = barcos.get(posicionBarco);
 			barco.tocaBarco();
			
 			
 			//si el tam coincide con tocadaos
			if(barco.getTamanyo() == barco.getTocadas()) {
				
				//con la info del barco 
				
				int tam = barco.getTamanyo();
				int col = barco.getColumnaInicial();
				int fila = barco.getFilaInicial();
				char orientacion = barco.getOrientacion();
				
				//Barco horizontal
				//en funcion del numero de casillas del barco lo marcaremos como hundido
				//desde la columna x a la fila y+1 (hasta hundirlo)
				if (orientacion == 'v') {
					for(int i=0; i<tam; i++) {
						mar[fila+1][col] = HUNDIDO;
					}
				}
				//Barco vertical
				//en funcion del numero de casillas del barco lo marcaremos como hundido
				//desde la fila x a la colunmna y+1 (hasta hundirlo)
				else {
					for(int i=0; i<tam; i++) {
						mar[fila][col+1] = HUNDIDO;
					}
				}
				quedan--; // restamos 1 a la cantidad
				//devolvemos la posicion
				return posicionBarco;
			
			
			} else {
				
				//Si tam barco no coincide el numero de veces tocado lo marcamos solamente como tocado
				mar[f][c] = TOCADO;
				return TOCADO;
			}
		}
    }
    

	/**
	 * Devuelve una cadena con los datos de un barco dado: filIni, colIni, orientacion, tamanyo
	 * Los datos se separan con el caracter especial '#'
	 * @param	idBarco	indice del barco en el vector de barcos
	 * @return	        cadena con los datos del barco
	 */	
	public String getBarco(int idBarco) {
        // POR IMPLEMENTAR
		//nos interesa los datos del barco, su "posicion"
		return barcos.get(idBarco).toString();
	}
	
	/**
	 * Devuelve un vector de cadenas con los datos de todos los barcos
	 * @return	vector de cadenas, una por barco con la informacion de getBarco
	 */	
	public String[] getSolucion() {
        // POR IMPLEMENTAR
		
		//vector donde meteremos los barcos para asignarles su posicion 
		String[] vectorBarcosPosicion = new String[barcos.size()];

        for(int i=0; i<barcos.size(); i++){
        
        	//en el string del vector le vamos metiendo su posicion a cada barco
        	//con getbarco sacamos esta informacion
        	vectorBarcosPosicion[i] = getBarco(i);
        }
        //Devolveremos el vector
		return vectorBarcosPosicion;
	}
    

	/********************************    METODOS PRIVADOS  ********************************************/
    
	/**
	 * Inicia todas las casillas del tablero a AGUA
	 */	
	private void iniciaMatriz(int nf, int nc) {
		this.mar = new int[nf][nc];
		for (int i = 0; i < numFilas; i++ ) {
			for (int j = 0; j < numColumnas; j++) {
				mar[i][j] = AGUA;
			}
		}
	}
	
	/**
	 * Coloca los barcos en el tablero
	 */	
	private void ponBarcos() {
		/* Por defecto colocamos un barco de tamaño 4, uno de tamaño 3, otro de tamaño 2 y tres barcos de tamaño 1 */
		barcos.add( ponBarco(0, 4) );
		barcos.add( ponBarco(1, 3) );
		barcos.add( ponBarco(2, 2) );
		barcos.add( ponBarco(3, 1) );
		barcos.add( ponBarco(4, 1) );
		barcos.add( ponBarco(5, 1) );
	}
	
	
	/**
	 * Busca hueco para un barco y lo coloca en el tablero.
	 * @param  id   indice del barco en el vector de barcos
	 * @param  tam  tamanyo del barco
	 * @return      un barco guardado como un objeto Barco
	 */	
	private Barco ponBarco(int id, int tam) {
        char orientacion=' ';
        boolean ok = false;
        int fila = 0, col = 0;
        Random random = new Random(); // Para generar aleatoriamente la orientacion y posicion de los barcos
        
        // Itera hasta que encuentra hueco para colocar el barco cumpliendo las restricciones
        while (!ok) {
        	// Primero genera aleatoriamente la orientacion del barco
            if (random.nextInt(2) == 0) { // Se dispone horizontalmente
            	// Ahora genera aleatoriamente la posicion del barco
                col = random.nextInt(numColumnas + 1 - tam); // resta tam para asegurar que cabe
                fila = random.nextInt(numFilas);

                // Comprueba si cabe a partir de la posicion generada con mar o borde alrededor
                if (librePosiciones(fila, col, tam+1, 'H')) {
                	// Coloca el barco en el mar
                    for (int i = 0; i < tam; i++) {
                        mar[fila][col+i] = id;
                    }
                    ok = true;
                    orientacion = 'H';
                }
            }
            else { //Se dispone verticalmente
                fila = random.nextInt(numFilas + 1 - tam);
                col = random.nextInt(numColumnas);
                if (librePosiciones(fila, col, tam+1, 'V')) {
                    for (int i = 0; i < tam; i++) {
                        mar[fila + i][col] = id;
                    }
                    ok = true;
                    orientacion = 'V';
                }
            } // end if H o V
        } // end while
        return new Barco(fila, col, orientacion, tam);
	}
	
	/**
	 * Comprueba si hay hueco para un barco a partir de una casilla inicial.
	 * Los barcos se colocan dejando una casilla de hueco con los otros.
	 * Pueden pegarse a los bordes.
	 * @param  fila   fila de la casilla inicial
	 * @param  col    columna de la casilla inicial
	 * @param  tam    tamanyo del barco + 1 para dejar hueco alrededor
	 * @param  ori    orientacion del barco: 'H' o 'V'
	 * @return        true si se encuentra hueco, false si no.
	 */	
    private boolean librePosiciones(int fila, int col, int tam, char ori) {
    	int i;
        if (ori == 'H') {
        	i = ( (col > 0) ? -1 : 0 );
        	// Comprueba que "cabe" horizontalmente a partir de la columna dada.
        	// Esto implica que:
        	// haya 'tam' casillas vacias (con mar) en la fila 'fila'
        	// y que quede rodeado por el mar o por un borde
        	while ( (col+i < numColumnas) && (i<tam) && (mar[fila][col+i] == AGUA) && 
        			( (fila == 0) || (mar[fila-1][col+i] == AGUA) )  &&
        			( (fila == numFilas-1) || (mar[fila+1][col+i] == AGUA) )         			
        		  ) i++;
        }
        else { // ori == 'V'
        	i = ( (fila > 0) ? -1 : 0 );
        	while ( (fila+i < numFilas) &&  (i<tam) && (mar[fila+i][col] == AGUA) &&
        			( (col == 0) || (mar[fila+i][col-1] == AGUA) )  &&
        			( (col == numColumnas-1) || (mar[fila+i][col+1] == AGUA) )  
        		  ) i++;
        }
        // Ha encontrado un hueco cuando ha generado el barco totalmente rodeado de agua o
        boolean resultado = (i == tam);
        // lo ha generado horizontal pegado al borde derecho o
        resultado = resultado || ( (ori == 'H') && (col+i == numColumnas) );
        // lo ha generado vertical pegado al borde inferior.
        resultado = resultado || ( (ori == 'V') && (fila+i == numFilas) );
        return resultado;
    }
    
    
} // end class Partida
