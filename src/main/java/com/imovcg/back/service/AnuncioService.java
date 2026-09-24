package com.imovcg.back.service;

import com.imovcg.back.dto.AnuncioDTO;
import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.model.Anunciante;
import com.imovcg.back.model.Imovel;
import com.imovcg.back.model.ImovelFoto;
import com.imovcg.back.repository.ImovelRepository;
import com.imovcg.back.util.ImovelHash;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Anuncios cadastrados a mao no imovCG. Cada operacao so enxerga os imoveis do proprio anunciante.
 */
@Service
@Transactional
public class AnuncioService {

    /** Marca o inventario proprio, separando-o do que vem da OLX e do Facebook. */
    public static final String FONTE_PROPRIA = "imovcg";

    private final ImovelRepository imovelRepository;
    private final AuthService authService;

    public AnuncioService(ImovelRepository imovelRepository, AuthService authService) {
        this.imovelRepository = imovelRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<ImovelGetDTO> listarMeus(Long anuncianteId) {
        return imovelRepository.findByAnuncianteIdOrderByCreatedAtDesc(anuncianteId)
                .stream()
                .map(ImovelGetDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ImovelGetDTO getMeu(Long anuncianteId, Long imovelId) {
        return new ImovelGetDTO(buscarDoDono(anuncianteId, imovelId));
    }

    public ImovelGetDTO criar(Long anuncianteId, AnuncioDTO dto) {
        Anunciante anunciante = authService.buscarAnunciante(anuncianteId);

        Imovel imovel = new Imovel();
        imovel.setAnunciante(anunciante);
        imovel.setFonte(FONTE_PROPRIA);
        imovel.setExternalId(UUID.randomUUID().toString());
        imovel.setDataColeta(LocalDate.now());

        aplicar(imovel, dto);
        imovelRepository.save(imovel);

        return new ImovelGetDTO(imovel);
    }

    public ImovelGetDTO atualizar(Long anuncianteId, Long imovelId, AnuncioDTO dto) {
        Imovel imovel = buscarDoDono(anuncianteId, imovelId);

        aplicar(imovel, dto);
        imovelRepository.save(imovel);

        return new ImovelGetDTO(imovel);
    }

    public void excluir(Long anuncianteId, Long imovelId) {
        imovelRepository.delete(buscarDoDono(anuncianteId, imovelId));
    }

    private Imovel buscarDoDono(Long anuncianteId, Long imovelId) {
        return imovelRepository.findByIdAndAnuncianteId(imovelId, anuncianteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Anuncio nao encontrado: " + imovelId));
    }

    private void aplicar(Imovel imovel, AnuncioDTO dto) {
        imovel.setTitulo(dto.getTitulo());
        imovel.setPreco(dto.getPreco());
        imovel.setEndereco(dto.getEndereco());
        imovel.setBairro(dto.getBairro());
        imovel.setCidade(dto.getCidade() == null || dto.getCidade().isBlank()
                ? "Campina Grande" : dto.getCidade());
        imovel.setEstado(dto.getEstado() == null || dto.getEstado().isBlank()
                ? "PB" : dto.getEstado());
        imovel.setTipoAnuncio(dto.getTipoAnuncio());
        imovel.setCategoria(dto.getCategoria());
        imovel.setLatitude(dto.getLatitude());
        imovel.setLongitude(dto.getLongitude());
        imovel.setQuartos(dto.getQuartos());
        imovel.setBanheiros(dto.getBanheiros());
        imovel.setAreaM2(dto.getAreaM2());
        imovel.setCondominio(dto.getCondominio());
        imovel.setIptu(dto.getIptu());
        imovel.setVagas(dto.getVagas());
        imovel.setDescricao(dto.getDescricao());

        sincronizarFotos(imovel, dto.getFotos());

        imovel.setHash(ImovelHash.gerarHash(imovel));
    }

    private void sincronizarFotos(Imovel imovel, List<String> fotos) {
        imovel.getFotos().clear();

        if (fotos == null || fotos.isEmpty()) {
            return;
        }

        int ordem = 0;
        for (String url : fotos) {
            if (url == null || url.isBlank()) {
                continue;
            }

            ImovelFoto foto = new ImovelFoto();
            foto.setImovel(imovel);
            foto.setUrl(url.trim());
            foto.setOrdem(ordem++);
            imovel.getFotos().add(foto);
        }
    }
}
