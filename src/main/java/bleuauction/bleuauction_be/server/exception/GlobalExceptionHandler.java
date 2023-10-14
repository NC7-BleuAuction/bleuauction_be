package bleuauction.bleuauction_be.server.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ModelAndView exceptionHandler(Exception e) {
    ModelAndView mv = new ModelAndView();
    mv.addObject("exception", e);
    mv.addObject("message", e.getMessage());
    mv.setViewName("error");
    return mv;
  }
}
