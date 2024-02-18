package bo.com.tesla.externo.sitio.dao;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.externo.sitio.dto.SitioEntidadDto;
import bo.com.tesla.recaudaciones.dto.EntidadDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ISitioEntidadDao extends JpaRepository<EntidadEntity,Long> {
    @Query("select e "
            + " from EntidadEntity e "
            + " Where e.estado='ACTIVO' "
            + " and e.tipoEntidad.dominioId = :tipoEntidadId ")
    public List<EntidadEntity> findEntidadByTipoEntidadId(@Param("tipoEntidadId") Long tipoEntidadId);

    @Query(value = "select new bo.com.tesla.externo.sitio.dto.SitioEntidadDto(" +
            " e.entidadId,e.nombre, e.nombreComercial,e.direccion, e.telefono,e.nit,'',e.pathLogo,false) "+
            " from EntidadEntity e " +
            " where e.estado = 'ACTIVO' and e.nombre!='-' and e.tipoEntidad.dominioId =:pTipoEntidadId ")
    List<SitioEntidadDto> findByTipoEntidadId(@Param("pTipoEntidadId") Long pTipoEntidadId);

    @Query(value = "select new bo.com.tesla.externo.sitio.dto.SitioEntidadDto(" +
            " e.entidadId,e.nombre, e.nombreComercial,e.direccion, e.telefono,e.nit,'',e.pathLogo,false) "+
            " from EntidadEntity e " +
            " where e.estado = 'ACTIVO' and e.entidadId =:pEntidadId ")
    Optional<SitioEntidadDto> findByEntidadId(@Param("pEntidadId") Long pEntidadId);





}
