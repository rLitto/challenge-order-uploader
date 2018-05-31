package com.igindex.challenge.io;

import com.igindex.challenge.application.DestinationProperties;
import com.igindex.challenge.application.OrderSubmitApplication;
import com.igindex.challenge.domain.order.Order;
import com.igindex.challenge.infrastructure.XmlParser;
import com.igindex.challenge.infrastructure.configuration.DefaultDestinationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(UploadController.ORDER)
public class UploadController {
    public static final String ORDER = "/order";
    public static final String UPLOAD = "/upload";
    private static final Logger log = LoggerFactory.getLogger(UploadController.class);
    private final DefaultDestinationProperties defaultProperties;
    private final XmlSanitizer sanitizer;
    private final XmlParser fileParser;
    private final OrderSubmitApplication application;

    public UploadController(DefaultDestinationProperties defaultProperties, XmlSanitizer sanitizer, XmlParser fileParser, OrderSubmitApplication application) {
        this.defaultProperties = defaultProperties;
        this.sanitizer = sanitizer;
        this.fileParser = fileParser;
        this.application = application;
    }


    @GetMapping(UPLOAD)
    public String upload(Model model) {
        model.addAttribute("configuration", defaultProperties);
        return "upload";
    }

    @PostMapping(UPLOAD)
    public String uploadSubmit(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @Validated @ModelAttribute("configuration") DestinationProperties destination,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (!bindingResult.hasErrors()) {
            try {
                Assert.isTrue(!file.isEmpty(), "File must not be empty");
                final String xml = sanitizer.sanitize(file.getInputStream());
                final List<Order> orders = fileParser.parseString(xml);
                application.submit(destination, orders.toArray(new Order[]{}));
                String result = String.format("Successfully uploaded %s to %s: %s", file.getOriginalFilename(), destination.getType().toString(), destination.getName());
                redirectAttributes.addFlashAttribute("message", result);
                return "redirect:" + ORDER + UPLOAD;
            } catch (Exception e) {
                log.error("Unable to upload", e);
                String error = String.format("Unable to upload %s to %s: %s. %s", file.getOriginalFilename(), destination.getType().toString(), destination.getName(), e.getLocalizedMessage());
                model.addAttribute("message", error);
                model.addAttribute("failure", true);
            }
        }
        return "upload";

    }
}
