
package me.u6k.bookmark_bundler.controller;

import java.util.List;
import java.util.stream.Collectors;

import me.u6k.bookmark_bundler.model.Bookmark;
import me.u6k.bookmark_bundler.service.BookmarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookmarkController {

    private static final Logger L = LoggerFactory.getLogger(BookmarkService.class);

    @Autowired
    private BookmarkService service;

    @RequestMapping(value = "/bookmarks", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public BookmarkVO create(@RequestBody BookmarkVO bookmarkVO) {
        L.debug("#create: bookmarkVO={}", bookmarkVO);

        if (bookmarkVO == null) {
            throw new IllegalArgumentException("bookmarkVO is null.");
        }

        Bookmark newBookmark = this.service.create(bookmarkVO.getName(), bookmarkVO.getUrl());

        BookmarkVO newBookmarkVO = new BookmarkVO(newBookmark);

        return newBookmarkVO;
    }

    @RequestMapping(value = "/bookmarks", method = RequestMethod.GET)
    @ResponseBody
    public List<BookmarkVO> findAll() {
        L.debug("#findAll");

        List<Bookmark> list = this.service.findAll();

        List<BookmarkVO> voList = list.stream().map(BookmarkVO::new).collect(Collectors.toList());

        return voList;
    }

}
