
package me.u6k.bookmark_bundler.controller;

import me.u6k.bookmark_bundler.model.Bookmark;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class BookmarkVO {

    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    public BookmarkVO() {
    }

    public BookmarkVO(Bookmark bookmark) {
        this.id = bookmark.getId();
        this.name = bookmark.getName();
        this.url = bookmark.getUrl();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
