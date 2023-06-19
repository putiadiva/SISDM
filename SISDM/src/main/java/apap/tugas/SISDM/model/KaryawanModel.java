package apap.tugas.SISDM.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "karyawan")
public class KaryawanModel {

    @Id
    @Column(name = "id_karyawan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKaryawan;

    @NotNull
    @Size(max = 255)
    @Column(name = "nama_depan")
    private String namaDepan;

    @NotNull
    @Size(max = 255)
    @Column(name = "nama_belakang")
    private String namaBelakang;

    @NotNull
    @Column(name = "jenis_kelamin")
    private Integer jenisKelamin;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @NotNull
    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "karyawan")
    private List<PresensiModel> listPresensi;

    @OneToMany(mappedBy = "karyawan")
    private List<SertifikasiKaryawanModel> listSertifikasi;

}
