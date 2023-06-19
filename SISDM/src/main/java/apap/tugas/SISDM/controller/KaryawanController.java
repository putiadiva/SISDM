package apap.tugas.SISDM.controller;

import apap.tugas.SISDM.model.KaryawanModel;
import apap.tugas.SISDM.model.SertifikasiKaryawanModel;
import apap.tugas.SISDM.model.SertifikasiModel;
import apap.tugas.SISDM.service.KaryawanService;
import apap.tugas.SISDM.service.SertifikasiKaryawanService;
import apap.tugas.SISDM.service.SertifikasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class KaryawanController {

    @Qualifier("karyawanServiceImpl")
    @Autowired
    private KaryawanService karyawanService;

    @Qualifier("sertifikasiServiceImpl")
    @Autowired
    private SertifikasiService sertifikasiService;

    @Qualifier("sertifikasiKaryawanServiceImpl")
    @Autowired
    private SertifikasiKaryawanService sertifikasiKaryawanService;

    @GetMapping("karyawan")
    public String daftarKaryawan(Model model) {
        List<KaryawanModel> daftarKaryawan = karyawanService.getDaftarKaryawan();
        model.addAttribute("daftarKaryawan", daftarKaryawan);
        return "daftar-karyawan";
    }

    @GetMapping("karyawan/tambah")
    public String tambahKaryawanForm(Model model) {
//        Aim:
//        Pass ke presentation layer: karyawan, pilihan sertifikasi.

        KaryawanModel karyawan = new KaryawanModel();
        List<SertifikasiKaryawanModel> daftarSertifikasiKaryawanBaru = new ArrayList<>();
        karyawan.setListSertifikasi(daftarSertifikasiKaryawanBaru);
        karyawan.getListSertifikasi().add(new SertifikasiKaryawanModel());

        List<SertifikasiModel> daftarSertifikasi = sertifikasiService.getDaftarSertifikasi();

        model.addAttribute("karyawan", karyawan);
        model.addAttribute("daftarSertifikasiExisting", daftarSertifikasi);
        return "form-tambah-karyawan";
    }

    private String generateNoSertifikat(SertifikasiKaryawanModel sk) {
        System.out.println("========================================");
        System.out.println(sk.getKaryawan().getTanggalLahir().toString());
        System.out.println("========================================");
        String mm = sk.getKaryawan().getTanggalLahir().toString().substring(5,7);
        String dd = sk.getKaryawan().getTanggalLahir().toString().substring(8,10);
        String ddmmLahir = dd + mm;
        int ddmmLahirInt = Integer.parseInt(ddmmLahir);

        String mmAmbil = sk.getTanggalPengambilan().toString().substring(5,7);
        String ddAmbil = sk.getTanggalPengambilan().toString().substring(8,10);
        String ddmmAmbil = dd+mm;
        int ddmmAmbilInt = Integer.parseInt(ddmmAmbil);

        char hurufSerti = sk.getSertifikasi().getNama().charAt(0);
        int asciiSerti = (int) hurufSerti;
        String urutanHurufSerti = String.format("%02d", (asciiSerti-64));

        char hurufNama = sk.getKaryawan().getNamaDepan().charAt(0);
        int asciiNama = (int) hurufNama;
        String urutanHurufNama = String.format("%02d", (asciiNama-64));

        String idKar = String.format("%02d", (sk.getKaryawan().getIdKaryawan()));

        String s1 = String.format("%04d", ddmmLahirInt + ddmmAmbilInt);
        String s2 = urutanHurufSerti;
        String s3 = urutanHurufNama;
        String s4 = idKar;
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        String noSertifikat = String.format("SER%s%s%s%s", s1, s2, s3, s4);

        return noSertifikat;
    }

    @PostMapping(value = "karyawan/tambah", params = {"save"})
    public String tambahKaryawanSubmit(
            @ModelAttribute KaryawanModel karyawan,
            Model model
    ) {
        if (karyawan.getListSertifikasi() == null) {
            karyawan.setListSertifikasi(new ArrayList<>());
        }
        // Misal ada 1 karyawan dan n sertifikasi.
        // Save sertifikasi (perubahan di listKaryawan) sebanyak n kali.
        // Save karyawan (baru).
        // Save sertifikasi karyawan (baru) sebanyak n kali.
        // ============================================================

        // Save karyawan. Yg null biarin null.
        // Yg penting SK bisa punya foreign key.
        karyawanService.addKaryawan(karyawan);

        for (int i=0; i<karyawan.getListSertifikasi().size(); i++) {
            // Isi list di atas adalah sertifikasikaryawan.
            // Punya idSertifikasi tapi belum punya nama sertifikasi.

            // Refer setiap elemen.
            SertifikasiKaryawanModel currPair = karyawan.getListSertifikasi().get(i);

            // Ambil objek sertifikasi dari db, by id elemen tsb.
            SertifikasiModel temp = currPair.getSertifikasi();
            SertifikasiModel currSertifikasi = sertifikasiService.getSertifikasiByIdSertifikasi(temp.getIdSertifikasi());

            // Di objek sertifikasi tsb, masukin karyawan ke listKaryawan.
            // Save sertifikasi.
            if (currSertifikasi.getListKaryawan() == null) {
                currSertifikasi.setListKaryawan(new ArrayList<>());
            }
            currSertifikasi.getListKaryawan().add(currPair);

            // Di elemen yg skrg:
            // Isi atribut karyawan dan sertifikasi.
            // Isi atribut tanggal. --> udah ke-bind di template
            // Isi atribut noSertifikat.
            currPair.setKaryawan(karyawan);
            currPair.setSertifikasi(currSertifikasi);
            currPair.setNoSertifikasi(
                    generateNoSertifikat(currPair)
            );

            // Save sertifikasikaryawan.
            sertifikasiKaryawanService.tambahSertifikasiKaryawan(currPair);

        }

        // Save


        model.addAttribute("karyawan", karyawan);
        return "sukses-tambah-karyawan";
    }

    @PostMapping(value = "karyawan/tambah", params = {"addRow"})
    public String addRowSertifikasiMultiple(
            @ModelAttribute KaryawanModel karyawan,
            Model model
    ) {
        if (karyawan.getListSertifikasi() == null || karyawan.getListSertifikasi().size() == 0) {
            karyawan.setListSertifikasi(new ArrayList<>());
        }
        karyawan.getListSertifikasi().add(new SertifikasiKaryawanModel());
        List<SertifikasiKaryawanModel> daftarSertifikasi = karyawan.getListSertifikasi();
        List<SertifikasiModel> daftarSertifikasiExisting = sertifikasiService.getDaftarSertifikasi();

        // Pass yang udah dipilih sebelumnya,
        // dan pass semua pilihan.
        model.addAttribute("karyawan", karyawan);
//        model.addAttribute("daftarSertifikasi", daftarSertifikasi);
        model.addAttribute("daftarSertifikasiExisting", daftarSertifikasiExisting);
        return "form-tambah-karyawan";
    }

    @PostMapping(value = "karyawan/tambah", params = {"deleteRow"})
    public String deleteRowSertifikasiMultiple (
        @ModelAttribute KaryawanModel karyawan,
        @RequestParam("deleteRow") Integer row,
        Model model
    ) {
        final Integer rowId = Integer.valueOf(row);
        karyawan.getListSertifikasi().remove(rowId.intValue());

        List<SertifikasiModel> daftarSertifikasiExisting = sertifikasiService.getDaftarSertifikasi();

        model.addAttribute("karyawan", karyawan);
        model.addAttribute("daftarSertifikasiExisting", daftarSertifikasiExisting);
        return "form-tambah-karyawan";
    }

    @GetMapping("karyawan/{id}")
    public String detailKaryawan(
            @PathVariable Long id,
            Model model
    ) {
        KaryawanModel karyawan = karyawanService.getKaryawanByIdKaryawan(id);

        model.addAttribute("karyawan", karyawan);
        model.addAttribute("sertifikasiThisKaryawan", karyawan.getListSertifikasi());
        return "detail-karyawan";
    }

    @GetMapping("karyawan/{id}/ubah")
    public String updateKaryawanForm(
            @PathVariable Long id,
            Model model
    ) {
        KaryawanModel karyawan = karyawanService.getKaryawanByIdKaryawan(id);

        if (karyawan.getListSertifikasi() == null || karyawan.getListSertifikasi().size() == 0) {
            karyawan.setListSertifikasi(new ArrayList<>());
        }

        // ternyata perubahan sertifikat bisa lgsg keubah di db??????

        List<SertifikasiModel> daftarSertifikasiExisting = sertifikasiService.getDaftarSertifikasi();

        model.addAttribute("karyawan", karyawan);
        model.addAttribute("daftarSertifikasiExisting", daftarSertifikasiExisting);
        return "form-update-karyawan";
    }

    @PostMapping(value = "karyawan/{id}/ubah", params = {"addRow"})
    public String updateKaryawanAddRow(
            @ModelAttribute KaryawanModel karyawan,
            Model model
    ) {
        if (karyawan.getListSertifikasi() == null || karyawan.getListSertifikasi().size() == 0) {
            karyawan.setListSertifikasi(new ArrayList<>());
        }
        karyawan.getListSertifikasi().add(new SertifikasiKaryawanModel());
        List<SertifikasiKaryawanModel> daftarSertifikasi = karyawan.getListSertifikasi();
        List<SertifikasiModel> daftarSertifikasiExisting = sertifikasiService.getDaftarSertifikasi();

        // Pass yang udah dipilih sebelumnya,
        // dan pass semua pilihan.
        model.addAttribute("karyawan", karyawan);
        model.addAttribute("daftarSertifikasiExisting", daftarSertifikasiExisting);
        return "form-update-karyawan";
    }

    @PostMapping(value = "karyawan/{id}/ubah", params = {"deleteRow"})
    public String updateKaryawanDeleteRow(
            @ModelAttribute KaryawanModel karyawan,
            @RequestParam("deleteRow") Integer row,
            Model model
    ) {
        final Integer rowId = Integer.valueOf(row);
        karyawan.getListSertifikasi().remove(rowId.intValue());

        List<SertifikasiModel> daftarSertifikasiExisting = sertifikasiService.getDaftarSertifikasi();

        model.addAttribute("karyawan", karyawan);
        model.addAttribute("daftarSertifikasiExisting", daftarSertifikasiExisting);
        return "form-update-karyawan";
    }

    @PostMapping(value = "karyawan/{id}/ubah", params = {"updateKaryawan"})
    public String updateKaryawanSubmit(
            @PathVariable Long id,
            @ModelAttribute KaryawanModel karyawan,
            Model model
    ) {
        KaryawanModel karyawanAwal = karyawanService.getKaryawanByIdKaryawan(id);

        // Pair karyawan-sertifikasi bisa berubah; makin banyak atau makin dikit.

        // Ubah listSertifikasi di kelas Karyawan.
        // // listSertifikasi yg sebelumnya nullin aja. Bikin baru.
        List<SertifikasiKaryawanModel> skLama = karyawanAwal.getListSertifikasi();
        karyawanAwal.setListSertifikasi(new ArrayList<>());

        // Benerin tabel SK.
        for (int i=0; i<skLama.size(); i++) {
            // akses tiap SK. hapus dari database.
            SertifikasiKaryawanModel tmp = skLama.get(i);
            sertifikasiKaryawanService.hapusSertifikasiKaryawan(tmp);

            // Ubah listKaryawan di kelas Sertifikasi.
            SertifikasiModel tempS = tmp.getSertifikasi();
            // // hapus karyawan dari kepemilikan sertifikasi.
            tempS.getListKaryawan().remove(karyawanAwal);
        }

        // Data awal udah diilangin.
        // Sekarang masukin data baru.

        if (karyawan.getListSertifikasi() == null) {
            karyawan.setListSertifikasi(new ArrayList<>());
        }

        List<SertifikasiKaryawanModel> skBaru = karyawan.getListSertifikasi();
        for (int i=0; i<skBaru.size(); i++) {
            // Isi list di atas adalah sertifikasikaryawan.
            // Punya idSertifikasi tapi belum punya nama sertifikasi.

            // Refer setiap elemen.
            SertifikasiKaryawanModel currPair = skBaru.get(i);

            // Ambil objek sertifikasi dari db, by id elemen tsb.
            SertifikasiModel tempp = currPair.getSertifikasi();
            SertifikasiModel currSertifikasi = sertifikasiService.getSertifikasiByIdSertifikasi(tempp.getIdSertifikasi());

            // Di objek sertifikasi tsb, masukin karyawan ke listKaryawan.
            // Save sertifikasi.
            if (currSertifikasi.getListKaryawan() == null) {
                currSertifikasi.setListKaryawan(new ArrayList<>());
            }
            currSertifikasi.getListKaryawan().add(currPair);

            // Di elemen yg skrg:
            // Isi atribut karyawan dan sertifikasi.
            // Isi atribut tanggal. --> udah ke-bind di template
            // Isi atribut noSertifikat.
            currPair.setKaryawan(karyawanAwal);
            currPair.setSertifikasi(currSertifikasi);
            currPair.setNoSertifikasi(
                    generateNoSertifikat(currPair)
            );

            // Save sertifikasikaryawan.
            sertifikasiKaryawanService.tambahSertifikasiKaryawan(currPair);

        }

        // benerin atribut2 si karyawan sblm masuk db.
        // ini harusnya taroh aja di serv impl.
        karyawanAwal.setNamaDepan(karyawan.getNamaDepan());
        karyawanAwal.setNamaBelakang(karyawan.getNamaBelakang());
        karyawanAwal.setJenisKelamin(karyawan.getJenisKelamin());
        karyawanAwal.setEmail(karyawan.getEmail());
        karyawanAwal.setListSertifikasi(karyawan.getListSertifikasi());
        karyawanAwal.setListPresensi(karyawan.getListPresensi());

        karyawanService.updateKaryawan(karyawanAwal);

        model.addAttribute("karyawan", karyawanAwal);

        return "sukses-update-karyawan";
    }


    @PostMapping("karyawan/{id}/hapus")
    public String deleteKaryawan(
            @PathVariable Long id,
            Model model
    ) {
        KaryawanModel karyawan = karyawanService.getKaryawanByIdKaryawan(id);
        karyawanService.deleteKaryawan(karyawan);
        return "delete-course";
    }

    @GetMapping("karyawan/{id}/insentif")
    public String jumlahInsentifKaryawan() {
        return "insentif-karyawan";
    }

    @GetMapping("/filter-sertifikasi")
    public String filterKaryawanbySertifikasi(Model model) {
        KaryawanModel karyawan = new KaryawanModel();
        List<SertifikasiModel> daftarSertifikasi = sertifikasiService.getDaftarSertifikasi();
        model.addAttribute("daftarSertifikasi", daftarSertifikasi);
        model.addAttribute("karyawan", karyawan);
        return "form-filter-karyawan";
    }

//    Request Hasil: GET, /filter-sertifikasi?id-sertifikasi={idSertifikasi}
    @PostMapping("/filter-sertifikasi")
    public String daftarKaryawanFiltered (
            @RequestParam(value = "idSertifikasi") Long idSertifikasi,
            Model model
    ) {
//        SertifikasiModel sertifikasi = sertifikasiService.getSertifikasiByIdSertifikasi(idSertifikasi);
//        Optional<KaryawanModel> karyawan = karyawanService.getFilteredKaryawan();

        return "daftar-karyawan-filtered";
    }

}
