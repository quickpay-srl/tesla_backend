package bo.com.tesla.pagos.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
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


import bo.com.tesla.pagos.dto.PPagosDto;

@Configuration
@EnableBatchProcessing
public class AbonosClientesBatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	private static final String OVERRIDDEN_BY_EXPRESSION = null;
	
	@Bean
    @JobScope
	public StepPagosItemReadListener stepPagosItemReadListener() {
		return new StepPagosItemReadListener();
		
	}
	
	@Bean
    @JobScope
	public StepPagosItemWriteListener stepPagosItemWriteListener() {
		return new StepPagosItemWriteListener();
		
	}
	
	@Bean
	@JobScope
	public FlatFileItemReader<PPagosDto> abonoClienteItemReader(
				@Value("#{jobParameters[pathToFile]}") String pathToFile
			) {
		return new FlatFileItemReaderBuilder<PPagosDto>()
				.name("abonoClienteItemReader")
				.resource(new FileSystemResource(pathToFile))
				.delimited()
				.delimiter("|")				
				.names(new String[] { "nroRegistro", "codigoCliente", "nombreCliente", "fechaNacimientoCliente","genero",
						"nroDocumentoCliente",	"tipoDocumentoId", "extencionDocumento","concepto",
						"cantidad", "montoUnitario","periodo" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<PPagosDto>() {
					{
						setTargetType(PPagosDto.class);
					}
				})
				.build();
	}
	

	@Bean
	@JobScope
	public JdbcBatchItemWriter<PPagosDto> pagosWriter(
			DataSource dataSource,
			@Value("#{jobParameters[archivoId]}") Long archivoId ) {
		return new JdbcBatchItemWriterBuilder<PPagosDto>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql(" INSERT INTO tesla.p_beneficiarios "
						+ " (nro_registro, codigo_cliente, nombre_cliente, fecha_nacimiento_cliente,genero, nro_documento_cliente, extencion_documento_id, tipo_documento_id,concepto, cantidad, monto_unitario, periodo, archivo_id) "
						+ " VALUES(:nroRegistro,:codigoCliente, :nombreCliente, :fechaNacimientoCliente,:genero, :nroDocumentoCliente, :extencionDocumento, :tipoDocumentoId,:concepto, :cantidad, :montoUnitario, :periodo,'"+archivoId +"' ); ")
				
				.dataSource(dataSource)
				.build();
	}
	
	@Bean
	public Step pagoClienteStep(
			JdbcBatchItemWriter<PPagosDto> pagosWriter,
			StepPagosItemReadListener stepPagosItemReadListener,
			StepPagosItemWriteListener stePagospItemWriteListener) {
		return stepBuilderFactory.get("pagoClienteStep")
				.<PPagosDto, PPagosDto>chunk(1000)
				.reader(abonoClienteItemReader(OVERRIDDEN_BY_EXPRESSION))
				.listener(stepPagosItemReadListener)	
				.writer(pagosWriter)		
				.listener(stePagospItemWriteListener)
				.build();
	}
	
	@Bean
	public Job pagoClienteJob( JobCompletionPagosNotificationListener listener,Step pagoClienteStep) {
		return jobBuilderFactory
				.get("pagoClienteJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(pagoClienteStep)				
				.end()
				.build();
	}

}
