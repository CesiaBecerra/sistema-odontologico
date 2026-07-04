package com.digitaldentic.sistemaodontologico.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // ==============================
    // ✅ PAGOS POR HISTORIAL
    // ==============================
    @GetMapping("/historial/{id}")
    public String verPagos(@PathVariable Long id, Model model) {

        HistorialEntity historial = historialService.buscarPorId(id);

        model.addAttribute("historial", historial);
        model.addAttribute("pagos", pagoService.buscarPorHistorial(id));
        model.addAttribute("deuda", pagoService.deudaHistorial(id));

        return "pago/lista";
    }

    // ==============================
    // ✅ NUEVO PAGO
    // ==============================
    @GetMapping("/nuevo/{historialId}")
    public String nuevo(@PathVariable Long historialId, Model model) {

        HistorialEntity historial = historialService.buscarPorId(historialId);

        if(historial == null){
            return "redirect:/pago/lista";
        }

        Double deuda = pagoService.deudaHistorial(historialId);

        if(deuda <= 0){
            return "redirect:/pago/historial/" + historialId;
        }

        PagoEntity p = new PagoEntity();
        p.setHistorial(historial);
        p.setFechaPago(LocalDate.now());

        model.addAttribute("pago", p);
        model.addAttribute("historial", historial);
        model.addAttribute("deuda", deuda);

        return "pago/form";
    }

    // ==============================
    // ✅ GUARDAR
    // ==============================
 @PostMapping("/guardar")
public String guardar(@RequestParam("historialId") Long historialId,
                      @ModelAttribute PagoEntity p) {

    HistorialEntity historial = historialService.buscarPorId(historialId);
    p.setHistorial(historial);

    pagoService.guardar(p);

    return "redirect:/pago/historial/" + historialId;
}

    // ==============================
    // 🧾 VISTA IMPRIMIBLE
    // ==============================
    @GetMapping("/comprobante/{id}")
    public String verComprobante(@PathVariable Long id, Model model){

        PagoEntity pago = pagoService.buscarPorId(id);
        model.addAttribute("pago", pago);

        return "pago/comprobante";
    }

    // ==============================
    // 📄 PDF AUTOMÁTICO
    // ==============================
    @GetMapping("/comprobante/pdf/{id}")
    public void generarPDF(@PathVariable Long id,
                           HttpServletResponse response) throws Exception {

        PagoEntity pago = pagoService.buscarPorId(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=comprobante.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("DIGITAL DENTIC"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Comprobante: " + pago.getNumeroComprobante()));
        document.add(new Paragraph("Fecha: " + pago.getFechaPago()));
        document.add(new Paragraph("Metodo: " + pago.getMetodoPago()));
        document.add(new Paragraph("Monto: S/ " + pago.getMonto()));
        document.close();
    }

    // ==============================
    // 📊 REPORTE MENSUAL EXCEL
    // ==============================
    @GetMapping("/reporte-mensual")
    public void exportarExcel(HttpServletResponse response) throws IOException {

        List<PagoEntity> pagos = pagoService.listar();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment; filename=reporte_mensual.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Pagos");

        int rowNum = 0;

        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Comprobante");
        header.createCell(1).setCellValue("Fecha");
        header.createCell(2).setCellValue("Monto");
        header.createCell(3).setCellValue("Metodo");

        for(PagoEntity p : pagos){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getNumeroComprobante());
            row.createCell(1).setCellValue(p.getFechaPago().toString());
            row.createCell(2).setCellValue(p.getMonto());
            row.createCell(3).setCellValue(p.getMetodoPago());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // ==============================
    // 📦 CAJA DIARIA
    // ==============================
    @GetMapping("/caja")
    public String cajaDiaria(Model model){

        model.addAttribute("pagos", pagoService.pagosHoy());
        model.addAttribute("total", pagoService.totalHoy());

        return "pago/caja";
    }

    // ==============================
    // 💰 CERRAR CAJA
    // ==============================
    @GetMapping("/cerrar-caja")
    public String cerrarCaja(){
        pagoService.cerrarCaja();
        return "redirect:/pago/caja";
    }

    // ==============================
    // 📋 LISTA GENERAL
    // ==============================
    @GetMapping("/lista")
    public String lista(Model model){

        model.addAttribute("pagos", pagoService.listar());
        model.addAttribute("total", pagoService.total());
        model.addAttribute("metodos", pagoService.conteoMetodo());
        model.addAttribute("historial", null);

        return "pago/lista";
    }

    // ==============================
    // 🔎 FILTRO POR FECHAS
    // ==============================
    @GetMapping("/filtrar")
    public String filtrarPorFechas(
            @RequestParam("inicio") String inicio,
            @RequestParam("fin") String fin,
            Model model){

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);

        model.addAttribute("pagos", pagoService.pagosPorRango(fechaInicio, fechaFin));
        model.addAttribute("total", pagoService.totalPorRango(fechaInicio, fechaFin));
        model.addAttribute("metodos", pagoService.conteoMetodo());
        model.addAttribute("historial", null);

        return "pago/lista";
    }
    // ==============================
// 💳 TRATAMIENTOS PENDIENTES
// ==============================
@GetMapping("/pendientes")
public String tratamientosPendientes(Model model){

    List<HistorialEntity> historiales = historialService.listar();

    // Filtrar solo los que tienen deuda
    List<HistorialEntity> conDeuda = historiales.stream()
            .filter(h -> pagoService.deudaHistorial(h.getId()) > 0)
            .toList();

    model.addAttribute("historiales", conDeuda);

    return "pago/pendientes";
}
}