
package me.u6k.bookmark_bundler.exception;

public class BookmarkNotFoundException extends BusinessException {

    public BookmarkNotFoundException(String id) {
        super(String.format("id=%s not found.", id));
    }

}
