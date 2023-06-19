package apap.tugas.SISDM.controller;

import apap.tugas.SISDM.model.KaryawanModel;
import apap.tugas.SISDM.model.TugasModel;
import apap.tugas.SISDM.service.KaryawanService;
import apap.tugas.SISDM.service.TugasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TugasController {

    @Qualifier("tugasServiceImpl")
    @Autowired
    TugasService tugasService;

    @Qualifier("karyawanServiceImpl")
    @Autowired
    KaryawanService karyawanService;

    @GetMapping("tambah-tugas")
    public String tambahTugasForm(Model model) {
        TugasModel tugas = new TugasModel();
        model.addAttribute("tugas", tugas);
        return "form-tambah-tugas";
    }

    @PostMapping("tambah-tugas")
    public String tambahTugasSubmit(
            @ModelAttribute TugasModel tugas,
            Model model
    ) {
        tugasService.addTugas(tugas);
        model.addAttribute("tugas", tugas);
        return "sukses-tambah-tugas";
    }

    @GetMapping("/filter-tugas")
    public String filterTugas(Model model) {
        List<KaryawanModel> daftarKaryawan = karyawanService.getDaftarKaryawan();

        model.addAttribute("daftarKaryawan", daftarKaryawan);

        return "form-filter-tugas";
    }

//    Request Hasil: GET, /filter-tugas?id-karyawan={idKaryawan}&status={id-status}
    @PostMapping("/filter-tugas")
    public String daftarTugasFiltered (
            @RequestParam(value = "idKaryawan") Long idKaryawan,
            @RequestParam(value = "status") Long status,
            Model model
    ) {
        return "daftar-tugas-filtered";
    }
}
