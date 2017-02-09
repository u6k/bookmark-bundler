
package me.u6k.bookmark_bundler.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, String> {

    @Query("select count(b) from Bookmark b where b.url = :url")
    int countByUrl(@Param("url") String url);

    @Query("select b from Bookmark b order by b.updated desc")
    List<Bookmark> findAll();

}
