package firstSprint.application.pd2.task1;

import firstSprint.application.pd2.task1.email.SendEmail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


@RequestMapping("/api")
@Controller
public class ApiController {

    @GetMapping("/books")
    @ResponseBody
    public String bookList()  {
        return "<p>Książki:</p><ul><li>Adam Mickiewicz \"Pan Tadeusz\", 2020</li><li>Janina Wesołowska \"...\", 2001</li></ul>";
    }

    @GetMapping("/books/{id}")
    @ResponseBody
    public String getBookById(@PathVariable int id) {
        return "Dane książki o id = " + id;
    }

    @GetMapping("/books/{author}/{year}")
    @ResponseBody
    public String getBooksByAuthor(@PathVariable String author, @PathVariable int year) {
        return "Dane książki o tytule \"" + author + "\" z roku " + year;
    }

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
        SendEmail.sendEmail("g_szybiak@wp.pl", "Spring is the best!");
        return "Wiadomość e-mail została wysłana.";
    }
}