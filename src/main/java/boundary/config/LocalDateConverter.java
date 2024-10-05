package boundary.config;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@FacesConverter(value = "localDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {

    /**
     * Converts a String representation of a date to a LocalDate object using the ISO_LOCAL_DATE format.
     *
     * @param context the FacesContext for the current request
     * @param component the UIComponent that is associated with the date value
     * @param value the date value as a String to be converted to a LocalDate object
     * @return a LocalDate object representing the input date value, or null if the input value is null or empty
     */
    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Converts a LocalDate object to a formatted String representation.
     *
     * @param context The FacesContext for the request being processed.
     * @param component The UIComponent whose value is being converted.
     * @param value The LocalDate object to be converted to a formatted String.
     * @return A formatted String representation of the LocalDate object, or an empty String if the value is null.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
        if (value == null) {
            return "";
        }
        return value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }
}