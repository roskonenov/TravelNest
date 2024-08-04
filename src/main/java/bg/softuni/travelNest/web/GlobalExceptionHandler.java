package bg.softuni.travelNest.web;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public String objectNotFoundHandler(Model model, ObjectNotFoundException onfe){
        model.addAttribute("message", onfe.getMessage());
        return "object_not_found";
    }
}
