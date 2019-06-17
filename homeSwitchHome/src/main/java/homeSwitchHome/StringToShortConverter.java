package homeSwitchHome; 

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class StringToShortConverter implements Converter<String, Short> {
    @Override
    public Result<Short> convertToModel(String fieldValue, ValueContext context) {
        // Produces a converted value or an error
        try {
            // ok is a static helper method that creates a Result
            return Result.ok(Short.valueOf(fieldValue));
        } catch (NumberFormatException e) {
            // error is a static helper method that creates a Result
            return Result.error("Please enter a number");
        }
    }

    @Override
    public String convertToPresentation(Short integer, ValueContext context) {
        // Converting to the field type should always succeed,
        // so there is no support for returning an error Result.
        return String.valueOf(integer);
    }
}