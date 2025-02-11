import java.util.Scanner;

public class Main {
    /**
     * El método main sólo maneja el menú de opciones. La lógica de cada opción
     * se deja a cargo de la clase PantallaEncriptar.
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try{
            int opcion;
        do {
            System.out.println("Menú de opciones:\n" +
                    "\t1. Encriptar\n" +
                    "\t2. Desencriptar (Forzar)\n" +
                    "\t0. Salir\n" +
                    "Ingresar una opción: ");

            while (!scanner.hasNextInt()){
                System.out.println("Debe ingresar un número.");
            }
            opcion = scanner.nextInt();
            switch (opcion){
                case 1:
                    PantallaEncriptar enc = new PantallaEncriptar(scanner);
                    enc.iniciarEncriptado();
                    break;
                case 2:
                    PantallaEncriptar dec = new PantallaEncriptar(scanner);
                    dec.iniciarDesencriptado();
                    break;
                case 0:
                    System.out.println("Cerrando programa...\nHasta la próxima!");
                    scanner.close();
                    break;
                default:
                    System.out.println("Opción incorrecta");
            }
            System.out.println("\n");
    }while (opcion !=0);
       }catch (Exception e){

        }


    }
}