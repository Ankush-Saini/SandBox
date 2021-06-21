package com.core.apis.coreapis.models.response;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderStatusQueryResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 354433904818250666L;
	@JsonProperty("orderId")
	public String orderId;
	
	@JsonProperty("itemType")
	public String itemType;
	
	@JsonProperty("price")
	public BigDecimal price;
	
	@JsonProperty("currency")
	public String currency;
	
	@JsonProperty("orderStatus")
	public String orderStatus;
}
