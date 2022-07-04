package com.example.statussvc.controller.handler;

import com.example.statussvc.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Global Rest Controllers exception handler.
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(Constants.ROOT_PACKAGE + ".controller")
public class GlobalExceptionHandler {

}
