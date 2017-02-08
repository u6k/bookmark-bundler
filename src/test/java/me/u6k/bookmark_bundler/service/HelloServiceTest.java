
package me.u6k.bookmark_bundler.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloServiceTest {

    @Autowired
    private HelloService service;

    @Test
    public void hello_正常() {
        // テスト
        String result = this.service.hello();

        // 結果確認
        assertThat(result, is("hello"));
    }

}
