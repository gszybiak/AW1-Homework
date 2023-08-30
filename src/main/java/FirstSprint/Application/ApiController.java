package FirstSprint.Application;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.math.BigDecimal;


@RequestMapping("/api")
@Controller
public class ApiController {

    @GetMapping("/books")
    @ResponseBody
    public String bookList(@RequestParam(required = false) int id)  {
        return "<p>Książki:</p><ul><li>Adam Mickiewicz \"Pan Tadeusz\", 2020</li><li>Janina Wesołowska \"...\", 2001</li></ul>";
    }

    @GetMapping("/books/{id}")
    @ResponseBody
    public String getBookById(@PathVariable int id) {
        return "Dane książki o id = " + id;
    }

//    @GetMapping("/books")
//    @ResponseBody
//    public String getBookByParamId(@RequestParam int id){
//        return "Dane książki o id = " + id;
//    }

    /** nie mogą być dwa przypadki z takim samym mappingiem mimo różnych parametrów
     *  Proszę o podpowiedź jak sprowadzić te 5 metod, aby każda mogła obsługiwać dane adresy URL jednocześnie
     *  Osobno działają bez problemu, razem niestety nie
     * */

//    @GetMapping("/books")
//    @ResponseBody
//    public String getBooksByAuthor(@RequestParam String author) {
//        return "Dane książki o tytule \"" + author + "\"";
//    }

//    @GetMapping("/books")
//    @ResponseBody
//    public String getBooksByAuthor(@RequestParam String author, @RequestParam int year) {
//        return "Dane książki o tytule \"" + author + "\" z roku " + year;
//    }

    @GetMapping("/count/add/{a}/{b}")
    @ResponseBody
    public Integer add(@PathVariable Integer a, @PathVariable Integer b){
        return a+b;
    }

    @GetMapping("/count")
    @ResponseBody
    public String calculate(@RequestParam String operation, @RequestParam Double a, @RequestParam Double b) {

        switch (operation) {
            case "add":
                return String.valueOf(a + b);
            case "subtract":
                return String.valueOf(a - b);
            case "multiply":
                return String.valueOf(a * b);
            case "divide":
                if (b == 0)
                    return "Błąd: dzielenie przez 0";
                return String.valueOf(a / b);
            default:
                return "Nieobsługiwana operacja.";
        }
    }

    @PostMapping("/clipboard/save")
    public String saveToClipboard(@RequestBody String json) {
        StringSelection selection = new StringSelection(json);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        return "Zapisano tekst w schowku: " + json;
    }

    @PostMapping("/mail/send*/{to}")
    public String senEmail(@RequestBody String json) {
        StringSelection selection = new StringSelection(json);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        return "Wiadomość e-mail została wysłana.";
    }

    @GetMapping("/count/cenyzlota")
    @ResponseBody
    public BigDecimal goldPrice(@RequestParam(value = "format", defaultValue = "json") String format){
        BigDecimal price = getGoldPrice();
        return price;
    }

    @GetMapping("/2/count/cenyzlota")
    @ResponseBody
    public String goldPriceFormat(){
        BigDecimal price = getGoldPrice();
        return "Cena złota w obecnej chwili wynosi " + price.toString();
    }

    private BigDecimal getGoldPrice() {
        String apiUrl = "http://api.nbp.pl/api/cenyzlota";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(apiUrl);

            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());

            JSONObject json = new JSONObject(responseBody);
            return json.getBigDecimal("cena");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
