package com.laamware.ejercito.doc.web;

import java.io.Console;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jasypt.encryption.StringEncryptor;

/**
 * Clase para el proceso de encriptación.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Mar 21, 2018
 * @version 1.0.0 (feature-154).
 */
public class JasyptEncrypt {

    /*
    * Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files.
    * Se reemplazan los archivos: local_policy.jar y US_export_policy.jar, para que
    * genere cifrado mas fuerte.
    */
    public static final String CLAVE_ENCRIPTACION = "QA[H]%S0SwNE$hWpcZ:h,P_#/J[V8?w?FRACaxe9VAVx8?7Sy0<R'lZj{^}'AM^";

    /**
     * Opcioń "p": Texto a encriptar.
     */
    private static final String C_OPTION = "c";

    /**
     * "jasypt.encryptor.password": Nombre del atributo de la lLave para
     * descifrar propiedades del archivo de propiedades.
     */
    public static final String KEY_SYSTEM = "jasypt.encryptor.password";

    private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();

    /**
     * Método de ejecución.
     *
     * @param args Argumentos.
     */
    @SuppressWarnings("UseSpecificCatch")
    public void exec(String[] args) {
        final Options options = buildOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (MissingOptionException ex) {
            System.err.println(ex);
            HELP_FORMATTER.printHelp("java -jar doc-web.jar", options, true);
            return;
        } catch (ParseException ex) {
            System.err.println(ex);
            HELP_FORMATTER.printHelp("java -jar doc-web.jar", options, true);
            return;
        }
        String texto = commandLine.getOptionValue(C_OPTION);

        if (texto.equals("cifrado")) {
            Boolean cifrar = Boolean.FALSE;
            do {
                Console console = System.console();
                console.printf("Por favor digite la contaseña: ");
                char[] passwordChars = console.readPassword();
                String passwordString = new String(passwordChars);
                StringEncryptor stringEncryptor = App.stringEncryptor();
                String encrypted = stringEncryptor.encrypt(passwordString);
                System.out.println("Contraseña encriptada: " + encrypted);
                Boolean valido;
                do {
                    console.printf("Desea encriptar otra contaseña(S/N)");
                    String opcion = console.readLine();
                    valido = opcion.equalsIgnoreCase("n") || opcion.equalsIgnoreCase("no") || opcion.equalsIgnoreCase("s") || opcion.equalsIgnoreCase("si");
                    cifrar = opcion.equalsIgnoreCase("s") || opcion.equalsIgnoreCase("si");
                } while (!valido);
            } while (cifrar);
        }else {
            System.err.println("Para desencriptar, agregue el parametro -c descifrado");
        }
    }

    /**
     * Construye las opciones de la aplicación.
     *
     * @return Conjunto de opciones.
     */
    private Options buildOptions() {
        Options options = new Options();
        options.addRequiredOption(C_OPTION, null, true, "Digite por favor: cifrado.");
        return options;
    }

 
    /**
     * Método de ejecución para setear la variable en el sistema.
     *
     */
    public static void setVariableAmbiente() {
        System.setProperty(KEY_SYSTEM, CLAVE_ENCRIPTACION);
    }
}
