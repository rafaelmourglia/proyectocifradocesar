import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Cifrado {
    private final String ALPHABET = "abcdefghijklmnñopqrstuvwxyzáéíóúABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚ ,.;:¿¡?!$=";
    private final String NOMBRE_ARCHIVO_DESTINO = "encriptado.txt";
    private Path pathArchivoOrigen;
    private Path pathArtchivoDestino;

    //Le clave se obtiene siempre que se utiliza un método de encriptado o desencriptado
    private int key;

    public Path getPathArchivoOrigen() {
        return pathArchivoOrigen;
    }

    public void setPathArchivoOrigen(Path pathArchivoOrigen) {
        this.pathArchivoOrigen = pathArchivoOrigen;
        //El archivo de destino se designa automaticamente a partir del archivo de origen. De esto
        //se encarga el método cambiarNombreArchivo
        this.pathArtchivoDestino = ManejadorArchivo.cambiarNombreArchivo(this.pathArchivoOrigen, NOMBRE_ARCHIVO_DESTINO);
    }

    public Path getPathArtchivoDestino() {
        return pathArtchivoDestino;
    }



    //key sólo puede ser leído cuando se llama fuerna del objeto Cifrado
    public int getKey(){
        return this.key;
    }

    public int getMaxChars(){
        return ALPHABET.length();
    }


    /**
     * Para cada caracter del parámetro línea que corresponde a un índice i del alfabeto,
     * devuelve el caracter que se encuentra en el índice i + key.
     * @return línea encriptada utilizando la clave key en un
     */
    private String encriptar(String linea){
         try {
            StringBuilder lineaEncriptada = new StringBuilder();

                int iFrom; //Especifica el índice desde donde se obtendrá el caracter cifrado
                for (char c : linea.toCharArray()) {
                    //Si el caracter no existe en el alfabeto, se ignora y se agrega a la salida
                    //sin encriptar.
                    if (ALPHABET.indexOf(c) == -1) {
                        lineaEncriptada.append(c);
                    } else {
                        //Se obtiene el índice que corresponde al caracter actual y se le suma key para
                        //sustituir por el que se encuentra key posiciones a la izquierda del alfabeto.
                        iFrom = ALPHABET.indexOf(c) + this.key;

                        //Si el índice
                        if (iFrom > getMaxChars() - 1) {
                            iFrom = iFrom - getMaxChars();
                        }
                        lineaEncriptada.append(ALPHABET.charAt(iFrom));
                    }
                }
            return lineaEncriptada.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public void encriptarArchivo(){
        this.generarClave();
        try(FileChannel inputChannel = FileChannel.open(this.pathArchivoOrigen);
            FileChannel outChannel = FileChannel.open(this.pathArtchivoDestino, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String contenido;
            while (inputChannel.read(buffer) > 0){
                buffer.flip();
                //Al método encriptar se le pasa como parámetro un objeto String que contiene lo leído en el buffer.
                //Para no generar "basura" se pasa exactamente la cantidad de caracteres leídos (remaining) donde
                //por defecto es 1024, pero puede no ser así en caso de que se trate de la última línea leída del
                //archivo o que el archivo contencia menos de 1024 caracteres.
                contenido = encriptar(new String(buffer.array(), buffer.position(), buffer.remaining(), Charset.forName("UTF-8")));
                outChannel.write(ByteBuffer.wrap(contenido.toString().getBytes()));

                buffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void generarClave(){
        Random random = new Random();
        //Se genera un número pseudoaleatorio entre 0 y la cantidad máxima de caracteres del alfabeto
        // menos 1, teniendo en cuenta que luego se sumará 1 para no tener en cuenta el cero.
        int rnd = random.nextInt(getMaxChars()-1);
        //Se suma 1 para no tener en cuenta el 0
        this.key = rnd+1;
    }

    public String desencriptar(String enc_text, int shift){
        String out = "";
        int iFrom;
        int key = shift % getMaxChars();
        for(char c : enc_text.toCharArray()){
            if(ALPHABET.indexOf(c) == -1){
                out += c;
            }else{
                iFrom = ALPHABET.indexOf(c) - key;
                if(iFrom < 0){
                    iFrom = iFrom + getMaxChars();
                }
                out += ALPHABET.charAt(iFrom);

            }

        }
        return out;
    }

    public StringBuilder desencriptarArchivo(int key){
        this.key = key;
        try(FileChannel inputChannel = FileChannel.open(this.pathArchivoOrigen)) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder contenido = new StringBuilder();
            while (inputChannel.read(buffer) > 0){
                buffer.flip();
                String data = new String(buffer.array(), buffer.position(), buffer.remaining(), Charset.forName("UTF-8"));
                contenido.append(desencriptar(data, key));
                buffer.clear();
            }
            return contenido;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private HashSet diccionario(){
        HashSet diccionario = new HashSet<>(Arrays.asList(
                "el", "la", "de", "que", "y", "en", "a", "los", "se", "del", "las", "por",
                "un", "para", "con", "no", "una", "su", "al", "es", "lo", "como", "más",
                "pero", "sus", "le", "ya", "o", "fue", "este", "ha", "sí", "porque",
                "esta", "entre", "cuando", "muy", "sin", "sobre", "también", "me",
                "hasta", "hay", "donde", "quien", "desde", "todo", "nos", "durante",
                "todos", "uno", "les", "ni", "contra", "otros", "ese", "eso", "ante",
                "ellos", "esto", "mí", "antes", "algunos", "qué", "unos", "yo", "otro",
                "otras", "otra", "él", "tanto", "esa", "estos", "tan", "luego", "cerca",
                "ahí", "tiempo", "aún", "manera", "cada", "siempre", "solo", "palabras",
                "nunca", "día", "ahora", "así", "después", "vida", "parte", "mundo",
                "casa", "cosas", "lugar", "año", "caso", "gran", "poco", "agua",
                "nuevo", "mujer", "hombre", "gente", "noche", "país", "trabajo",
                "ciudad", "grupo", "historia", "cuenta", "medio", "ejemplo", "amigo",
                "escuela", "forma", "familia", "número", "fuerza", "sistema", "niño", "niña",
                "madre", "padre", "centro", "equipo", "tarde", "amor", "gobierno",
                "programa", "sociedad", "perro", "gato", "tarde"
        ));
        return diccionario;

    }

    public StringBuilder desencriptarFuerzaBruta(){
        StringBuilder sb;
        int cantCoincidencias = 0, coincidencias, iProbable=0;
        HashSet dic = this.diccionario();

        for (int i = 1; i < this.getMaxChars(); i++) {
            sb = desencriptarArchivo(i);
            coincidencias = buscarPalabrasDiccionario(sb.toString().toLowerCase().split(" "));
            if(coincidencias > cantCoincidencias){
                cantCoincidencias = coincidencias;
                iProbable = i;
            }
        }
        return desencriptarArchivo(iProbable);
    }

    public int buscarPalabrasDiccionario(String[] palabras){
        int coincidencias = 0;
        for(String palabra : palabras){
            coincidencias ++;
        }
        return coincidencias;
    }

    public String forzar(String text, int limit){
        String out = "";
        for(int i=1; i<= limit; i++){
            out += desencriptar(text, i);
            out += "\t| Shift: " + i +"\n";
        }
        return out;
    }
}
