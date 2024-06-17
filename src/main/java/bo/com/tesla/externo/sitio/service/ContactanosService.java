package bo.com.tesla.externo.sitio.service;

import bo.com.tesla.administracion.entity.QRGeneradoEntity;
import bo.com.tesla.useful.constant.PlantillaEmail;
import bo.com.tesla.useful.cross.SendEmail;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ContactanosService {

    @Value("${tesla.mail.correoEnvio}")
    private String correoEnvio;

    @Autowired
    private SendEmail sendEmail;



    public ResponseDto envioCorreoContactanos(Map datosContacto) {
        ResponseDto res = new ResponseDto();
        try {
            String nombre =  datosContacto.get("nombre")+"";
            String correo =  datosContacto.get("correo")+"";
            String asunto =  datosContacto.get("asunto")+"";
            String mensaje =  datosContacto.get("mensaje")+"";
            String plantillaCorreo = PlantillaEmail.plantillaContactanos(nombre,correo, asunto,mensaje);
            return this.sendEmail.sendMail(correoEnvio, "contactos@quickpay.com.bo", "Quick Pay CONTACTANOS", plantillaCorreo);

        }catch (Exception ex){
            res.status = false;
            res.message = "No se pudo entregar el mensaje  ";
            return res;
        }
    }




}
