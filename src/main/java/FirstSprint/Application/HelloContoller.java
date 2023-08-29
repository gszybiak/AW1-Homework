package FirstSprint.Application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class HelloContoller {

    @GetMapping("/hello")
    @ResponseBody
    public String helloName(@RequestParam String name){
        return "Hello " + name.toUpperCase();
    }

    @GetMapping("/max")
    @ResponseBody
    public Integer max(@RequestParam Integer a, @RequestParam Integer b,@RequestParam Integer c){
        return Math.max(Math.max(a, b), c);
    }

    @GetMapping("/now")
    @ResponseBody
    public String now() {
        return "Dzisiaj mamy " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                + ", teraz jest godzina " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".";
    }

    /** W przypadku braku adnotacji @Controller, @GetMapping, @ResponseBody nie odpalają się eventy GetMappingu error 404 */
    /** W przypadku gdy jest za mało error 4, za dużo bierze pod uwagę pierwsze tyle ile potrzebuję */
    /** Zły typ danych error 400 */
    /** Nie da rady odpalić Aplikacji jeśli są dwa takie same mapowania, w dwóch przypadkach to samo */
}
