package me.u6k.bookmark_bundler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BookmarkUiController {

    @RequestMapping(value="/ui/bookmarks",method=RequestMethod.GET)
    public String findAll(Model model){
        model.addAttribute("hello", "はろー");

        return "bookmarks";
    }

}
