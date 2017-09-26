package com.vcredit.framework.formater;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vcredit.framework.util.DateUtil;

public class DateJacksonSerializer extends JsonSerializer<Date>{

	@Override
	public void serialize(Date value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
          String formattedDate = DateUtil.formatDateTime(value); 
          gen.writeString(formattedDate); 
	}

}
