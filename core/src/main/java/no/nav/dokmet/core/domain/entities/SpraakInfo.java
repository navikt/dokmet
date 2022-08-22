package no.nav.dokmet.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "SPRAAK_INFO")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SpraakInfo extends AbstractDomainObject {

	private static final String SPRAAK_INFO_SEQ = "SPRAAK_INFO_SEQ";
	private static final long serialVersionUID = 7086025747405646842L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SPRAAK_INFO_SEQ)
	@SequenceGenerator(name = SPRAAK_INFO_SEQ, sequenceName = SPRAAK_INFO_SEQ, allocationSize = 1)
	@Column(name = "id", nullable = false)
	protected Long id;

	@Column(name = "spraaklag", nullable = false)
	private String spraaklag;

}
