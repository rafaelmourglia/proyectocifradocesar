import java.util.NoSuchElementException;
import java.util.Scanner;

public class PantallaEncriptar {
    private final Scanner scanner;

    public  PantallaEncriptar(Scanner scanner){
        this.scanner = scanner;
    }
      public void iniciarEncriptado(){
        try{
            System.out.println("Ingrese una ruta válida: ");
            scanner.nextLine();
            String ruta = scanner.nextLine();
            Cifrado cifrado = new Cifrado();
            cifrado.setPathArchivoOrigen(ManejadorArchivo.buscarRutaArchivo(ruta));

            //cifrado.encriptar();
            System.out.println("\nClave de encriptado: " + cifrado.getKey());
            cifrado.encriptarArchivo();
        }
        catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }

    }

    public void iniciarDesencriptado(){
//        System.out.println("Ruta del ");
//        System.out.println("Clave para desencriptar: ");
//        Cifrado cifrado = new Cifrado(scanner.nextInt());
//        cifrado.desencriptarArchivo();

        try{
            System.out.println("Ingrese una ruta válida del archivo a desencriptar: ");
            scanner.nextLine();
            String ruta = scanner.nextLine();

//            System.out.println("Clave para desencriptar: ");
            Cifrado cifrado = new Cifrado(0);
            cifrado.setPathArchivoOrigen(ManejadorArchivo.buscarRutaArchivo(ruta));
            System.out.println("\nDesencriptando: ");
            System.out.println("\nArchivo desencriptado: " + cifrado.desencriptarFuerzaBruta());
        }
        catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

}
