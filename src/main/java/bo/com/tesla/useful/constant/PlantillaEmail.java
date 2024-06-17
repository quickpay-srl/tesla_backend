package bo.com.tesla.useful.constant;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.useful.utils.FuncionesFechas;
import org.springframework.beans.factory.annotation.Value;

public class PlantillaEmail {

	@Value("${tesla.url.tesla}")
	private String teslaUrl;

	public static String plantillaCreacionUsuario(String nombreUsuario,String usuario,String password, String mensaje,String urlTesla ) {
		String bodyHtml="<!DOCTYPE html\r\n"
    			+ "    PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
    			+ "\r\n"
    			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n"
    			+ "\r\n"
    			+ "<head>\r\n"
    			+ "\r\n"
    			+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n"
    			+ "\r\n"
    			+ "    <title>Demystifying Email Design</title>\r\n"
    			+ "\r\n"
    			+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
    			+ "\r\n"
    			+ "</head>\r\n"
    			+ "\r\n"
    			+ "<body style=\"margin: 0; padding: 0;\">\r\n"
    			+ "\r\n"
    			+ "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
    			+ "        <tr>\r\n"
    			+ "            <td>\r\n"
    			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\">\r\n"
    			+ "                    <tr>\r\n"
    			+ "                        <td align=\"center\" bgcolor=\"#ffffff\" style=\"padding: 40px 0 30px 0; width: 100px; \">\r\n"
    			//+ "                            <img src=\""+ urlTesla +"+/tesla.png\" alt=\"Creating Email Magic\" style=\"display: block; width:200px\" />\r\n"
    			+ "                            <p\r\n"
    			+ "                                style=\"font-size:24px;color: rgba(0,0,0,0.87);  font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;\">\r\n"
    			+ "                                "+ nombreUsuario + " te damos la bienvenida al sistema, con tu cuenta asignada puedes ingresar al sistema QUICK PAY.\r\n"
    			
    			+ "                            </p>\r\n"
    			+ "\r\n"
    			+ "                            <hr />\r\n"
    			+ "                            <div\r\n"
    			+ "                                style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:left\">\r\n"
    			+ "                               Se han generado sus credenciales y se activó el acceso a la aplicación <a href='"+urlTesla+"'>QUICK PAY</a>.\r\n"
    			+ "                                Con tu cuenta asignada, puedes acceder a la aplicación.\r\n"
    			+ "                            </div>\r\n"
    			+ "\r\n"
    			+ "                        </td>\r\n"
    			+ "                    </tr>\r\n"
    			+ "                    <tr>\r\n"
    			+ "                        <td bgcolor=\"#ffffff\" >\r\n"
    			+ "                            <p style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);\">"
    			+ "								 "+mensaje
    			+ "								</p>\r\n"
    			+ "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
    			+ "\r\n"
    			+ "                                <tr>\r\n"
    			+ "                                    <th align=\"right\" style=\" width: 150px; font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);\">\r\n"
    			+ "                                        Nombre de Usuario :&nbsp; \r\n"
    			+ "                                    </th>\r\n"
    			+ "                                    <td>\r\n"
    			+ "                                        "+usuario+"\r\n"
    			+ "                                    </td>\r\n"
    			+ "                                </tr>\r\n"
    			+ "                                <tr>\r\n"
    			+ "                                    <th align=\"right\"  style=\" width: 150px; font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);\">\r\n"
    			+ "                                        Contraseña :&nbsp; \r\n"
    			+ "                                    </th>\r\n"
    			+ "                                    <td>\r\n"
    			+ "                                        "+password+"\r\n"
    			+ "                                    </td>\r\n"
    			+ "                                </tr>\r\n"
    			+ "                            </table>\r\n"
    			+ "\r\n"
    			+ "                        </td>\r\n"
    			+ "                    </tr>\r\n"
    			+ " 				   <tr>"
    			+ "						<td>Por seguridad, cambiar la contraseña tras ingresar por primera vez al sistema.</td>	"
    			+ "					   </tr>"
    			+ "                    <tr>\r\n"
    			+ "                        <td bgcolor=\"#FFFFFF\"\r\n"
    			+ "                            style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;color:rgba(0,0,0,0.54);font-size:11px;line-height:18px;padding-top:12px;text-align:center\">\r\n"
    			+ "                            <hr />\r\n"
    			+ "                            Te hemos enviado este correo electrónico para informarte de cambios importantes en tu cuenta\r\n"
    			+ "                            y en los servicios de QUICK PAY.\r\n"
    			//+ "                            © 2021 EXACTA SRL, LA PAZ, BOLIVIA.\r\n"
    			+ "\r\n"
    			+ "                        </td>\r\n"
    			+ "                    </tr>\r\n"
    			+ "                </table>\r\n"
    			+ "            </td>\r\n"
    			+ "        </tr>\r\n"
    			+ "    </table>\r\n"
    			+ "</body>\r\n"
    			+ "\r\n"
    			+ "</html>";
		return bodyHtml;
	}

	public static String plantillaModificacionPassord(String nombreUsuario,String usuario,String password, String mensaje,String urlTesla ) {
		String bodyHtml="<!DOCTYPE html\r\n"
    			+ "    PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
    			+ "\r\n"
    			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n"
    			+ "\r\n"
    			+ "<head>\r\n"
    			+ "\r\n"
    			+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n"
    			+ "\r\n"
    			+ "    <title>Demystifying Email Design</title>\r\n"
    			+ "\r\n"
    			+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
    			+ "\r\n"
    			+ "</head>\r\n"
    			+ "\r\n"
    			+ "<body style=\"margin: 0; padding: 0;\">\r\n"
    			+ "\r\n"
    			+ "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
    			+ "        <tr>\r\n"
    			+ "            <td>\r\n"
    			+ "                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"80%\">\r\n"
    			+ "                    <tr>\r\n"
    			+ "                        <td align=\"center\" bgcolor=\"#ffffff\" style=\"padding: 40px 0 30px 0; width: 100px; \">\r\n"
    			//+ "                            <img src=\""+ urlTesla +"/tesla.png\" alt=\"Creating Email Magic\" style=\"display: block; width:200px\" />\r\n"
    			+ "                            <p\r\n"    			+ "                                style=\"font-size:24px;color: rgba(0,0,0,0.87);  font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;\">\r\n"
    			+ "                                "+ nombreUsuario + " te damos la bienvenida al sistema, con tu cuenta asignada puedes ingresar al sistema QUICK PAY.\r\n"
    			
    			+ "                            </p>\r\n"
    			+ "\r\n"
    			+ "                            <hr />\r\n"
    			+ "                            <div\r\n"
    			+ "                                style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:left\">\r\n"
    			+ "                               Se han generado sus credenciales y se activó el acceso a la aplicación <a href='"+urlTesla+"'>QUICK PAY</a>.\r\n"
    			+ "                                Con tu cuenta asignada, puedes acceder a la aplicación.\r\n"
    			+ "                            </div>\r\n"
    			+ "\r\n"
    			+ "                        </td>\r\n"
    			+ "                    </tr>\r\n"
    			+ "                    <tr>\r\n"
    			+ "                        <td bgcolor=\"#ffffff\" >\r\n"
    			+ "                            <p style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);\">"
    			+ "								 "+mensaje
    			+ "								</p>\r\n"
    			+ "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
    			+ "\r\n"
    			+ "                                <tr>\r\n"
    			+ "                                    <th align=\"right\" style=\" width: 150px; font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);\">\r\n"
    			+ "                                        Nombre de Usuario :&nbsp; \r\n"
    			+ "                                    </th>\r\n"
    			+ "                                    <td>\r\n"
    			+ "                                        "+usuario+"\r\n"
    			+ "                                    </td>\r\n"
    			+ "                                </tr>\r\n"
    			+ "                                <tr>\r\n"
    			+ "                                    <th align=\"right\"  style=\" width: 150px; font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);\">\r\n"
    			+ "                                        Contraseña :&nbsp; \r\n"
    			+ "                                    </th>\r\n"
    			+ "                                    <td>\r\n"
    			+ "                                        "+password+"\r\n"
    			+ "                                    </td>\r\n"
    			+ "                                </tr>\r\n"
    			+ "                            </table>\r\n"
    			+ "\r\n"
    			+ "                        </td>\r\n"
    			+ "                    </tr>\r\n"
    		
    			+ "                    <tr>\r\n"
    			+ "                        <td bgcolor=\"#FFFFFF\"\r\n"
    			+ "                            style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;color:rgba(0,0,0,0.54);font-size:11px;line-height:18px;padding-top:12px;text-align:center\">\r\n"
    			+ "                            <hr />\r\n"
    			+ "                            Te hemos enviado este correo electrónico para informarte de cambios importantes en tu cuenta\r\n"
    			+ "                            y en los servicios de QUICK PAY.\r\n"
    			//+ "                            © 2021 EXACTA SRL, LA PAZ, BOLIVIA.\r\n"
    			+ "\r\n"
    			+ "                        </td>\r\n"
    			+ "                    </tr>\r\n"
    			+ "                </table>\r\n"
    			+ "            </td>\r\n"
    			+ "        </tr>\r\n"
    			+ "    </table>\r\n"
    			+ "</body>\r\n"
    			+ "\r\n"
    			+ "</html>";
		return bodyHtml;
	}
	public static String plantillaNotfClientePago(String codigo,DeudaClienteEntity deudaClienteEntity, EntidadEntity entidadEntity, String monto, String moneda, String aliasQr ) {



		/*String bodyHtml="<p>Estimado(a):</p>\n" +
				"<p>"+nombreCliente+"</p>\n" +
				"<p>Le informamos que en fecha: <strong>"+ FuncionesFechas.ObtenerFechaActuamasHora()
				+"</strong> se ha recibido el pago de <strong>"+monto+" "+moneda+" </strong>con QR&nbsp; <strong>"+aliasQr+"</strong>.</p>\n" +
				"<p>Cuando se genere la factura se notificara por este medio.</p>\n" +
				"<p>Saludos cordiales.</p>\n" +
				"<p>Este es un mensaje autom&aacute;tico y no es necesario responder.</p>\n" +
				"<p>Si tiene alguna consulta puede contactarse con los administradores de la plataforma QUICKPAY.</p>";*/




		String bodyHtml = "<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\"><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://quickpay.com.bo:8087/assets/QuickPayLogo1-83c293a3.png\" alt=\"\" width=\"200\" height=\"53\" /></span></span></p>\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">Hola "+deudaClienteEntity.getNombreCliente()+", </span></span></p>\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">Su pago se ha registrado exitosamente.</span></span></p>\n" +
				"<p>&nbsp;</p>\n" +
				"<table class=\"MsoTableGrid\" style=\"border-collapse: collapse; border: none;\" width=\"90%\" cellspacing=\"0\" align=\"center\">\n" +
				"<tbody>\n" +
				"<tr>\n" +
				"<td style=\"vertical-align: top; width: 255px; border: 1px solid black;\">\n" +
				"<p style=\"text-align: right;\"><strong><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">C&oacute;digo de transacci&oacute;n</span></span></strong></p>\n" +
				"</td>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: none; border-right: 1px solid black; border-top: 1px solid black; vertical-align: top; width: 255px;\">\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">"+codigo+"</span></span></p>\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: 1px solid black; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p style=\"text-align: right;\"><strong><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">Entidad de cobro</span></span></strong></p>\n" +
				"</td>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: none; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">"+entidadEntity.getNombre()+"</span></span></p>\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: 1px solid black; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p style=\"text-align: right;\"><strong><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">Fecha de pago</span></span></strong></p>\n" +
				"</td>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: none; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">"+FuncionesFechas.ObtenerFechaActuamasHora()+"</span></span></p>\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: 1px solid black; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p style=\"text-align: right;\"><strong><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">Concepto</span></span></strong></p>\n" +
				"</td>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: none; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">"+deudaClienteEntity.getConcepto()+"</span></span></p>\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: 1px solid black; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p style=\"text-align: right;\"><strong><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">Monto Total</span></span></strong></p>\n" +
				"</td>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: none; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">"+moneda+". "+monto+"</span></span></p>\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: 1px solid black; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p style=\"text-align: right;\"><strong><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">M&eacute;todo de Pago</span></span></strong></p>\n" +
				"</td>\n" +
				"<td style=\"border-bottom: 1px solid black; border-left: none; border-right: 1px solid black; border-top: none; vertical-align: top; width: 255px;\">\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">QR</span></span></p>\n" +
				"</td>\n" +
				"</tr>\n" +
				"</tbody>\n" +
				"</table>\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">Gracias por utilizar <a href=\"https://quickpay.com.bo:7085\">Quick Pay</a>.</span></span></p>\n" +
				"<p><span style=\"font-size: 12pt;\"><span style=\"font-family: Calibri,sans-serif;\">Saludos cordiales.</span></span></p>\n" +
				"<p>&nbsp;</p>\n" +
				"<p><span style=\"font-size: 10px;\"><em><span style=\"font-family: Calibri,sans-serif;\">Este es un mensaje autom&aacute;tico y no debe ser respondido.</span></em></span></p>\n" +
				"<p><span style=\"font-size: 10px;\"><em><span style=\"font-family: 'Calibri',sans-serif;\">Si requiere soporte con respecto a su pago, por favor cont&aacute;ctese al Whatsapp 640 74742</span></em></span></p>";
		return bodyHtml;
	}
	public static String plantillaContactanos(String nombre,String correo, String asunto, String mensaje) {

		String bodyHtml = "<p>&nbsp;</p>\n" +
				"<p style=\"text-align: center;\"><strong>NOTIFICACI&Oacute;N PARA CONTACTO</strong></p>\n" +
				"<ul>\n" +
				"<li style=\"text-align: left;\"><strong>Nombre: </strong>"+nombre+"</li>\n" +
				"<li style=\"text-align: left;\"><strong>Correo: </strong>"+correo+"</li>\n" +
				"<li style=\"text-align: left;\"><strong>Asunto:</strong>"+asunto+"</li>\n" +
				"<li style=\"text-align: left;\"><strong>Mensaje:</strong>"+mensaje+"</li>\n" +
				"</ul>\n" +
				"<p>&nbsp; &nbsp; &nbsp; &nbsp;</p>";
		return bodyHtml;
	}
}
