
package me.u6k.bookmark_bundler.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookmarkControllerTest {

    @Autowired
    private BookmarkController controller;

    @Test
    public void create_正常() {
        // 準備
        BookmarkVO bookmarkVO = new BookmarkVO();
        bookmarkVO.setName("テスト　サイト");
        bookmarkVO.setUrl("https://example.com/test");

        // 実行
        BookmarkVO newBookmarkVO = this.controller.create(bookmarkVO);

        // 結果確認
        assertThat(newBookmarkVO.getId().length(), is(36));
        assertThat(newBookmarkVO.getName(), is("テスト　サイト"));
        assertThat(newBookmarkVO.getUrl(), is("https://example.com/test"));
    }

}
