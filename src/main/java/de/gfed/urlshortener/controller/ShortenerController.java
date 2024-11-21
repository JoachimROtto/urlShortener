package de.gfed.urlshortener.controller;
import de.gfed.urlshortener.model.FormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import de.gfed.urlshortener.model.Shortener;
import de.gfed.urlshortener.model.ShortenerRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.*;
import java.util.regex.Pattern;

@RestController
public class ShortenerController {

    @Autowired
    private final ShortenerRepository repo;

    ShortenerController(ShortenerRepository repo){
        this.repo=repo;
    }

    @CrossOrigin @PostMapping(path ="/shorten")
    String requestShortener(@RequestBody Message url){
        return getOrCreateShortener(url.text()).getShortID();
    }

    @CrossOrigin @GetMapping("/{id}")
    String targetUrl(@PathVariable String id){
        return getForwarding(id,true);
    }

    @RequestMapping(value="/shortenui/add", method= RequestMethod.POST)
    public ModelAndView addShortener(@ModelAttribute FormData form, Model model){
        Shortener result = getOrCreateShortener(form.getText());
        return new ModelAndView("success")
                .addObject("back",
                        linkTo(methodOn(ShortenerController.class).shortenerAdded(model)).withRel("back"))
                .addObject("result",
                        result  );
    }

    @RequestMapping(value="/ui/shortenui", method= RequestMethod.GET)
    public ModelAndView shortenerAdded(Model model){
        model.addAttribute("formdata", new FormData(""));
        return new ModelAndView("shorten")
                .addObject("createLink", linkTo(
                        methodOn(ShortenerController.class).addShortener(new FormData(""), model))
                        .withRel("create"));
    }

    Shortener getOrCreateShortener(String url){
        if ( !isValidURL(url) )
            throw new ResponseStatusException(
                    HttpStatus.EXPECTATION_FAILED);
        Shortener result = repo.findById(url).orElse(null);
        if (result==null){
            result=new Shortener(url, computeKey(url));
            repo.save(result);
        }
        return result;
    }

    public String computeKey(String url){
        String id= UUID.randomUUID().toString().substring(0, Shortener.ID_LENGTH);
        while (!repo.findFirstByShortID(id).isEmpty())
            id= UUID.randomUUID().toString().substring(0, Shortener.ID_LENGTH);
        return id;
    }

    String getForwarding(String id, boolean withHTMLCorpus){
        List<Shortener> result= repo.findFirstByShortID(id);
        String htmlCorpus = withHTMLCorpus?"<!DOCTYPE html>" +
                "<head>\n" +
                "  <meta http-equiv=\"refresh\" content=\"0;url=%s\">" +
                "</head></html>"
                :"%s";
        return result.isEmpty()?"Wrong ID!":String.format(htmlCorpus,result.getFirst().getOriginalURL());
    }

    boolean isValidURL(String url){
        if (!url.toLowerCase().startsWith("https://"))
            return false;
        Pattern pattern= Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        return pattern.matcher(url).matches();
    }
    public record Message(String text){}
}