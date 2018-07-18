package com.laamware.ejercito.doc.web.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        /*
         * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Nuevo campo para identificar el atributo validado.
         */
        private final String attribute;

        private final String message;

        /**
         * Constructor.
         *
         * @param objectValidated Objeto de referencia de la validación
         * realizada.
         * @param attribute Nombre del atributo validado.
         * @param message Mensaje de descripción del error.
         */
        /*
         * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Nuevo campo para identificar el atributo validado.
         */
        public ValidationError(Object objectValidated, String attribute, String message) {
            this.objectValidated = objectValidated;
            this.attribute = attribute;
            this.message = message;
        }

        public Object getObjectValidated() {
            return objectValidated;
        }

        public String getAttribute() {
            return attribute;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + Objects.hashCode(this.objectValidated);
            hash = 37 * hash + Objects.hashCode(this.attribute);
            hash = 37 * hash + Objects.hashCode(this.message);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ValidationError other = (ValidationError) obj;
            if (!Objects.equals(this.attribute, other.attribute)) {
                return false;
            }
            if (!Objects.equals(this.message, other.message)) {
                return false;
            }
            return Objects.equals(this.objectValidated, other.objectValidated);
        }

        @Override
        public String toString() {
            return "{" + "objectValidated=" + objectValidated + ", attribute=" + attribute + ", message=" + message + '}';
        }

    }

    private static final long serialVersionUID = -4006163667304822282L;

    /*
     * 2018-05-31 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    private static final String ERRORS_TO_STRING_SEPARATOR = " - ";

    private final List<ValidationError> errors = new LinkedList<>();
    /**
     * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Mapa para la referenciación de atributos.
     */
    private final Map<String, ValidationError> errorsMap = new LinkedHashMap<>();

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
     * @param attribute Nombre del atributo validado.
     * @param message Mensaje de descripción del error.
     */
    /*
     * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Nuevo campo para identificar el atributo validado.
     */
    public void addError(final Object objectValidated, final String attribute, final String message) {
        final ValidationError validationError = new ValidationError(objectValidated, attribute, message);

        if (attribute == null) {
            errors.add(validationError);
            return;
        }

        final ValidationError current = getError(attribute);
        if (current == null) {
            errors.remove(current);
        }

        errors.add(validationError);
        errorsMap.put(attribute, validationError);
    }

    /**
     * Obtiene un error de validación.
     *
     * @param index Índice del error.
     * @return Instancia del error de validación correspondiente al índice.
     */
    public ValidationError getError(final int index) {
        return errors.get(index);
    }

    /**
     * Obtiene un error de validación correspondiente al atributo.
     *
     * @param attribute Atributo. No puede ser {@code null}.
     * @return Instancia del error de validación correspondiente al atributo.
     */
    /*
     * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Uso de mapa de errores por atributo.
     */
    public ValidationError getError(final String attribute) {
        return errorsMap.get(attribute);
    }

    /**
     * Indica si contiene un error para el atributo.
     *
     * @param attribute Atributo. No puede ser {@code null}.
     * @return {@code true} si contiene un error para el atributo; de lo
     * contrario, {@code false}.
     */
    public boolean containsError(final String attribute) {
        return errorsMap.containsKey(attribute);
    }

    /**
     * Imprime en una cadena de caracteres la lista de errores de la validación.
     *
     * @return Cadena de caracteres enlazando la lista de errores.
     */
    /*
     * 2018-05-31 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public String errorsToString() {
        final StringBuilder builder = new StringBuilder();

        for (int index = 0; index < getNumberOfErrors(); index++) {
            final String message = getError(index).getMessage();
            builder.append(message);

            if (index < (getNumberOfErrors() - 1)) {
                builder.append(ERRORS_TO_STRING_SEPARATOR);
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return "BusinessLogicValidation{" + "errors=" + errors + ", errorsMap=" + errorsMap + '}';
    }

}
