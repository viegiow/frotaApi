package com.example.frota.errors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parametro = ex.getName();
//        String tipoEsperado = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido";

        ErrorResponse erro = new ErrorResponse(
            400,
            "Parâmetro inválido",
            "O parâmetro '" + parametro + "' é inválido."
        );

        return ResponseEntity.badRequest().body(erro);
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        ErrorResponse erro = new ErrorResponse(404, "Rota não encontrada", "O endpoint acessado não existe.");
        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse erro = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Recurso não encontrado",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensagem = "Erro de integridade de dados. Verifique se os campos obrigatórios estão preenchidos corretamente ou se não há duplicidades.";

        if (ex.getMostSpecificCause() != null) {
            String detalhe = ex.getMostSpecificCause().getMessage();
            if (detalhe != null) {
            	if (detalhe.toLowerCase().contains("unique")) 
            		{ mensagem = "Já existe um registro com esse valor. Verifique campos únicos (ex: e-mail, CPF, etc.)."; }
            	if (detalhe.toLowerCase().contains("foreign key")) 
                    { mensagem = "Não é possível excluir este registro, pois ele está vinculado a outros dados."; }
            	if (detalhe.toLowerCase().contains("duplicate entry")) 
                { mensagem = "Não é possível cadastrar a solicitação, pois já existe uma solicitação para esse produto"; }
            }
        }
        
        ErrorResponse erro = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Violação de integridade de dados",
            mensagem
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    @ExceptionHandler(ProdutoIncompativelCaixa.class)
    public ResponseEntity<String> handleRegra(ProdutoIncompativelCaixa e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegal(IllegalArgumentException e){
    	return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(EntregaJaRealizada.class)
    public ResponseEntity<String> handleIllegal(EntregaJaRealizada e){
    	return ResponseEntity.badRequest().body(e.getMessage());
    }

}

