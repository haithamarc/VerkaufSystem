package boundary;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("DiscountValidator")
public class DiscountValidator implements Validator {

    /**
     * Validates the input value to ensure that it is a positive number.
     * If the input value is not a number or a negative number, a ValidatorException is thrown with an appropriate error message.
     *
     * @param context The FacesContext for the request being processed.
     * @param component The UIComponent whose value is being validated.
     * @param value The input value to be validated.
     * @throws ValidatorException If the input value is not a positive number.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String v =(String)value;
        try {
            Double.parseDouble(v);
            double  number = Double.parseDouble(v);
            if (number < 0.0) {
                FacesMessage message = new FacesMessage("Input must be a equal or bigger than 0.");
                throw new ValidatorException(message);
            } else if (number > 1.0) {
                FacesMessage message = new FacesMessage("Input must be a equal or smaller than 1.");
                throw new ValidatorException(message);
            }
        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage("Input must be a  number.");
            throw new ValidatorException(message);
        }
    }
}
