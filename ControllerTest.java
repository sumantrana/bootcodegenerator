import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controller = { PhoneCousellingController.class })
public class PhoneCousellingControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PhoneCousellingService phoneCounsellingService;

    @BeforeEach
    public void setup() {
        PhoneCousellingController phoneCounsellingController = new PhoneCousellingController(phoneCounsellingService);
        mockMvc = MockMvcBuilders.standaloneSetup(phoneCounsellingController).build();
    }

    @Test
    public void getPhoneCouselling_WillReturn_PhoneCouselling() throws Exception {
        PhoneCouselling phoneCounselling = PhoneCouselling.builder().onlnHmAppfNo(123456789).appfKdDivsCd(LAZY123).build();
        given(phoneCounsellingService.getAllPhoneCouselling()).willReturn(Arrays.asList(phoneCounselling));
        mockMvc.perform(get("/ococ/registration/v1/phoneCounsellings")).andExpect(jsonPath("$.onlnHmAppfNo", is("123456789"))).andExpect(jsonPath("$.appfKdDivsCd", is("LAZY123")));
        verify(phoneCounsellingService, times(1)).getAllPhoneCouselling(anyString());
    }
}
