package com.example.imagemanagement.controller;

import com.example.imagemanagement.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ImageMvcController {

    private final ImageService imageService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("filenames", imageService.listFilenames());
        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestPart List<MultipartFile> files, RedirectAttributes redirectAttributes) {
        try {
            imageService.saveAll(files);
            redirectAttributes.addFlashAttribute(
                    "success",
                    String.format("Successfully uploaded %d images", files.size())
            );
        } catch (Exception e) {
            log.error("Failed to save image", e);
            redirectAttributes.addFlashAttribute(
                    "error",
                    String.format("Failed to upload %d images", files.size())
            );
        }
        return "redirect:/";
    }

    @PostMapping("/{filename}/delete")
    public String delete(@PathVariable String filename, RedirectAttributes redirectAttributes) {
        try {
            imageService.delete(filename);
            redirectAttributes.addFlashAttribute("success", "Successfully deleted image " + filename);
        } catch (Exception e) {
            log.error("Failed to delete image", e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete image " + filename);
        }
        return "redirect:/";
    }


    @GetMapping("/{filename}/replace")
    public String replacePage(@PathVariable String filename, Model model) {
        var image = imageService.getImage(filename);
        model.addAttribute("image", image);
        return "replace";
    }

    @PostMapping("/{filename}/replace")
    public String replace(@PathVariable String filename, @RequestPart MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            imageService.replace(filename, file);
            redirectAttributes.addFlashAttribute("success", "Successfully replaced image " + filename);
        } catch (Exception e) {
            log.error("Failed to replace image", e);
            redirectAttributes.addFlashAttribute("error", "Failed to replace image " + filename);
        }
        return "redirect:/";
    }
}
