
package me.u6k.bookmark_bundler;

import me.u6k.bookmark_bundler.exception.BookmarkDuplicateException;
import me.u6k.bookmark_bundler.exception.BookmarkNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler({ IllegalArgumentException.class, BookmarkDuplicateException.class })
    @ResponseBody
    public ExceptionVO badRequestHandler(Exception e) {
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.exception = e.getClass().getName();
        exceptionVO.message = e.getMessage();

        return exceptionVO;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(BookmarkNotFoundException.class)
    @ResponseBody
    public ExceptionVO notFoundHandler(Exception e) {
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.exception = e.getClass().getName();
        exceptionVO.message = e.getMessage();

        return exceptionVO;
    }

    class ExceptionVO {

        public String exception;

        public String message;

    }

}
