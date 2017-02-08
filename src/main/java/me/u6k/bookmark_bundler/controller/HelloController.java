
package me.u6k.bookmark_bundler.controller;

import me.u6k.bookmark_bundler.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    private static final Logger L = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private HelloService service;

    @RequestMapping(method = RequestMethod.GET)
    public String hello() {
        L.debug("#hello");

        String value = this.service.hello();

        return value;
    }

}
