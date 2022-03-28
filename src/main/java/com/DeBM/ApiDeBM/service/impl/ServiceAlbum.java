package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.repository.IRepositoryAlbum;
import com.DeBM.ApiDeBM.repository.IRepositoryArtisti;
import com.DeBM.ApiDeBM.repository.IRepositoryBrani;
import com.DeBM.ApiDeBM.repository.IRepositoryGeneri;
import com.DeBM.ApiDeBM.service.Interface.InterfaceAlbum;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ServiceAlbum implements InterfaceAlbum {

    @Autowired
    private IRepositoryAlbum iRepositoryAlbum;
    @Autowired
    private IRepositoryArtisti iRepositoryArtisti;
    @Autowired
    private IRepositoryBrani iRepositoryBrani;
    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;

    @Override
    public Album inserisciAlbum(AlbumDTO dto) {

        Album album = new Album();
        album.setIdAlbum(dto.getIdAlbum());
        if (dto.getNome() == null)
            return null;
        else
            album.setNome(dto.getNome());

        if (dto.getDataPubblicazione() == null)
            return null;
        else
            album.setDataPubblicazione(dto.getDataPubblicazione());

        if (dto.getDescrizione() == null)
            return null;
        else
            album.setDescrizione(dto.getDescrizione());

        if (dto.getCasaDiscografica() == null)
            return null;
        else
            album.setCasaDiscografica(dto.getCasaDiscografica());

        if (dto.getUrlCopertina() == null)
            return null;
        else
            album.setUrlCopertina(dto.getUrlCopertina());

        if (dto.getApprovato() == null)
            return null;
        else
            album.setApprovato(dto.getApprovato());

        if (dto.getDiscoOro() < 0)
            return null;
        else
            album.setDiscoOro(dto.getDiscoOro());

        if (dto.getDiscoPlatino() < 0)
            return null;
        else
            album.setDiscoPlatino(dto.getDiscoPlatino());

        for (ArtistiDTO artista : dto.getListArtisti()) {
            Optional<Artisti> artisti = iRepositoryArtisti.findById(artista.getIdArtista());
            if (artisti.isPresent()) {
                iRepositoryAlbum.save(album); //le informazioni sono uguali a quelle salvate
                artisti.get().getListAlbum().add(album);
                iRepositoryArtisti.save(artisti.get());
            } else {
                return null;
            }
        }
        return album;
    }

    @Override
    public Album inserisciAlbumWithSongs(AlbumDTO dto) {

        Album album = new Album();
        album.setIdAlbum(dto.getIdAlbum());
        if (dto.getNome() == null)
            return null;
        else
            album.setNome(dto.getNome());

        if (dto.getDataPubblicazione() == null)
            return null;
        else
            album.setDataPubblicazione(dto.getDataPubblicazione());

        if (dto.getDescrizione() == null)
            return null;
        else
            album.setDescrizione(dto.getDescrizione());

        if (dto.getCasaDiscografica() == null)
            return null;
        else
            album.setCasaDiscografica(dto.getCasaDiscografica());

        if (dto.getUrlCopertina() == null)
            return null;
        else
            album.setUrlCopertina(dto.getUrlCopertina());

        if (dto.getApprovato() == null)
            album.setApprovato(false);
        else
            album.setApprovato(dto.getApprovato());

        if (dto.getDiscoOro() < 0)
            return null;
        else
            album.setDiscoOro(dto.getDiscoOro());

        if (dto.getDiscoPlatino() < 0)
            return null;
        else
            album.setDiscoPlatino(dto.getDiscoPlatino());

        for (ArtistiDTO artista : dto.getListArtisti()) {
            Optional<Artisti> artisti = iRepositoryArtisti.findById(artista.getIdArtista());
            if (artisti.isPresent()) {
                Set<Brani> listBrani = new ModelMapper().map(dto.getBraniList(), new TypeToken<Set<Brani>>() {
                }.getType());
                for (Brani brano : listBrani) {
                    brano.setAlbum(album);
                    iRepositoryBrani.save(brano);
                }
                iRepositoryAlbum.save(album); //le informazioni sono uguali a quelle salvate
                artisti.get().getListAlbum().add(album);
                iRepositoryArtisti.save(artisti.get());
            } else {
                return null;
            }
        }
        return album;
    }

    @Override
    public Album modificaAlbum(AlbumDTO dto, String oldAlbum) {
        if (oldAlbum == null)
            return null;
        Album album = iRepositoryAlbum.findByNomeIgnoreCase(oldAlbum); //cerco con quello vecchio
        //utente.setNumeriTelefonoList(dto.getNumeroTelefono());
        album.setIdAlbum(dto.getIdAlbum());  //setta lo username nuovo
        album.setNome(dto.getNome());
        album.setDataPubblicazione(dto.getDataPubblicazione());
        album.setDescrizione(dto.getDescrizione());
        album.setCasaDiscografica(dto.getCasaDiscografica());
        album.setUrlCopertina(dto.getUrlCopertina());
        album.setApprovato(dto.getApprovato());
        album.setDiscoOro(dto.getDiscoOro());
        album.setDiscoPlatino(dto.getDiscoPlatino());
        iRepositoryAlbum.save(album);
        return album;
    }

    @Override
    public Album updateAlbumById(AlbumDTO dto) {

        var optional = iRepositoryAlbum.findById(dto.getIdAlbum());//cerco con quello vecchio

        if (!optional.isPresent()) return null;

        Album album = optional.get();

        if (dto.getNome() != null && dto.getNome().length() > 0)
            album.setNome(dto.getNome());
        if (dto.getDataPubblicazione() != null)
            album.setDataPubblicazione(dto.getDataPubblicazione());
        if (dto.getDescrizione() != null && dto.getDescrizione().length() > 0)
            album.setDescrizione(dto.getDescrizione());
        if (dto.getCasaDiscografica() != null && dto.getCasaDiscografica().length() > 0)
            album.setCasaDiscografica(dto.getCasaDiscografica());
        if (dto.getUrlCopertina() != null && dto.getUrlCopertina().length() > 0)
            album.setUrlCopertina(dto.getUrlCopertina());
        if (Boolean.TRUE.equals(dto.getApprovato()))
            album.setApprovato(dto.getApprovato());
        if (dto.getDiscoOro() > 0)
            album.setDiscoOro(dto.getDiscoOro());
        if (dto.getDiscoPlatino() > 0)
            album.setDiscoPlatino(dto.getDiscoPlatino());
        iRepositoryAlbum.save(album);
        return album;
    }

    public Album approvaAlbum(Album dao) {
        dao.setApprovato(true);
        iRepositoryAlbum.save(dao);
        return dao;
    }

    @Override
    public String eliminaAlbum(String nome) {
        if (nome == null)
            return null;
        iRepositoryAlbum.delete(iRepositoryAlbum.findByNomeIgnoreCase(nome));
        return "deleted";
    }

    @Override
    public Set<Album> getAllAlbum() {
        Set<Album> listaAlbum = new HashSet<>(iRepositoryAlbum.findAll());
        if (listaAlbum.isEmpty()) //se è vuota
            return null;
        return listaAlbum;
    }

    @Override
    public Album getAlbum(String nome) {
        if (nome == null)
            return null;
        return iRepositoryAlbum.findByNomeIgnoreCase(nome);
    }

    @Override
    public Set<Album> getAlbumByIdAutore(int id) {
        return iRepositoryAlbum.findAlbumByListArtisti_IdArtista(id);
    }

    @Override
    public Album getAlbumWithBraniById(int id) {
        return iRepositoryAlbum.findAlbumAndBraniByIdAlbum(id);
    }

    @Override
    public Set<Album> getAlbumNotApprovedWithBrani(boolean approvato) {
        return iRepositoryAlbum.findAlbumByApprovato(approvato);
    }

    @Override
    public Set<Album> getAlbumNotApprovedWithBraniById(boolean approvato, int id) {
        return iRepositoryAlbum.findAlbumByApprovatoAndListArtisti_IdArtista(approvato, id);
    }

    @Override
    public Set<Album> getAlbumBySearchValue(String searchValue,Pageable page) {
        return iRepositoryAlbum.findAlbumByNomeContainingIgnoreCase(searchValue,page).toSet();
    }

    @Override
    public Set<Album> getAlbumNotApprovedBySearchValue(String searchValue,Pageable page) {
        return iRepositoryAlbum.findAlbumByNomeContainingIgnoreCaseAndApprovato(searchValue, false,page).toSet();
    }

    @Override
    public Set<Album> getAlbumMoreListened(Pageable page) {
        return iRepositoryAlbum.findByOrderByDiscoPlatinoDesc(page).toSet();
    }

    @Override
    public Set<Album> getTopAlbumByIdArtista(int id) {
        return iRepositoryAlbum.findTop5AlbumByListArtisti_IdArtista(id);
    }

    @Override
    @Transactional
    public String eliminaAlbumById(Integer id) {
        if(id == 0)
            return null;

        Artisti artista = iRepositoryArtisti.findByListAlbum_IdAlbum(id);

        for(Album album: artista.getListAlbum()){
            if(album.getIdAlbum()==id) {
                for(Brani brani: album.getBraniList()){
                    for(Generi generi : brani.getListGeneri()){
                        generi.getListBrani().removeAll(album.getBraniList());
                    }
                }
                artista.getListAlbum().remove(album);
                break;
            }
        }

        iRepositoryArtisti.save(artista);
        iRepositoryBrani.deleteByAlbum_IdAlbum(id);
        iRepositoryAlbum.deleteById(id);
        return "deleted";
    }
}