package com.digitaldentic.sistemaodontologico.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.digitaldentic.sistemaodontologico.Entity.HistorialEntity;
import com.digitaldentic.sistemaodontologico.Entity.PagoEntity;
import com.digitaldentic.sistemaodontologico.Service.HistorialService;
import com.digitaldentic.sistemaodontologico.Service.PagoService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/pago")
public class PagoController {

    private final PagoService pagoService;
    private final HistorialService historialService;

    public PagoController(PagoService pagoService, HistorialService historialService) {
        this.pagoService = pagoService;
        this.historialService = historialService;
    }

    @GetMapping("/historial/{id}")
    public String verPagos(@PathVariable Long id, Model model) {
        model.addAttribute("historial", historialService.buscarPorId(id));
        model.addAttribute("pagos", pagoService.buscarPorHistorial(id));
        model.addAttribute("deuda", pagoService.deudaHistorial(id));
        return "pago/lista";
    }

    @GetMapping("/nuevo/{historialId}")
    public String nuevo(@PathVariable Long historialId, Model model) {
        HistorialEntity historial = historialService.buscarPorId(historialId);
        if(historial == null) return "redirect:/pago/lista";

        Double deuda = pagoService.deudaHistorial(historialId);
        if(deuda <= 0) return "redirect:/pago/historial/" + historialId;

        PagoEntity p = new PagoEntity();
        p.setHistorial(historial);
        model.addAttribute("pago", p);
        model.addAttribute("historial", historial);
        return "pago/form";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam("historialId") Long historialId, @ModelAttribute PagoEntity p) {
        HistorialEntity h = historialService.buscarPorId(historialId);
        if (h != null) {
            p.setHistorial(h);
            pagoService.guardar(p);
        }
        return "redirect:/pago/historial/" + historialId;
    }

    @GetMapping("/comprobante/{id}")
    public String verComprobante(@PathVariable Long id, Model model){
        PagoEntity p = pagoService.buscarPorId(id);
        if (p == null) return "redirect:/pago/lista";
        model.addAttribute("pago", p);
        return "pago/comprobante";
    }

    @GetMapping("/comprobante/pdf/{id}")
    public void generarPDF(@PathVariable Long id, HttpServletResponse response) throws Exception {
        PagoEntity pago = pagoService.buscarPorId(id);
        if (pago == null) return;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=comprobante_" + id + ".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("DIGITAL DENTIC"));
        document.add(new Paragraph("Comprobante: " + pago.getNumeroComprobante()));
        document.add(new Paragraph("Monto: S/ " + pago.getMonto()));
        document.close();
    }

    @GetMapping("/exportar-excel")
    public void exportarExcel(HttpServletResponse response) throws IOException {
        List<PagoEntity> pagos = pagoService.listar();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_mensual.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Pagos");
        int rowNum = 0;
        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Comprobante");
        header.createCell(1).setCellValue("Fecha");
        header.createCell(2).setCellValue("Monto");
        for(PagoEntity p : pagos){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getNumeroComprobante());
            row.createCell(1).setCellValue(p.getFechaPago() != null ? p.getFechaPago().toString() : "");
            row.createCell(2).setCellValue(p.getMonto());
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/caja")
    public String cajaDiaria(Model model){
        model.addAttribute("pagos", pagoService.pagosHoy());
        model.addAttribute("total", pagoService.totalHoy());
        return "pago/caja";
    }

    @PostMapping("/cerrar-caja")
    public String cerrarCaja(){
        pagoService.cerrarCaja();
        return "redirect:/pago/caja";
    }

    @GetMapping("/lista")
    public String lista(Model model){
        model.addAttribute("pagos", pagoService.listar());
        model.addAttribute("total", pagoService.total());
        model.addAttribute("metodos", pagoService.conteoMetodo());
        return "pago/lista";
    }

    @GetMapping("/filtrar")
    public String filtrarPorFechas(@RequestParam(value="inicio", required=false) String inicio,
                                   @RequestParam(value="fin", required=false) String fin, Model model){
        try {
            LocalDate fInicio = (inicio != null) ? LocalDate.parse(inicio) : LocalDate.now().minusMonths(1);
            LocalDate fFin = (fin != null) ? LocalDate.parse(fin) : LocalDate.now();
            model.addAttribute("pagos", pagoService.pagosPorRango(fInicio, fFin));
            model.addAttribute("total", pagoService.totalPorRango(fInicio, fFin));
        } catch (DateTimeParseException e) {
            return "redirect:/pago/lista";
        }
        return "pago/lista";
    }

    @GetMapping("/pendientes")
    public String tratamientosPendientes(Model model){
        model.addAttribute("historiales", historialService.listar().stream()
                .filter(h -> pagoService.deudaHistorial(h.getId()) > 0).toList());
        return "pago/pendientes";
    }
}