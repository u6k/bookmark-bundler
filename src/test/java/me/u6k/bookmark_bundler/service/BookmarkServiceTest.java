
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
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(Enclosed.class)
public class BookmarkServiceTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @Transactional
    public static class create {

        @Autowired
        private BookmarkService bookmarkService;

        @Autowired
        private BookmarkRepository bookmarkRepo;

        private Bookmark b1;

        @Before
        public void setup() {
            // クリーンアップ
            this.bookmarkRepo.deleteAllInBatch();

            // テストデータ準備
            this.b1 = new Bookmark();
            this.b1.setName("テスト　サイト");
            this.b1.setUrl("https://example.com/test");
        }

        @Test
        public void 登録できる() {
            // 実行
            Bookmark result = this.bookmarkService.create(this.b1.getName(), this.b1.getUrl());

            // 結果確認
            assertThat(result.getId().length(), is(36));
            assertThat(result.getName(), is("テスト　サイト"));
            assertThat(result.getUrl(), is("https://example.com/test"));
            assertThat(result.getUpdated(), is(not(nullValue())));

            assertThat(this.bookmarkRepo.count(), is(1L));
        }

        @Test
        public void name引数が空の場合はIllegalArgumentException_1() {
            try {
                // 実行
                this.bookmarkService.create(null, this.b1.getUrl());

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("name is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void name引数が空の場合はIllegalArgumentException_2() {
            try {
                // 実行
                this.bookmarkService.create("", this.b1.getUrl());

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("name is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void name引数が空の場合はIllegalArgumentException_3() {
            try {
                // 実行
                this.bookmarkService.create(" ", this.b1.getUrl());

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("name is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void url引数が空の場合はIllegalArgumentException_1() {
            try {
                // 実行
                this.bookmarkService.create(this.b1.getName(), null);

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("url is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void url引数が空の場合はIllegalArgumentException_2() {
            try {
                // 実行
                this.bookmarkService.create(this.b1.getName(), "");

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("url is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void url引数が空の場合はIllegalArgumentException_3() {
            try {
                // 実行
                this.bookmarkService.create(this.b1.getName(), " ");

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("url is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void URLが重複した場合はBookmarkDuplicateException() {
            // 準備
            this.bookmarkService.create(this.b1.getName(), this.b1.getUrl());

            try {
                // 実行
                this.bookmarkService.create(this.b1.getName() + "2", this.b1.getUrl());

                fail();
            } catch (BookmarkDuplicateException e) {
                // 結果確認
                assertThat(e.getMessage(), is("url=https://example.com/test is duplicated."));
            }

            assertThat(this.bookmarkRepo.count(), is(1L));

            Bookmark b = this.bookmarkRepo.findAll().get(0);

            assertThat(b.getId().length(), is(36));
            assertThat(b.getName(), is("テスト　サイト"));
            assertThat(b.getUrl(), is("https://example.com/test"));
            assertThat(b.getUpdated(), is(not(nullValue())));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @Transactional
    public static class findAll {

        @Autowired
        private BookmarkService bookmarkService;

        @Autowired
        private BookmarkRepository bookmarkRepo;

        private Bookmark b1;

        private Bookmark b2;

        private Bookmark b3;

        private Bookmark b4;

        private Bookmark b5;

        @Before
        public void setup() {
            // クリーンアップ
            this.bookmarkRepo.deleteAllInBatch();

            // テストデータ準備
            this.b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
            this.b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
            this.b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
            this.b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
            this.b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");
        }

        @Test
        public void 検索結果が0個() {
            // 準備
            this.bookmarkRepo.deleteAllInBatch();

            // 実行
            List<Bookmark> l = this.bookmarkService.findAll();

            // 結果確認
            assertThat(l.size(), is(0));
        }

        @Test
        public void 検索結果が1個() {
            // 準備
            this.bookmarkRepo.delete(this.b2.getId());
            this.bookmarkRepo.delete(this.b3.getId());
            this.bookmarkRepo.delete(this.b4.getId());
            this.bookmarkRepo.delete(this.b5.getId());

            // 実行
            List<Bookmark> l = this.bookmarkService.findAll();

            // 結果確認
            assertThat(l.size(), is(1));
            assertThat(l.get(0), is(this.b1));
        }

        @Test
        public void 検索結果が複数個() {
            // 実行
            List<Bookmark> l = this.bookmarkService.findAll();

            // 結果確認
            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @Transactional
    public static class findOne {

        @Autowired
        private BookmarkService bookmarkService;

        @Autowired
        private BookmarkRepository bookmarkRepo;

        private Bookmark b1;

        private Bookmark b2;

        private Bookmark b3;

        private Bookmark b4;

        private Bookmark b5;

        @Before
        public void setup() {
            // クリーンアップ
            this.bookmarkRepo.deleteAllInBatch();

            // テストデータ準備
            this.b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
            this.b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
            this.b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
            this.b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
            this.b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");
        }

        @Test
        public void 取得できる() {
            // 実行
            Bookmark result3 = this.bookmarkService.findOne(this.b3.getId());
            Bookmark result1 = this.bookmarkService.findOne(this.b1.getId());
            Bookmark result4 = this.bookmarkService.findOne(this.b4.getId());
            Bookmark result5 = this.bookmarkService.findOne(this.b5.getId());
            Bookmark result2 = this.bookmarkService.findOne(this.b2.getId());

            // 結果確認
            assertThat(result1, is(b1));
            assertThat(result2, is(b2));
            assertThat(result3, is(b3));
            assertThat(result4, is(b4));
            assertThat(result5, is(b5));
        }

        @Test
        public void 該当Bookmarkが存在しない場合はnull() {
            // 実行
            Bookmark result = this.bookmarkService.findOne(UUID.randomUUID().toString());

            // 結果確認
            assertThat(result, is(nullValue()));
        }

        @Test
        public void id引数が空の場合は該当Bookmarkが存在しない場合と同じ() {
            // 実行
            Bookmark result1 = this.bookmarkService.findOne(null);
            Bookmark result2 = this.bookmarkService.findOne("");

            // 結果確認
            assertThat(result1, is(nullValue()));
            assertThat(result2, is(nullValue()));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @Transactional
    public static class update {

        @Autowired
        private BookmarkService bookmarkService;

        @Autowired
        private BookmarkRepository bookmarkRepo;

        private Bookmark b1;

        private Bookmark b2;

        private Bookmark b3;

        private Bookmark b4;

        private Bookmark b5;

        @Before
        public void setup() {
            // クリーンアップ
            this.bookmarkRepo.deleteAllInBatch();

            // テストデータ準備
            this.b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
            this.b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
            this.b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
            this.b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
            this.b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");
        }

        @Test
        public void 更新できる() {
            // 実行
            Bookmark result = this.bookmarkService.update(this.b2.getId(), "Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース", "http://news.livedoor.com/article/detail/12650086/");

            // 結果確認
            assertThat(result.getId(), is(this.b2.getId()));
            assertThat(result.getName(), is("Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース"));
            assertThat(result.getUrl(), is("http://news.livedoor.com/article/detail/12650086/"));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(result));
            assertThat(l.get(1), is(this.b5));
            assertThat(l.get(2), is(this.b4));
            assertThat(l.get(3), is(this.b3));
            assertThat(l.get(4), is(this.b1));
        }

        @Test
        public void タイトルを変更するだけの場合は更新できる() {
            // URL重複でエラーにならない。

            // 実行
            Bookmark result = this.bookmarkService.update(this.b3.getId(), "テストサイト", this.b3.getUrl());

            // 結果確認
            assertThat(result.getId(), is(this.b3.getId()));
            assertThat(result.getName(), is("テストサイト"));
            assertThat(result.getUrl(), is(this.b3.getUrl()));
        }

        @Test
        public void 他のブックマークと同じURLに変更する場合はBookmarkDuplicateException() {
            try {
                // 実行
                this.bookmarkService.update(this.b3.getId(), this.b3.getName(), this.b2.getUrl());

                fail();
            } catch (BookmarkDuplicateException e) {
                // 結果確認
                assertThat(e.getMessage(), is("url=http://www.asahi.com/articles/ASK2941FKK29UTIL012.html is duplicated."));
            }

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.get(2), is(this.b3));
        }

        @Test
        public void id引数が空の場合はIllegalArgumentException_1() {
            // 準備
            String name = "テスト　サイト";
            String url = "https://example.com/test";

            try {
                // 実行
                this.bookmarkService.update(null, name, url);

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("id is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(5L));
        }

        @Test
        public void id引数が空の場合はIllegalArgumentException_2() {
            // 準備
            String name = "テスト　サイト";
            String url = "https://example.com/test";

            try {
                // 実行
                this.bookmarkService.update("", name, url);

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("id is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(5L));
        }

        @Test
        public void name引数が空の場合はIllegalArgumentException_1() {
            // 準備
            String name = null;
            String url = "https://example.com/test";

            try {
                // 実行
                this.bookmarkService.update(this.b4.getId(), name, url);

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("name is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(5L));
        }

        @Test
        public void name引数が空の場合はIllegalArgumentException_2() {
            // 準備
            String name = "";
            String url = "https://example.com/test";

            try {
                // 実行
                this.bookmarkService.update(this.b4.getId(), name, url);

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("name is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(5L));
        }

        @Test
        public void url引数が空の場合はIllegalArgumentException_1() {
            // 準備
            String name = "テスト　サイト";
            String url = null;

            try {
                // 実行
                this.bookmarkService.update(this.b4.getId(), name, url);

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("url is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(5L));
        }

        @Test
        public void url引数が空の場合はIllegalArgumentException_2() {
            // 準備
            String name = "テスト　サイト";
            String url = "";

            try {
                // 実行
                this.bookmarkService.update(this.b4.getId(), name, url);

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("url is blank."));
            }

            assertThat(this.bookmarkRepo.count(), is(5L));
        }

        @Test
        public void 該当Bookmarkが存在しない場合はBookmarkNotFoundException() {
            // 準備
            String id = UUID.randomUUID().toString();
            String name = "テスト　サイト";
            String url = "https://example.com/test";

            try {
                // 実行
                this.bookmarkService.update(id, name, url);

                fail();
            } catch (BookmarkNotFoundException e) {
                // 結果確認
                assertThat(e.getMessage(), is("id=" + id + " not found."));
            }

            assertThat(this.bookmarkRepo.count(), is(5L));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @Transactional
    public static class delete {

        @Autowired
        private BookmarkService bookmarkService;

        @Autowired
        private BookmarkRepository bookmarkRepo;

        private Bookmark b1;

        private Bookmark b2;

        private Bookmark b3;

        private Bookmark b4;

        private Bookmark b5;

        @Before
        public void setup() {
            // クリーンアップ
            this.bookmarkRepo.deleteAllInBatch();

            // テストデータ準備
            this.b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
            this.b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
            this.b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
            this.b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
            this.b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");
        }

        @Test
        public void 削除できる() throws Exception {
            // 実行
            this.bookmarkService.delete(this.b3.getId());

            // 結果確認
            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(4));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b2));
            assertThat(l.get(3), is(this.b1));
        }

        @Test
        public void id引数が空の場合はIllegalArgumentException_1() throws Exception {
            try {
                // 実行
                this.bookmarkService.delete(null);

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("id is blank."));
            }

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

        @Test
        public void id引数が空の場合はIllegalArgumentException_2() throws Exception {
            try {
                // 実行
                this.bookmarkService.delete("");

                fail();
            } catch (IllegalArgumentException e) {
                // 結果確認
                assertThat(e.getMessage(), is("id is blank."));
            }

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

        @Test
        public void 該当Bookmarkが存在しない場合はBookmarkNotFoundException() throws Exception {
            // 準備
            String id = UUID.randomUUID().toString();

            try {
                // 実行
                this.bookmarkService.delete(id);

                fail();
            } catch (BookmarkNotFoundException e) {
                // 結果確認
                assertThat(e.getMessage(), is("id=" + id + " not found."));
            }

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @Transactional
    public static class findByKeyword {

        @Autowired
        private BookmarkService bookmarkService;

        @Autowired
        private BookmarkRepository bookmarkRepo;

        private Bookmark asahi1;

        private Bookmark asahi2;

        private Bookmark asahi3;

        private Bookmark asahi4;

        private Bookmark asahi5;

        private Bookmark gigazine1;

        private Bookmark gigazine2;

        private Bookmark gigazine3;

        private Bookmark github1;

        @Before
        public void setup() {
            // クリーンアップ
            this.bookmarkRepo.deleteAllInBatch();

            // テストデータ準備
            this.asahi1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
            this.asahi2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
            this.asahi3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
            this.gigazine1 = this.bookmarkService.create("あらゆるものが風船のように膨らんでいく謎の現象に巻き込まれたある牧場主のアニメーション「Fat」 - GIGAZINE", "http://gigazine.net/news/20170210-fat/");
            this.gigazine2 = this.bookmarkService.create("ネス湖のネッシーを四半世紀も探し続けている本物のモンスターハンター - GIGAZINE", "http://gigazine.net/news/20170210-loch-ness-watchman/");
            this.asahi4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
            this.asahi5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");
            this.github1 = this.bookmarkService.create("u6k/bookmark-bundler: ブックマークを束ねる(管理する)サービスを構築します。", "https://github.com/u6k/bookmark-bundler");
            this.gigazine3 = this.bookmarkService.create("Amazon Dash Buttonのようにポチっとするだけで反トランプな人権団体に寄付できる装置が誕生 - GIGAZINE", "http://gigazine.net/news/20170210-aclu-dash-button/");
        }

        @Test
        public void 検索結果が0個() {
            // 実行
            List<Bookmark> l = this.bookmarkService.findByKeyword("example");

            // 結果確認
            assertThat(l.size(), is(0));
        }

        @Test
        public void 名前で1個ヒット() {
            // 実行
            List<Bookmark> l = this.bookmarkService.findByKeyword("ネッシー");

            // 結果確認
            assertThat(l.size(), is(1));
            assertThat(l.get(0), is(this.gigazine2));
        }

        @Test
        public void 名前で複数個ヒット() {
            // 実行
            List<Bookmark> l = this.bookmarkService.findByKeyword("朝日");

            // 結果確認
            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.asahi5));
            assertThat(l.get(1), is(this.asahi4));
            assertThat(l.get(2), is(this.asahi3));
            assertThat(l.get(3), is(this.asahi2));
            assertThat(l.get(4), is(this.asahi1));
        }

        @Test
        public void URLで1個ヒット() {
            // 実行
            List<Bookmark> l = this.bookmarkService.findByKeyword("ASK2941FKK29UTIL012");

            // 結果確認
            assertThat(l.size(), is(1));
            assertThat(l.get(0), is(this.asahi2));
        }

        @Test
        public void URLで複数個ヒット() {
            // 実行
            List<Bookmark> l = this.bookmarkService.findByKeyword("gigazine");

            // 結果確認
            assertThat(l.size(), is(3));
            assertThat(l.get(0), is(this.gigazine3));
            assertThat(l.get(1), is(this.gigazine2));
            assertThat(l.get(2), is(this.gigazine1));
        }

        @Test
        public void キーワードがnullの場合は0個_1() {
            // 実行
            List<Bookmark> l = this.bookmarkService.findByKeyword(null);

            // 結果確認
            assertThat(l.size(), is(0));
        }

        @Test
        public void キーワードが空文字列の場合は全件ヒット_2() {
            // 実行
            List<Bookmark> l = this.bookmarkService.findByKeyword("");

            // 結果確認
            assertThat(l.size(), is(9));
            assertThat(l.get(0), is(this.gigazine3));
            assertThat(l.get(1), is(this.github1));
            assertThat(l.get(2), is(this.asahi5));
            assertThat(l.get(3), is(this.asahi4));
            assertThat(l.get(4), is(this.gigazine2));
            assertThat(l.get(5), is(this.gigazine1));
            assertThat(l.get(6), is(this.asahi3));
            assertThat(l.get(7), is(this.asahi2));
            assertThat(l.get(8), is(this.asahi1));
        }

    }

}
