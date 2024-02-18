package bo.com.tesla.administracion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolTransferDto {
	
	public String key;
	public String title;
	public String description;
	
	public RolTransferDto(Long key, String title, String description) {

		if(key!=null) {
			this.key = key.toString();	
		}		
		this.title = title;
		this.description = description;
	}
	
	public RolTransferDto(Long key) {
		if(key!=null) {
			this.key = key.toString();	
		}
		
	}

	public RolTransferDto(String description) {
		this.description = description;
	}
	
	
		

}
