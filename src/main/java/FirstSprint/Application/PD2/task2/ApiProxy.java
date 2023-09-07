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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@Controller
public class ApiProxy {

    @GetMapping("/cenyzlota")
    @ResponseBody
    public BigDecimal goldPrice(@RequestParam(required = false, defaultValue = "json") String format){
        BigDecimal price = getGoldPrice();
        if ("xml".equals(format)) {
            return price; //nie wiem jak sformatować na xml
        } else {
            return price;
        }
    }

    @GetMapping("/2/cenyzlota")
    @ResponseBody
    public String goldPriceFormat(){
        BigDecimal price = getGoldPrice();
        return "Cena złota w obecnej chwili wynosi " + price.toString();
    }

    @GetMapping("/cenyzlota/last/{count}")
    @ResponseBody
    public ArrayList goldPriceLast(@PathVariable Integer count){
        ArrayList<BigDecimal> price = getGoldPriceLast(count);
        return price;
    }

    @GetMapping("/exchangerates/rates/a/chf")
    @ResponseBody
    public BigDecimal goldPriceLast(){
        BigDecimal price = getCHFPrice();
        return price;
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

    private ArrayList<BigDecimal> getGoldPriceLast(Integer count) {
        String apiUrl = "http://api.nbp.pl/api/cenyzlota/last/" + count + "?format=json";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(apiUrl);

            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());

            JSONArray jsonArray = new JSONArray(responseBody);
            ArrayList<BigDecimal> prices = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                BigDecimal price = json.getBigDecimal("cena");
                prices.add(price);
            }

            return prices;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BigDecimal getCHFPrice() {
        String apiUrl = "http://api.nbp.pl/api/exchangerates/rates/a/chf?format=json";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(apiUrl);

            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());

            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray ratesArray = jsonObject.getJSONArray("rates");

            JSONObject firstRate = ratesArray.getJSONObject(0);
            return  firstRate.getBigDecimal("mid");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
