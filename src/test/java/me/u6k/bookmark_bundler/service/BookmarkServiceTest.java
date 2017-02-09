
package me.u6k.bookmark_bundler.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import me.u6k.bookmark_bundler.exception.BookmarkDuplicateException;
import me.u6k.bookmark_bundler.exception.BookmarkNotFoundException;
import me.u6k.bookmark_bundler.model.Bookmark;
import me.u6k.bookmark_bundler.model.BookmarkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    public void create_引数がblankの場合はIllegalArgumentException_1() {
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
    public void create_引数がblankの場合はIllegalArgumentException_2() {
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
    public void create_引数がblankの場合はIllegalArgumentException_3() {
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
    public void create_引数がblankの場合はIllegalArgumentException_4() {
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
    public void create_引数がblankの場合はIllegalArgumentException_5() {
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
    public void create_引数がblankの場合はIllegalArgumentException_6() {
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
    public void create_URLが重複した場合はBookmarkDuplicateException() {
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

    @Test
    public void findAll_正常_0個() {
        // 実行
        List<Bookmark> l = this.bookmarkService.findAll();

        // 結果確認
        assertThat(l.size(), is(0));
    }

    @Test
    public void findAll_正常_1個() {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");

        // 実行
        List<Bookmark> l = this.bookmarkService.findAll();

        // 結果確認
        assertThat(l.size(), is(1));
        assertThat(l.get(0), is(b1));
    }

    @Test
    public void findAll_正常_複数個() {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        Bookmark b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        Bookmark b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        Bookmark b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        Bookmark b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        // 実行
        List<Bookmark> l = this.bookmarkService.findAll();

        // 結果確認
        assertThat(l.size(), is(5));
        assertThat(l.get(0), is(b5));
        assertThat(l.get(1), is(b4));
        assertThat(l.get(2), is(b3));
        assertThat(l.get(3), is(b2));
        assertThat(l.get(4), is(b1));
    }

    @Test
    public void findOne_正常() {
        // 準備
        this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        Bookmark b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        // 実行
        Bookmark result = this.bookmarkService.findOne(b3.getId());

        // 結果確認
        assertThat(result, is(b3));
    }

    @Test
    public void findOne_該当Bookmarkが存在しない場合はnull() {
        // 準備
        this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        // 実行
        Bookmark result = this.bookmarkService.findOne(UUID.randomUUID().toString());

        // 結果確認
        assertThat(result, is(nullValue()));
    }

    @Test
    public void findOne_引数が空の場合は該当Bookmarkが存在しない場合と同じ() {
        // 準備
        this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        // 実行
        Bookmark result1 = this.bookmarkService.findOne(null);
        Bookmark result2 = this.bookmarkService.findOne("");

        // 結果確認
        assertThat(result1, is(nullValue()));
        assertThat(result2, is(nullValue()));
    }

    @Test
    public void update_正常() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";
        String id = this.bookmarkService.create(name, url).getId();

        name = "u6k.Redmine";
        url = "https://redmine.u6k.me";

        // 実行
        Bookmark result = this.bookmarkService.update(id, name, url);

        // 結果確認
        assertThat(result.getId(), is(id));
        assertThat(result.getName(), is("u6k.Redmine"));
        assertThat(result.getUrl(), is("https://redmine.u6k.me"));

        assertThat(this.bookmarkRepo.count(), is(1L));

        Bookmark b = this.bookmarkRepo.findOne(id);

        assertThat(b.getId(), is(id));
        assertThat(b.getName(), is("u6k.Redmine"));
        assertThat(b.getUrl(), is("https://redmine.u6k.me"));
    }

    @Test
    public void update_正常_更新日が更新されてfindAllの順番が変わる() {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        Bookmark b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        Bookmark b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        Bookmark b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        Bookmark b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        b3 = this.bookmarkService.update(b3.getId(), "Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース", "http://news.livedoor.com/article/detail/12650086/");

        // 実行1
        List<Bookmark> l = this.bookmarkService.findAll();

        // 結果確認1
        assertThat(l.size(), is(5));
        assertThat(l.get(0), is(b3));
        assertThat(l.get(1), is(b5));
        assertThat(l.get(2), is(b4));
        assertThat(l.get(3), is(b2));
        assertThat(l.get(4), is(b1));
    }

    @Test
    public void update_引数が空の場合はIllegalArgumentException_1() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";
        String id = this.bookmarkService.create(name, url).getId();

        id = null;
        name = "u6k.Redmine";
        url = "https://redmine.u6k.me";

        try {
            // 実行
            this.bookmarkService.update(id, name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("id is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));
    }

    @Test
    public void update_引数が空の場合はIllegalArgumentException_2() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";
        String id = this.bookmarkService.create(name, url).getId();

        id = "";
        name = "u6k.Redmine";
        url = "https://redmine.u6k.me";

        try {
            // 実行
            this.bookmarkService.update(id, name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("id is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));
    }

    @Test
    public void update_引数が空の場合はIllegalArgumentException_3() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";
        String id = this.bookmarkService.create(name, url).getId();

        name = null;
        url = "https://redmine.u6k.me";

        try {
            // 実行
            this.bookmarkService.update(id, name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("name is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));
    }

    @Test
    public void update_引数が空の場合はIllegalArgumentException_4() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";
        String id = this.bookmarkService.create(name, url).getId();

        name = "";
        url = "https://redmine.u6k.me";

        try {
            // 実行
            this.bookmarkService.update(id, name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("name is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));
    }

    @Test
    public void update_引数が空の場合はIllegalArgumentException_5() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";
        String id = this.bookmarkService.create(name, url).getId();

        name = "u6k.Redmine";
        url = null;

        try {
            // 実行
            this.bookmarkService.update(id, name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("url is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));
    }

    @Test
    public void update_引数が空の場合はIllegalArgumentException_6() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";
        String id = this.bookmarkService.create(name, url).getId();

        name = "u6k.Redmine";
        url = "";

        try {
            // 実行
            this.bookmarkService.update(id, name, url);

            fail();
        } catch (IllegalArgumentException e) {
            // 結果確認
            assertThat(e.getMessage(), is("url is blank."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));
    }

    @Test
    public void update_該当Bookmarkが存在しない場合はBookmarkNotFoundException() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";

        String id = this.bookmarkService.create(name, url).getId();

        String ngId = UUID.randomUUID().toString();
        name = "u6k.Redmine";
        url = "https://redmine.u6k.me";

        try {
            // 実行
            this.bookmarkService.update(ngId, name, url);

            fail();
        } catch (BookmarkNotFoundException e) {
            // 結果確認
            assertThat(e.getMessage(), is("bookmark.id=" + ngId + " not found."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));

        Bookmark b = this.bookmarkRepo.findOne(id);

        assertThat(b.getId(), is(id));
        assertThat(b.getName(), is("テスト　サイト"));
        assertThat(b.getUrl(), is("https://example.com/test"));
    }

    @Test
    public void update_URLが重複した場合はBookmarkDuplicateException() {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";

        String id = this.bookmarkService.create(name, url).getId();

        name = "u6k.Redmine";

        try {
            // 実行
            this.bookmarkService.update(id, name, url);

            fail();
        } catch (BookmarkDuplicateException e) {
            // 結果確認
            assertThat(e.getMessage(), is("url=https://example.com/test is duplicated."));
        }

        assertThat(this.bookmarkRepo.count(), is(1L));

        Bookmark b = this.bookmarkRepo.findOne(id);

        assertThat(b.getId(), is(id));
        assertThat(b.getName(), is("テスト　サイト"));
        assertThat(b.getUrl(), is("https://example.com/test"));
    }

    @Test
    public void delete_正常() throws Exception {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        Bookmark b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        Bookmark b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        Bookmark b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        Bookmark b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        // 実行
        this.bookmarkService.delete(b3.getId());

        // 結果確認
        List<Bookmark> l = this.bookmarkService.findAll();

        assertThat(l.size(), is(4));
        assertThat(l.get(0), is(b5));
        assertThat(l.get(1), is(b4));
        assertThat(l.get(2), is(b2));
        assertThat(l.get(3), is(b1));

        l = this.bookmarkRepo.findAll();

        assertThat(l.size(), is(4));
        assertThat(l.get(0), is(b5));
        assertThat(l.get(1), is(b4));
        assertThat(l.get(2), is(b2));
        assertThat(l.get(3), is(b1));
    }

    @Test
    public void delete_引数が空の場合はIllegalArgumentException_1() throws Exception {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        Bookmark b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        Bookmark b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        Bookmark b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        Bookmark b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        try {
            // 実行
            this.bookmarkService.delete(null);

            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("id is blank."));
        }

        List<Bookmark> l = this.bookmarkRepo.findAll();

        assertThat(l.size(), is(5));
        assertThat(l.get(0), is(b5));
        assertThat(l.get(1), is(b4));
        assertThat(l.get(2), is(b3));
        assertThat(l.get(3), is(b2));
        assertThat(l.get(4), is(b1));
    }

    @Test
    public void delete_引数が空の場合はIllegalArgumentException_2() throws Exception {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        Bookmark b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        Bookmark b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        Bookmark b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        Bookmark b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        try {
            // 実行
            this.bookmarkService.delete("");

            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("id is blank."));
        }

        List<Bookmark> l = this.bookmarkRepo.findAll();

        assertThat(l.size(), is(5));
        assertThat(l.get(0), is(b5));
        assertThat(l.get(1), is(b4));
        assertThat(l.get(2), is(b3));
        assertThat(l.get(3), is(b2));
        assertThat(l.get(4), is(b1));
    }

    @Test
    public void delete_該当Bookmarkが存在しない場合はBookmarkNotFoundException() throws Exception {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        Bookmark b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        Bookmark b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        Bookmark b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        Bookmark b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        String ngId = UUID.randomUUID().toString();

        try {
            // 実行
            this.bookmarkService.delete(ngId);

            fail();
        } catch (BookmarkNotFoundException e) {
            assertThat(e.getMessage(), is("bookmark.id=" + ngId + " not found."));
        }

        List<Bookmark> l = this.bookmarkRepo.findAll();

        assertThat(l.size(), is(5));
        assertThat(l.get(0), is(b5));
        assertThat(l.get(1), is(b4));
        assertThat(l.get(2), is(b3));
        assertThat(l.get(3), is(b2));
        assertThat(l.get(4), is(b1));
    }

}
