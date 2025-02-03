import java.util.NoSuchElementException;
import java.util.Scanner;

public class PantallaEncriptar {
    private Scanner scanner;

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
            cifrado.encriptarFileChanel();
        }
        catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }

    }

    public void iniciarDesencriptado(Scanner scanner){
        System.out.println("Clave para desencriptar: ");
        Cifrado cifrado = new Cifrado(scanner.nextInt());
        cifrado.decryptFileChanel();
    }

}
