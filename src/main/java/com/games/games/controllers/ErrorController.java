package com.games.games.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
Controlador especial para capturar excepciones y mostrar una pantalla de error

Centraliza la gestión de errores, para que sea más simple y no tener que repetirla en todos los controladores
 */
@ControllerAdvice
public class ErrorController {

    /*
    Metodo que captura cualquier excepción que se lance y no sea capturada con try catch desde otro sitio
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("mensaje", e.getMessage());
        return "error";
    }
}