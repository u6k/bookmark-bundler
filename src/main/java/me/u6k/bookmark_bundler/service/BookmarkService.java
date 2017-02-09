
package me.u6k.bookmark_bundler.service;

import java.util.List;
import java.util.UUID;

import me.u6k.bookmark_bundler.exception.BookmarkDuplicateException;
import me.u6k.bookmark_bundler.exception.BookmarkNotFoundException;
import me.u6k.bookmark_bundler.model.Bookmark;
import me.u6k.bookmark_bundler.model.BookmarkRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

    private static final Logger L = LoggerFactory.getLogger(BookmarkService.class);

    @Autowired
    private BookmarkRepository bookmarkRepo;

    public Bookmark create(String name, String url) {
        L.debug("#create: name={}, url={}", name, url);

        // 入力チェック
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank.");
        }
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("url is blank.");
        }

        // URL重複チェック
        int count = this.bookmarkRepo.countByUrl(url);
        if (count > 0) {
            throw new BookmarkDuplicateException(url);
        }

        // 新規登録
        Bookmark b = new Bookmark();
        b.setId(UUID.randomUUID().toString());
        b.setName(name.trim());
        b.setUrl(url.trim());

        this.bookmarkRepo.save(b);

        L.debug("bookmark={}", b);

        return b;
    }

    public Bookmark update(String id, String name, String url) {
        L.debug("#update: id={}, name={}, url={}", id, name, url);

        // 入力チェック
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id is blank.");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank.");
        }
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("url is blank.");
        }

        // 更新対象のBookmarkを取得
        Bookmark b = this.findOne(id);
        if (b == null) {
            throw new BookmarkNotFoundException(id);
        }

        // URL重複チェック
        int count = this.bookmarkRepo.countByUrl(url);
        if (count > 0) {
            throw new BookmarkDuplicateException(url);
        }

        // 更新
        b.setName(name.trim());
        b.setUrl(url.trim());

        this.bookmarkRepo.save(b);

        L.debug("bookmark={}", b);

        return b;
    }

    public void delete(String id) {
        L.debug("#delete: id={}", id);

        // 入力チェック
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id is blank.");
        }

        // 削除対象のBookmarkを取得
        Bookmark b = this.findOne(id);
        if (b == null) {
            throw new BookmarkNotFoundException(id);
        }

        // 削除
        this.bookmarkRepo.delete(b);
    }

    public List<Bookmark> findAll() {
        L.debug("#findAll");

        List<Bookmark> l = this.bookmarkRepo.findAll();

        return l;
    }

    public Bookmark findOne(String id) {
        L.debug("#findOne: id={}", id);

        if (StringUtils.isEmpty(id)) {
            return null;
        }

        Bookmark b = this.bookmarkRepo.findOne(id);

        return b;
    }

}
