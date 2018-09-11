package Tiny;

import java.util.*;


import ast.*;

public class TablaSimbolos {
	private HashMap<String, RegistroSimbolo> tabla;
	private int direccion;  //Contador de las localidades de memoria asignadas a la tabla
	
	public TablaSimbolos() {
		super();
		tabla = new HashMap<String, RegistroSimbolo>();
		direccion=0;
	}

	public void cargarTabla(NodoBase raiz){
            
            while (raiz != null) {
                if (raiz instanceof NodoIdentificador){
                    InsertarSimbolo(((NodoIdentificador)raiz).getNombre(),-1);
                    //TODO: Añadir el numero de linea y localidad de memoria correcta
				}

				/* Hago el recorrido recursivo */
				else if(raiz instanceof NodoVector){
					cargarTabla(((NodoVector)raiz).getIdentificador());
					cargarTabla(((NodoVector)raiz).getExpresion());
				}else if (raiz instanceof  NodoIf){
                    cargarTabla(((NodoIf)raiz).getPrueba());
                    cargarTabla(((NodoIf)raiz).getParteThen());
                    if(((NodoIf)raiz).getParteElse()!=null){
                        cargarTabla(((NodoIf)raiz).getParteElse());
                    }
                }
                else if (raiz instanceof  NodoRepeat){
                    cargarTabla(((NodoRepeat)raiz).getCuerpo());
                    cargarTabla(((NodoRepeat)raiz).getPrueba());
                }
                else if (raiz instanceof  NodoAsignacion){
                    cargarTabla(((NodoAsignacion)raiz).getVariable());
                    cargarTabla(((NodoAsignacion)raiz).getExpresion());
                }
                else if (raiz instanceof  NodoEscribir)
                    cargarTabla(((NodoEscribir)raiz).getExpresion());
                else if (raiz instanceof  NodoLeer)
                    cargarTabla(((NodoLeer)raiz).getVariable());
                else if (raiz instanceof NodoOperacion){
                    cargarTabla(((NodoOperacion)raiz).getOpIzquierdo());
                    cargarTabla(((NodoOperacion)raiz).getOpDerecho());
                }else if (raiz instanceof NodoFuncion){
					cargarTabla(((NodoFuncion)raiz).getIdentificador());
					cargarTabla(((NodoFuncion)raiz).getArgumentos());
				}else if (raiz instanceof NodoListaArgs){
					cargarTabla(((NodoListaArgs)raiz).getVariable());
				}
                raiz = raiz.getHermanoDerecha();
            }
	}
	
	//true es nuevo no existe se insertara, false ya existe NO se vuelve a insertar 
	public boolean InsertarSimbolo(String identificador, int numLinea){
		RegistroSimbolo simbolo;
		if(tabla.containsKey(identificador)){
			return false;
		}else{
			simbolo= new RegistroSimbolo(identificador,numLinea,direccion++);
			tabla.put(identificador,simbolo);
			return true;			
		}
	}
	
	public RegistroSimbolo BuscarSimbolo(String identificador){
		RegistroSimbolo simbolo=(RegistroSimbolo)tabla.get(identificador);
		return simbolo;
	}
	
	public void ImprimirClaves(){
		System.out.println("*** Tabla de Simbolos ***");
                for (String s : tabla.keySet()) {
                    System.out.println("Consegui Key: "+s+" con direccion: " + BuscarSimbolo(s).getDireccionMemoria());
                }
	}

	public int getDireccion(String Clave){
		return BuscarSimbolo(Clave).getDireccionMemoria();
	}
	
	/*
	 * TODO:
	 * 1. Crear lista con las lineas de codigo donde la variable es usada.
	 * */
}
