import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "", description = "")
public class PhoneCousellingController {

    private PhoneCousellingService phoneCounsellingService;

    public PhoneCousellingController(PhoneCousellingService phoneCounsellingService) {
        this.phoneCounsellingService = phoneCounsellingService;
    }

    @GetMapping(path = "/phoneCounsellings")
    @ApiOperation(value = "Get all phoneCounselling", response = PhoneCouselling.class)
    public PhoneCouselling getAllPhoneCouselling() {
        return phoneCounsellingService.getAllPhoneCouselling();
    }
}
