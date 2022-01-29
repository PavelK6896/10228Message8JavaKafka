package app.web.pavelk.message8;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
        MatcherAssert.assertThat(0L, CoreMatchers.equalTo(0L));
    }

}
