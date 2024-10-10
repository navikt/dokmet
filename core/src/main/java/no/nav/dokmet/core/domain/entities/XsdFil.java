package no.nav.dokmet.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "xsd_fil")
public class XsdFil {

	private static final String XSD_FIL_SEQ = "XSD_FIL_SEQ";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XSD_FIL_SEQ)
	@SequenceGenerator(name = XSD_FIL_SEQ, sequenceName = XSD_FIL_SEQ, allocationSize = 1)
	private Long id;

	@Column(name = "brevpakke", nullable = false)
	private String brevpakke;

	@Column(name = "filsti", nullable = false)
	private String filsti;

	@Column(name = "filnavn", nullable = false)
	private String filnavn;

	@Column(name = "xsd_fil", nullable = false)
	@Lob
	private byte[] xsdfil;
}