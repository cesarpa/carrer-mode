package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Track;
import com.cesarpacode.carrer_mode.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tracks")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @GetMapping
    public String trackList(Model model) {
        model.addAttribute("tracks", trackService.getAll());
        return "tracks-list";
    }

    @GetMapping("/new")
    public String showAddTrackForm(Model model) {
        model.addAttribute("track", new Track());
        return "track-form";
    }

    @PostMapping
    public String saveTrack(@ModelAttribute Track track, RedirectAttributes redirectAttributes) {
        trackService.create(track);
        redirectAttributes.addFlashAttribute("message", "Track created successfully!");
        return "redirect:/tracks";
    }

    @GetMapping("/{id}/edit")
    public String showEditTrackForm(@PathVariable Long id, Model model) {
        Track track = trackService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid track Id: " + id));
        model.addAttribute("track", track);
        return "track-form";
    }

    @PostMapping("/{id}")
    public String updateTrack(@PathVariable Long id, @ModelAttribute Track track, RedirectAttributes redirectAttributes) {
        trackService.save(id, track);
        redirectAttributes.addFlashAttribute("message", "Track updated successfully!");
        return "redirect:/tracks";
    }

    @GetMapping("/{id}/delete")
    public String deleteTrack(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        trackService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Track deleted successfully!");
        return "redirect:/tracks";
    }

    // REST API endpoints
    @GetMapping("/api")
    @ResponseBody
    public Iterable<Track> getAllApi() {
        return trackService.getAll();
    }
}
