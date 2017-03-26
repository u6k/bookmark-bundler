
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
import org.springframework.web.bind.annotation.PathVariable;
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
            return "bookmarks-create";
        }

        try {
            this.service.create(form.getName(), form.getUrl());
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "bookmarks-create";
        }

        return "redirect:/ui/bookmarks";
    }

    @RequestMapping(value = "/ui/bookmarks/{id}/edit", method = RequestMethod.GET)
    public String editInit(@PathVariable String id, @ModelAttribute("form") BookmarkVO form, Model model) {
        Bookmark bookmark = this.service.findOne(id);
        if (bookmark == null) {
            throw new IllegalArgumentException("bookmark not found. id=" + id);
        }

        form.setName(bookmark.getName());
        form.setUrl(bookmark.getUrl());

        return "bookmarks-edit";
    }

    @RequestMapping(value = "/ui/bookmarks/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable String id, @ModelAttribute("form") BookmarkVO form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bookmarks-edit";
        }

        try {
            this.service.update(id, form.getName(), form.getUrl());
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "bookmarks-edit";
        }

        return "redirect:/ui/bookmarks";
    }

}
