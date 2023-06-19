package apap.tugas.SISDM.controller;

import apap.tugas.SISDM.model.KaryawanModel;
import apap.tugas.SISDM.model.PresensiModel;
import apap.tugas.SISDM.model.TugasModel;
import apap.tugas.SISDM.service.KaryawanService;
import apap.tugas.SISDM.service.PresensiService;
import apap.tugas.SISDM.service.TugasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PresensiController {

    final LocalTime batasWaktu = LocalTime.of(7,0);

    @Qualifier("presensiServiceImpl")
    @Autowired
    PresensiService presensiService;

    @Qualifier("tugasServiceImpl")
    @Autowired
    TugasService tugasService;

    @Qualifier("karyawanServiceImpl")
    @Autowired
    KaryawanService karyawanService;

    @GetMapping("presensi")
    public String daftarPresensi(Model model) {
        List<PresensiModel> daftarPresensi = presensiService.getDaftarPresensi();
        model.addAttribute("daftarPresensi", daftarPresensi);
        return "daftar-presensi";
    }

    @GetMapping("presensi/tambah")
    public String tambahPresensiForm(Model model) {
        PresensiModel presensi = new PresensiModel();
        presensi.setListTugas(new ArrayList<>());

        List<Integer> statusOption = tugasService.getStatus();
        List<KaryawanModel> karyawanOption = karyawanService.getDaftarKaryawan();
        List<TugasModel> daftarTugasOption = tugasService.getDaftarTugas();

        model.addAttribute("presensi", presensi);
        model.addAttribute("daftarStatus", statusOption);
        model.addAttribute("daftarKaryawan", karyawanOption);
        model.addAttribute("daftarTugas", daftarTugasOption);
        return "form-tambah-presensi";
    }

    @PostMapping(value = "presensi/tambah", params = {"addRow"})
    public String addRowPresensiTugas(
            @ModelAttribute PresensiModel presensi,
            Model model
    ) {
        if (presensi.getListTugas() == null || presensi.getListTugas().size() == 0) {
            presensi.setListTugas(new ArrayList<>());
        }

        // akses list.
        // masukin elemen baru ke list. null.
        presensi.getListTugas().add(new TugasModel());

        List<TugasModel> daftarTugasOption = tugasService.getDaftarTugas();
        List<Integer> statusOption = tugasService.getStatus();
        List<KaryawanModel> karyawanOption = karyawanService.getDaftarKaryawan();

        model.addAttribute("presensi", presensi);
        model.addAttribute("daftarStatus", statusOption);
        model.addAttribute("daftarTugas", daftarTugasOption);
        model.addAttribute("daftarKaryawan", karyawanOption);

        return "form-tambah-presensi";
    }

    @PostMapping(value = "presensi/tambah", params = {"deleteRow"})
    public String deleteRowPresensiTugas(
            @ModelAttribute PresensiModel presensi,
            @RequestParam("deleteRow") Integer row,
            Model model
    ) {
        final Integer rowId = Integer.valueOf(row);
        presensi.getListTugas().remove(rowId.intValue());

        // pass lagi pilihan tugas
        List<TugasModel> daftarTugasOption = tugasService.getDaftarTugas();
        List<Integer> statusOption = tugasService.getStatus();
        List<KaryawanModel> karyawanOption = karyawanService.getDaftarKaryawan();

        model.addAttribute("presensi", presensi);
        model.addAttribute("daftarStatus", statusOption);
        model.addAttribute("daftarTugas", daftarTugasOption);
        model.addAttribute("daftarKaryawan", karyawanOption);

        return "form-tambah-presensi";
    }

    @PostMapping("presensi/tambah")
    public String tambahPresensiSubmit(
            @ModelAttribute PresensiModel presensi,
            Model model
    ) {
        // set status terlambat/engga.
        if (presensi.getWaktuMasuk().isAfter(batasWaktu)) {
            presensi.setStatus(0);
        } else {
            presensi.setStatus(1);
        }
        presensiService.addPresensi(presensi);

        // benerin tugasModel.
        for (int i=0; i<presensi.getListTugas().size(); i++) {
            TugasModel curr = presensi.getListTugas().get(i);
            TugasModel tugas = tugasService.getTugasByIdTugas(curr.getIdTugas()).get();
            if (tugas == null) {
                return "notfound-tugas";
            }
            tugas.setPresensi(presensi);
            tugasService.updateTugas(tugas);
        }

        model.addAttribute("presensi", presensi);
        return "sukses-tambah-presensi";
    }

    @GetMapping("presensi/{id}")
    public String detailPresensi(
            @PathVariable Long id,
            Model model
    ) {
        PresensiModel presensi = presensiService.getPresensiByIdPresensi(id).get();
        if (presensi == null) {
            return "notfound-presensi";
        }
        model.addAttribute("presensi", presensi);
        return "detail-presensi";
    }

    @GetMapping("presensi/{id}/ubah")
    public String updatePresensiForm(
            @PathVariable Long id,
            Model model
    ) {
        PresensiModel presensi = presensiService.getPresensiByIdPresensi(id).get();
        if (presensi == null) {
            return "notfound-presensi";
        }
        if (presensi.getListTugas() == null || presensi.getListTugas().size() == 0) {
            presensi.setListTugas(new ArrayList<>());
        }

        List<Integer> statusOption = tugasService.getStatus();
        List<KaryawanModel> karyawanOption = karyawanService.getDaftarKaryawan();
        List<TugasModel> daftarTugasOption = tugasService.getDaftarTugas();

        model.addAttribute("presensi", presensi);
        model.addAttribute("daftarStatus", statusOption);
        model.addAttribute("daftarKaryawan", karyawanOption);
        model.addAttribute("daftarTugas", daftarTugasOption);

        return "form-update-presensi";
    }

    @PostMapping(value = "presensi/{id}/ubah", params = {"addRow"})
    public String addRowUbahPresensi(
            @PathVariable Long id,
            @ModelAttribute PresensiModel presensi,
            Model model
    ) {
        if (presensi.getListTugas() == null || presensi.getListTugas().size() == 0) {
            presensi.setListTugas(new ArrayList<>());
        }
        presensi.getListTugas().add(new TugasModel());

        List<Integer> statusOption = tugasService.getStatus();
        List<KaryawanModel> karyawanOption = karyawanService.getDaftarKaryawan();
        List<TugasModel> daftarTugasOption = tugasService.getDaftarTugas();

        model.addAttribute("presensi", presensi);
        model.addAttribute("daftarStatus", statusOption);
        model.addAttribute("daftarKaryawan", karyawanOption);
        model.addAttribute("daftarTugas", daftarTugasOption);

        return "form-update-presensi";
    }

    @PostMapping(value = "presensi/{id}/ubah", params = {"deleteRow"})
    public String deleteRowUbahPresensi(
            @ModelAttribute PresensiModel presensi,
            @RequestParam("deleteRow") Integer row,
            Model model
    ) {
        final Integer rowId = Integer.valueOf(row);
        presensi.getListTugas().remove(rowId.intValue());

        List<Integer> statusOption = tugasService.getStatus();
        List<KaryawanModel> karyawanOption = karyawanService.getDaftarKaryawan();
        List<TugasModel> daftarTugasOption = tugasService.getDaftarTugas();

        model.addAttribute("presensi", presensi);
        model.addAttribute("daftarStatus", statusOption);
        model.addAttribute("daftarKaryawan", karyawanOption);
        model.addAttribute("daftarTugas", daftarTugasOption);

        return "form-update-presensi";
    }

//    @PostMapping("presensi/{id}/ubah")
    public String updatePresensiSubmit(
            @ModelAttribute PresensiModel presensi,
            @PathVariable Long id,
            Model model
    ) {
        // variabel Presensi Awal dipakai untuk nge-persist id presensi walaupun datanya berubah.

        PresensiModel presensiAwal = presensiService.getPresensiByIdPresensi(id).get();
        List<TugasModel> temp = presensiAwal.getListTugas();
        for (int i=0; i<temp.size(); i++) {
            Optional<TugasModel> curr = tugasService.getTugasByIdTugas(temp.get(i).getIdTugas());
            // ilangin presensi dari foreign key tugas.
            if (curr.get() != null) {
                TugasModel currT = curr.get();
                currT.setPresensi(null);
                tugasService.updateTugas(currT);
            }
        }

        // list baruuuuuuuuu
        presensiAwal.setListTugas(new ArrayList<>());

        // Ambil list tugas yg ada di html.
        temp = presensi.getListTugas();
        for (int i=0; i<temp.size(); i++) {
            Optional<TugasModel> curr = tugasService.getTugasByIdTugas(temp.get(i).getIdTugas());
            TugasModel currT = curr.get();
            // pair dengan presensi awal.
            currT.setPresensi(presensiAwal);
            currT.setStatus(temp.get(i).getStatus());
            currT.setDeskripsi(temp.get(i).getDeskripsi());
            currT.setNama(temp.get(i).getNama());
            currT.setStoryPoint(temp.get(i).getStoryPoint());
            tugasService.updateTugas(currT);
            presensiAwal.getListTugas().add(currT);
        }
        presensiAwal.setTanggal(presensi.getTanggal());
        presensiAwal.setWaktuMasuk(presensi.getWaktuMasuk());
        presensiAwal.setWaktuKeluar(presensi.getWaktuKeluar());
        presensiAwal.setKaryawan(presensi.getKaryawan());
        if (presensi.getWaktuMasuk().isAfter(batasWaktu)) {
            presensiAwal.setStatus(0);
        } else {
            presensiAwal.setStatus(1);
        }

        presensiService.updatePresensi(presensiAwal);
        model.addAttribute("presensi", presensi);
        return "sukses-update-presensi";
    }

    @PostMapping("presensi/{id}/ubah")
    public String updatePresensiSubmit2(
            @ModelAttribute PresensiModel presensi,
            @PathVariable Long id,
            Model model
    ) {
        // loop thru list tugas yg lama. (yg previously udh ada di db)
        // nullin foreign key setiap elemennya.
        // save dari tugasservice.

        PresensiModel presensiLama = presensiService.getPresensiByIdPresensi(id).get();
        List<TugasModel> tugasLama = presensiLama.getListTugas();
        if (tugasLama == null) {
            presensiLama.setListTugas(new ArrayList<>());
        }

        for (int i=0; i<tugasLama.size(); i++) {
            Optional<TugasModel> temp = tugasService.getTugasByIdTugas(tugasLama.get(i).getIdTugas());
            TugasModel tugas = temp.get();

            tugas.setPresensi(null);
            tugasService.updateTugas(tugas);
        }

        // loop thru list tugas yg baru.
        // set presensi ke foreign key setiap elemennya.
        // save dari tugasservice.

        List<TugasModel> tugasBaru = presensi.getListTugas();
        if (tugasBaru == null) {
            presensi.setListTugas(new ArrayList<>());
        }

        for (int i=0; i<tugasBaru.size(); i++) {
            Optional<TugasModel> temp = tugasService.getTugasByIdTugas(tugasLama.get(i).getIdTugas());
            TugasModel tugas = temp.get();

            tugas.setPresensi(presensi);
            tugasService.updateTugas(tugas);
        }

        // save presensi.

        presensiLama.setTanggal(presensi.getTanggal());
        presensiLama.setWaktuMasuk(presensi.getWaktuMasuk());
        presensiLama.setWaktuKeluar(presensi.getWaktuKeluar());
        presensiLama.setKaryawan(presensi.getKaryawan());
        if (presensi.getWaktuMasuk().isAfter(batasWaktu)) {
            presensiLama.setStatus(0);
        } else {
            presensiLama.setStatus(1);
        }
        presensiService.updatePresensi(presensiLama);
        model.addAttribute("presensi", presensi);
        return "sukses-update-presensi";
    }

}
