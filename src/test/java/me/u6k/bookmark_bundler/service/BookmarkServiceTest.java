
package me.u6k.bookmark_bundler.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.transaction.Transactional;

import me.u6k.bookmark_bundler.exception.BookmarkDuplicateException;
import me.u6k.bookmark_bundler.model.Bookmark;
import me.u6k.bookmark_bundler.model.BookmarkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private BookmarkRepository bookmarkRepo;

    @Before
    public void setup() {
        this.bookmarkRepo.deleteAllInBatch();
    }

    @Test
    public void create_正常() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";

        // 実行
        Bookmark b = this.bookmarkService.create(name, url);

        // 結果確認
        assertThat(b.getId().length(), is(36));
        assertThat(b.getName(), is("テスト　サイト"));
        assertThat(b.getUrl(), is("https://example.com/test"));
        assertNotNull(b.getUpdated());

        assertThat(this.bookmarkRepo.count(), is(1L));
    }

    @Test
    public void create_引数がblank_1() {
        // 準備
        String name = null;
        String url = "https://example.com/test";

        try {
            // 実行
            this.bookmarkService.create(name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("name is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(0L));
    }

    @Test
    public void create_引数がblank_2() {
        // 準備
        String name = "";
        String url = "https://example.com/test";

        try {
            // 実行
            this.bookmarkService.create(name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("name is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(0L));
    }

    @Test
    public void create_引数がblank_3() {
        // 準備
        String name = " ";
        String url = "https://example.com/test";

        try {
            // 実行
            this.bookmarkService.create(name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("name is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(0L));
    }

    @Test
    public void create_引数がblank_4() {
        // 準備
        String name = "テスト　サイト";
        String url = null;

        try {
            // 実行
            this.bookmarkService.create(name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("url is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(0L));
    }

    @Test
    public void create_引数がblank_5() {
        // 準備
        String name = "テスト　サイト";
        String url = "";

        try {
            // 実行
            this.bookmarkService.create(name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("url is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(0L));
    }

    @Test
    public void create_引数がblank_6() {
        // 準備
        String name = "テスト　サイト";
        String url = " ";

        try {
            // 実行
            this.bookmarkService.create(name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("url is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(0L));
    }

    @Test
    public void create_URLが重複() {
        // 準備
        String name1 = "テスト　サイト1";
        String url1 = "https://example.com/test1";

        this.bookmarkService.create(name1, url1);

        String name2 = "テスト　サイト2";
        String url2 = "https://example.com/test1";

        try {
            // 実行
            this.bookmarkService.create(name2, url2);

            fail();
        } catch (BookmarkDuplicateException e) {
            // 結果確認
            assertThat(e.getMessage(), is("url=https://example.com/test1 is duplicated."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));

        Bookmark b = this.bookmarkRepo.findAll().get(0);

        assertThat(b.getId().length(), is(36));
        assertThat(b.getName(), is("テスト　サイト1"));
        assertThat(b.getUrl(), is("https://example.com/test1"));
        assertNotNull(b.getUpdated());
    }

}
