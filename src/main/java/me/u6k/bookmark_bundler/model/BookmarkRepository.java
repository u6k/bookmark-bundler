
package me.u6k.bookmark_bundler.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, String> {

    @Query("select count(b) from Bookmark b where b.url = :url")
    int countByUrl(@Param("url") String url);

}
