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
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * Clase para el proceso de encriptación.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Mar 21, 2018
 * @version 1.0.0 (feature-154).
 */
public class JasyptEncrypt {

    private static final String CLAVE_ENCRIPTACION = "clave";

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
                StringEncryptor stringEncryptor = stringEncryptor();
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
        } else {
            System.err.println("Para encriptar, agregue el parametro -c cifrado");
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
     * Construye la clase StringEncryptor con los atributos por default.
     *
     * @return Configuracion del encriptador.
     */
    private StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(CLAVE_ENCRIPTACION);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

    /**
     * Método de ejecución para setear la variable en el sistema.
     *
     */
    public static void setVariableAmbiente() {
        System.setProperty(KEY_SYSTEM, CLAVE_ENCRIPTACION);
    }
}
