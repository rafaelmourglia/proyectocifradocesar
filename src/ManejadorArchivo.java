import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
public class ManejadorArchivo {

    /**
     * Valida la ruta a un archivo comprobando si la ruta es válida, si la ruta existe y si
     * corresponde a un archivo, devolviendo un Path
     * @param ruta Ruta del archivo
     * @return Path especificando un archivo
     * @throws InvalidPathException
     */
    public static Path buscarRutaArchivo(String ruta) throws InvalidPathException{
        try{
            Path path = Paths.get(ruta);
            if(Files.exists(path) && Files.isRegularFile(path)){
                return path;
            }
            return null;
        }
        catch (InvalidPathException e){
            throw new InvalidPathException(e.getMessage(), "Error al especificar ruta al archivo o no es un archivo válido");
        }
    }

    /**
     * Cambia el nombre de archivo de un Path dado y retorna otro path con el nuevo nombre
     * @param path Path del archivo original
     * @param nuevoNombreArchivo Nuevo nombre del archivo
     * @return
     */
    public static Path cambiarNombreArchivo(Path path, String nuevoNombreArchivo){
        Path nuevoPath = path.getParent();
        return nuevoPath.resolve(nuevoNombreArchivo);
    }

}