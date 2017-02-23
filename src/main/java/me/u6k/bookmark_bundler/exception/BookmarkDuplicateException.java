
package me.u6k.bookmark_bundler.exception;

public class BookmarkDuplicateException extends BusinessException {

    public BookmarkDuplicateException(String url) {
        super(String.format("url=%s is duplicated.", url));
    }

}
