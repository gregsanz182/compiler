package Tiny;

import java.util.*;


import ast.*;

public class TablaSimbolos {

    private ArrayList<HashMap<String, RegistroSimbolo>> secciones;
    private HashMap<String, RegistroSimbolo> tabla;
    int direccion;
    
    public TablaSimbolos() {
        super();
        this.direccion = 0;
        this.secciones = new ArrayList<HashMap<String, RegistroSimbolo>>();
    }

    public void cargarTabla(NodoBase raiz, int bloque) throws IdNotFoundException{
            while (raiz != null) {
                if (raiz instanceof NodoIdentificador){
                    //Si el identificador no ha sido declarado. Se lanza un error.
                    if(BuscarSimbolo(((NodoIdentificador)raiz).getNombre(),bloque) == null){
                        throw new IdNotFoundException("El identificador '"+((NodoIdentificador)raiz).getNombre()+"' no ha sido declarado.");
                    }
                }

                /* Hago el recorrido recursivo */
                else if(raiz instanceof NodoVector){
                    cargarTabla(((NodoVector)raiz).getIdentificador(), bloque);
                    cargarTabla(((NodoVector)raiz).getExpresion(), bloque);
                }
                
                else if (raiz instanceof  NodoIf){
                    cargarTabla(((NodoIf)raiz).getPrueba(), bloque);
                    cargarTabla(((NodoIf)raiz).getParteThen(), bloque);
                    cargarTabla(((NodoIf)raiz).getParteElse(), bloque);
                }
                
                else if (raiz instanceof  NodoRepeat){
                    cargarTabla(((NodoRepeat)raiz).getCuerpo(), bloque);
                    cargarTabla(((NodoRepeat)raiz).getPrueba(), bloque);
                }
                
                else if (raiz instanceof  NodoAsignacion){
                    if (((NodoAsignacion) raiz).getVariable() instanceof NodoIdentificador) {
                        InsertarSimbolo(((NodoIdentificador) ((NodoAsignacion) raiz).getVariable()).getNombre(),bloque);
                    }else{
                        cargarTabla(((NodoAsignacion) raiz).getVariable(), bloque);
                    }
                    cargarTabla(((NodoAsignacion)raiz).getExpresion(), bloque);
                }
                
                else if (raiz instanceof  NodoEscribir)
                    cargarTabla(((NodoEscribir)raiz).getExpresion(), bloque);
                
                    else if (raiz instanceof  NodoLeer)
                    cargarTabla(((NodoLeer)raiz).getVariable(), bloque);
                
                    else if (raiz instanceof NodoOperacion){
                    cargarTabla(((NodoOperacion)raiz).getOpIzquierdo(), bloque);
                    cargarTabla(((NodoOperacion)raiz).getOpDerecho(), bloque);
                }
                
                else if (raiz instanceof NodoFuncion){
                    if(((NodoFuncion) raiz).getCuerpo() != null){
                        InsertarSimbolo(((NodoIdentificador)((NodoFuncion) raiz).getIdentificador()).getNombre(), bloque);
                        cargarTabla(((NodoFuncion) raiz).getArgumentos(), ((NodoFuncion) raiz).getNroBloque());
                        cargarTabla(((NodoFuncion) raiz).getCuerpo(), ((NodoFuncion) raiz).getNroBloque());
                        //cargarTabla(((NodoFuncion) raiz).getRetorno(), bloque + 1);
                    }else{
                        cargarTabla(((NodoFuncion) raiz).getArgumentos(), bloque);
                    }
                }
                
                else if(raiz instanceof NodoArgumento){
                    InsertarSimbolo(((NodoIdentificador) ((NodoArgumento) raiz).getIdentificador()).getNombre(), bloque);
                }
                
                raiz = raiz.getHermanoDerecha();
            }
    }
    
    //true es nuevo no existe se insertara, false ya existe NO se vuelve a insertar 
    public boolean InsertarSimbolo(String identificador, int bloque){
        HashMap<String, RegistroSimbolo> seccion;
        try {
            //si la seccion existe
            seccion = secciones.get(bloque);
        } catch (IndexOutOfBoundsException e) {
            seccion = new HashMap<String, RegistroSimbolo>();
            secciones.add(seccion);
        }
        return InsertarSimboloEnSeccion(identificador, seccion);
    }

    // true es nuevo no existe se insertara, false ya existe NO se vuelve a insertar
    private boolean InsertarSimboloEnSeccion(String identificador, HashMap<String, RegistroSimbolo> seccion){
        RegistroSimbolo simbolo;
        if (seccion.containsKey(identificador)) {
            return false;
        } else {
            simbolo = new RegistroSimbolo(identificador, direccion++);
            seccion.put(identificador, simbolo);
            return true;
        }
    }
    
    public RegistroSimbolo BuscarSimbolo(String identificador, int bloque){
        HashMap<String, RegistroSimbolo> seccion;
        try {
            // si la seccion existe
            seccion = secciones.get(bloque);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return seccion.get(identificador);
    }
    
    public void ImprimirTabla(){
        System.out.println("*** Tabla de Simbolos ***");
        for(int i = 0; i<secciones.size(); i++){
            for (String s : secciones.get(i).keySet()) {
                System.out.println("Bloque: " + i + " Nombre: "+ s + " Con direccion: " + BuscarSimbolo(s,i).getDireccionMemoria());
            }
        }
                
    }

    public int getDireccion(String Clave, int bloque){
        return BuscarSimbolo(Clave,bloque).getDireccionMemoria();
    }
    
    /*
     * TODO:
     * 1. Crear lista con las lineas de codigo donde la variable es usada.
     * */
}
