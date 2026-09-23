package cookie;

import com.java.lld.design.command.CookieFilterCommand;
import com.java.lld.design.reader.CookieLogReader;
import com.java.lld.design.service.MostActiveCookieService;

import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Cookie filter application")
class CookieFilterApplicationIntegrationTest {

    @Autowired
    private CookieFilterCommand cookieFilterCommand;

    @Autowired
    private MostActiveCookieService mostActiveCookieService;

    @Autowired
    private CookieLogReader cookieLogReader;

    @Test
    @DisplayName("wires CLI, service, and reader components")
    void shouldWireApplicationComponents() {
        assertThat(cookieFilterCommand).isNotNull();
        assertThat(mostActiveCookieService).isNotNull();
        assertThat(cookieLogReader).isNotNull();
    }
}
