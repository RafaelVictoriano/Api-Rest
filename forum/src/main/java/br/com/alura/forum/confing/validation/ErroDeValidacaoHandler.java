package br.com.alura.forum.confing.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource; //Pega a mensagem do erro

    @ResponseStatus(code = HttpStatus.BAD_REQUEST) //Faz retornar o codigo 400
    @ExceptionHandler(MethodArgumentNotValidException.class) //o metodo fz qualquer exception de validação cair aqui
    public List<ErrroFormDto> handle(MethodArgumentNotValidException exception){

        List<ErrroFormDto> dto = new ArrayList<>();

        List<FieldError> fieldError = exception.getBindingResult().getFieldErrors(); //Pega o campo do erro
        fieldError.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale()); //Pega a mensagem e seu idioma
            ErrroFormDto erro = new ErrroFormDto(e.getField(), message); //Passa o campo e erro o dto
            dto.add(erro); // adiciona o erro a lista
        });

        return dto;

    }

}
