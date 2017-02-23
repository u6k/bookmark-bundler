
package me.u6k.bookmark_bundler.controller;

import java.util.List;
import java.util.stream.Collectors;

import me.u6k.bookmark_bundler.exception.BusinessException;
import me.u6k.bookmark_bundler.model.Bookmark;
import me.u6k.bookmark_bundler.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BookmarkUiController {

    @Autowired
    private BookmarkService service;

    @RequestMapping(value = "/ui/bookmarks", method = RequestMethod.GET)
    public String findAll(Model model) {
        List<Bookmark> bookmarkList = this.service.findAll();
        List<BookmarkVO> list = bookmarkList.stream().map(BookmarkVO::new).collect(Collectors.toList());

        model.addAttribute("bookmarks", list);

        return "bookmarks";
    }

    @RequestMapping(value = "/ui/bookmarks/create", method = RequestMethod.GET)
    public String createInit(@ModelAttribute("form") BookmarkVO form, Model model) {
        return "bookmarks-create";
    }

    @RequestMapping(value = "/ui/bookmarks/create", method = RequestMethod.POST)
    public String create(@Validated @ModelAttribute("form") BookmarkVO form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return this.createInit(form, model);
        }

        try {
            this.service.create(form.getName(), form.getUrl());
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return this.createInit(form, model);
        }

        return "redirect:/ui/bookmarks";
    }

}
