package com.laamware.ejercito.doc.web.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Objeto de validación. Permite enviar, ya sea como retorno de un método o como
 * valor de parámetro por referencia, el estado general de una validación y un
 * conjunto de mensajes que indiquen la situación validada.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/13/2018 (SICDI-Controltech Issue #156 feature-156)
 */
public class BusinessLogicValidation implements Serializable {

    /**
     * Objeto de error de validación.
     */
    public class ValidationError implements Serializable {

        private static final long serialVersionUID = -5009028214936375275L;

        private final Object objectValidated;
        private final String message;

        /**
         * Constructor.
         *
         * @param objectValidated Objeto de referencia de la validación
         * realizada.
         * @param message Mensaje de descripción del error.
         */
        public ValidationError(Object objectValidated, String message) {
            this.objectValidated = objectValidated;
            this.message = message;
        }

        public Object getObjectValidated() {
            return objectValidated;
        }

        public String getMessage() {
            return message;
        }

    }

    private static final long serialVersionUID = -4006163667304822282L;

    private final List<ValidationError> errors = new LinkedList<>();

    /**
     * Obtiene el indicador que define si todas las validaciones realizadas se
     * encuentran correctas o no.
     *
     * @return {@code true} si todas las validaciones son correctas (No hay
     * registros de error); de lo contrario, {@code false}.
     */
    public boolean isAllOK() {
        return errors.isEmpty();
    }

    /**
     * Obtiene el número de errores registrados en la validación.
     *
     * @return Número de errores.
     */
    public int getNumberOfErrors() {
        return errors.size();
    }

    /**
     * Agrega un error de validación.
     *
     * @param objectValidated Objeto de referencia de la validación realizada.
     * @param message Mensaje de descripción del error.
     */
    public void addError(final Object objectValidated, final String message) {
        errors.add(new ValidationError(objectValidated, message));
    }

    /**
     * Obtiene un error de validación.
     *
     * @param index Índice del error.
     * @return Instancia del error de validación correspondiente al índice.
     */
    public ValidationError getError(int index) {
        return errors.get(index);
    }

}
