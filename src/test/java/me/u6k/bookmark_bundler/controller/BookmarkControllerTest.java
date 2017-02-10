
package me.u6k.bookmark_bundler.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.u6k.bookmark_bundler.model.Bookmark;
import me.u6k.bookmark_bundler.model.BookmarkRepository;
import me.u6k.bookmark_bundler.service.BookmarkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(Enclosed.class)
public class BookmarkControllerTest {

    private static final Logger L = LoggerFactory.getLogger(BookmarkControllerTest.class);

    private static String buildJson(String name, String url) throws JsonProcessingException {
        BookmarkVO vo = new BookmarkVO();
        vo.setName(name);
        vo.setUrl(url);

        String json = new ObjectMapper()
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(vo);
        L.debug("request: json={}", json);

        return json;
    }

    private static ResultActions perform(MockMvc mvc, RequestBuilder request) throws Exception {
        ResultActions result = mvc.perform(request);

        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        return result;
    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    @Transactional
    public static class create {

        @Autowired
        private WebApplicationContext context;

        private MockMvc mvc;

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

            // インスタンス準備
            this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        @Test
        public void 登録できる() throws Exception {
            // 準備
            String json = buildJson(this.b1.getName(), this.b1.getUrl());

            // 実行
            ResultActions result = perform(mvc, post("/bookmarks")
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            List<Bookmark> l = this.bookmarkRepo.findAll();

            result.andExpect(status().isCreated())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.id", is(l.get(0).getId())))
                            .andExpect(jsonPath("$.name", is(l.get(0).getName())))
                            .andExpect(jsonPath("$.url", is(l.get(0).getUrl())));

            assertThat(l.size(), is(1));
        }

        @Test
        public void name引数が空の場合は400_1() throws Exception {
            // 準備
            String json = buildJson(null, this.b1.getUrl());

            // 実行
            ResultActions result = perform(mvc, post("/bookmarks")
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                            .andExpect(jsonPath("$.message", is("name is blank.")));

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void name引数が空の場合は400_2() throws Exception {
            // 準備
            String json = buildJson("", this.b1.getUrl());

            // 実行
            ResultActions result = perform(mvc, post("/bookmarks")
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                            .andExpect(jsonPath("$.message", is("name is blank.")));

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void url引数が空の場合は400_1() throws Exception {
            // 準備
            String json = buildJson(this.b1.getName(), null);

            // 実行
            ResultActions result = perform(mvc, post("/bookmarks")
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                            .andExpect(jsonPath("$.message", is("url is blank.")));

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void url引数が空の場合は400_2() throws Exception {
            // 準備
            String json = buildJson(this.b1.getName(), "");

            // 実行
            ResultActions result = perform(mvc, post("/bookmarks")
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                            .andExpect(jsonPath("$.message", is("url is blank.")));

            assertThat(this.bookmarkRepo.count(), is(0L));
        }

        @Test
        public void URLが重複した場合は400() throws Exception {
            // 準備
            this.b1 = this.bookmarkService.create(this.b1.getName(), this.b1.getUrl());

            String json = buildJson("u6k.Redmine", this.b1.getUrl());

            // 実行
            ResultActions result = perform(mvc, post("/bookmarks")
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("me.u6k.bookmark_bundler.exception.BookmarkDuplicateException")))
                            .andExpect(jsonPath("$.message", is("url=https://example.com/test is duplicated.")));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(1));
            assertThat(l.get(0), is(this.b1));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    @Transactional
    public static class findAll {

        @Autowired
        private WebApplicationContext context;

        private MockMvc mvc;

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

            // インスタンス準備
            this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        @Test
        public void 検索結果が0個() throws Exception {
            // 実行
            this.bookmarkRepo.deleteAllInBatch();

            ResultActions result = perform(mvc, get("/bookmarks"));

            // 結果確認
            result.andExpect(status().isOk())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(content().string("[]"));
        }

        @Test
        public void 検索結果が1個() throws Exception {
            // 準備
            this.bookmarkRepo.delete(this.b2.getId());
            this.bookmarkRepo.delete(this.b3.getId());
            this.bookmarkRepo.delete(this.b4.getId());
            this.bookmarkRepo.delete(this.b5.getId());

            // 実行
            ResultActions result = perform(mvc, get("/bookmarks"));

            // 結果確認
            result.andExpect(status().isOk())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$[0].id", is(b1.getId())))
                            .andExpect(jsonPath("$[0].name", is("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[0].url", is("http://www.asahi.com/articles/ASK29336BK29UTFK001.html")))
                            .andExpect(jsonPath("$[1]").doesNotExist());
        }

        @Test
        public void 検索結果が複数個() throws Exception {
            // 実行
            ResultActions result = this.mvc.perform(get("/bookmarks"));

            // 結果確認
            result.andExpect(status().isOk())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$[0].id", is(b5.getId())))
                            .andExpect(jsonPath("$[0].name", is("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[0].url", is("http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html")))
                            .andExpect(jsonPath("$[1].id", is(b4.getId())))
                            .andExpect(jsonPath("$[1].name", is("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[1].url", is("http://www.asahi.com/articles/ASK293G2WK29PTIL004.html")))
                            .andExpect(jsonPath("$[2].id", is(b3.getId())))
                            .andExpect(jsonPath("$[2].name", is("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[2].url", is("http://www.asahi.com/articles/ASK292TYJK29OIPE006.html")))
                            .andExpect(jsonPath("$[3].id", is(b2.getId())))
                            .andExpect(jsonPath("$[3].name", is("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[3].url", is("http://www.asahi.com/articles/ASK2941FKK29UTIL012.html")))
                            .andExpect(jsonPath("$[4].id", is(b1.getId())))
                            .andExpect(jsonPath("$[4].name", is("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[4].url", is("http://www.asahi.com/articles/ASK29336BK29UTFK001.html")))
                            .andExpect(jsonPath("$[5]").doesNotExist());
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    @Transactional
    public static class findOne {

        @Autowired
        private WebApplicationContext context;

        private MockMvc mvc;

        @Autowired
        private BookmarkService bookmarkService;

        @Autowired
        private BookmarkRepository bookmarkRepo;

        private Bookmark b3;

        @Before
        public void setup() {
            // クリーンアップ
            this.bookmarkRepo.deleteAllInBatch();

            // テストデータ準備
            this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
            this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
            this.b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
            this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
            this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

            // インスタンス準備
            this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        @Test
        public void 取得できる() throws Exception {
            // 実行
            ResultActions result = perform(this.mvc, get("/bookmarks/" + this.b3.getId()));

            // 結果確認
            result.andExpect(status().isOk())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.id", is(b3.getId())))
                            .andExpect(jsonPath("$.name", is("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル")))
                            .andExpect(jsonPath("$.url", is("http://www.asahi.com/articles/ASK292TYJK29OIPE006.html")));
        }

        @Test
        public void 該当Bookmarkが存在しない場合は404() throws Exception {
            // 実行
            ResultActions result = perform(this.mvc, get("/bookmarks/" + UUID.randomUUID().toString()));

            // 結果確認
            result.andExpect(status().isNotFound());
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    @Transactional
    public static class update {

        @Autowired
        private WebApplicationContext context;

        private MockMvc mvc;

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

            // インスタンス準備
            this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        @Test
        public void 更新できる() throws Exception {
            // 準備
            String json = buildJson("Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース", "http://news.livedoor.com/article/detail/12650086/");

            // 実行
            ResultActions result = perform(this.mvc, put("/bookmarks/" + this.b4.getId())
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isOk())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.id", is(this.b4.getId())))
                            .andExpect(jsonPath("$.name", is("Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース")))
                            .andExpect(jsonPath("$.url", is("http://news.livedoor.com/article/detail/12650086/")));

            result = perform(this.mvc, get("/bookmarks/"));

            result.andExpect(status().isOk())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$[0].id", is(b4.getId())))
                            .andExpect(jsonPath("$[0].name", is("Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース")))
                            .andExpect(jsonPath("$[0].url", is("http://news.livedoor.com/article/detail/12650086/")))
                            .andExpect(jsonPath("$[1].id", is(b5.getId())))
                            .andExpect(jsonPath("$[1].name", is("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[1].url", is("http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html")))
                            .andExpect(jsonPath("$[2].id", is(b3.getId())))
                            .andExpect(jsonPath("$[2].name", is("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[2].url", is("http://www.asahi.com/articles/ASK292TYJK29OIPE006.html")))
                            .andExpect(jsonPath("$[3].id", is(b2.getId())))
                            .andExpect(jsonPath("$[3].name", is("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[3].url", is("http://www.asahi.com/articles/ASK2941FKK29UTIL012.html")))
                            .andExpect(jsonPath("$[4].id", is(b1.getId())))
                            .andExpect(jsonPath("$[4].name", is("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル")))
                            .andExpect(jsonPath("$[4].url", is("http://www.asahi.com/articles/ASK29336BK29UTFK001.html")))
                            .andExpect(jsonPath("$[5]").doesNotExist());
        }

        @Test
        public void name引数が空の場合は400_1() throws Exception {
            // 準備
            String json = buildJson(null, "http://news.livedoor.com/article/detail/12650086/");

            // 実行
            ResultActions result = perform(this.mvc, put("/bookmarks/" + this.b4.getId())
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                            .andExpect(jsonPath("$.message", is("name is blank.")));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

        @Test
        public void name引数が空の場合は400_2() throws Exception {
            // 準備
            String json = buildJson("", "http://news.livedoor.com/article/detail/12650086/");

            // 実行
            ResultActions result = perform(this.mvc, put("/bookmarks/" + this.b4.getId())
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                            .andExpect(jsonPath("$.message", is("name is blank.")));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

        @Test
        public void url引数が空の場合は400_1() throws Exception {
            // 準備
            String json = buildJson("Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース", null);

            // 実行
            ResultActions result = perform(this.mvc, put("/bookmarks/" + this.b2.getId())
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                            .andExpect(jsonPath("$.message", is("url is blank.")));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

        @Test
        public void url引数が空の場合は400_2() throws Exception {
            // 準備
            String json = buildJson("Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース", null);

            // 実行
            ResultActions result = perform(this.mvc, put("/bookmarks/" + this.b2.getId())
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                            .andExpect(jsonPath("$.message", is("url is blank.")));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

        @Test
        public void 該当Bookmarkが存在しない場合は404() throws Exception {
            // 準備
            String id = UUID.randomUUID().toString();
            String json = buildJson("Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース", "http://news.livedoor.com/article/detail/12650086/");

            // 実行
            ResultActions result = perform(this.mvc, put("/bookmarks/" + id)
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isNotFound())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("me.u6k.bookmark_bundler.exception.BookmarkNotFoundException")))
                            .andExpect(jsonPath("$.message", is("id=" + id + " not found.")));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(this.b5));
            assertThat(l.get(1), is(this.b4));
            assertThat(l.get(2), is(this.b3));
            assertThat(l.get(3), is(this.b2));
            assertThat(l.get(4), is(this.b1));
        }

        @Test
        public void update_URLが重複した場合は400() throws Exception {
            // 準備
            String json = buildJson("Googleが「Android Wear2.0」の提供を開始 大規模な改善 - ライブドアニュース", this.b3.getUrl());

            // 実行
            ResultActions result = perform(this.mvc, put("/bookmarks/" + this.b3.getId())
                            .contentType("application/json")
                            .content(json));

            // 結果確認
            result.andExpect(status().isBadRequest())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("me.u6k.bookmark_bundler.exception.BookmarkDuplicateException")))
                            .andExpect(jsonPath("$.message", is("url=http://www.asahi.com/articles/ASK292TYJK29OIPE006.html is duplicated.")));

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
    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    @Transactional
    public static class delete {

        @Autowired
        private WebApplicationContext context;

        private MockMvc mvc;

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

            // インスタンス準備
            this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        @Test
        public void delete_正常() throws Exception {
            // 実行
            ResultActions result = perform(this.mvc, delete("/bookmarks/" + b3.getId()));

            // 結果確認
            result.andExpect(status().isNoContent())
                            .andExpect(content().string(""));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(4));
            assertThat(l.get(0), is(b5));
            assertThat(l.get(1), is(b4));
            assertThat(l.get(2), is(b2));
            assertThat(l.get(3), is(b1));
        }

        @Test
        public void delete_該当Bookmarkが存在しない場合は404() throws Exception {
            // 準備
            String id = UUID.randomUUID().toString();

            // 実行
            ResultActions result = perform(this.mvc, delete("/bookmarks/" + id));

            // 結果確認
            MockHttpServletResponse response = result.andReturn().getResponse();
            L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

            result.andExpect(status().isNotFound())
                            .andExpect(content().contentType("application/json;charset=UTF-8"))
                            .andExpect(jsonPath("$.exception", is("me.u6k.bookmark_bundler.exception.BookmarkNotFoundException")))
                            .andExpect(jsonPath("$.message", is("id=" + id + " not found.")));

            List<Bookmark> l = this.bookmarkRepo.findAll();

            assertThat(l.size(), is(5));
            assertThat(l.get(0), is(b5));
            assertThat(l.get(1), is(b4));
            assertThat(l.get(2), is(b3));
            assertThat(l.get(3), is(b2));
            assertThat(l.get(4), is(b1));
        }

    }

}
