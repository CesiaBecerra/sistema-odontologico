package com.digitaldentic.sistemaodontologico.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.digitaldentic.sistemaodontologico.Entity.EstadoOdontogramaEntity;
import com.digitaldentic.sistemaodontologico.Entity.OdontogramaEntity;
import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Repository.HistorialRepository;
import com.digitaldentic.sistemaodontologico.Service.OdontogramaService;

@Controller
@RequestMapping("/odontograma")
public class OdontogramaController {

    private final OdontogramaService service;
    private final HistorialRepository historialRepo;

    public OdontogramaController(OdontogramaService service,
                                 HistorialRepository historialRepo) {
        this.service = service;
        this.historialRepo = historialRepo;
    }

    // ===========================
    // VER ODONTOGRAMA
    // ===========================
    @GetMapping("/historial/{id}")
    public String verOdontograma(@PathVariable Long id, Model model) {

        Optional<HistorialEntity> historialOpt = historialRepo.findById(id);
        if (historialOpt.isEmpty()) return "redirect:/";

        HistorialEntity historial = historialOpt.get();
        List<OdontogramaEntity> lista = service.buscarPorHistorial(id);

        model.addAttribute("historialId", id);
        model.addAttribute("odontos", lista);
        model.addAttribute("anotacion", historial.getOdontogramaAnotacion());

        return "odontograma/premium";
    }

    // ===========================
    // NUEVO
    // ===========================
    @GetMapping("/nuevo/{id}")
    public String nuevo(@PathVariable Long id, Model model) {

        Optional<HistorialEntity> historialOpt = historialRepo.findById(id);
        if (historialOpt.isEmpty()) return "redirect:/";

        OdontogramaEntity o = new OdontogramaEntity();
        o.setHistorial(historialOpt.get());

        model.addAttribute("odontograma", o);

        return "odontograma/form";
    }

    // ===========================
    // GUARDAR NORMAL
    // ===========================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute OdontogramaEntity o) {

        if (o.getHistorial() == null || o.getHistorial().getId() == null) {
            return "redirect:/";
        }

        service.guardar(o);

        return "redirect:/odontograma/historial/" + o.getHistorial().getId();
    }

    // ===========================
    // AJAX GUARDAR
    // ===========================
    @PostMapping("/ajax/guardar")
    @ResponseBody
    public ResponseEntity<String> guardarAjax(@RequestBody Map<String, String> datos) {

        try {
            Long historialId;

            try {
                historialId = Long.parseLong(datos.get("historialId"));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("ERROR");
            }

            Optional<HistorialEntity> historialOpt = historialRepo.findById(historialId);
            if (historialOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("ERROR");
            }

            OdontogramaEntity o = new OdontogramaEntity();
            o.setHistorial(historialOpt.get());
            o.setPieza(datos.get("pieza"));
            o.setSuperficie(datos.get("superficie"));
            o.setEstado(EstadoOdontogramaEntity.valueOf(datos.get("estado")));

            service.guardar(o);

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ERROR");
        }
    }

    // ===========================
    // GUARDAR ANOTACIÓN
    // ===========================
    @PostMapping("/ajax/guardar-anotacion")
    @ResponseBody
    public ResponseEntity<String> guardarAnotacion(@RequestBody Map<String, String> datos) {

        try {
            String idStr = datos.get("historialId");
            String imagen = datos.get("imagen");

            if (idStr == null || imagen == null || imagen.isEmpty()) {
                return ResponseEntity.badRequest().body("ERROR: datos incompletos");
            }

            Long historialId;

            try {
                historialId = Long.parseLong(idStr);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("ERROR");
            }

            Optional<HistorialEntity> historialOpt = historialRepo.findById(historialId);
            if (historialOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("ERROR: historial no encontrado");
            }

            HistorialEntity historial = historialOpt.get();
            historial.setOdontogramaAnotacion(imagen);

            historialRepo.save(historial);

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ERROR: " + e.getMessage());
        }
    }
}