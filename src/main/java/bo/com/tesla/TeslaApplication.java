package bo.com.tesla;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TeslaApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(TeslaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		
		//System.out.println("administrador:  "+bCrypt.encode("exacta.tesla_facturacion"));
		System.out.println("administrador:  "+bCrypt.encode("CARTA2023"));
	

	}
	/*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/deudaCliente/upload").allowedOrigins("http://localhost:8080").allowedMethods("GET", "POST","PUT", "DELETE");
            }
        };
    }*/

}
