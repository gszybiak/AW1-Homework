package firstSprint.application.pd2.task2;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.math.BigDecimal;

@RequestMapping("/api")
@Controller
public class ApiProxy {

    @GetMapping("/cenyzlota")
    public ModelAndView goldPrice(@RequestParam(required = false, defaultValue = "json") String format) {
        String apiUrl = "http://api.nbp.pl/api/cenyzlota/last?format=";

        if ("json".equalsIgnoreCase(format)) {
            apiUrl += "json";
        } else if ("xml".equalsIgnoreCase(format)) {
            apiUrl += "xml";
        } else {
            apiUrl += "json";
        }

        RedirectView redirectView = new RedirectView(apiUrl);
        return new ModelAndView(redirectView);
    }

    @GetMapping("/2/cenyzlota")
    @ResponseBody
    public String goldPriceFormat(){
        BigDecimal price = getGoldPrice();
        return "Cena złota w obecnej chwili wynosi " + price.toString();
    }

    @GetMapping("/cenyzlota/last")
    @ResponseBody
    public ModelAndView goldPriceLast(@RequestParam(required = false, defaultValue = "1") int count,
                                      @RequestParam(required = false, defaultValue = "json") String format) {
        String apiUrl = "http://api.nbp.pl/api/cenyzlota/last?";
        String formatParam = "format=" + format;
        String countParam = "count=" + count;

        if ("json".equalsIgnoreCase(format)) {
            apiUrl += "json";
        } else if ("xml".equalsIgnoreCase(format)) {
            apiUrl += "xml";
        } else {
            apiUrl += "json";
        }

        apiUrl += formatParam;
        if (count > 0) {
            apiUrl += "&" + countParam;
        }

        RedirectView redirectView = new RedirectView(apiUrl);
        return new ModelAndView(redirectView);
    }

    @GetMapping("/exchangerates/rates/a/chf")
    public ModelAndView chfExchangeRate() {
        String apiUrl = "http://localhost:8080/api/exchangerates/rates/a/chf/";

        RedirectView redirectView = new RedirectView(apiUrl);
        return new ModelAndView(redirectView);
    }

    private BigDecimal getGoldPrice() {
        String apiUrl = "http://api.nbp.pl/api/cenyzlota";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(apiUrl);

            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());

            JSONArray jsonArray = new JSONArray(responseBody);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.getBigDecimal("cena");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
