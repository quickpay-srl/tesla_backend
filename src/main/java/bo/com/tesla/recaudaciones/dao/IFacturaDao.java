package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.entity.FacturaEntity;
import bo.com.tesla.recaudaciones.dto.FacturaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IFacturaDao extends JpaRepository<FacturaEntity,Long> {

    @Query(" select new bo.com.tesla.recaudaciones.dto.FacturaDto(f.facturaId,f.responseCuf,f.responseState,f.responseNroFactura, " +
            " f.responseCodigoCliente,f.responseNumeroDocumentoCliente,f.responseRazonSocialCliente,f.responseComplementoCliente,f.responseEmailCliente, " +
            " f.responsePdfRepgrafica,f.responseXmlRepgrafica,f.responseSinRepgrafica,f.responseRolloRepgrafica,f.fechaRegistro) " +
            " from FacturaEntity f INNER JOIN TransaccionCobroEntity t on t.facturaId = f.facturaId " +
            " where f.estado= 'ACTIVO' OR f.estado = 'ANULADO' and t.entidadId.entidadId = :entidadId")


    public List<FacturaDto> findAllByEntidadId(@Param("entidadId") Long entidadId);
}
