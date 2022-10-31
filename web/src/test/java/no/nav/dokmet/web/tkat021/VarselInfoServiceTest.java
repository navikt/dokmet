package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.exceptions.VarselInfoNotFoundException;
import no.nav.dokmet.core.repository.VarselInfoRepository;
import no.nav.dokmet.web.to.VarselInfoTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static no.nav.dokmet.web.TestUtils.createVarselInfo;
import static no.nav.dokmet.web.TestUtils.createVarselInfoTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VarselInfoServiceTest {

	@Mock
	private VarselInfoRepository varselInfoRepository;

	@Mock
	private VarselInfoMapper varselInfoMapper;

	@InjectMocks
	private VarselInfoService varselInfoService;

	private VarselInfo varselInfo;
	private VarselInfoTo varselInfoTo;


	@BeforeEach
	void setUp() {
		varselInfo = createVarselInfo();
		varselInfoTo = createVarselInfoTo();
	}

	@Test
	void shouldFindAllVarselInfo() {
		when(varselInfoRepository.findAll()).thenReturn(List.of(varselInfo));
		when(varselInfoMapper.map(varselInfo)).thenReturn(varselInfoTo);

		var result = varselInfoService.findAllVarselInfo();

		assertEquals(List.of(varselInfoTo), result);
		verify(varselInfoRepository).findAll();
		verify(varselInfoMapper).map(varselInfo);
		verifyNoMoreInteractions(varselInfoRepository);
		verifyNoMoreInteractions(varselInfoMapper);
	}

	@Test
	void shouldFindVarselInfoByVarselTypeId() {
		when(varselInfoRepository.findByVarseltypeId(any())).thenReturn(varselInfo);
		when(varselInfoMapper.map(varselInfo)).thenReturn(varselInfoTo);

		var result = varselInfoService.findVarselInfoByVarselTypeId(varselInfoTo.getVarseltypeId());

		assertEquals(varselInfoTo, result);
		verify(varselInfoRepository).findByVarseltypeId(varselInfoTo.getVarseltypeId());
		verify(varselInfoMapper).map(varselInfo);
		verifyNoMoreInteractions(varselInfoRepository);
		verifyNoMoreInteractions(varselInfoMapper);
	}

	@Test
	void shouldSaveNewVarselInfo() {
		when(varselInfoRepository.save(any())).thenReturn(varselInfo);
		when(varselInfoMapper.map(varselInfoTo)).thenReturn(varselInfo);

		var result = varselInfoService.saveNewVarselInfo(varselInfoTo);

		assertEquals(varselInfoTo.getVarseltypeId(), result);
		verify(varselInfoMapper).map(varselInfoTo);
		verify(varselInfoRepository).save(varselInfo);
		verifyNoMoreInteractions(varselInfoRepository);
		verifyNoMoreInteractions(varselInfoMapper);
	}

	@Test
	void shouldUpdateVarselInfo() {
		when(varselInfoRepository.findByVarseltypeId(varselInfoTo.getVarseltypeId())).thenReturn(varselInfo);
		when(varselInfoMapper.map(varselInfoTo, varselInfo)).thenReturn(varselInfo);
		when(varselInfoRepository.save(varselInfo)).thenReturn(varselInfo);

		var result = varselInfoService.updateVarselInfo(varselInfoTo.getVarseltypeId(), varselInfoTo);

		assertEquals(varselInfoTo.getVarseltypeId(), result);
		verify(varselInfoRepository).findByVarseltypeId(varselInfoTo.getVarseltypeId());
		verify(varselInfoMapper).map(varselInfoTo, varselInfo);
		verify(varselInfoRepository).save(varselInfo);
		verifyNoMoreInteractions(varselInfoRepository);
		verifyNoMoreInteractions(varselInfoMapper);
	}

	@Test
	void shouldThrowOnUdateVarselInfoNotFound() {
		when(varselInfoRepository.findByVarseltypeId(varselInfoTo.getVarseltypeId())).thenReturn(null);

		var result = assertThrows(VarselInfoNotFoundException.class,
				() -> varselInfoService.updateVarselInfo(varselInfoTo.getVarseltypeId(), varselInfoTo));

		assertTrue(result.getMessage().contains("Fant ikke varselInfo med varseltypeId=" + varselInfoTo.getVarseltypeId()));
	}

	@Test
	void shouldDeleteVarselInfo() {

		varselInfoService.deleteVarselInfo(varselInfoTo.getVarseltypeId());
		verify(varselInfoRepository).deleteByVarseltypeId(varselInfoTo.getVarseltypeId());
		verifyNoMoreInteractions(varselInfoRepository);
	}
}