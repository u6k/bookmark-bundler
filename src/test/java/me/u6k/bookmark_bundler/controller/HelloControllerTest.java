
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
public class HelloControllerTest {

    @Autowired
    private HelloController controller;

    @Test
    public void hello_正常() {
        // テスト
        String value = this.controller.hello();

        // 結果確認
        assertThat(value, is("hello"));
    }

}
