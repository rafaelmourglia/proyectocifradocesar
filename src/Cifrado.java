import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class Cifrado {
    private final String ALPHABET = "abcdefghijklmnñopqrstuvwxyzáéíóúABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚ ,.;:¿¡?!$=";
    private Path pathArchivoOrigen;
    private Path pathArtchivoDestino;
    private int key;

    public Cifrado(){
        generarClave();
    }

    public Cifrado(int key){
        this.key = key;
    }
    public Path getPathArchivoOrigen() {
        return pathArchivoOrigen;
    }

    public void setPathArchivoOrigen(Path pathArchivoOrigen) {
        this.pathArchivoOrigen = pathArchivoOrigen;
    }

    public Path getPathArtchivoDestino() {
        return pathArtchivoDestino;
    }

    public void setPathArtchivoDestino(Path pathArtchivoDestino) {
        this.pathArtchivoDestino = pathArtchivoDestino;
    }



    public int getKey(){
        return this.key;
    }

    public int getMaxChars(){
        return ALPHABET.length();
    }


    /**
     * Retorna un String donde para cada caracter del argumento text que corresponde a un
     * índice i del alfabeto, devuelve el caracter que se encuentra en el índice i+shift.
     * @return Texto encriptado aplicando cifrado César con clave shift
     */
    public StringBuilder encriptar(String linea){
        //generarClave();

        try {
            StringBuilder lineaEncriptada = new StringBuilder();
            //lineas.forEach(linea -> {
                int iFrom; //Especifica el índice desde donde se obtendrá el caracter cifrado
                for (char c : linea.toCharArray()) {
                    //Si el caracter no existe en el alfabeto, se ignora y se agrega a la salida
                    //sin encriptar.
                    if (ALPHABET.indexOf(c) == -1) {
                        //System.out.print(c);
                        lineaEncriptada.append(c);
                    } else {
                        //Se obtiene el índice que corresponde al caracter actual y se le suma key para
                        //sustituir por el que se encuentra key posiciones a la izquierda del alfabeto.
                        iFrom = ALPHABET.indexOf(c) + this.key;

                        //Si el índice
                        if (iFrom > getMaxChars() - 1) {
                            iFrom = iFrom - getMaxChars();
                        }
                        //System.out.print(ALPHABET.charAt(iFrom));
                        lineaEncriptada.append(ALPHABET.charAt(iFrom));
                    }
                }
            //});
            return lineaEncriptada;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public void encriptarFileChanel(){
        try(FileChannel inputChannel = FileChannel.open(this.pathArchivoOrigen);
            FileChannel outChannel = FileChannel.open(Paths.get("c:\\test\\salida.txt"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder content;
            while (inputChannel.read(buffer) > 0){
                buffer.flip();
                String data = new String(buffer.array(), buffer.position(), buffer.remaining(), Charset.forName("UTF-8"));
                //content.append(data);
                content = encriptar(data);


                outChannel.write(ByteBuffer.wrap(content.toString().getBytes()));

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

    public String decrypt(String enc_text, int shift){
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

    public void decryptFileChanel(){
        try(FileChannel inputChannel = FileChannel.open(Paths.get("c:\\test\\salida.txt"))) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            while (inputChannel.read(buffer) > 0){
                buffer.flip();
                String data = new String(buffer.array(), buffer.position(), buffer.remaining(), Charset.forName("UTF-8"));
                //content.append(data);
                content.append(decrypt(data, this.key));


                //outChannel.write(ByteBuffer.wrap(content.toString().getBytes()));

                buffer.clear();
            }
            System.out.println(content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String forzar(String text, int limit){
        String out = "";
        for(int i=1; i<= limit; i++){
            out += decrypt(text, i);
            out += "\t| Shift: " + i +"\n";
        }
        return out;
    }
}
