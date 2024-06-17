package bo.com.tesla.useful.cross;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.CurrencyAmount;
import com.opencsv.CSVReader;

import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.converter.UtilBase64Image;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class Util {
	public static String mensajeRow(String mensaje) {
		int input = mensaje.indexOf("input");
		int resource = mensaje.indexOf("resource");

		String newMenasje = "";

		if (resource > 0) {
			newMenasje = mensaje.substring(0, resource);
		}

		if (input > 0) {
			newMenasje = newMenasje + mensaje.substring(input, mensaje.length());
			if (newMenasje.contains("input=[]")) {
				newMenasje = newMenasje + ". El archivo contiene registros en blanco, estos no son permitidos. ";
			}
			return newMenasje.replace("Parsing error at line", "Se encontró un error en el archivo, verifique la línea ")
					.replace("in input=", ". el registro es el siguiente:\n ");
		}
		return newMenasje;
	}

	public static String causeRow(String mensaje) {

		if (mensaje.contains("nroRegistro")) {
			return "Verifique el campo “Nro de Registro” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("codigoCliente")) {
			return "Verifique el campo “Nro de Registro” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("servicio")) {
			return "Verifique el campo “Servicio” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("tipoServicio")) {
			return "Verifique el campo “Tipo de Servicio” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("periodo")) {
			return "Verifique el campo “Periodo” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("tipo")) {
			return "Verifique el campo “Tipo de Sección” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("concepto")) {
			return "Verifique el campo “Concepto” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("cantidad")) {
			return "Verifique el campo “Cantidad” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("montoUnitario")) {
			return "Verifique el campo “Monto Unitario” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("subTotal")) {
			return "Verifique el campo “Sub-Total” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}
		if (mensaje.contains("tipoComprobante")) {
			return "Verifique el campo “Tipo Comprobante” no se encuentra en el formato adecuado o no contiene ningún valor.";
		}

		return "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.";
	}

	public static Long fileDataValidatePagos(String path) throws BusinesException {
		Long rowInt = 0L;
		try {
			if (!path.contains(".csv")) {
				throw new BusinesException(
						"El archivo debe tener la extensión ‘csv’, por favor verifique la extensión del archivo y vuelva a cargarlo.");
			}
			CSVReader reader = new CSVReader(new FileReader(path), '|');
			List<String[]> allRows = reader.readAll();

			for (String[] rowString : allRows) {
				rowInt++;

				if (rowString.length <= 1) {
					throw new BusinesException("Se encontró un registro en blanco en la línea " + rowInt
							+ ", por favor verifique el archivo y vulva a cargarlo.");

				}
				if (rowString.length != 12) {

					throw new BusinesException(
							"Falta columna(s) en la línea " + rowInt + " verifique el archivo y vuelva a cargarlo.");
				}
			}
			if (rowInt == 0) {

				throw new BusinesException(
						"No se encontraron registros en el archivo, por favor verifique el contenido del archivo y vuelva a cargarlo.");
			}

		} catch (FileNotFoundException e) {
			throw new Technicalexception(e.getMessage(), e.getCause());
		} catch (IOException e) {
			throw new Technicalexception(e.getMessage(), e.getCause());
		}
		return rowInt;

	}

	public static Long fileDataValidate(String path) throws BusinesException, IOException {
		Long rowInt = 0L;
		BufferedReader br = null;
		try {

			if (!path.contains(".csv")) {
				throw new BusinesException(
						"El archivo debe tener la extensión ‘csv’, por favor verifique la extensión del archivo y vuelva a cargarlo.");
			}

			br = new BufferedReader(new FileReader(path));
			String line = br.readLine();

			while (null != line) {
				String[] fields = line.split("\\|",-1);
				rowInt++;				
				if (fields.length <= 1) {
					throw new BusinesException("Se encontró un registro en blanco en la línea " + rowInt
							+ ", por favor verifique el archivo y vuelva a cargarlo.");

				}
				/*if (fields.length != 21) {

					throw new BusinesException(
							"Falta o sobra columna(s) en la línea " + rowInt + " verifique el archivo y vuelva a cargarlo.");
				}*/
				if (fields.length != 23) {

					throw new BusinesException(
							"Falta o sobra columna(s) en la línea " + rowInt + " verifique el archivo y vuelva a cargarlo.");
				}

				line = br.readLine();
			}

			if (rowInt == 0) {

				throw new BusinesException(
						"No se encontraron registros en el archivo, por favor verifique el contenido del archivo y vuelva a cargarlo.");
			}

		} catch (FileNotFoundException e) {
			throw new Technicalexception(e.getMessage(), e.getCause());
		} catch (IOException e) {
			throw new Technicalexception(e.getMessage(), e.getCause());
		} finally {
			if (null != br) {
				br.close();
			}
		}
		return rowInt;

	}

	public static Date stringToDate(String fecha) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		} catch (Exception e) {
			throw new Technicalexception(e.getMessage(), e.getCause());

		}

	}

	public static Date formatDate(Date date) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date todayWithZeroTime = formatter.parse(formatter.format(date));
			return todayWithZeroTime;
		} catch (Exception e) {
			throw new Technicalexception(e.getMessage(), e.getCause());

		}
	}
	
	public static String formatDateLaRazon(Date date) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return formatter.format(date);
			
		} catch (Exception e) {
			throw new Technicalexception(e.getMessage(), e.getCause());

		}
	}

	public static String dateToStringFormat(Date date) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			return formatter.format(date);
		} catch (Exception e) {
			throw new Technicalexception(e.getMessage(), e.getCause());

		}
	}

	public static byte[] jasperExportFormat(JasperPrint jasperPrint, String format, String filesReport) {

		String filePath = "";
		try {
			switch (format) {
			case "pdf": // formato en pdf
				byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
				return pdf;

			case "msword":

				filePath = filesReport + "/temp/" + UUID.randomUUID().toString() + ".docx";
				JRDocxExporter exporterdoc = new JRDocxExporter();
				exporterdoc.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporterdoc.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
				exporterdoc.exportReport();

				byte[] docx = Files.readAllBytes(Paths.get(filePath));
				return docx;

			case "msexcel": // formato en excel
				filePath = filesReport + "/temp/" + UUID.randomUUID().toString() + ".xlsx";
				JRXlsxExporter exporterXls = new JRXlsxExporter();

				exporterXls.setExporterInput(new SimpleExporterInput(jasperPrint));
				File outputFileXls = new File(filePath);
				exporterXls.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFileXls));
				SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				configuration.setIgnoreGraphics(false);
				configuration.setCellHidden(false);
				configuration.setForcePageBreaks(false);

				exporterXls.setConfiguration(configuration);
				exporterXls.exportReport();
				byte[] xls = Files.readAllBytes(Paths.get(filePath));
				return xls;

			case "octet-stream": // formato en rtf

				filePath = filesReport + "/temp/" + UUID.randomUUID().toString() + ".rtf";
				JRRtfExporter rtfExporter = new JRRtfExporter();
				rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
				rtfExporter.exportReport();
				byte[] rtf = Files.readAllBytes(Paths.get(filePath));
				return rtf;

			}
		} catch (JRException | IOException e) {
			throw new Technicalexception(e.getMessage(), e.getCause());
		}
		return null;

	}

	public static String translate(String reqStr) {
		StringBuffer result = new StringBuffer();
		Locale locale = new Locale("es", "BOL");
		Currency crncy = Currency.getInstance(locale);
		String inputArr[] = StringUtils.split(new BigDecimal(reqStr).abs().toPlainString(), ".");
		RuleBasedNumberFormat rule = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
		int i = 0;
		boolean flac = true;
		for (String input : inputArr) {
			CurrencyAmount crncyAmt = new CurrencyAmount(new BigDecimal(input), crncy);
			if (i++ == 0) {
				result.append(rule.format(crncyAmt));
			} else {
				flac = false;
				result = result.append("  " + input + "/100");
			}
		}
		if (flac) {
			result = result.append("  00/100");
		}

		return result.toString().toUpperCase() + " Bs.";
	}

	public static String imgToText(String path) {

		String base64 = UtilBase64Image.encoder(path);
		String img64 = base64 != null ? "data:image/png;base64," + base64 : null;
		return img64;
	}

	public static String cleanString(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return texto;
	}
	public static String downloadFile(URL url, String fileName) throws IOException {
		byte[] data = new byte[1024];
		try (InputStream in = url.openStream();
			 BufferedInputStream bis = new BufferedInputStream(in);
			 FileOutputStream fos = new FileOutputStream(fileName)) {
			int count;
			while ((count = bis.read(data, 0, 1024)) != -1) {
				fos.write(data, 0, count);
			}
			File file = new File(fileName);
			byte [] bytes = Files.readAllBytes(file.toPath());
			return Base64.getEncoder().encodeToString(bytes);

		}catch (Exception ex){
			return null;
		}

	}
}
