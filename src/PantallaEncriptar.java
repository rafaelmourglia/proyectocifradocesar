/**
 * Esta clase se encarga de interactuar con el usuario sólo en lo que se relaciona con la
 * encriptación y desencriptación.
 */

import java.util.NoSuchElementException;
import java.util.Scanner;

public class PantallaEncriptar {
    private final Scanner scanner;

    public  PantallaEncriptar(Scanner scanner){
        this.scanner = scanner;
    }

    /**
     * Inicia el proceso de encriptado pidiendo al usuario una ruta válida y mostrando la clave de
     * encriptación.
     */
    public void iniciarEncriptado(){
        try{
            System.out.println("Ingrese una ruta válida: ");
            scanner.nextLine();
            String ruta = scanner.nextLine();
            Cifrado cifrado = new Cifrado();
            cifrado.setPathArchivoOrigen(ManejadorArchivo.buscarRutaArchivo(ruta));

            cifrado.encriptarArchivo();
            System.out.println("Archivo encriptado en: " + cifrado.getPathArtchivoDestino());
            System.out.println("Clave de encriptado: " + cifrado.getKey());
        }
        catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Inicia el proceso de desencriptado pidiendo al usuario una ruta válida de un archivo
     * encriptado, mostrando como resultado, el contenido del archivo desencriptado y la clave
     * que fue utilizada.
     */
    public void iniciarDesencriptado(){
        try{
            System.out.println("Ingrese una ruta válida del archivo a desencriptar: ");
            scanner.nextLine();
            String ruta = scanner.nextLine();

            Cifrado cifrado = new Cifrado();
            cifrado.setPathArchivoOrigen(ManejadorArchivo.buscarRutaArchivo(ruta));
            System.out.println("\nDesencriptando...");
            System.out.println("Archivo desencriptado");
            System.out.println("Contenido del archivo:\n" + cifrado.desencriptarFuerzaBruta());
            System.out.println("Clave: " + cifrado.getKey());
        }
        catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

}
