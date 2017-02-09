
package me.u6k.bookmark_bundler.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import me.u6k.bookmark_bundler.model.BookmarkRepository;
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

}
