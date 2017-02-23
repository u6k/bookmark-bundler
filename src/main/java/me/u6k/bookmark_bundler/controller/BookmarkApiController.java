
package me.u6k.bookmark_bundler.controller;

import java.util.List;
import java.util.stream.Collectors;

import me.u6k.bookmark_bundler.exception.BookmarkNotFoundException;
import me.u6k.bookmark_bundler.model.Bookmark;
import me.u6k.bookmark_bundler.service.BookmarkService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookmarkApiController {

    private static final Logger L = LoggerFactory.getLogger(BookmarkService.class);

    @Autowired
    private BookmarkService service;

    @RequestMapping(value = "/api/bookmarks", method = RequestMethod.POST)
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

    @RequestMapping(value = "/api/bookmarks/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public BookmarkVO update(@PathVariable String id, @RequestBody BookmarkVO bookmarkVO) {
        L.debug("#update: bookmarkVO={}", bookmarkVO);

        if (bookmarkVO == null) {
            throw new IllegalArgumentException("bookmarkVO is null.");
        }

        Bookmark updatedBookmark = this.service.update(id, bookmarkVO.getName(), bookmarkVO.getUrl());

        BookmarkVO updatedBookmarkVO = new BookmarkVO(updatedBookmark);

        return updatedBookmarkVO;
    }

    @RequestMapping(value = "/api/bookmarks/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        L.debug("#delete: id={}", id);

        this.service.delete(id);
    }

    @RequestMapping(value = "/api/bookmarks", method = RequestMethod.GET)
    @ResponseBody
    public List<BookmarkVO> findByKeyword(@RequestParam(value = "keyword", required = false) String keyword) {
        L.debug("#findByKeyword: keyword={}", keyword);

        List<Bookmark> list;
        if (!StringUtils.isBlank(keyword)) {
            list = this.service.findByKeyword(keyword);
        } else {
            list = this.service.findAll();
        }

        List<BookmarkVO> voList = list.stream().map(BookmarkVO::new).collect(Collectors.toList());

        return voList;
    }

    @RequestMapping(value = "/api/bookmarks/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BookmarkVO findOne(@PathVariable String id) {
        L.debug("#findOne: id={}", id);

        Bookmark bookmark = this.service.findOne(id);
        if (bookmark == null) {
            throw new BookmarkNotFoundException(id);
        }

        BookmarkVO bookmarkVO = new BookmarkVO(bookmark);

        return bookmarkVO;
    }

}
