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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "presensi")
public class PresensiModel {

    @Id
    @Column(name = "id_presensi")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPresensi;

    @NotNull
    @Column(name = "status")
    private Integer status;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "tanggal")
    private LocalDate tanggal;

    @NotNull
    @Column(name = "waktu_masuk")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime waktuMasuk;

    @NotNull
    @Column(name = "waktu_keluar")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime waktuKeluar;

    @OneToMany(mappedBy = "presensi")
    private List<TugasModel> listTugas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_karyawan", referencedColumnName = "id_karyawan")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private KaryawanModel karyawan;
}
