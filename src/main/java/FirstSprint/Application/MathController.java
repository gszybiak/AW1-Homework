package FirstSprint.Application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MathController {

    @GetMapping("/calculate")
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
}
