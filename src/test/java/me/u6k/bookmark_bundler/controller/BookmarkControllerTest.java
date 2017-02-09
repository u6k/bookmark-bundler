
package me.u6k.bookmark_bundler.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import me.u6k.bookmark_bundler.model.Bookmark;
import me.u6k.bookmark_bundler.model.BookmarkRepository;
import me.u6k.bookmark_bundler.service.BookmarkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookmarkControllerTest {

    private static final Logger L = LoggerFactory.getLogger(BookmarkControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private BookmarkRepository bookmarkRepo;

    private MockMvc mvc;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        this.bookmarkRepo.deleteAllInBatch();
    }

    @Test
    public void create_正常() throws Exception {
        // 準備
        String name = "テスト　サイト";
        String url = "https://example.com/test";

        String json = String.format("{\"name\":\"%s\",\"url\":\"%s\"}", name, url);
        L.debug("request: json={}", json);

        // 実行
        ResultActions result = this.mvc.perform(post("/bookmarks")
                        .contentType("application/json")
                        .content(json));

        // 結果確認
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isCreated())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.id", is(notNullValue())))
                        .andExpect(jsonPath("$.name", is("テスト　サイト")))
                        .andExpect(jsonPath("$.url", is("https://example.com/test")));
    }

    @Test
    public void create_引数が空の場合400_1() throws Exception {
        // 準備
        String name = "";
        String url = "https://example.com/test";

        String json = String.format("{\"name\":\"%s\",\"url\":\"%s\"}", name, url);
        L.debug("request: json={}", json);

        // 実行
        ResultActions result = this.mvc.perform(post("/bookmarks")
                        .contentType("application/json")
                        .content(json));

        // 結果確認
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isBadRequest())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                        .andExpect(jsonPath("$.message", is("name is blank.")));
    }

    @Test
    public void create_引数が空の場合400_2() throws Exception {
        // 準備
        String url = "https://example.com/test";

        String json = String.format("{\"url\":\"%s\"}", url);
        L.debug("request: json={}", json);

        // 実行
        ResultActions result = this.mvc.perform(post("/bookmarks")
                        .contentType("application/json")
                        .content(json));

        // 結果確認
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isBadRequest())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                        .andExpect(jsonPath("$.message", is("name is blank.")));
    }

    @Test
    public void create_引数が空の場合400_3() throws Exception {
        // 準備
        String name = "テスト　サイト";
        String url = "";

        String json = String.format("{\"name\":\"%s\",\"url\":\"%s\"}", name, url);
        L.debug("request: json={}", json);

        // 実行
        ResultActions result = this.mvc.perform(post("/bookmarks")
                        .contentType("application/json")
                        .content(json));

        // 結果確認
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isBadRequest())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                        .andExpect(jsonPath("$.message", is("url is blank.")));
    }

    @Test
    public void create_引数が空の場合400_4() throws Exception {
        // 準備
        String name = "テスト　サイト";

        String json = String.format("{\"name\":\"%s\"}", name);
        L.debug("request: json={}", json);

        // 実行
        ResultActions result = this.mvc.perform(post("/bookmarks")
                        .contentType("application/json")
                        .content(json));

        // 結果確認
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isBadRequest())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.exception", is("java.lang.IllegalArgumentException")))
                        .andExpect(jsonPath("$.message", is("url is blank.")));
    }

    @Test
    public void create_URLが重複() throws Exception {
        // 準備1
        String name1 = "テスト　サイト1";
        String url1 = "https://example.com/test1";

        String json1 = String.format("{\"name\":\"%s\",\"url\":\"%s\"}", name1, url1);
        L.debug("request1: json={}", json1);

        // 実行1
        ResultActions result = this.mvc.perform(post("/bookmarks")
                        .contentType("application/json")
                        .content(json1));

        // 結果確認1
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isCreated());

        // 準備2
        String name2 = "テスト　サイト2";
        String url2 = "https://example.com/test1";

        String json2 = String.format("{\"name\":\"%s\",\"url\":\"%s\"}", name2, url2);
        L.debug("request2: json={}", json2);

        // 実行2
        result = this.mvc.perform(post("/bookmarks")
                        .contentType("application/json")
                        .content(json2));

        // 結果確認2
        response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isBadRequest())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.exception", is("me.u6k.bookmark_bundler.exception.BookmarkDuplicateException")))
                        .andExpect(jsonPath("$.message", is("url=https://example.com/test1 is duplicated.")));
    }

    @Test
    public void findAll_正常_0個() throws Exception {
        // 実行
        ResultActions result = this.mvc.perform(get("/bookmarks"));

        // 結果確認
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(content().string("[]"));
    }

    @Test
    public void findAll_正常_1個() throws Exception {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");

        // 実行
        ResultActions result = this.mvc.perform(get("/bookmarks"));

        // 結果確認
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

        result.andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$[0].id", is(b1.getId())))
                        .andExpect(jsonPath("$[0].name", is("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル")))
                        .andExpect(jsonPath("$[0].url", is("http://www.asahi.com/articles/ASK29336BK29UTFK001.html")))
                        .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    public void findAll_正常_複数個() throws Exception {
        // 準備
        Bookmark b1 = this.bookmarkService.create("「廃棄」日報、発見報告まで１カ月　稲田氏、隠蔽を否定：朝日新聞デジタル", "http://www.asahi.com/articles/ASK29336BK29UTFK001.html");
        Bookmark b2 = this.bookmarkService.create("Ｃ・Ｗ・ニコルさんの長女を逮捕　覚醒剤使用の疑い：朝日新聞デジタル", "http://www.asahi.com/articles/ASK2941FKK29UTIL012.html");
        Bookmark b3 = this.bookmarkService.create("タリウム被害の男性が証言 「枕にびっしりと髪の毛が」：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292TYJK29OIPE006.html");
        Bookmark b4 = this.bookmarkService.create("日本海側、大雪のおそれ 中国地方で８０センチ予想：朝日新聞デジタル", "http://www.asahi.com/articles/ASK293G2WK29PTIL004.html");
        Bookmark b5 = this.bookmarkService.create("トランプ氏「娘が不当に扱われた」 販売中止の店を批判：朝日新聞デジタル", "http://www.asahi.com/articles/ASK292PWFK29UHBI00D.html");

        // 実行
        ResultActions result = this.mvc.perform(get("/bookmarks"));

        // 結果確認
        MockHttpServletResponse response = result.andReturn().getResponse();
        L.debug("response: status={}, body={}", response.getStatus(), response.getContentAsString());

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
