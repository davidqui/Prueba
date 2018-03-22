package com.laamware.ejercito.doc.web;

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

    private static final String claveEncriptacion = "password";

    /**
     * Opcioń "p": Texto a encriptar.
     */
    private static final String C_OPTION = "c";

    /**
     * Opcioń "p": Clave a utilizar en .
     */
    private static final String P_OPTION = "p";
    
    /**
     * Opcioń "llaveCifrado": LLave para descifrar propiedades del archivo de propiedades.
     */
    public static final String KEY_OPTION = "llaveCifrado";
    
    /**
     * "jasypt.encryptor.password": LLave para descifrar propiedades del
     * archivo de propiedades.
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

        String texto = commandLine.getOptionValue(P_OPTION);
        String clave = commandLine.getOptionValue(C_OPTION);
        StringEncryptor stringEncryptor = stringEncryptor(clave);
        String encrypted = stringEncryptor.encrypt(texto);
        System.out.println("ORIGINAL: " + texto);
        System.out.println("ENCRYPTED: " + encrypted);
    }

    /**
     * Construye las opciones de la aplicación.
     *
     * @return Conjunto de opciones.
     */
    private Options buildOptions() {
        Options options = new Options();
        options.addRequiredOption(P_OPTION, null, true, "Texto a encriptar.");
        options.addOption(C_OPTION, null, true, "Clave para encriptación.");
        return options;
    }

    /**
     * Construye la clase StringEncryptor con los atributos por default.
     *
     * @return Configuracion del encriptador.
     */
    static private StringEncryptor stringEncryptor(String clave) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword((clave == null || clave.trim().length() == 0) ? claveEncriptacion : clave);
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
     * @param args Argumentos.
     */
    @SuppressWarnings("UseSpecificCatch")
    public void execEnvironmentKey(String[] args) {
        final Options options = buildOptionKey();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (MissingOptionException ex) {
            HELP_FORMATTER.printHelp("java -jar doc-web.jar", options, true);
            return;
        } catch (ParseException ex) {
            HELP_FORMATTER.printHelp("java -jar doc-web.jar", options, true);
            return;
        }

        String texto = commandLine.getOptionValue(KEY_OPTION);
        System.setProperty(KEY_SYSTEM, texto);
    }
    
    /**
     * Construye las opciones de la aplicación para setear variable de entorno.
     *
     * @return Conjunto de opciones.
     */
    private Options buildOptionKey() {
        Options options = new Options();
        options.addRequiredOption(KEY_OPTION, null, true, "LLave a utilizar en el sistema.");
        return options;
    }
}
