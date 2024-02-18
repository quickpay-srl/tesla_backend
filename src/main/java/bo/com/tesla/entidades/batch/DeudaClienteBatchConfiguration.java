package bo.com.tesla.entidades.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;

@Configuration
@EnableBatchProcessing
public class DeudaClienteBatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	private static final String OVERRIDDEN_BY_EXPRESSION = null;
	
		
	@Bean
    @JobScope
	public StepItemReadListener stepItemReadListener(@Value("#{jobParameters[archivoId]}") Long archivoId) {
		return new StepItemReadListener(archivoId);
		
	}
	
	@Bean
    @JobScope
	public StepItemWriteListener stepItemWriteListener(@Value("#{jobParameters[archivoId]}") Long archivoId) {
		return new StepItemWriteListener(archivoId);
		
	}
	

	@Bean
	@JobScope
	public FlatFileItemReader<DeudaClienteEntity> reader(@Value("#{jobParameters[pathToFile]}") String pathToFile) {
		return new FlatFileItemReaderBuilder<DeudaClienteEntity>()
				.name("deudaClienteItemReader")
				.resource(new FileSystemResource(pathToFile))
				.delimited()
				.delimiter("|")				
				.names(new String[] { "nroRegistro", "codigoCliente", "nombreCliente", "nroDocumento", "direccion",
						"telefono", "nit", "tipoServicio", "servicio", "periodo", "tipo", "concepto", "montoUnitario",
						"cantidad","subTotal", "datoExtras", "tipoComprobante", "periodoCabecera","esPostpago","codigoActividadEconomica","correoCliente" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<DeudaClienteEntity>() {
					{
						setTargetType(DeudaClienteEntity.class);
					}
				})
				.build();
	}

	

	@Bean
	@JobScope
	public JdbcBatchItemWriter<DeudaClienteEntity> writer(DataSource dataSource,@Value("#{jobParameters[archivoId]}") Long archivoId) {
		return new JdbcBatchItemWriterBuilder<DeudaClienteEntity>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO tesla.deudas_clientes"
						+ "(archivo_id, nro_registro, codigo_cliente, nombre_cliente, nro_documento, direccion, nit, telefono,tipo_servicio,servicio , periodo, tipo, concepto, cantidad, monto_unitario, sub_total, dato_extras, tipo_comprobante,  periodo_cabecera,es_postpago,codigo_actividad_economica,correo_cliente) "
						+ "VALUES('"+archivoId+"', :nroRegistro, :codigoCliente, :nombreCliente, :nroDocumento, :direccion, :nit, :telefono,:tipoServicio ,:servicio , :periodo, :tipo, :concepto, :cantidad, :montoUnitario, :subTotal, :datoExtras, :tipoComprobante,:periodoCabecera,:esPostpago,:codigoActividadEconomica,:correoCliente);"
						+ "")
				
				.dataSource(dataSource)
				.build();
	}
	
	@Bean
	public Step deudaClienteStep(
			JdbcBatchItemWriter<DeudaClienteEntity> writer,			
			StepItemReadListener stepItemReadListener,
			StepItemWriteListener stepItemWriteListener
			) {
		return stepBuilderFactory.get("deudaClienteStep")
				.<DeudaClienteEntity, DeudaClienteEntity>chunk(1000)
				.reader(reader(OVERRIDDEN_BY_EXPRESSION))
				.listener(stepItemReadListener)						
				.writer(writer)			
				.listener(stepItemWriteListener)							
				.build();
	}

	@Bean
	public Job deudaClienteJob( JobCompletionNotificationListener listener,Step deudaClienteStep) {
		return jobBuilderFactory
				.get("deudaClienteJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(deudaClienteStep)				
				.end()
				.build();
	}

	
}
